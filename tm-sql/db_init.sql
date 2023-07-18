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

 Date: 18/07/2023 18:57:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for SPRING_SESSION
-- ----------------------------
DROP TABLE IF EXISTS `SPRING_SESSION`;
CREATE TABLE `SPRING_SESSION`  (
  `PRIMARY_ID` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SESSION_ID` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`) USING BTREE,
  UNIQUE INDEX `SPRING_SESSION_IX1`(`SESSION_ID` ASC) USING BTREE,
  INDEX `SPRING_SESSION_IX2`(`EXPIRY_TIME` ASC) USING BTREE,
  INDEX `SPRING_SESSION_IX3`(`PRINCIPAL_NAME` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for SPRING_SESSION_ATTRIBUTES
-- ----------------------------
DROP TABLE IF EXISTS `SPRING_SESSION_ATTRIBUTES`;
CREATE TABLE `SPRING_SESSION_ATTRIBUTES`  (
  `SESSION_PRIMARY_ID` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`) USING BTREE,
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for api_ip_port_config
-- ----------------------------
DROP TABLE IF EXISTS `api_ip_port_config`;
CREATE TABLE `api_ip_port_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '接口ip端口配置表',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口路径',
  `env_id` int NULL DEFAULT NULL COMMENT '环境',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0 COMMENT '0-正常1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_url_env_id`(`url` ASC, `env_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `city_id` int NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `finish` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3714 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for auto_case_history
-- ----------------------------
DROP TABLE IF EXISTS `auto_case_history`;
CREATE TABLE `auto_case_history`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `auto_case_id` int NULL DEFAULT NULL,
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `group_variables` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1070 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for auto_cases
-- ----------------------------
DROP TABLE IF EXISTS `auto_cases`;
CREATE TABLE `auto_cases`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` int NULL DEFAULT 1 COMMENT '1-普通自动化用例',
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `last_run_env_id` int NULL DEFAULT NULL,
  `group_variables` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10230 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for auto_plans
-- ----------------------------
DROP TABLE IF EXISTS `auto_plans`;
CREATE TABLE `auto_plans`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` int NULL DEFAULT NULL COMMENT '1-普通计划',
  `mail_list` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `max_occurs` int NULL DEFAULT NULL COMMENT '最大并发数',
  `auto_case_run_timeout` int NULL DEFAULT NULL COMMENT '用例执行超时时间，单位：秒',
  `runs` int NULL DEFAULT NULL COMMENT '计划运行次数',
  `env_id` int NULL DEFAULT NULL COMMENT '计划运行环境id',
  `fail_continue` int NULL DEFAULT 1 COMMENT '用例失败后计划是否继续执行，0-停止执行1-继续执行',
  `plan_variables` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10018 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for auto_scripts
-- ----------------------------
DROP TABLE IF EXISTS `auto_scripts`;
CREATE TABLE `auto_scripts`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` int NULL DEFAULT 1 COMMENT '1-shell 2-python',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bugs
-- ----------------------------
DROP TABLE IF EXISTS `bugs`;
CREATE TABLE `bugs`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `system_story_id` int NULL DEFAULT NULL COMMENT '所属系统需求d',
  `project_id` int NULL DEFAULT NULL,
  `add_user_id` int NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `discovery_phase` int NULL DEFAULT 1 COMMENT '发现阶段，1-SIT，2-UAT，3-回归测试，4-冒烟测试，5-集成测试，6-性能测试，7-生产问题，8-联调阶段，9-灰度测试',
  `level` int NULL DEFAULT 50 COMMENT '10-致命，20-严重，30-一般，40-提示，50-建议',
  `status` int NULL DEFAULT 0 COMMENT '0-未解决，1-已解决，2-已拒绝，3-遗留，4-已关闭',
  `handler_user_id` int NULL DEFAULT NULL COMMENT '处理人用户id',
  `from_type` int NULL DEFAULT NULL COMMENT 'bug来源类型',
  `bug_type` int NULL DEFAULT 1 COMMENT '1-代码问题，2-环境问题，3-疑问，4-优化建议，5-安全缺陷，6-与需求不符，7-性能问题',
  `close_user_id` int NULL DEFAULT NULL COMMENT '关闭缺陷的用户id',
  `close_time` datetime NULL DEFAULT NULL,
  `solution_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '解决方案描述',
  `is_valid_bug` int NULL DEFAULT 1 COMMENT '是否是有效bug，0-无效，1-有效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_execute_result_202305
-- ----------------------------
DROP TABLE IF EXISTS `case_execute_result_202305`;
CREATE TABLE `case_execute_result_202305`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_timestamp` bigint NULL DEFAULT NULL,
  `end_timestamp` bigint NULL DEFAULT NULL,
  `result_status` int NULL DEFAULT 1 COMMENT '1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止',
  `seq` int NULL DEFAULT 1,
  `worker_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `case_result_query_index`(`plan_result_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_execute_result_202306
-- ----------------------------
DROP TABLE IF EXISTS `case_execute_result_202306`;
CREATE TABLE `case_execute_result_202306`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_timestamp` bigint NULL DEFAULT NULL,
  `end_timestamp` bigint NULL DEFAULT NULL,
  `result_status` int NULL DEFAULT 1 COMMENT '1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止',
  `seq` int NULL DEFAULT 1,
  `worker_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `case_result_query_index`(`plan_result_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20090 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_execute_result_202307
-- ----------------------------
DROP TABLE IF EXISTS `case_execute_result_202307`;
CREATE TABLE `case_execute_result_202307`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_timestamp` bigint NULL DEFAULT NULL,
  `end_timestamp` bigint NULL DEFAULT NULL,
  `result_status` int NULL DEFAULT 1 COMMENT '1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止',
  `seq` int NULL DEFAULT 1,
  `worker_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `case_result_query_index`(`plan_result_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3535 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_execute_result_202308
-- ----------------------------
DROP TABLE IF EXISTS `case_execute_result_202308`;
CREATE TABLE `case_execute_result_202308`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_timestamp` bigint NULL DEFAULT NULL,
  `end_timestamp` bigint NULL DEFAULT NULL,
  `result_status` int NULL DEFAULT 1 COMMENT '1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止',
  `seq` int NULL DEFAULT 1,
  `worker_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `case_result_query_index`(`plan_result_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_step_execute_result_202305
-- ----------------------------
DROP TABLE IF EXISTS `case_step_execute_result_202305`;
CREATE TABLE `case_step_execute_result_202305`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `start_timestamp` bigint NULL DEFAULT NULL,
  `end_timestamp` bigint NULL DEFAULT NULL,
  `result_status` int NULL DEFAULT 1 COMMENT '用例结果状态 1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止',
  `step_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `result_query_index`(`plan_result_id` ASC, `case_id` ASC, `group_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 165 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_step_execute_result_202306
-- ----------------------------
DROP TABLE IF EXISTS `case_step_execute_result_202306`;
CREATE TABLE `case_step_execute_result_202306`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `start_timestamp` bigint NULL DEFAULT NULL,
  `end_timestamp` bigint NULL DEFAULT NULL,
  `result_status` int NULL DEFAULT 1 COMMENT '用例结果状态 1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止',
  `step_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `result_query_index`(`plan_result_id` ASC, `case_id` ASC, `group_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 192506 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_step_execute_result_202307
-- ----------------------------
DROP TABLE IF EXISTS `case_step_execute_result_202307`;
CREATE TABLE `case_step_execute_result_202307`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `start_timestamp` bigint NULL DEFAULT NULL,
  `end_timestamp` bigint NULL DEFAULT NULL,
  `result_status` int NULL DEFAULT 1 COMMENT '用例结果状态 1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止',
  `step_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `result_query_index`(`plan_result_id` ASC, `case_id` ASC, `group_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29020 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_step_execute_result_202308
-- ----------------------------
DROP TABLE IF EXISTS `case_step_execute_result_202308`;
CREATE TABLE `case_step_execute_result_202308`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `start_timestamp` bigint NULL DEFAULT NULL,
  `end_timestamp` bigint NULL DEFAULT NULL,
  `result_status` int NULL DEFAULT 1 COMMENT '用例结果状态 1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止',
  `step_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `result_query_index`(`plan_result_id` ASC, `case_id` ASC, `group_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_variable_value_result_202305
-- ----------------------------
DROP TABLE IF EXISTS `case_variable_value_result_202305`;
CREATE TABLE `case_variable_value_result_202305`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `variable_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `variable_value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 363 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_variable_value_result_202306
-- ----------------------------
DROP TABLE IF EXISTS `case_variable_value_result_202306`;
CREATE TABLE `case_variable_value_result_202306`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `variable_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `variable_value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 491108 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_variable_value_result_202307
-- ----------------------------
DROP TABLE IF EXISTS `case_variable_value_result_202307`;
CREATE TABLE `case_variable_value_result_202307`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `variable_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `variable_value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 89579 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for case_variable_value_result_202308
-- ----------------------------
DROP TABLE IF EXISTS `case_variable_value_result_202308`;
CREATE TABLE `case_variable_value_result_202308`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `group_no` int NULL DEFAULT NULL,
  `variable_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `variable_value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `province_id` int NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `finish` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 405 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cron_job_plan_relation
-- ----------------------------
DROP TABLE IF EXISTS `cron_job_plan_relation`;
CREATE TABLE `cron_job_plan_relation`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_cron_job_id` int NULL DEFAULT NULL,
  `plan_id` int NULL DEFAULT NULL,
  `env_id` int NULL DEFAULT NULL,
  `run_type` int NULL DEFAULT 1,
  `status` int NULL DEFAULT 0 COMMENT '0-正常1-删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_dict
-- ----------------------------
DROP TABLE IF EXISTS `data_dict`;
CREATE TABLE `data_dict`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `display` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `data_type_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_nodes
-- ----------------------------
DROP TABLE IF EXISTS `data_nodes`;
CREATE TABLE `data_nodes`  (
  `id` int NOT NULL,
  `data_type_id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `project_id` int NULL DEFAULT NULL,
  `is_folder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` int NULL DEFAULT NULL,
  `level` int NULL DEFAULT NULL,
  `seq` int NULL DEFAULT 1,
  `add_time` datetime NULL DEFAULT NULL,
  `add_user_id` int NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `last_modify_user_id` int NULL DEFAULT NULL,
  `parent1` int NULL DEFAULT NULL,
  `parent2` int NULL DEFAULT NULL,
  `parent3` int NULL DEFAULT NULL,
  `parent4` int NULL DEFAULT NULL,
  `parent5` int NULL DEFAULT NULL,
  `parent6` int NULL DEFAULT NULL,
  `parent7` int NULL DEFAULT NULL,
  `parent8` int NULL DEFAULT NULL,
  `parent9` int NULL DEFAULT NULL,
  `parent10` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0,
  PRIMARY KEY (`id`, `data_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_type
-- ----------------------------
DROP TABLE IF EXISTS `data_type`;
CREATE TABLE `data_type`  (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for db_config
-- ----------------------------
DROP TABLE IF EXISTS `db_config`;
CREATE TABLE `db_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `env_id` int NULL DEFAULT NULL,
  `db_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `password` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT 1 COMMENT '1-mysql',
  `status` int NULL DEFAULT 0 COMMENT '0-正常1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for distributed_lock
-- ----------------------------
DROP TABLE IF EXISTS `distributed_lock`;
CREATE TABLE `distributed_lock`  (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for global_variables
-- ----------------------------
DROP TABLE IF EXISTS `global_variables`;
CREATE TABLE `global_variables`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `modify_flag` int UNSIGNED NULL DEFAULT 1 COMMENT '是否允许在用例中调用API修改全局变量值，1，允许，0，不允许',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10019 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for http_api
-- ----------------------------
DROP TABLE IF EXISTS `http_api`;
CREATE TABLE `http_api`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` int NULL DEFAULT 1 COMMENT '1-http(s)',
  `method` int NULL DEFAULT 2 COMMENT '1-GET 2-POST',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口地址',
  `req_body_type` int NULL DEFAULT 3 COMMENT '1: form-data, 2: x-www-from-urlencoded, 3: json',
  `req_body_form` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `req_body_kv` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `req_headers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `req_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `req_body_other` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `res_body_type` int NULL DEFAULT 1 COMMENT '1-json',
  `res_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `req_body_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `res_body_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for http_mock_rules
-- ----------------------------
DROP TABLE IF EXISTS `http_mock_rules`;
CREATE TABLE `http_mock_rules`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `uri` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接口地址',
  `method` int NULL DEFAULT 1 COMMENT '1-GET 2-POST',
  `response_rule` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `status` int NULL DEFAULT 0 COMMENT '0-正常 1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for http_mock_source_config
-- ----------------------------
DROP TABLE IF EXISTS `http_mock_source_config`;
CREATE TABLE `http_mock_source_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mock_source_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mock_source_port` int NULL DEFAULT NULL,
  `http_protocol_type` int NULL DEFAULT 1 COMMENT '1-http 2-https',
  `status` int NULL DEFAULT 0 COMMENT '0-正常 1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for http_mock_target_config
-- ----------------------------
DROP TABLE IF EXISTS `http_mock_target_config`;
CREATE TABLE `http_mock_target_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mock_target_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mock_target_port` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0 COMMENT '0-正常 1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for labels
-- ----------------------------
DROP TABLE IF EXISTS `labels`;
CREATE TABLE `labels`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `data_type_id` int NULL DEFAULT NULL,
  `project_id` int NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `add_user_id` int NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `last_modify_user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 224 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mock_agent_instances
-- ----------------------------
DROP TABLE IF EXISTS `mock_agent_instances`;
CREATE TABLE `mock_agent_instances`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `application_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `first_register_time` datetime NULL DEFAULT NULL,
  `last_register_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0 COMMENT '0-黑1-亮',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mock_rule_agent_relation
-- ----------------------------
DROP TABLE IF EXISTS `mock_rule_agent_relation`;
CREATE TABLE `mock_rule_agent_relation`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mock_rule_id` int NULL DEFAULT NULL,
  `mock_rule_type` int NULL DEFAULT 1 COMMENT '1-http',
  `mock_agent_id` int NULL DEFAULT NULL,
  `enabled` int NULL DEFAULT 0 COMMENT '0-启用 1-禁用',
  `mock_source_config_id` int NULL DEFAULT NULL,
  `mock_target_config_id` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0 COMMENT '0-正常 1-删除',
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for neighborhood
-- ----------------------------
DROP TABLE IF EXISTS `neighborhood`;
CREATE TABLE `neighborhood`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `street_id` int NULL DEFAULT NULL,
  `finish` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 805926 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for plan_case
-- ----------------------------
DROP TABLE IF EXISTS `plan_case`;
CREATE TABLE `plan_case`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `plan_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `seq` int NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 667 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for plan_case_setup
-- ----------------------------
DROP TABLE IF EXISTS `plan_case_setup`;
CREATE TABLE `plan_case_setup`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `plan_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `seq` int NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for plan_case_teardown
-- ----------------------------
DROP TABLE IF EXISTS `plan_case_teardown`;
CREATE TABLE `plan_case_teardown`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `plan_id` int NULL DEFAULT NULL,
  `case_id` int NULL DEFAULT NULL,
  `seq` int NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for plan_cron_jobs
-- ----------------------------
DROP TABLE IF EXISTS `plan_cron_jobs`;
CREATE TABLE `plan_cron_jobs`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0 COMMENT '0-启用1-禁用',
  `last_run_time` datetime NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for plan_execute_result
-- ----------------------------
DROP TABLE IF EXISTS `plan_execute_result`;
CREATE TABLE `plan_execute_result`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_or_case_id` int NULL DEFAULT NULL,
  `plan_or_case_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `result_status` int NULL DEFAULT 1 COMMENT '结果状态1-初始化中2-任务初始化完毕，等待调度执行3-执行中4-执行完成5-暂停中6-用户停止执行 99-异常结束',
  `start_timestamp` bigint NULL DEFAULT NULL,
  `end_timestamp` bigint NULL DEFAULT NULL,
  `total` int NULL DEFAULT 0,
  `success_count` int NULL DEFAULT 0,
  `fail_count` int NULL DEFAULT 0,
  `result_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `submitter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `submit_timestamp` bigint NULL DEFAULT NULL,
  `mail_sent` int NULL DEFAULT 0 COMMENT '0-未发送结果邮件1-已发送结果邮件',
  `worker_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行机IP',
  `plan_cron_job_id` int NULL DEFAULT NULL,
  `from_type` int NULL DEFAULT 1 COMMENT '1-计划2-用例3-定时计划',
  `submit_date` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年月日yyyyMMdd',
  `plan_setup_result_id` int NULL DEFAULT NULL,
  `plan_teardown_result_id` int NULL DEFAULT NULL,
  `plan_case_type` int NULL DEFAULT 0 COMMENT '0-计划用例1-计划前用例2-计划后用例',
  `run_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3465 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for plan_running_config_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `plan_running_config_snapshot`;
CREATE TABLE `plan_running_config_snapshot`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_result_id` int NULL DEFAULT NULL,
  `run_type` int NULL DEFAULT 1 COMMENT '1-非组合方式运行2-组合方式运行',
  `runs` int NULL DEFAULT 1,
  `max_occurs` int NULL DEFAULT 10,
  `env_id` int NULL DEFAULT NULL,
  `env_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fail_continue` int NULL DEFAULT 1 COMMENT '用例失败后计划是否继续执行，0-停止执行1-继续执行',
  `plan_variables` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3462 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
-- Table structure for project_user
-- ----------------------------
DROP TABLE IF EXISTS `project_user`;
CREATE TABLE `project_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `project_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for project_user_role
-- ----------------------------
DROP TABLE IF EXISTS `project_user_role`;
CREATE TABLE `project_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `project_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `role_id` int NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for projects
-- ----------------------------
DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0 COMMENT '0 正常 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `finish` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for release_plans
-- ----------------------------
DROP TABLE IF EXISTS `release_plans`;
CREATE TABLE `release_plans`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `predict_relase_time` datetime NULL DEFAULT NULL COMMENT '预计发布时间',
  `project_id` int NULL DEFAULT NULL,
  `add_user_id` int NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `type` int NULL DEFAULT 1 COMMENT '1-常规版本，2-项目版本，3-bugfix版本，4-紧急需求版本，99-其他版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rights
-- ----------------------------
DROP TABLE IF EXISTS `rights`;
CREATE TABLE `rights`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT 1 COMMENT '1-系统权限 2-项目权限',
  `add_time` datetime NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0 COMMENT '0 正常 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE,
  UNIQUE INDEX `uri`(`uri` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role_right
-- ----------------------------
DROP TABLE IF EXISTS `role_right`;
CREATE TABLE `role_right`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NULL DEFAULT NULL,
  `right_id` int NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `chinese_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL COMMENT '1-系统角色 2-项目角色',
  `add_time` datetime NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for run_env
-- ----------------------------
DROP TABLE IF EXISTS `run_env`;
CREATE TABLE `run_env`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0 COMMENT '0 正常 1 已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for street
-- ----------------------------
DROP TABLE IF EXISTS `street`;
CREATE TABLE `street`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `area_id` int NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `finish` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51711 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for system_stories
-- ----------------------------
DROP TABLE IF EXISTS `system_stories`;
CREATE TABLE `system_stories`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `release_plan_id` int NULL DEFAULT NULL COMMENT '所属发布计划id',
  `project_id` int NULL DEFAULT NULL,
  `add_user_id` int NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `priority` int NULL DEFAULT 0 COMMENT '0-无1-低2-中3-高',
  `current_stage` int NULL DEFAULT 0 COMMENT '0-未开始，1-设计中，2-编码中，3-联调中，4-SIT中，5-UAT中，6-完成，7-上线等',
  `type` int NULL DEFAULT 1 COMMENT '1-业务功能需求，2-IT优化需求，3-运维优化需求，4-事件处理及bug修复需求，5-IT参数配置及修改，6-性能需求',
  `begin_time` datetime NULL DEFAULT NULL COMMENT '预计开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '预计结束时间',
  `submit_test_time` datetime NULL DEFAULT NULL,
  `predict_cost_time` int NULL DEFAULT 0 COMMENT '预计耗时，单位小时',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for test_cases
-- ----------------------------
DROP TABLE IF EXISTS `test_cases`;
CREATE TABLE `test_cases`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` int NULL DEFAULT 1 COMMENT '1-接口用例2-性能用例3-界面用例',
  `automatic` int NULL DEFAULT 0 COMMENT '0-手工1-自动化',
  `priority` int NULL DEFAULT 0 COMMENT '0-无1-低2-中3-高',
  `auto_case_id` int NULL DEFAULT NULL COMMENT '自动化用例id',
  `sub_system_id` int NULL DEFAULT NULL,
  `system_story_id` int NULL DEFAULT NULL,
  `precondition` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '前置条件',
  `steps` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '用例步骤',
  `expect_result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '预期结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for test_suites
-- ----------------------------
DROP TABLE IF EXISTS `test_suites`;
CREATE TABLE `test_suites`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `release_plan_id` int NULL DEFAULT NULL,
  `project_id` int NULL DEFAULT NULL,
  `add_user_id` int NULL DEFAULT NULL,
  `handler_user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `role_id` int NULL DEFAULT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `chinese_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '$2a$04$lCUcajPwUQ.dDtNmA5uT4.PuaBLtYz.xagG7OC.QNWh5WG31tOTCG',
  `add_time` datetime NULL DEFAULT NULL,
  `add_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_modify_time` datetime NULL DEFAULT NULL,
  `last_modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `first_login_time` datetime NULL DEFAULT NULL,
  `department_id` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_2`(`username` ASC) USING BTREE,
  INDEX `username`(`username`(191) ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7074 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
