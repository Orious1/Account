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

 Date: 03/04/2023 18:30:43
*/



-- ----------------------------
-- Records of income
-- ----------------------------
INSERT INTO `income` VALUES (1, 1, 1, 1, '100', '2023-04-03 09:37:23', 'BI17', NULL, '无', '无');
INSERT INTO `income` VALUES (2, 1, 3, 1, '400', '2023-01-09 13:25:30', 'BI17', '', '无', '无');
INSERT INTO `income` VALUES (3, 1, 3, 1, '420', '2023-01-09 13:25:30', 'BI17', '', '无', '无');
INSERT INTO `income` VALUES (4, 1, 1, 1, '100', '2023-04-04 09:37:23', 'BI18', NULL, '无', '无');
INSERT INTO `income` VALUES (5, 1, 1, 1, '160', '2023-04-05 10:50:08', 'BI19', NULL, '无', '无');
INSERT INTO `income` VALUES (6, 1, 1, 1, '130', '2023-04-05 12:50:45', 'BI19', NULL, '无', '无');
INSERT INTO `income` VALUES (7, 1, 1, 1, '600', '2023-04-10 14:57:37', 'BI19', NULL, '无', '无');
INSERT INTO `income` VALUES (8, 1, 1, 1, '300', '2023-05-01 15:07:06', 'BI19', NULL, '无', '无');
INSERT INTO `income` VALUES (9, 1, 1, 1, '300', '2022-03-17 15:20:49', 'BI18', NULL, '无', '无');
INSERT INTO `income` VALUES (10, 1, 1, 1, '200', '2023-04-04 19:18:10', NULL, 'CI1', '无', '无');

-- ----------------------------
-- Triggers structure for table income
-- ----------------------------
DROP TRIGGER IF EXISTS `accountBalanceAdd`;
delimiter ;;
CREATE TRIGGER `accountBalanceAdd` AFTER INSERT ON `income` FOR EACH ROW BEGIN

			UPDATE account_details
			SET balance=balance+0+NEW.amount
			WHERE account_detail_id=NEW.account_detail_id;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
