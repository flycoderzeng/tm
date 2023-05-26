/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:36:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data_dict
-- ----------------------------
DROP TABLE IF EXISTS `data_dict`;
CREATE TABLE `data_dict` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `display` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `data_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of data_dict
-- ----------------------------
INSERT INTO `data_dict` VALUES ('1', 'shell', '1', '10000');
INSERT INTO `data_dict` VALUES ('2', 'python2', '2', '10000');
INSERT INTO `data_dict` VALUES ('3', 'python3', '3', '10000');
INSERT INTO `data_dict` VALUES ('20', 'HTTP(S)', '1', '10001');
INSERT INTO `data_dict` VALUES ('30', 'GET', '1', '10002');
INSERT INTO `data_dict` VALUES ('31', 'POST', '2', '10002');
