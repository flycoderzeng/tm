/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2023-01-10 12:11:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for api_ip_port_config
-- ----------------------------
DROP TABLE IF EXISTS `api_ip_port_config`;
CREATE TABLE `api_ip_port_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '接口ip端口配置表',
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接口路径',
  `env_id` int(11) DEFAULT NULL COMMENT '环境',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0-正常1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `city_id` int(11) DEFAULT NULL,
  `url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `finish` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3714 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for auto_cases
-- ----------------------------
DROP TABLE IF EXISTS `auto_cases`;
CREATE TABLE `auto_cases` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT '1' COMMENT '1-普通自动化用例',
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `last_run_env_id` int(11) DEFAULT NULL,
  `group_variables` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10044 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for auto_case_history
-- ----------------------------
DROP TABLE IF EXISTS `auto_case_history`;
CREATE TABLE `auto_case_history` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `auto_case_id` int(11) DEFAULT NULL,
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `group_variables` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `add_time` datetime DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for auto_plans
-- ----------------------------
DROP TABLE IF EXISTS `auto_plans`;
CREATE TABLE `auto_plans` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '1-普通计划',
  `mail_list` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `max_occurs` int(11) DEFAULT NULL COMMENT '最大并发数',
  `auto_case_run_timeout` int(11) DEFAULT NULL COMMENT '用例执行超时时间，单位：秒',
  `runs` int(11) DEFAULT NULL COMMENT '计划运行次数',
  `env_id` int(11) DEFAULT NULL COMMENT '计划运行环境id',
  `fail_continue` int(11) DEFAULT '1' COMMENT '用例失败后计划是否继续执行，0-停止执行1-继续执行',
  `plan_variables` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for auto_scripts
-- ----------------------------
DROP TABLE IF EXISTS `auto_scripts`;
CREATE TABLE `auto_scripts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT '1' COMMENT '1-shell 2-python',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `author` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `add_user` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `last_modify_user` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for bugs
-- ----------------------------
DROP TABLE IF EXISTS `bugs`;
CREATE TABLE `bugs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `system_story_id` int(11) DEFAULT NULL COMMENT '所属系统需求d',
  `project_id` int(11) DEFAULT NULL,
  `add_user_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `discovery_phase` int(11) DEFAULT '1' COMMENT '发现阶段，1-SIT，2-UAT，3-回归测试，4-冒烟测试，5-集成测试，6-性能测试，7-生产问题，8-联调阶段，9-灰度测试',
  `level` int(11) DEFAULT '50' COMMENT '10-致命，20-严重，30-一般，40-提示，50-建议',
  `status` int(11) DEFAULT '0' COMMENT '0-未解决，1-已解决，2-已拒绝，3-遗留，4-已关闭',
  `handler_user_id` int(11) DEFAULT NULL COMMENT '处理人用户id',
  `from_type` int(11) DEFAULT NULL COMMENT 'bug来源类型',
  `bug_type` int(11) DEFAULT '1' COMMENT '1-代码问题，2-环境问题，3-疑问，4-优化建议，5-安全缺陷，6-与需求不符，7-性能问题',
  `close_user_id` int(11) DEFAULT NULL COMMENT '关闭缺陷的用户id',
  `close_time` datetime DEFAULT NULL,
  `solution_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '解决方案描述',
  `is_valid_bug` int(11) DEFAULT '1' COMMENT '是否是有效bug，0-无效，1-有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `province_id` int(11) DEFAULT NULL,
  `url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `finish` int(255) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=405 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for cron_job_plan_relation
-- ----------------------------
DROP TABLE IF EXISTS `cron_job_plan_relation`;
CREATE TABLE `cron_job_plan_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_cron_job_id` int(11) DEFAULT NULL,
  `plan_id` int(11) DEFAULT NULL,
  `env_id` int(11) DEFAULT NULL,
  `run_type` int(11) DEFAULT '1',
  `status` int(11) DEFAULT '0' COMMENT '0-正常1-删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
-- Table structure for db_config
-- ----------------------------
DROP TABLE IF EXISTS `db_config`;
CREATE TABLE `db_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `env_id` int(11) DEFAULT NULL,
  `db_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `password` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` int(11) DEFAULT '1' COMMENT '1-mysql',
  `status` int(11) DEFAULT '0' COMMENT '0-正常1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for distributed_lock
-- ----------------------------
DROP TABLE IF EXISTS `distributed_lock`;
CREATE TABLE `distributed_lock` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `add_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for global_variables
-- ----------------------------
DROP TABLE IF EXISTS `global_variables`;
CREATE TABLE `global_variables` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `value` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `modify_flag` int(11) unsigned DEFAULT '1' COMMENT '是否允许在用例中调用API修改全局变量值，1，允许，0，不允许',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for http_api
-- ----------------------------
DROP TABLE IF EXISTS `http_api`;
CREATE TABLE `http_api` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT '1' COMMENT '1-http(s)',
  `method` int(11) DEFAULT '2' COMMENT '1-GET 2-POST',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接口地址',
  `req_body_type` int(11) DEFAULT '3' COMMENT '1: form-data, 2: x-www-from-urlencoded, 3: json',
  `req_body_form` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `req_body_kv` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `req_headers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `req_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `req_body_other` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `res_body_type` int(11) DEFAULT '1' COMMENT '1-json',
  `res_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `req_body_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `res_body_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for http_mock_rules
-- ----------------------------
DROP TABLE IF EXISTS `http_mock_rules`;
CREATE TABLE `http_mock_rules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `uri` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '接口地址',
  `method` int(11) DEFAULT '1' COMMENT '1-GET 2-POST',
  `response_rule` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `status` int(11) DEFAULT '0' COMMENT '0-正常 1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for http_mock_source_config
-- ----------------------------
DROP TABLE IF EXISTS `http_mock_source_config`;
CREATE TABLE `http_mock_source_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mock_source_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mock_source_port` int(11) DEFAULT NULL,
  `http_protocol_type` int(11) DEFAULT '1' COMMENT '1-http 2-https',
  `status` int(11) DEFAULT '0' COMMENT '0-正常 1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for http_mock_target_config
-- ----------------------------
DROP TABLE IF EXISTS `http_mock_target_config`;
CREATE TABLE `http_mock_target_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mock_target_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mock_target_port` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0-正常 1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for labels
-- ----------------------------
DROP TABLE IF EXISTS `labels`;
CREATE TABLE `labels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `data_type_id` int(11) DEFAULT NULL,
  `project_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `add_user_id` int(11) DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
-- Table structure for mock_agent_instances
-- ----------------------------
DROP TABLE IF EXISTS `mock_agent_instances`;
CREATE TABLE `mock_agent_instances` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `application_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `first_register_time` datetime DEFAULT NULL,
  `last_register_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0-黑1-亮',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for mock_rule_agent_relation
-- ----------------------------
DROP TABLE IF EXISTS `mock_rule_agent_relation`;
CREATE TABLE `mock_rule_agent_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mock_rule_id` int(11) DEFAULT NULL,
  `mock_rule_type` int(11) DEFAULT '1' COMMENT '1-http',
  `mock_agent_id` int(11) DEFAULT NULL,
  `enabled` int(11) DEFAULT '0' COMMENT '0-启用 1-禁用',
  `mock_source_config_id` int(11) DEFAULT NULL,
  `mock_target_config_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0-正常 1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for neighborhood
-- ----------------------------
DROP TABLE IF EXISTS `neighborhood`;
CREATE TABLE `neighborhood` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `street_id` int(11) DEFAULT NULL,
  `finish` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=805926 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for plan_case
-- ----------------------------
DROP TABLE IF EXISTS `plan_case`;
CREATE TABLE `plan_case` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `plan_id` int(11) DEFAULT NULL,
  `case_id` int(11) DEFAULT NULL,
  `seq` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for plan_case_setup
-- ----------------------------
DROP TABLE IF EXISTS `plan_case_setup`;
CREATE TABLE `plan_case_setup` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `plan_id` int(11) DEFAULT NULL,
  `case_id` int(11) DEFAULT NULL,
  `seq` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for plan_case_teardown
-- ----------------------------
DROP TABLE IF EXISTS `plan_case_teardown`;
CREATE TABLE `plan_case_teardown` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `plan_id` int(11) DEFAULT NULL,
  `case_id` int(11) DEFAULT NULL,
  `seq` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for plan_cron_jobs
-- ----------------------------
DROP TABLE IF EXISTS `plan_cron_jobs`;
CREATE TABLE `plan_cron_jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0-启用1-禁用',
  `last_run_time` datetime DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for plan_execute_result
-- ----------------------------
DROP TABLE IF EXISTS `plan_execute_result`;
CREATE TABLE `plan_execute_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_or_case_id` int(11) DEFAULT NULL,
  `plan_or_case_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `result_status` int(11) DEFAULT '1' COMMENT '结果状态1-初始化中2-任务初始化完毕，等待调度执行3-执行中4-执行完成5-暂停中6-用户停止执行 99-异常结束',
  `start_timestamp` bigint(20) DEFAULT NULL,
  `end_timestamp` bigint(20) DEFAULT NULL,
  `total` int(11) DEFAULT '0',
  `success_count` int(11) DEFAULT '0',
  `fail_count` int(11) DEFAULT '0',
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `submitter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `submit_timestamp` bigint(20) DEFAULT NULL,
  `mail_sent` int(11) DEFAULT '0' COMMENT '0-未发送结果邮件1-已发送结果邮件',
  `worker_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行机IP',
  `plan_cron_job_id` int(11) DEFAULT NULL,
  `from_type` int(11) DEFAULT '1' COMMENT '1-计划2-用例3-定时计划',
  `submit_date` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '年月日yyyyMMdd',
  `plan_setup_result_id` int DEFAULT NULL,
  `plan_teardown_result_id` int DEFAULT NULL,
  `plan_case_type` int DEFAULT '0' COMMENT '0-计划用例1-计划前用例2-计划后用例',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for plan_running_config_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `plan_running_config_snapshot`;
CREATE TABLE `plan_running_config_snapshot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_result_id` int(11) DEFAULT NULL,
  `run_type` int(11) DEFAULT '1' COMMENT '1-非组合方式运行2-组合方式运行',
  `runs` int(11) DEFAULT '1',
  `max_occurs` int(11) DEFAULT '10',
  `env_id` int(11) DEFAULT NULL,
  `env_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fail_continue` int(11) DEFAULT '1' COMMENT '用例失败后计划是否继续执行，0-停止执行1-继续执行',
  `plan_variables` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for platform_api
-- ----------------------------
DROP TABLE IF EXISTS `platform_api`;
CREATE TABLE `platform_api` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `define_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for project_user_role
-- ----------------------------
DROP TABLE IF EXISTS `project_user_role`;
CREATE TABLE `project_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `add_user` varchar(255) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `finish` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for release_plans
-- ----------------------------
DROP TABLE IF EXISTS `release_plans`;
CREATE TABLE `release_plans` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `predict_relase_time` datetime DEFAULT NULL COMMENT '预计发布时间',
  `project_id` int(11) DEFAULT NULL,
  `add_user_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `type` int(11) DEFAULT '1' COMMENT '1-常规版本，2-项目版本，3-bugfix版本，4-紧急需求版本，99-其他版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rights
-- ----------------------------
DROP TABLE IF EXISTS `rights`;
CREATE TABLE `rights` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `type` int(11) DEFAULT '1' COMMENT '1-系统权限 2-项目权限',
  `add_time` datetime DEFAULT NULL,
  `add_user` varchar(255) DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0 正常 1 已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `uri` (`uri`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
-- Table structure for role_right
-- ----------------------------
DROP TABLE IF EXISTS `role_right`;
CREATE TABLE `role_right` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `right_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `add_user` varchar(255) DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for run_env
-- ----------------------------
DROP TABLE IF EXISTS `run_env`;
CREATE TABLE `run_env` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `add_time` datetime DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0 正常 1 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for spring_session
-- ----------------------------
DROP TABLE IF EXISTS `spring_session`;
CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) COLLATE utf8mb4_general_ci NOT NULL,
  `SESSION_ID` char(36) COLLATE utf8mb4_general_ci NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for spring_session_attributes
-- ----------------------------
DROP TABLE IF EXISTS `spring_session_attributes`;
CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) COLLATE utf8mb4_general_ci NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`primary_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for street
-- ----------------------------
DROP TABLE IF EXISTS `street`;
CREATE TABLE `street` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `area_id` int(11) DEFAULT NULL,
  `url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `finish` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51711 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for system_stories
-- ----------------------------
DROP TABLE IF EXISTS `system_stories`;
CREATE TABLE `system_stories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `release_plan_id` int(11) DEFAULT NULL COMMENT '所属发布计划id',
  `project_id` int(11) DEFAULT NULL,
  `add_user_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `priority` int(11) DEFAULT '0' COMMENT '0-无1-低2-中3-高',
  `current_stage` int(11) DEFAULT '0' COMMENT '0-未开始，1-设计中，2-编码中，3-联调中，4-SIT中，5-UAT中，6-完成，7-上线等',
  `type` int(11) DEFAULT '1' COMMENT '1-业务功能需求，2-IT优化需求，3-运维优化需求，4-事件处理及bug修复需求，5-IT参数配置及修改，6-性能需求',
  `begin_time` datetime DEFAULT NULL COMMENT '预计开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '预计结束时间',
  `submit_test_time` datetime DEFAULT NULL,
  `predict_cost_time` int(11) DEFAULT '0' COMMENT '预计耗时，单位小时',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for test_cases
-- ----------------------------
DROP TABLE IF EXISTS `test_cases`;
CREATE TABLE `test_cases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT '1' COMMENT '1-接口用例2-性能用例3-界面用例',
  `automatic` int(11) DEFAULT '0' COMMENT '0-手工1-自动化',
  `priority` int(11) DEFAULT '0' COMMENT '0-无1-低2-中3-高',
  `auto_case_id` int(11) DEFAULT NULL COMMENT '自动化用例id',
  `sub_system_id` int(11) DEFAULT NULL,
  `system_story_id` int(11) DEFAULT NULL,
  `precondition` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '前置条件',
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '用例步骤',
  `expect_result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '预期结果',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for test_suites
-- ----------------------------
DROP TABLE IF EXISTS `test_suites`;
CREATE TABLE `test_suites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `release_plan_id` int(11) DEFAULT NULL,
  `project_id` int(11) DEFAULT NULL,
  `add_user_id` int(11) DEFAULT NULL,
  `handler_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `add_user` varchar(255) DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `last_modify_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
