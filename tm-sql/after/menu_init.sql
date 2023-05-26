/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:16:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(255) DEFAULT '',
  `url` varchar(255) DEFAULT '',
  `parent_id` int(11) DEFAULT '0',
  `icon` varchar(255) DEFAULT '',
  `seq` int(11) DEFAULT '1',
  `add_time` datetime DEFAULT NULL,
  `add_user` varchar(255) DEFAULT '',
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) DEFAULT '',
  `level` int(11) DEFAULT '1',
  `status` int(11) DEFAULT '0' COMMENT '0 正常, 1 已删除',
  `page_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '主页', '/main', '0', 'home', '1', '2020-03-28 21:50:33', 'admin', '2020-03-28 19:41:53', 'v_wbzjzhang', '1', '0', null);
INSERT INTO `menu` VALUES ('47', '项目', '/projectlist', '100', '', '5', '2020-03-28 21:56:39', 'admin', '2020-03-28 21:56:39', 'admin', '2', '0', null);
INSERT INTO `menu` VALUES ('48', '自动化测试', '', '0', 'schedule', '100', '2020-03-28 22:01:00', 'admin', '2020-03-28 22:03:51', 'admin', '1', '0', null);
INSERT INTO `menu` VALUES ('49', '自动化用例', '/nodemanage/6', '48', '', '1', '2020-03-28 22:02:45', 'admin', '2021-01-14 22:37:12', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('50', '权限', '/rightlist', '100', '', '110', '2020-03-28 22:05:24', 'admin', '2020-12-31 22:27:31', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('51', '角色', '/rolelist', '100', '', '19', '2020-03-28 22:05:56', 'admin', '2020-03-28 22:05:56', 'admin', '2', '0', null);
INSERT INTO `menu` VALUES ('52', '用户', '/userlist', '100', '', '10', '2020-03-28 22:06:34', 'admin', '2021-01-07 23:29:53', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('100', '系统配置', '', '0', 'setting', '10000', '2020-03-28 21:34:52', 'admin', '2021-01-09 19:23:43', 'testmanzeng', '1', '0', null);
INSERT INTO `menu` VALUES ('200', '菜单', '/menulist', '100', '', '1', '2020-03-28 21:35:29', 'admin', '2020-03-28 21:35:36', 'admin', '2', '0', null);
INSERT INTO `menu` VALUES ('201', '平台API', '/nodemanage/5', '48', '', '100', '2020-03-28 22:59:56', 'testmanzeng', '2021-01-17 19:11:38', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('202', '脚本配置', '/nodemanage/3', '48', '', '600', '2020-03-29 18:23:05', 'testmanzeng', '2021-01-17 19:12:11', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('203', '自动化计划', '/nodemanage/7', '48', '', '2', '2020-03-29 18:26:38', 'testmanzeng', '2021-01-17 19:11:23', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('204', 'HTTP接口资源', '/nodemanage/2', '48', '', '300', '2020-03-30 23:07:05', 'testmanzeng', '2021-05-23 19:00:47', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('205', '持续集成', '', '0', 'ci', '1', '2020-03-30 23:18:22', 'testmanzeng', '2020-03-30 23:18:22', 'testmanzeng', '1', '1', null);
INSERT INTO `menu` VALUES ('206', '构建计划', '/jenkinsjoblist', '205', '', '1', '2020-03-30 23:19:56', 'testmanzeng', '2020-03-30 23:20:13', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('207', '自定义流水线', '/pipelines', '205', '', '2', '2020-03-30 23:20:58', 'testmanzeng', '2020-03-30 23:20:58', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('208', '全局变量', '/nodemanage/4', '48', '', '5000', '2021-01-07 23:30:20', 'testmanzeng', '2021-01-17 22:12:26', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('209', '测试管理', '', '0', '', '400', '2021-01-07 23:31:04', 'testmanzeng', '2021-01-07 23:31:04', 'testmanzeng', '1', '0', null);
INSERT INTO `menu` VALUES ('210', '数据库', '/dbconfig', '209', '', '10', '2021-01-07 23:31:21', 'testmanzeng', '2021-10-23 00:29:05', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('211', '定时计划', '/cronjoblist', '209', '', '200', '2021-02-28 20:32:12', 'testmanzeng', '2021-08-01 00:55:03', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('212', '环境', '/runenvlist', '209', '', '300', '2021-05-23 22:04:06', 'testmanzeng', '2021-10-23 00:29:26', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('213', '双色球', '/ssq', '0', '', '1000', '2021-07-12 20:24:17', 'testmanzeng', '2021-07-12 20:24:17', 'testmanzeng', '1', '1', null);
INSERT INTO `menu` VALUES ('214', '接口地址', '/urlconfig', '209', '', '2', '2021-10-23 00:28:16', 'testmanzeng', '2021-10-23 00:29:17', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('215', '项目管理', '', '0', '', '450', '2021-12-24 20:46:50', 'testmanzeng', '2021-12-24 20:46:50', 'testmanzeng', '1', '0', null);
INSERT INTO `menu` VALUES ('216', '需求', '', '215', '', '100', '2021-12-24 20:47:15', 'testmanzeng', '2021-12-24 20:47:15', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('217', '缺陷', '', '215', '', '200', '2021-12-24 20:47:28', 'testmanzeng', '2021-12-24 20:47:28', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('218', '测试用例', '', '215', '', '300', '2021-12-24 20:47:50', 'testmanzeng', '2021-12-24 20:47:50', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('219', '发布计划', '', '215', '', '400', '2021-12-24 21:48:10', 'testmanzeng', '2021-12-24 21:48:10', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('220', '动态mock', '', '0', '', '600', '2022-01-12 21:28:57', 'testmanzeng', '2022-01-12 21:28:57', 'testmanzeng', '1', '0', null);
INSERT INTO `menu` VALUES ('221', '挡板规则', '/mockrules', '220', '', '10', '2022-01-14 19:24:22', 'testmanzeng', '2022-01-14 19:24:22', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('222', '实例管理', '/mockinstances', '220', '', '5', '2022-01-14 19:25:26', 'testmanzeng', '2022-01-15 19:08:06', 'testmanzeng', '2', '0', null);
INSERT INTO `menu` VALUES ('223', '标签', '/tags', '48', null, '6000', '2022-12-01 23:58:32', 'testmanzeng', '2022-12-01 23:59:00', 'testmanzeng', null, '0', null);
