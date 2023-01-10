/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:37:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data_nodes
-- ----------------------------
DROP TABLE IF EXISTS `data_nodes`;
CREATE TABLE `data_nodes` (
  `id` int(11) NOT NULL,
  `data_type_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `project_id` int(11) DEFAULT NULL,
  `is_folder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `seq` int(11) DEFAULT '1',
  `add_time` datetime DEFAULT NULL,
  `add_user_id` int(11) DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user_id` int(11) DEFAULT NULL,
  `parent1` int(11) DEFAULT NULL,
  `parent2` int(11) DEFAULT NULL,
  `parent3` int(11) DEFAULT NULL,
  `parent4` int(11) DEFAULT NULL,
  `parent5` int(11) DEFAULT NULL,
  `parent6` int(11) DEFAULT NULL,
  `parent7` int(11) DEFAULT NULL,
  `parent8` int(11) DEFAULT NULL,
  `parent9` int(11) DEFAULT NULL,
  `parent10` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`,`data_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of data_nodes
-- ----------------------------
INSERT INTO `data_nodes` VALUES ('1', '1', 'ROOT', 'ROOT', '1', '1', '0', '1', '1', '2022-07-09 16:35:54', '1', '2022-07-09 16:35:57', '1', '0', null, null, null, null, null, null, null, null, null, '0');
INSERT INTO `data_nodes` VALUES ('10000', '5', '测试号码', '获取随机数，随机字符串等方法', '57', '1', '1', '2', '2', '2022-08-28 17:47:12', '1', '2022-09-24 21:37:44', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10001', '5', '__getRandomInt', '获取随机数', '57', '0', '10000', '3', '2', '2022-08-26 19:45:17', '1', '2022-09-13 18:02:41', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10002', '5', '__getDate', '获取日期', '57', '0', '10006', '3', '1', '2022-09-01 19:22:53', '1', '2022-09-19 22:02:18', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10003', '5', '参数提取', '参数提取', '57', '1', '1', '2', '3', '2022-01-25 12:28:15', '1', NULL, NULL, '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10004', '5', '__jsonMultiExtractor', 'json提取器，提取多个参数', '57', '0', '10003', '3', '2', '2022-01-25 12:29:23', '1', '2022-12-05 15:08:31', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10005', '5', '__xmlMultiExtractor', 'xml提取器，提取多个参数', '57', '0', '10003', '3', '3', '2022-02-11 22:21:37', '1', '2022-05-13 18:00:38', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10006', '5', '时间', '生成时间', '57', '1', '1', '2', '4', '2022-02-14 14:50:46', '1', NULL, NULL, '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10007', '5', '__getTimestamp', '获取时间戳', '57', '0', '10006', '3', '2', '2022-02-14 14:51:27', '1', '2022-02-14 14:52:46', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10008', '5', '__getMobile', '随机一个手机号', '57', '0', '10000', '3', '3', '2022-05-10 22:37:56', '1', '2022-05-22 22:47:46', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10009', '5', '__getChineseName', '获取中国姓名', '57', '0', '10000', '3', '4', '2022-05-26 21:06:56', '1', '2022-05-26 21:07:14', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10010', '5', '__getEmail', '获取邮箱地址', '57', '0', '10000', '3', '5', '2022-05-26 21:11:23', '1', '2022-05-26 21:11:34', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10011', '5', '__getEnglishName', '获取英文名', '57', '0', '10000', '3', '6', '2022-05-26 21:13:54', '1', '2022-05-26 21:14:05', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10012', '5', '__getChineseAddress', '获取中国地址', '57', '0', '10000', '3', '7', '2022-05-26 21:16:18', '1', '2022-05-26 21:16:31', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10013', '5', '__getChineseIdCardNo', '获取身份证号', '57', '0', '10000', '3', '8', '2022-05-26 21:25:02', '1', '2022-05-29 21:57:02', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10014', '5', '计算', '表达式计算等', '57', '1', '1', '2', '5', '2022-06-14 21:39:02', '1', NULL, NULL, '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10015', '5', '__sleep', '休眠', '57', '0', '10006', '3', '3', '2022-06-15 20:25:18', '1', '2022-06-15 20:26:18', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10016', '5', '__operationExpression', '表达式运算', '57', '0', '10014', '3', '2', '2022-06-15 21:45:46', '1', '2022-06-15 21:49:49', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10017', '5', '__getGlobalKeyValue', '获取全局变量值', '57', '0', '10018', '3', '1', '2022-06-21 21:20:48', '1', '2022-06-21 21:22:08', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10018', '5', '全局变量', '获取、设置全局变量', '57', '1', '1', '2', '8', '2022-06-21 21:21:23', '1', NULL, NULL, '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10019', '5', '__setGlobalKeyValue', '设置全局变量值', '57', '0', '10018', '3', '2', '2022-06-21 21:22:29', '1', NULL, NULL, '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10020', '5', '编码解码', '编码解码相关api', '57', '1', '1', '2', '9', '2022-06-21 21:23:00', '1', NULL, NULL, '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10021', '5', '__encodeURIComponent', '把字符串作为 URI 组件进行编码', '57', '0', '10020', '3', '2', '2022-06-21 21:27:19', '1', '2022-08-05 18:48:06', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10022', '5', '__decodeURIComponent', '对URI 组件进行解码', '57', '0', '10020', '3', '3', '2022-06-21 21:29:27', '1', '2022-08-05 18:48:49', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10023', '5', '字符串', '字符串处理相关api', '57', '1', '1', '2', '10', '2022-06-21 21:30:23', '1', NULL, NULL, '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10024', '5', '__base64Encode', '把字符串编码为base64', '57', '0', '10020', '3', '4', '2022-06-21 21:31:29', '1', '2022-08-05 18:11:12', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10025', '5', '__base64Decode', '解码base64字符串', '57', '0', '10020', '3', '5', '2022-06-21 21:32:12', '1', '2022-08-05 18:04:23', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10026', '5', '加解密', '加解密相关api', '57', '1', '1', '2', '11', '2022-06-21 21:32:34', '1', NULL, NULL, '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10027', '5', '__md5', '得到md5值', '57', '0', '10026', '3', '2', '2022-06-21 21:33:09', '1', '2022-08-05 22:21:04', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10028', '5', '__subString', '截取字符串', '57', '0', '10023', '3', '2', '2022-06-21 22:02:57', '1', '2022-08-06 22:20:45', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10029', '5', '断言', '断言', '57', '1', '1', '2', '12', '2022-08-09 23:02:44', '1', NULL, NULL, '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `data_nodes` VALUES ('10030', '5', '__assert', '断言', '57', '0', '10029', '3', '2', '2022-08-09 23:04:42', '1', '2022-08-09 23:50:52', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
