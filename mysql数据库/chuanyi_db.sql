/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : chuanyi_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-03-23 13:56:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `area`
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `areaId` int(11) NOT NULL auto_increment,
  `areaName` varchar(20) default NULL,
  PRIMARY KEY  (`areaId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of area
-- ----------------------------
INSERT INTO `area` VALUES ('1', '成都');
INSERT INTO `area` VALUES ('2', '南充');

-- ----------------------------
-- Table structure for `notice`
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `noticeId` int(11) NOT NULL auto_increment,
  `title` varchar(80) default NULL,
  `content` longtext,
  `videoFile` varchar(50) default NULL,
  `publishDate` varchar(30) default NULL,
  PRIMARY KEY  (`noticeId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES ('1', '2018年春季淑女装搭配', '又到一年春天了，气温也回升了很多，妹子们也爱美起来了，下面是今年春季淑女搭配风格，大家学习下吧！', 'upload/48FEE02C24F105E69F6E13182A3F184D.mp4', '2018-03-13 14:15:15');
INSERT INTO `notice` VALUES ('2', '2018年春季型男装搭配', '2018年春季型男装搭配,2018年春季型男装搭配,2018年春季型男装搭配,2018年春季型男装搭配,2018年春季型男装搭配,2018年春季型男装搭配', 'upload/48FEE02C24F105E69F6E13182A3F184D.mp4', '2018-03-23 10:15:15');
INSERT INTO `notice` VALUES ('3', '2018年春季潮男穿衣装搭配', '2018年春季潮男穿衣装搭配,2018年春季潮男穿衣装搭配,2018年春季潮男穿衣装搭配,2018年春季潮男穿衣装搭配,2018年春季潮男穿衣装搭配,2018年春季潮男穿衣装搭配', 'upload/48FEE02C24F105E69F6E13182A3F184D.mp4', '2018-03-23 13:55:42');

-- ----------------------------
-- Table structure for `userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `user_name` varchar(20) NOT NULL,
  `password` varchar(30) default NULL,
  `areaObj` int(11) default NULL,
  `name` varchar(20) default NULL,
  `gender` varchar(4) default NULL,
  `birthDate` datetime default NULL,
  `userPhoto` varchar(50) default NULL,
  `telephone` varchar(20) default NULL,
  `email` varchar(50) default NULL,
  `address` varchar(80) default NULL,
  `regTime` varchar(30) default NULL,
  PRIMARY KEY  (`user_name`),
  KEY `FKF3F34B39F0A702D9` (`areaObj`),
  CONSTRAINT `FKF3F34B39F0A702D9` FOREIGN KEY (`areaObj`) REFERENCES `area` (`areaId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES ('user1', '123', '1', '双鱼林', '男', '2018-03-07 00:00:00', 'upload/E5B163A7B4E88642519B06D8F3DDD6AF.jpg', '13589834234', 'dashen@163.com', '四川成都红星路13号', '2018-03-12 14:15:15');
INSERT INTO `userinfo` VALUES ('user2', '123', '1', '王芬', '女', '2018-03-21 00:00:00', 'upload/65F5AFABC49F0A0E260243FBF8F8BCCB.jpg', '13508903423', 'wangfen@163.com', '四川自贡', '2018-03-23 13:42:19');

-- ----------------------------
-- Table structure for `weather`
-- ----------------------------
DROP TABLE IF EXISTS `weather`;
CREATE TABLE `weather` (
  `weatherId` int(11) NOT NULL auto_increment,
  `areaObj` int(11) default NULL,
  `weatherDate` datetime default NULL,
  `weatherDataObj` int(11) default NULL,
  `weatherImage` varchar(50) default NULL,
  `temperature` varchar(20) default NULL,
  `airQuality` varchar(20) default NULL,
  PRIMARY KEY  (`weatherId`),
  KEY `FKAC24CFD4F0A702D9` (`areaObj`),
  KEY `FKAC24CFD4FD005115` (`weatherDataObj`),
  CONSTRAINT `FKAC24CFD4F0A702D9` FOREIGN KEY (`areaObj`) REFERENCES `area` (`areaId`),
  CONSTRAINT `FKAC24CFD4FD005115` FOREIGN KEY (`weatherDataObj`) REFERENCES `weatherdata` (`weatherDataId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weather
-- ----------------------------
INSERT INTO `weather` VALUES ('1', '1', '2018-03-15 00:00:00', '1', 'upload/ad35bd30-837e-4af6-99ea-454338a22999.jpg', '18~15℃', '56良');
INSERT INTO `weather` VALUES ('2', '2', '2018-03-15 00:00:00', '2', 'upload/a83413fd-ed56-4f08-ad49-b60c7de02dbf.jpg', '22~17℃', '75良');
INSERT INTO `weather` VALUES ('3', '2', '2018-03-23 00:00:00', '1', 'upload/ad35bd30-837e-4af6-99ea-454338a22999.jpg', '17~14℃', '55良');
INSERT INTO `weather` VALUES ('4', '1', '2018-03-23 00:00:00', '2', 'upload/a83413fd-ed56-4f08-ad49-b60c7de02dbf.jpg', '20~18℃', '58良');

-- ----------------------------
-- Table structure for `weatherdata`
-- ----------------------------
DROP TABLE IF EXISTS `weatherdata`;
CREATE TABLE `weatherdata` (
  `weatherDataId` int(11) NOT NULL auto_increment,
  `weatherDataName` varchar(20) default NULL,
  `weatherImage` varchar(50) default NULL,
  PRIMARY KEY  (`weatherDataId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weatherdata
-- ----------------------------
INSERT INTO `weatherdata` VALUES ('1', '小雨', 'upload/ad35bd30-837e-4af6-99ea-454338a22999.jpg');
INSERT INTO `weatherdata` VALUES ('2', '晴间多云', 'upload/a83413fd-ed56-4f08-ad49-b60c7de02dbf.jpg');
