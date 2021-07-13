drop database tododb;
drop database calendardb;
drop database loginDB;

create database tododb;
use tododb;

CREATE TABLE todotable (
    numid int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userid char(20) NOT NULL,
    progress int(4),
    work char(100) DEFAULT NULL
);
insert into todotable(userid,work) values('kimjh5182','study, laundry');

create database calendardb;
use calendardb;

CREATE TABLE calendartable (
    numid int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    groupid char(20) NOT NULL,
    date char(40) not null,
    calendartext char(100) DEFAULT NULL,
    UNIQUE index(date)
);
ALTER TABLE calendartable ADD UNIQUE (date);


insert into calendartable(groupid,date,calendartext) values(1,"2021711_1","hello world");
/*alter TABLE todotable convert to character set UTF8;*/

-- 드래그 후 "ctrl" + "/" 치면 주석 풀려요!

CREATE DATABASE loginDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE loginDB;

CREATE TABLE user(
    UserID CHAR(25) PRIMARY KEY,
    UserName VARCHAR(25) NOT NULL,
    UserProfile CHAR(25) NOT NULL,
    Flag BOOLEAN NOT NULL,
    UserPassword VARCHAR(25) DEFAULT NULL
);

CREATE DATABASE chatDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE chatDB;
CREATE TABLE groupName(
   GroupID INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   GroupName CHAR(10) NOT NULL
);

CREATE TABLE message(
    MessageID INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    GroupID CHAR(10) NOT NULL,
    UserID CHAR(25) NOT NULL,
    UserName VARCHAR(25) NOT NULL,
    UserProfile CHAR(25) NOT NULL,
    content CHAR(100),
    SendTime CHAR(25) NOT NULL
);