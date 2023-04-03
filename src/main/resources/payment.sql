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

 Date: 03/04/2023 18:30:50
*/

-- ----------------------------
-- Records of payment
-- ----------------------------
INSERT INTO `payment` VALUES (1, 1, 1, 1, '100', '2002-11-14 09:37:23', 'BO1', NULL, '无', '无');
INSERT INTO `payment` VALUES (2, 1, 1, 1, '200', '2023-03-18 12:27:20', 'BO2', '', '无', '无');
INSERT INTO `payment` VALUES (3, 1, 2, 1, '300', '2023-01-01 13:25:30', 'BO3', '', '无', '无');
INSERT INTO `payment` VALUES (4, 1, 3, 1, '400', '2023-01-09 13:25:30', 'BO4', '', '无', '无');
INSERT INTO `payment` VALUES (5, 1, 3, 1, '450', '2023-01-09 13:25:30', 'BO4', '', '无', '无');
INSERT INTO `payment` VALUES (6, 1, 1, 1, '100', '2002-11-14 09:37:23', 'BO5', NULL, '无', '无');
INSERT INTO `payment` VALUES (7, 1, 1, 1, '500', '2023-04-03 20:24:04', 'BO5', NULL, '无', '无');
INSERT INTO `payment` VALUES (8, 1, 1, 1, '300', '2023-04-06 20:24:51', 'BO3', NULL, '无', '无');
INSERT INTO `payment` VALUES (9, 1, 1, 1, '100', '2023-04-08 20:28:05', 'BO1', NULL, '无', '无');
INSERT INTO `payment` VALUES (10, 1, 1, 1, '300', '2023-05-10 20:28:24', '', 'CO1a0bebddc08111ed8be0002b67dd5ef6', '无', '无');
INSERT INTO `payment` VALUES (11, 1, 1, 1, '100', '2023-04-03 20:33:39', 'BO5', NULL, '无', '无');

-- ----------------------------
-- Triggers structure for table payment
-- ----------------------------
DROP TRIGGER IF EXISTS `accountBalanceDecrease`;
delimiter ;;
CREATE TRIGGER `accountBalanceDecrease` AFTER INSERT ON `payment` FOR EACH ROW BEGIN

			UPDATE account_details
			SET balance=balance+0-NEW.amount
			WHERE account_detail_id=NEW.account_detail_id;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
