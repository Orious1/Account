/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : account

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 03/04/2023 18:30:21
*/



-- ----------------------------
-- Records of bookkeeping
-- ----------------------------
INSERT INTO `bookkeeping` VALUES (1, 1, 1, '红包', '我的账簿1', '一个月', '2002-11-14 09:37:23', '2003-11-14 09:37:23', 'CO1', 2, 3);
INSERT INTO `bookkeeping` VALUES (2, 1, 3, '红色', '我的账簿2', '一个月', '2023-03-11 11:05:40', '2023-03-11 11:05:46', NULL, 2, NULL);
INSERT INTO `bookkeeping` VALUES (3, 1, 2, '红包', '我的账簿3', '一个月', '2002-11-14 09:37:23', '2003-11-14 09:37:23', 'CO1', 2, 3);
INSERT INTO `bookkeeping` VALUES (6, 1, 4, '大海', '我的账簿4', '三个月', '2002-11-14 09:37:23', '2002-11-30 09:37:23', NULL, 2, 3);

SET FOREIGN_KEY_CHECKS = 1;
