var express = require('express');
var http = require('http');
var bodyParser = require('body-parser');
var app = express();
var server = http.createServer(app);
app.set('port', process.env.PORT || 443);
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
var io = require('socket.io')(server);
var mysql = require('mysql');
const { createConnection } = require('net');
const { connect } = require('http2');

var connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'wngud5182',
    database: 'tododb'
});


var connection2 = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'wngud5182',
    database: 'calendardb'
});


var connection3 = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'wngud5182',
    port: 3306,
    database: 'loginDB'
});

var chat_conn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'wngud5182',
    port: 3306,
    database: 'chatDB'
})


//calendar
app.post('/getcalendar', function (req, res) {
    var date = req.body.date;
    console.log('get date: ' + date);

    connection2.query('select calendartext from calendartable where groupid=? and date=?', [1, date], function (error, results, fields) {
        if (error) {
            console.log(error);
        }
        var textList = [];
        for (var data of results) {
            textList.push(data.calendartext)
        };
        var array = { 'calendartext': textList[0] }
        console.log(array);
        res.status(200).send(array);
    });
});

app.post('/savecalendar', function (req, res) {
    var date = req.body.date;
    var text = req.body.text;
    console.log('save date: ' + date);

    connection2.query('insert into calendartable(groupid, date,calendartext) values(?,?,?)ON DUPLICATE KEY update calendartext=?', [1, date, text, text], function (error, results, fields) {
        if (error) {
            console.log(error);
        }
        res.status(200);
    });
});
app.post('/removecalendar', function (req, res) {
    var date = req.body.date;
    console.log('delete date: ' + date);

    connection2.query('delete from calendartable where date=? and groupid=?', [date, 1], function (error, results, fields) {
        if (error) {
            console.log(error);
        }
        res.status(200);
    });
});

//todolist
app.post('/work', function (req, res) {
    var work = req.body.work;
    var userID = req.body.userID;
    console.log('work text is ' + work + ' and id is' + userID);
    connection.query('insert into todotable(userid, work) values(?,?)', [userID, work], function (error, results, fields) {
        if (error) console.log(error);
        connection.query('select work,progress from todotable where userid=?', userID, function (error, results2, fields) {
            if (error) {
                console.log(error);
            }
            var workList = [];
            var progressList = [];
            for (var data of results2) {
                workList.push(data.work)
                if (data.progress == null) progressList.push(0);
                else progressList.push(data.progress);
            };
            var array = { 'work': workList, 'progress': progressList }
            console.log(array);
            res.status(200).send(array);
        });
    });
});

app.post('/gettodo', function (req, res) {
    var userID = req.body.userID;
    console.log('id is ' + userID);
    connection.query('select work,progress from todotable where userid=?', userID, function (error, results, fields) {
        if (error) {
            console.log(error);
        }
        //console.log(results);
        //res.status(200).send(results[]);

        var workList = [];
        var progressList = [];
        for (var data of results) {
            workList.push(data.work)
            if (data.progress == null) progressList.push(0);
            else progressList.push(data.progress);
        };
        var array = { 'work': workList, 'progress': progressList }
        console.log(array);
        res.status(200).send(array);

    });
});

app.post('/progress', function (req, res) {
    var progress = req.body.progress;
    var worktext = req.body.worktext;
    var userID = req.body.userID;
    console.log('progress is ' + progress + ' and worktext is ' + worktext);
    connection.query('update todotable set progress=? where userid=? and work=?', [progress, userID, worktext], function (error, results, fields) {
        if (error) console.log(error);
        connection.query('select work,progress from todotable where userid=?', userID, function (error, results2, fields) {
            if (error) {
                console.log(error);
            }
            var workList = [];
            var progressList = [];
            for (var data of results2) {
                workList.push(data.work)
                if (data.progress == null) progressList.push(0);
                else progressList.push(data.progress);
            };
            var array = { 'work': workList, 'progress': progressList }
            console.log(array);
            res.status(200).send(array);
        });
    });
});

app.post('/deletetodo', function (req, res) {
    var progress = req.body.progress;
    var worktext = req.body.worktext;
    var userID = req.body.userID;
    console.log('delete todo is ' + worktext + ' and userID ' + userID);

    connection.query('delete from todotable where userid=? and work=?', [userID, worktext], function (error, results2, fields) {
        if (error) {
            console.log(error);
        }
        connection.query('select work,progress from todotable where userid=?', userID, function (error, results2, fields) {
            if (error) {
                console.log(error);
            }
            var workList = [];
            var progressList = [];
            for (var data of results2) {
                workList.push(data.work)
                if (data.progress == null) progressList.push(0);
                else progressList.push(data.progress);
            };
            var array = { 'work': workList, 'progress': progressList }
            console.log(array);
            res.status(200).send(array);
        });
    });
});





//혜민이 파트
// socket connection code for chatting
io.sockets.on('connection', (socket) => {
    console.log(`Socket connected : ${socket.id}`)

    socket.on('enter', (data) => {
        const roomData = JSON.parse(data)
        const username = roomData.username
        const userid = roomData.userID
        const roomNumber = roomData.roomNumber

        socket.join(`${roomNumber}`) //그룹으로 묶기. 추후 broadcast에서 사용할 group
        sql = "SELECT * FROM message WHERE GroupID = ?";
        chat_conn.query(sql, [roomNumber], function (err, results, fields) {
            if (err) { console.log(err); }
            else {
                for (i = 0; i < results.length; i++) {
                    //DB 저장된 메세지 업로드
                    var typedata = "LEFT";
                    if (results[i].UserID == userid) { typedata = "LEFT" } else { typedata = "RIGHT" };
                    const dbData = {
                        type: typedata,
                        content: results[i].content,
                        sendTime: results[i].SendTime,
                        to: null,
                        userid: null,
                        from: results[i].UserName
                    }
                    socket.emit('dbUpdate', JSON.stringify(dbData));
                }
            }
        })


        const enterData = {
            type: "ENTER",
            content: `${username} entered the room`
        }
        //특정 roomNumber에 연결되어있는 모든 soket 에 event 발생 but 보내는 socket은 제외한다
        //socket.broadcast.to(`${roomNumber}`).emit('update', JSON.stringify(enterData))
    })

    socket.on('left', (data) => {
        const roomData = JSON.parse(data)
        const username = roomData.username
        const roomNumber = roomData.roomNumber

        socket.leave(`${roomNumber}`)
        console.log(`[Username : ${username}] left [room number : ${roomNumber}]`)

        const leftData = {
            type: "LEFT",
            content: `${username} left the room`
        }
        socket.broadcast.to(`${roomNumber}`).emit('update', JSON.stringify(leftData))
    })

    socket.on('newMessage', (data) => {
        const messageData = JSON.parse(data)
        const username = messageData.from;
        const userid = messageData.userid;
        const roomnumber = messageData.to;
        const content = messageData.content;
        const sendtime = messageData.sendTime;

        sql = "INSERT INTO message(GroupID, UserID, UserName, content, SendTime) VALUES (?,?,?,?,?)"
        chat_conn.query(sql, [roomnumber, userid, username, content, sendtime], function (error, results, fields) {
            if (error) {
                console.log(error);
            }
            console.log(`Message saved in chatDB`);
        })
        socket.broadcast.to(`${messageData.to}`).emit('update', JSON.stringify(messageData))
    })

    socket.on('disconnect', () => {
        console.log(`Socket disconnected : ${socket.id}`)
    })
})

app.post('/signup', (req, res) => {
    //connection.connect();
    var sql = "SELECT * FROM user WHERE UserID=?"
    connection3.query(sql, [req.body.id], function (error, results, fields) {
        if (error) {
            //same ID already exists
            console.log(error);
            res.status(404).send();
        }
        else {
            //create new ID
            sql = "INSERT INTO user VALUES (?, ? ,?, ?)";
            connection3.query(sql, [req.body.id, req.body.name, "1", req.body.pw], function (error, results, fields) {
                if (error) {
                    console.log(error);
                }
                console.log(results);
            })
        }
    })
    var approve = { 'approve_id': 'OK', 'approve_pw': 'OK' };
    res.status(200).send(approve);
    console.log("Signup success");
})

app.post('/login', (req, res) => {
    //id와 일치하는 user 정보 가져오기
    var sql = "SELECT * FROM user WHERE UserID=?"
    var approve_id = "NO";
    var approve_pw = "NO"

    connection3.query(sql, [req.body.id], function (error, result, fields) {
        if (error) {
            console.log(error);
        }
        else if (result.length > 0) {
            approve_id = "YES";
            if (result[0].UserPassword == req.body.pw)
                approve_pw = "YES"
        }
        res.send({ approve_id: approve_id, approve_pw: approve_pw });
        console.log("Login success");
    })
})

app.post('/googleLogin', (req, res) => {
    //id와 일치하는 user 정보 가져오기
    var sql = "SELECT * FROM user WHERE UserID=? "
    var approve_id = "NO";
    var approve_pw = "NO"
    console.log("google login");
    connection3.query(sql, [req.body.id], function (error, result, fields) {
        if (error) {
            console.log(error);
        }
        else if (result.length > 0) {
            approve_id = "YES";
        } else {
            //구글 ㄱㅖ정으로 등록
            var sql = "INSERT INTO user VALUES (?, ? ,?, ?)";
            connection3.query(sql, [req.body.id, req.body.name, "0", ""], function (error, results, fields) {
                if (error) {
                    console.log(error);
                }
                console.log(results);
            })
        }
        res.send({ approve_id: approve_id });
        console.log("GoogleLogin success");
    })

})

app.post('/joingroup', (req, res) => {
    const groupName = req.body.groupName;
    var approve_name = "YES";

    var sql = "SELECT * FROM  groupName WHERE GroupName=? "
    chat_conn.query(sql, [groupName], function (error, result, fields) {
        if (error) { console.log(error); }
        else if (result.length > 0) { //이미 같은 group이름이 존재할때 : 등록 거부
            approve_name = "NO";
        } else {
            var sql = "INSERT INTO groupName(GroupName) VALUES (?)";
            chat_conn.query(sql, [groupName], function (error, results, fields) {
                if (error) { console.log(error); }
                else { console.log(results); }
            })
        }
        res.send({ approve_id: approve_name });
    })
})

app.post('/grouplist', (req, res) => {
    var sql = "SELECT * FROM  groupName"
    var groupList = new Array();
    chat_conn.query(sql, function (error, results, fields) {
        if (error) { console.log(error); }
        else {
            console.log(results);
            for (i = 0; i < results.length; i++) {
                groupList.push(results[i].GroupName);
            }
        }
        res.send({ groupids: groupList });
    })
})


server.listen(443, () => {
    console.log(`Server listening at http://localhost:80`)
})
