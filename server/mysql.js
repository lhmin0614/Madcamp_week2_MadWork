var mysql = require('mysql');
const { createConnection } = require('net');
var connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'wngud5182',
    database: 'tododb'
});

connection.connect();

connection.query('select * from todotable', function (error, results, fields) {
    if (error) {
        console.log(error);
    }
    console.log(results);
})

connection.end();