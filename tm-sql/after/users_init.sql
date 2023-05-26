/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:34:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `chinese_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `add_user` varchar(255) DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) DEFAULT NULL,
  `org_full_name` varchar(255) DEFAULT NULL,
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `first_login_time` datetime DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_2` (`username`),
  KEY `username` (`username`(191))
) ENGINE=InnoDB AUTO_INCREMENT=7058 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'admin', 'admin', '$2a$04$hedVZVAsWL0okS/x5aStt.jAn71.vVhO2SoktlO9QOcbLc3Tr5xuy', '2022-03-28 21:28:32', 'admin', '2022-10-30 22:06:51', 'testmanzeng', null, null, null, null, '0');
