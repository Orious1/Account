/*
 Navicat Premium Data Transfer

 Source Server         : MyLink
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : account_dbs

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 03/03/2023 14:54:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `account_id` int NOT NULL AUTO_INCREMENT COMMENT '账户id',
  `uid` int NULL DEFAULT NULL,
  `account_type_id` int NULL DEFAULT NULL COMMENT '账户类型id',
  `account_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账户名称',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`account_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for account_details
-- ----------------------------
DROP TABLE IF EXISTS `account_details`;
CREATE TABLE `account_details`  (
  `account_detail_id` int NOT NULL AUTO_INCREMENT COMMENT '账户内容id',
  `account_detail_type_id` int NULL DEFAULT NULL COMMENT '账户内容类型id',
  `account_id` int NULL DEFAULT NULL COMMENT '账户id',
  `uid` int NULL DEFAULT NULL,
  `balance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '余额',
  `budget` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预算',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`account_detail_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for account_details_type
-- ----------------------------
DROP TABLE IF EXISTS `account_details_type`;
CREATE TABLE `account_details_type`  (
  `account_detail_type_id` int NOT NULL AUTO_INCREMENT COMMENT '账户内容类型id',
  `account_detail_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`account_detail_type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for account_type
-- ----------------------------
DROP TABLE IF EXISTS `account_type`;
CREATE TABLE `account_type`  (
  `account_type_id` int NOT NULL AUTO_INCREMENT COMMENT '账户类型id',
  `account_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账户类型名称',
  `account_details_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拥有的内容类型id 1-2-3',
  PRIMARY KEY (`account_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for basic_funds
-- ----------------------------
DROP TABLE IF EXISTS `basic_funds`;
CREATE TABLE `basic_funds`  (
  `fund_id` varchar(255) NOT NULL COMMENT '款项id BO表示基础支出 BI表示基础收入',
  `fund_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '款项名称',
  PRIMARY KEY (`fund_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bookkeeping
-- ----------------------------
DROP TABLE IF EXISTS `bookkeeping`;
CREATE TABLE `bookkeeping`  (
  `bookkeeping_id` int NOT NULL AUTO_INCREMENT COMMENT '账本id',
  `uid` int NULL DEFAULT NULL COMMENT '用户id',
  `bookkeeping_type_id` int NULL DEFAULT NULL COMMENT '账本类型id',
  `bookkeeping_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账本封面',
  `bookkeeping_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账本名称',
  `bookkeeping_period` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账本记账周期',
  `bookkeeping_create_date` timestamp NULL DEFAULT NULL COMMENT '创建日期',
  `bookkeeping_end_date` timestamp NULL DEFAULT NULL COMMENT '结束日期',
  `customed_funds_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '自定义款项id',
  `extra_member1` int NULL DEFAULT NULL COMMENT '额外用户1，使用uid，用于家庭账本',
  `extra_member2` int NULL DEFAULT NULL COMMENT '额外用户2，使用uid，用于家庭账本',
  PRIMARY KEY (`bookkeeping_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bookkeeping_tpye
-- ----------------------------
DROP TABLE IF EXISTS `bookkeeping_tpye`;
CREATE TABLE `bookkeeping_tpye`  (
  `bookkeeping_type_id` int NOT NULL AUTO_INCREMENT COMMENT '账本类型id',
  `bookkeeping_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账本类型名称',
  `bookkeeping_type_funds_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拥有的款项类型id，用-间隔，例如1-2-3',
  PRIMARY KEY (`bookkeeping_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customed_funds
-- ----------------------------
DROP TABLE IF EXISTS `customed_funds`;
CREATE TABLE `customed_funds`  (
  `customed_fund_id` varchar(255) NOT NULL COMMENT '自定义款项id CO表示自定义支出 CI表示自定义收入',
  `uid` int NOT NULL,
  `customed_fund_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
	`bookkeeping_type_id` int NOT NULL,
  PRIMARY KEY (`customed_fund_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for income
-- ----------------------------
DROP TABLE IF EXISTS `income`;
CREATE TABLE `income`  (
  `income_id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `uid` int NULL DEFAULT NULL,
  `bookkeeping_id` int NULL DEFAULT NULL COMMENT '记入账本名称',
  `account_id` int NULL DEFAULT NULL COMMENT '来自的账户名称',
  `amount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '金额',
  `time` timestamp NULL DEFAULT NULL COMMENT '时间',
  `fund_id` varchar(255) NULL DEFAULT NULL COMMENT '款项id 若是自定义款项则此项空',
  `customed_fund_id` varchar(255) NULL DEFAULT NULL COMMENT '自定义款项类型id  若是系统自带款项则此项空',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `enclosure` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件',
  PRIMARY KEY (`income_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`  (
  `payment_id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `uid` int NULL DEFAULT NULL,
  `bookkeeping_id` int NULL DEFAULT NULL COMMENT '记入账本名称',
  `account_id` int NULL DEFAULT NULL COMMENT '来自的账户名称',
  `amount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '金额',
  `time` timestamp NULL DEFAULT NULL COMMENT '时间',
  `fund_id` varchar(255) NULL DEFAULT NULL COMMENT '款项id 若是自定义款项则此项空',
  `customed_fund_id` varchar(255) NULL DEFAULT NULL COMMENT '自定义款项类型id  若是系统自带款项则此项空',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `enclosure` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件',
  PRIMARY KEY (`payment_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for transfer
-- ----------------------------
DROP TABLE IF EXISTS `transfer`;
CREATE TABLE `transfer`  (
  `transfer_id` int NOT NULL AUTO_INCREMENT COMMENT '转账记录id',
  `uid` int NULL DEFAULT NULL,
  `source_account_detail_id` int NULL DEFAULT NULL COMMENT '原账户id',
  `destination_account_detail_id` int NULL DEFAULT NULL COMMENT '目标账户id',
  `amount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '金额',
  `time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`transfer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认为微信名',
  `wxid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信号',
  `register_date` timestamp NULL DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
