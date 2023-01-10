/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:29:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `chinese_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '1-系统角色 2-项目角色',
  `add_time` datetime DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('1', 'ROLE_ROOT_ADMIN', '系统最高管理员', '系统最高管理员，拥有一切权限', '1', '2022-08-31 22:30:21', 'testmanzeng', '2022-09-01 17:21:16', 'testmanzeng', '0');
INSERT INTO `roles` VALUES ('2', 'ROLE_PROJECT_TESTER', '测试', '可以在项目中新增、修改、运行用例', '2', '2022-05-07 22:46:27', 'testmanzeng', '2022-05-09 17:21:37', 'testmanzeng', '0');
INSERT INTO `roles` VALUES ('3', 'ROLE_COMMON_ADMIN', '系统普通管理员', '系统普通管理员', '1', '2022-05-08 22:16:02', 'testmanzeng', '2022-05-09 17:21:49', 'testmanzeng', '0');
INSERT INTO `roles` VALUES ('4', 'ROLE_PROJECT_ADMIN', '项目管理员', '项目管理员，项目用户维护', '2', '2022-05-09 16:17:42', 'testmanzeng', '2022-05-09 17:21:54', 'testmanzeng', '0');
