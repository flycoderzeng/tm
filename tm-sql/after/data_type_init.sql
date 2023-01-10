/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:36:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data_type
-- ----------------------------
DROP TABLE IF EXISTS `data_type`;
CREATE TABLE `data_type` (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of data_type
-- ----------------------------
INSERT INTO `data_type` VALUES ('1', 'ROOT', 'ROOT');
INSERT INTO `data_type` VALUES ('2', '接口资源', '接口资源，如http、https等');
INSERT INTO `data_type` VALUES ('3', '脚本', '自动化脚本');
INSERT INTO `data_type` VALUES ('4', '全局变量', '全局变量');
INSERT INTO `data_type` VALUES ('5', '平台API', '平台api');
INSERT INTO `data_type` VALUES ('6', '自动化用例', '自动化用例');
INSERT INTO `data_type` VALUES ('7', '自动化计划', '自动化计划');
INSERT INTO `data_type` VALUES ('10000', '自动化脚本类型', '自动化脚本类型');
INSERT INTO `data_type` VALUES ('10001', '接口资源类型', '接口资源类型');
INSERT INTO `data_type` VALUES ('10002', 'HTTP(S)请求类型', 'HTTP(S)请求类型');
