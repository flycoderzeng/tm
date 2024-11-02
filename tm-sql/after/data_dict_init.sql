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
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10000, 5, '测试号码', '获取随机数，随机字符串等方法', 57, '1', 1, 2, 2, '2022-08-28 17:47:12', 1, '2022-09-24 21:37:44', 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10001, 5, '__getRandomInt', '获取随机整数', 57, '0', 10000, 3, 2, '2022-08-26 19:45:17', 1, '2023-08-10 11:29:49', 7058, 1, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10002, 5, '__getDate', '获取日期', 57, '0', 10006, 3, 1, '2022-09-01 19:22:53', 1, '2023-07-10 15:48:25', 7058, 1, 10006, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10003, 5, '参数提取', '参数提取', 57, '1', 1, 2, 3, '2022-01-25 12:28:15', 1, NULL, NULL, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10004, 5, '__jsonMultiExtractor', 'json提取器，提取多个参数', 57, '0', 10003, 3, 2, '2022-01-25 12:29:23', 1, '2023-07-10 16:34:17', 7058, 1, 10003, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10005, 5, '__xmlMultiExtractor', 'xml提取器，提取多个参数', 57, '0', 10003, 3, 3, '2022-02-11 22:21:37', 1, '2023-06-05 13:12:14', 7058, 1, 10003, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10006, 5, '时间', '生成时间', 57, '1', 1, 2, 4, '2022-02-14 14:50:46', 1, NULL, NULL, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10007, 5, '__getTimestamp', '获取时间戳', 57, '0', 10006, 3, 2, '2022-02-14 14:51:27', 1, '2023-06-05 13:12:21', 7058, 1, 10006, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10008, 5, '__getMobile', '随机一个手机号', 57, '0', 10000, 3, 3, '2022-05-10 22:37:56', 1, '2023-06-05 13:11:48', 7058, 1, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10009, 5, '__getChineseName', '获取中国姓名', 57, '0', 10000, 3, 4, '2022-05-26 21:06:56', 1, '2023-06-05 13:11:51', 7058, 1, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10010, 5, '__getEmail', '获取邮箱地址', 57, '0', 10000, 3, 5, '2022-05-26 21:11:23', 1, '2023-06-05 13:11:54', 7058, 1, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10011, 5, '__getEnglishName', '获取英文名', 57, '0', 10000, 3, 6, '2022-05-26 21:13:54', 1, '2023-06-05 13:11:57', 7058, 1, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10012, 5, '__getChineseAddress', '获取中国地址', 57, '0', 10000, 3, 7, '2022-05-26 21:16:18', 1, '2023-06-05 13:12:00', 7058, 1, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10013, 5, '__getChineseIdCardNo', '获取身份证号', 57, '0', 10000, 3, 8, '2022-05-26 21:25:02', 1, '2023-06-05 13:12:03', 7058, 1, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10014, 5, '计算', '表达式计算等', 57, '1', 1, 2, 5, '2022-06-14 21:39:02', 1, NULL, NULL, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10015, 5, '__sleep', '休眠', 57, '0', 10006, 3, 3, '2022-06-15 20:25:18', 1, '2023-06-05 13:12:24', 7058, 1, 10006, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10016, 5, '__operationExpression', '表达式运算', 57, '0', 10014, 3, 2, '2022-06-15 21:45:46', 1, '2023-06-05 13:12:28', 7058, 1, 10014, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10017, 5, '__getGlobalKeyValue', '获取全局变量值', 57, '0', 10018, 3, 1, '2022-06-21 21:20:48', 1, '2023-06-05 15:20:37', 7058, 1, 10018, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10018, 5, '全局变量', '获取、设置全局变量', 57, '1', 1, 2, 8, '2022-06-21 21:21:23', 1, NULL, NULL, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10019, 5, '__setGlobalKeyValue', '设置全局变量值', 57, '0', 10018, 3, 2, '2022-06-21 21:22:29', 1, '2023-06-05 13:12:35', 7058, 1, 10018, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10020, 5, '编码解码', '编码解码相关api', 57, '1', 1, 2, 9, '2022-06-21 21:23:00', 1, NULL, NULL, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10021, 5, '__encodeURIComponent', '把字符串作为 URI 组件进行编码', 57, '0', 10020, 3, 2, '2022-06-21 21:27:19', 1, '2023-06-05 13:12:39', 7058, 1, 10020, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10022, 5, '__decodeURIComponent', '对URI 组件进行解码', 57, '0', 10020, 3, 3, '2022-06-21 21:29:27', 1, '2023-06-05 13:12:41', 7058, 1, 10020, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10023, 5, '字符串', '字符串处理相关api', 57, '1', 1, 2, 10, '2022-06-21 21:30:23', 1, NULL, NULL, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10024, 5, '__base64Encode', '把字符串编码为base64', 57, '0', 10020, 3, 4, '2022-06-21 21:31:29', 1, '2023-06-05 13:12:45', 7058, 1, 10020, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10025, 5, '__base64Decode', '解码base64字符串', 57, '0', 10020, 3, 5, '2022-06-21 21:32:12', 1, '2023-06-05 13:12:48', 7058, 1, 10020, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10026, 5, '加解密', '加解密相关api', 57, '1', 1, 2, 11, '2022-06-21 21:32:34', 1, NULL, NULL, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10027, 5, '__md5', '得到md5值', 57, '0', 10026, 3, 2, '2022-06-21 21:33:09', 1, '2023-06-05 13:13:00', 7058, 1, 10026, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10028, 5, '__subString', '截取字符串', 57, '0', 10023, 3, 2, '2022-06-21 22:02:57', 1, '2023-06-05 13:12:55', 7058, 1, 10023, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10029, 5, '断言', '断言', 57, '1', 1, 2, 12, '2022-08-09 23:02:44', 1, NULL, NULL, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10030, 5, '__assert', '断言', 57, '0', 10029, 3, 2, '2022-08-09 23:04:42', 1, '2023-06-05 13:13:05', 7058, 1, 10029, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10031, 5, '__encodeCipherCredential', '编码策略平台获取token的凭据', 57, '0', 10020, 3, 6, '2023-05-30 14:01:25', 7058, '2023-06-05 13:12:51', 7058, 1, 10020, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10032, 5, '__encodeMessagesDigest', '编码调用统服接口需要的MessagesDigest', 57, '0', 10020, 3, 7, '2023-07-05 11:49:20', 7058, '2023-07-10 16:55:55', 7058, 1, 10020, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `autotest`.`data_nodes` (`id`, `data_type_id`, `name`, `description`, `project_id`, `is_folder`, `parent_id`, `level`, `seq`, `add_time`, `add_user_id`, `last_modify_time`, `last_modify_user_id`, `parent1`, `parent2`, `parent3`, `parent4`, `parent5`, `parent6`, `parent7`, `parent8`, `parent9`, `parent10`, `status`) VALUES (10033, 5, '__encodeAdminUserPassword', '编码admin user password', 57, '0', 10020, 3, 8, '2023-07-06 17:16:04', 7058, '2023-07-10 16:57:47', 7058, 1, 10020, 0, 0, 0, 0, 0, 0, 0, 0, 0);

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
/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.18.41-docker-mysql
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.18.41:3306
 Source Schema         : autotest

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 11/09/2023 15:41:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `menu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
                         `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
                         `parent_id` int NULL DEFAULT 0,
                         `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
                         `seq` int NULL DEFAULT 1,
                         `add_time` datetime NULL DEFAULT NULL,
                         `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
                         `last_modify_time` datetime NULL DEFAULT NULL,
                         `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
                         `level` int NULL DEFAULT 1,
                         `status` int NULL DEFAULT 0 COMMENT '0 正常, 1 已删除',
                         `page_id` int NULL DEFAULT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 225 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '主页', '/main', 0, 'home', 1, '2020-03-28 21:50:33', 'admin', '2020-03-28 19:41:53', 'admin', 1, 0, NULL);
INSERT INTO `menu` VALUES (47, '项目', '/projectlist', 100, '', 5, '2020-03-28 21:56:39', 'admin', '2020-03-28 21:56:39', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (48, '自动化测试', '', 0, 'schedule', 100, '2020-03-28 22:01:00', 'admin', '2020-03-28 22:03:51', 'admin', 1, 0, NULL);
INSERT INTO `menu` VALUES (49, '自动化用例', '/nodemanage/6', 48, '', 1, '2020-03-28 22:02:45', 'admin', '2021-01-14 22:37:12', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (50, '权限', '/rightlist', 100, '', 110, '2020-03-28 22:05:24', 'admin', '2020-12-31 22:27:31', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (51, '角色', '/rolelist', 100, '', 19, '2020-03-28 22:05:56', 'admin', '2020-03-28 22:05:56', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (52, '用户', '/userlist', 100, '', 10, '2020-03-28 22:06:34', 'admin', '2021-01-07 23:29:53', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (100, '系统配置', '', 0, 'setting', 10000, '2020-03-28 21:34:52', 'admin', '2021-01-09 19:23:43', 'admin', 1, 0, NULL);
INSERT INTO `menu` VALUES (200, '菜单', '/menulist', 100, '', 1, '2020-03-28 21:35:29', 'admin', '2020-03-28 21:35:36', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (201, '平台API', '/nodemanage/5', 48, '', 10000, '2020-03-28 22:59:56', 'admin', '2023-07-18 18:13:43', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (202, '脚本配置', '/nodemanage/3', 48, '', 600, '2020-03-29 18:23:05', 'admin', '2021-01-17 19:12:11', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (203, '自动化计划', '/nodemanage/7', 48, '', 2, '2020-03-29 18:26:38', 'admin', '2021-01-17 19:11:23', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (204, 'HTTP接口资源', '/nodemanage/2', 48, '', 300, '2020-03-30 23:07:05', 'admin', '2023-07-04 10:28:20', 'admin', 2, 1, NULL);
INSERT INTO `menu` VALUES (205, '持续集成', '', 0, 'ci', 1, '2020-03-30 23:18:22', 'admin', '2020-03-30 23:18:22', 'admin', 1, 1, NULL);
INSERT INTO `menu` VALUES (206, '构建计划', '/jenkinsjoblist', 205, '', 1, '2020-03-30 23:19:56', 'admin', '2020-03-30 23:20:13', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (207, '自定义流水线', '/pipelines', 205, '', 2, '2020-03-30 23:20:58', 'admin', '2020-03-30 23:20:58', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (208, '全局变量', '/nodemanage/4', 48, '', 5000, '2021-01-07 23:30:20', 'admin', '2021-01-17 22:12:26', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (209, '测试管理', '', 0, '', 400, '2021-01-07 23:31:04', 'admin', '2021-01-07 23:31:04', 'admin', 1, 0, NULL);
INSERT INTO `menu` VALUES (210, '数据库', '/dbconfig', 209, '', 10, '2021-01-07 23:31:21', 'admin', '2021-10-23 00:29:05', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (211, '定时计划', '/cronjoblist', 209, '', 200, '2021-02-28 20:32:12', 'admin', '2021-08-01 00:55:03', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (212, '环境', '/runenvlist', 209, '', 300, '2021-05-23 22:04:06', 'admin', '2021-10-23 00:29:26', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (213, '双色球', '/ssq', 0, '', 1000, '2021-07-12 20:24:17', 'admin', '2021-07-12 20:24:17', 'admin', 1, 1, NULL);
INSERT INTO `menu` VALUES (214, '接口地址', '/urlconfig', 209, '', 2, '2021-10-23 00:28:16', 'admin', '2023-07-18 18:20:51', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (215, '项目管理', '', 0, '', 450, '2021-12-24 20:46:50', 'admin', '2023-06-19 14:18:10', 'admin', 1, 1, NULL);
INSERT INTO `menu` VALUES (216, '需求', '', 215, '', 100, '2021-12-24 20:47:15', 'admin', '2021-12-24 20:47:15', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (217, '缺陷', '', 215, '', 200, '2021-12-24 20:47:28', 'admin', '2021-12-24 20:47:28', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (218, '测试用例', '', 215, '', 300, '2021-12-24 20:47:50', 'admin', '2021-12-24 20:47:50', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (219, '发布计划', '', 215, '', 400, '2021-12-24 21:48:10', 'admin', '2021-12-24 21:48:10', 'admin', 2, 0, NULL);
INSERT INTO `menu` VALUES (223, '标签', '/tags', 209, NULL, 6000, '2022-12-01 23:58:32', 'admin', '2023-07-31 11:02:32', 'admin', NULL, 0, NULL);
INSERT INTO `menu` VALUES (224, 'DCN管理', '/dcnlist', 209, NULL, 500, '2023-07-20 10:18:56', 'admin', NULL, NULL, NULL, 0, NULL);

SET FOREIGN_KEY_CHECKS = 1;
/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.18.41-docker-mysql
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.18.41:3306
 Source Schema         : autotest

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 11/09/2023 15:42:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for platform_api
-- ----------------------------
DROP TABLE IF EXISTS `platform_api`;
CREATE TABLE `platform_api`  (
                                 `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                                 `define_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10034 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of platform_api
-- ----------------------------
INSERT INTO `platform_api` VALUES (10000, '');
INSERT INTO `platform_api` VALUES (10001, '[{\"name\":\"count\",\"description\":\"随机数位数\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"2\",\"key\":\"169163814207011745916355887709008536920280957982\"},{\"name\":\"prefix\",\"description\":\"前缀\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"169163814207076704977276542897368635511108117\"},{\"name\":\"suffix\",\"description\":\"后缀\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1691638142070154159590386297124985651841664973\"},{\"name\":\"minInclude\",\"description\":\"最小值\",\"inout\":\"1\",\"type\":\"2\",\"defaultValue\":\"\",\"key\":\"16916381446296478169925225833995321621261412\"},{\"name\":\"maxInclude\",\"description\":\"最大值\",\"inout\":\"1\",\"type\":\"2\",\"defaultValue\":\"\",\"key\":\"1691638155101336900303925290259067612701787611\"},{\"name\":\"result\",\"description\":\"随机结果\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"169163814207017133312814013295552007976730426\"},{\"name\":\"result_1\",\"description\":\"年月日+随机结果\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"169163814207000291820115434937587222347513211094\"},{\"name\":\"result_2\",\"description\":\"年月日时分秒+随机结果\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1691638142070097981408846496094475797207085921\"}]');
INSERT INTO `platform_api` VALUES (10002, '[{\"name\":\"format\",\"description\":\"日期格式，默认：yyyy-MM-dd HH:mm:ss\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897492138379161315297650038456094685476045\"},{\"name\":\"offset\",\"description\":\"偏移值，可以为负数。默认为0\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"2\",\"key\":\"1688974921383031841767472009518241416228221301\"},{\"name\":\"unit\",\"description\":\"单位1-秒；2-分；3-时；4-日；5-月；6-年。默认为4\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889749213838713555363019578563579275122549\"},{\"name\":\"baseTime\",\"description\":\"基准时间，yyyy-MM-dd HH:mm:ss或者时间戳\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889749213830354779093806896219069298054570738\"},{\"name\":\"result\",\"description\":\"日期格式字符串\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897492138372005742861419858308842611449911\"},{\"name\":\"result_1\",\"description\":\"时间戳毫秒格式\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688974921383352644945799948765600175865387464\"},{\"name\":\"result_2\",\"description\":\"时间戳秒格式\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897492138364235871351107314709449203856193\"}]');
INSERT INTO `platform_api` VALUES (10003, '');
INSERT INTO `platform_api` VALUES (10004, '[{\"name\":\"content\",\"description\":\"json字符串,默认上一个请求的响应\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897795993895737277380226596809668708914987\"},{\"name\":\"path_1\",\"description\":\"参数提取路径，如：$.code\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688977959938803424071012438161923658730043\"},{\"name\":\"path_2\",\"description\":\"json路径\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688977959938922034710240297542100907549916955\"},{\"name\":\"path_3\",\"description\":\"json路径\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688977959938153886035391053175815771633065807\"},{\"name\":\"path_4\",\"description\":\"json路径\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688977959938736219055950052102122487777533566\"},{\"name\":\"path_5\",\"description\":\"json路径\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889779599387292130010824629309696339024867\"},{\"name\":\"path_6\",\"description\":\"json路径\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897795993882656091795234889183027552653926\"},{\"name\":\"path_7\",\"description\":\"json路径\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688977959938050573850281414599712612774518166\"},{\"name\":\"path_8\",\"description\":\"json路径\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897795993866440586664253219073598063451307\"},{\"name\":\"path_9\",\"description\":\"json路径\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889779599381699330107633161314872089715130432\"},{\"name\":\"path_10\",\"description\":\"json路径\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897795993881384286930748466374307076407655\"},{\"name\":\"result_1\",\"description\":\"path_1的结果输出到变量名1\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897795993878554495910050417788982006354364\"},{\"name\":\"result_2\",\"description\":\"path_2的结果输出到变量名2\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889779599384094124318578587740095862587882\"},{\"name\":\"result_3\",\"description\":\"变量名\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897795993874477534511310342433920193316601\"},{\"name\":\"result_4\",\"description\":\"变量名\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688977959938053437775599731069954054119044928\"},{\"name\":\"result_5\",\"description\":\"变量名\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688977959938045360442658867854818447297803474\"},{\"name\":\"result_6\",\"description\":\"变量名\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889779599384579161433490994722571530550281915\"},{\"name\":\"result_6\",\"description\":\"变量名\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897795993877156931602112176898136496787823\"},{\"name\":\"result_7\",\"description\":\"变量名\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688977959938365603585047619146441645966408258\"},{\"name\":\"result_8\",\"description\":\"变量名\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889779599380186191786698348682273537637962184\"},{\"name\":\"result_9\",\"description\":\"变量名\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889779599388965595790415366585533534327396\"},{\"name\":\"result_10\",\"description\":\"变量名\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889779599385496922716737542493769336132581\"}]');
INSERT INTO `platform_api` VALUES (10005, '[{\"name\":\"content\",\"description\":\"xml字符串，默认为上一个请求的响应内容\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428093385227987311\"},{\"name\":\"path_1\",\"description\":\"xpath\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428099801469465906\"},{\"name\":\"path_2\",\"description\":\"\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428099771189335657\"},{\"name\":\"path_3\",\"description\":\"\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428093527340951491\"},{\"name\":\"path_4\",\"description\":\"\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428099544352691701\"},{\"name\":\"path_5\",\"description\":\"\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428099270988759828\"},{\"name\":\"path_6\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16367975874007837797870076\"},{\"name\":\"path_7\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16367975866593899053088043\"},{\"name\":\"path_8\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"1636797586391809851584634\"},{\"name\":\"path_9\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"1636797586095284097755932\"},{\"name\":\"path_10\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"1636797585245278596936273\"},{\"name\":\"result_1\",\"description\":\"结果变量\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428094022144234342\"},{\"name\":\"result_2\",\"description\":\"\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428095464250814961\"},{\"name\":\"result_3\",\"description\":\"\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428097247650814533\"},{\"name\":\"result_4\",\"description\":\"\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428098643474928924\"},{\"name\":\"result_5\",\"description\":\"\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16367975428098666469316229\"},{\"name\":\"result_6\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16367976097737104220966529\"},{\"name\":\"result_7\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16367976088753061274264601\"},{\"name\":\"result_8\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16367976085274684693484655\"},{\"name\":\"result_9\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"1636797608222403717188338\"},{\"name\":\"result_10\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"163679761590421741595864\"}]');
INSERT INTO `platform_api` VALUES (10006, '');
INSERT INTO `platform_api` VALUES (10007, '[{\"name\":\"type\",\"description\":\"0-毫秒级，其他秒级，默认为0\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16289238880924190584063603\"},{\"name\":\"result\",\"description\":\"时间戳\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16289239396647275916971865\"}]');
INSERT INTO `platform_api` VALUES (10008, '[{\"name\":\"result\",\"description\":\"输出手机号\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16375924515462674080532022\"}]');
INSERT INTO `platform_api` VALUES (10009, '[{\"name\":\"result\",\"description\":\"姓名\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16379320231394200876137019\"}]');
INSERT INTO `platform_api` VALUES (10010, '[{\"name\":\"result\",\"description\":\"邮箱地址\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16379322844657885896745988\"}]');
INSERT INTO `platform_api` VALUES (10011, '[{\"name\":\"result\",\"description\":\"英文名\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16379324346596817759423684\"}]');
INSERT INTO `platform_api` VALUES (10012, '[{\"name\":\"result\",\"description\":\"中国地址\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"1637932578966275986966784\"}]');
INSERT INTO `platform_api` VALUES (10013, '[{\"name\":\"age\",\"description\":\"指定年龄\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"2\",\"key\":\"16381942156058461039347835\"},{\"name\":\"sex\",\"description\":\"指定性别0-男，1-女\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"2\",\"key\":\"1638194215605657530768363\"},{\"name\":\"birthday\",\"description\":\"指定生日，格式：yyyyMMdd\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1638194215605950614149639\"},{\"name\":\"baseDate\",\"description\":\"指定基准日期，格式：yyyyMMdd\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16381942156056573131414357\"},{\"name\":\"result\",\"description\":\"身份证号\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"163819421560521413562180\"},{\"name\":\"result_1\",\"description\":\"开卡公安局\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16381942156054171649744620\"},{\"name\":\"result_2\",\"description\":\"有效日期\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16381942156057750648299430\"}]');
INSERT INTO `platform_api` VALUES (10014, '');
INSERT INTO `platform_api` VALUES (10015, '[{\"name\":\"seconds\",\"description\":\"休眠秒数\",\"inout\":\"1\",\"type\":\"2\",\"defaultValue\":\"\",\"key\":\"16395711233197214866292755\"}]');
INSERT INTO `platform_api` VALUES (10016, '[{\"name\":\"expression_0\",\"description\":\"表达式0，如：${total} - 1\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395759473842259217322437\"},{\"name\":\"expression_1\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395760341106552609558388\"},{\"name\":\"expression_2\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395760335543811911896241\"},{\"name\":\"expression_3\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395760331232998680346538\"},{\"name\":\"expression_4\",\"description\":\"\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395760543068050211712072\"},{\"name\":\"result_0\",\"description\":\"表达式结果0，输出到变量\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395760849288212770772616\"},{\"name\":\"result_1\",\"description\":\"\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395760875708538713118973\"},{\"name\":\"result_2\",\"description\":\"\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395760847244215191488554\"},{\"name\":\"result_3\",\"description\":\"\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395760844729310674526112\"},{\"name\":\"result_4\",\"description\":\"\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16395760842628915217277153\"}]');
INSERT INTO `platform_api` VALUES (10017, '[{\"name\":\"globalKeyName\",\"description\":\"全局变量名称\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1685949634060332476936437660106421256539720388\"},{\"name\":\"result\",\"description\":\"全局变量结果\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168594963406087383825838144667197676261443327\"}]');
INSERT INTO `platform_api` VALUES (10018, '');
INSERT INTO `platform_api` VALUES (10019, '[{\"name\":\"globalKeyName\",\"description\":\"全局变量名称\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"1685410920048256003210611992941063733274398424\"},{\"name\":\"globalKeyValue\",\"description\":\"全局变量值\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"168541092551862315246705273131761605337473704\"}]');
INSERT INTO `platform_api` VALUES (10020, '');
INSERT INTO `platform_api` VALUES (10021, '[{\"name\":\"srcString\",\"description\":\"原始字符串\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16596964598684727105772244531\"},{\"name\":\"result\",\"description\":\"编码后的字符串\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16596964674146531508518666056\"}]');
INSERT INTO `platform_api` VALUES (10022, '[{\"name\":\"encodedString\",\"description\":\"编码的字符串\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16596965005906335275174657107\"},{\"name\":\"result\",\"description\":\"解码后的字符串\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16596965112341051956597847896\"}]');
INSERT INTO `platform_api` VALUES (10023, '');
INSERT INTO `platform_api` VALUES (10024, '[{\"name\":\"srcString\",\"description\":\"原始字符串\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16596942377755736817613561093\"},{\"name\":\"result\",\"description\":\"编码后的字符串\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16596942550495926897895309082\"}]');
INSERT INTO `platform_api` VALUES (10025, '[{\"name\":\"encodedString\",\"description\":\"base64编码的字符串\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16596937549522258559966766400\"},{\"name\":\"result\",\"description\":\"解码后的字符串\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16596938456121215350189757316\"}]');
INSERT INTO `platform_api` VALUES (10026, '');
INSERT INTO `platform_api` VALUES (10027, '[{\"name\":\"srcString\",\"description\":\"原字符串\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16597092611964076758529266039\"},{\"name\":\"result\",\"description\":\"md5值\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16597092611965959731160149447\"}]');
INSERT INTO `platform_api` VALUES (10028, '[{\"name\":\"srcString\",\"description\":\"原字符串\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16597716624101795369227662573\"},{\"name\":\"fromIndexInclude\",\"description\":\"默认0\",\"inout\":\"1\",\"type\":\"2\",\"defaultValue\":\"\",\"key\":\"16597755788854681114119520069\"},{\"name\":\"toIndexExclude\",\"description\":\"结束索引，可以是负数。不填截取到最后。\",\"inout\":\"1\",\"type\":\"2\",\"defaultValue\":\"\",\"key\":\"1659775670915160722074899825\"},{\"name\":\"fromStringExclude\",\"description\":\"起始字符串，优先级高于起始索引。\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"1659776257285687666136980976\"},{\"name\":\"toStringExclude\",\"description\":\"结束字符串，优先级高于结束索引和截取长度。\",\"inout\":\"1\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16597762902853066319231904162\"},{\"name\":\"length\",\"description\":\"截取长度，从起始位置开始截取多长。可以是负数。\",\"inout\":\"1\",\"type\":\"2\",\"defaultValue\":\"\",\"key\":\"1659777093017106900394475103\"},{\"name\":\"result\",\"description\":\"截取结果\",\"inout\":\"2\",\"type\":\"1\",\"defaultValue\":\"\",\"key\":\"16597763128997108215976143970\"}]');
INSERT INTO `platform_api` VALUES (10029, '');
INSERT INTO `platform_api` VALUES (10030, '[{\"name\":\"expression\",\"description\":\"断言表达式，结果为false时，用例结束\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16600602450831396411930035767\"}]');
INSERT INTO `platform_api` VALUES (10031, '[{\"name\":\"user_id\",\"description\":\"机构管理员登录用户名\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16854271430942759896511938222423765565310278802\"},{\"name\":\"password\",\"description\":\"机构管理员登录密码\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1685427143094420520909987763608506837613711027\"},{\"name\":\"result\",\"description\":\"JSON字符串base64 编码之后的数据\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1685427143094593955028069276731975696797909015\"}]');
INSERT INTO `platform_api` VALUES (10032, '[{\"name\":\"digestKey\",\"description\":\"digestKey\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688979296510354450048660282858489339186190239\"},{\"name\":\"content\",\"description\":\"请求内容\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897929651070052387599445682385904002335646\"},{\"name\":\"requestEncType\",\"description\":\"请求加密类型,1或者2,默认1\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688979296510926040099616295211806736059490519\"},{\"name\":\"result\",\"description\":\"编码结果\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"1688979296510263621293163919742578234536127124\"}]');
INSERT INTO `platform_api` VALUES (10033, '[{\"name\":\"plaintext\",\"description\":\"\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897937835249950138555863437764404807740284\"},{\"name\":\"algo\",\"description\":\"1或者2, 默认为1\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"16889793783520738455858013997720931886216345075\"},{\"name\":\"sm2PublicKey\",\"description\":\"\",\"inout\":\"1\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897937835296368847124945848712504041241682\"},{\"name\":\"result\",\"description\":\"\",\"inout\":\"2\",\"defaultValue\":\"\",\"type\":\"1\",\"key\":\"168897937835204010679013010465621208702348371\"}]');

SET FOREIGN_KEY_CHECKS = 1;
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:48:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for project_user
-- ----------------------------
DROP TABLE IF EXISTS `project_user`;
CREATE TABLE `project_user` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `project_id` int(11) DEFAULT NULL,
                                `user_id` int(11) DEFAULT NULL,
                                `add_user` varchar(255) DEFAULT NULL,
                                `add_time` datetime DEFAULT NULL,
                                `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `last_modify_time` datetime DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of project_user
-- ----------------------------
INSERT INTO `project_user` VALUES ('30', '57', '1', 'admin', '2023-01-10 12:27:09', null, null);
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:45:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for projects
-- ----------------------------
DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                            `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                            `add_user` varchar(255) DEFAULT NULL,
                            `add_time` datetime DEFAULT NULL,
                            `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                            `last_modify_time` datetime DEFAULT NULL,
                            `status` int(11) DEFAULT '0' COMMENT '0 正常 1 已删除',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of projects
-- ----------------------------
INSERT INTO `projects` VALUES ('57', '默认项目', '默认项目', 'admin', '2022-09-30 22:39:34', 'admin', '2022-10-30 22:35:35', '0');
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
INSERT INTO `roles` VALUES ('1', 'ROLE_ROOT_ADMIN', '系统最高管理员', '系统最高管理员，拥有一切权限', '1', '2022-08-31 22:30:21', 'admin', '2022-09-01 17:21:16', 'admin', '0');
INSERT INTO `roles` VALUES ('2', 'ROLE_PROJECT_TESTER', '测试', '可以在项目中新增、修改、运行用例', '2', '2022-05-07 22:46:27', 'admin', '2022-05-09 17:21:37', 'admin', '0');
INSERT INTO `roles` VALUES ('3', 'ROLE_COMMON_ADMIN', '系统普通管理员', '系统普通管理员', '1', '2022-05-08 22:16:02', 'admin', '2022-05-09 17:21:49', 'admin', '0');
INSERT INTO `roles` VALUES ('4', 'ROLE_PROJECT_ADMIN', '项目管理员', '项目管理员，项目用户维护', '2', '2022-05-09 16:17:42', 'admin', '2022-05-09 17:21:54', 'admin', '0');
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:57:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for run_env
-- ----------------------------
DROP TABLE IF EXISTS `run_env`;
CREATE TABLE `run_env` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           `http_ip` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `http_port` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `db_username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `db_password` text COLLATE utf8mb4_general_ci,
                           `db_ip` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `db_port` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `db_type` int DEFAULT NULL,
                           `db_schema_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `add_time` datetime DEFAULT NULL,
                           `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `last_modify_time` datetime DEFAULT NULL,
                           `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `status` int DEFAULT '0' COMMENT '0 正常 1 已删除',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of run_env
-- ----------------------------
INSERT INTO `run_env` VALUES ('1', 'A', '第一套测试环境', '2022-08-21 16:12:20', 'admin', '2022-08-21 16:15:22', 'admin', '0');
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
INSERT INTO `users` VALUES ('1', 'admin', 'admin', '$2a$04$hedVZVAsWL0okS/x5aStt.jAn71.vVhO2SoktlO9QOcbLc3Tr5xuy', '2022-03-28 21:28:32', 'admin', '2022-10-30 22:06:51', 'admin', null, null, null, null, '0');
