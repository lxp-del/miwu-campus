/*
 Navicat Premium Dump SQL

 Source Server         : carbon
 Source Server Type    : MySQL
 Source Server Version : 80012 (8.0.12)
 Source Host           : localhost:3306
 Source Schema         : second_platform

 Target Server Type    : MySQL
 Target Server Version : 80012 (8.0.12)
 File Encoding         : 65001

 Date: 11/04/2026 21:47:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `thread_id` bigint(20) NOT NULL COMMENT '会话ID',
  `sender_id` bigint(20) NOT NULL COMMENT '发送者用户ID',
  `message_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'text' COMMENT 'text, image',
  `content` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '消息内容',
  `is_read` tinyint(4) NULL DEFAULT 0 COMMENT '0-未读 1-已读',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_thread_time`(`thread_id` ASC, `created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_message
-- ----------------------------
INSERT INTO `chat_message` VALUES (1, 1, 1, 'text', '123', 0, '2026-04-05 23:25:32');
INSERT INTO `chat_message` VALUES (2, 1, 1, 'text', '好好哈后你好', 0, '2026-04-05 23:26:11');
INSERT INTO `chat_message` VALUES (3, 1, 3, 'text', '11', 0, '2026-04-05 23:49:38');
INSERT INTO `chat_message` VALUES (4, 1, 3, 'text', '3231', 0, '2026-04-05 23:49:40');
INSERT INTO `chat_message` VALUES (5, 1, 1, 'text', '32131', 0, '2026-04-05 23:50:01');
INSERT INTO `chat_message` VALUES (6, 1, 3, 'text', '312', 0, '2026-04-06 00:05:22');
INSERT INTO `chat_message` VALUES (7, 1, 1, 'text', '312', 0, '2026-04-06 00:05:32');
INSERT INTO `chat_message` VALUES (8, 1, 3, 'text', '3123', 0, '2026-04-06 00:05:34');
INSERT INTO `chat_message` VALUES (9, 1, 1, 'text', '哈哈哈哈哈哈', 0, '2026-04-06 00:05:38');
INSERT INTO `chat_message` VALUES (10, 1, 3, 'text', '我草泥马', 0, '2026-04-06 00:05:42');
INSERT INTO `chat_message` VALUES (11, 1, 1, 'text', '我是张三', 0, '2026-04-06 00:05:52');
INSERT INTO `chat_message` VALUES (12, 1, 3, 'text', '我是李峰', 0, '2026-04-06 00:05:56');
INSERT INTO `chat_message` VALUES (13, 1, 3, 'text', '3123', 0, '2026-04-06 00:06:22');
INSERT INTO `chat_message` VALUES (14, 1, 1, 'text', '31231', 0, '2026-04-06 00:06:25');
INSERT INTO `chat_message` VALUES (15, 1, 1, 'text', '3123', 0, '2026-04-06 00:06:27');
INSERT INTO `chat_message` VALUES (16, 1, 1, 'text', '3123', 0, '2026-04-06 00:06:28');
INSERT INTO `chat_message` VALUES (17, 1, 1, 'text', '3123', 0, '2026-04-06 00:06:30');
INSERT INTO `chat_message` VALUES (18, 1, 1, 'text', '1', 0, '2026-04-06 00:17:16');
INSERT INTO `chat_message` VALUES (19, 1, 1, 'text', '32131', 0, '2026-04-06 00:17:32');
INSERT INTO `chat_message` VALUES (20, 1, 3, 'text', '1', 0, '2026-04-06 00:17:35');
INSERT INTO `chat_message` VALUES (21, 1, 1, 'text', '很好', 0, '2026-04-06 00:17:43');
INSERT INTO `chat_message` VALUES (22, 1, 3, 'text', '1', 0, '2026-04-06 00:24:15');
INSERT INTO `chat_message` VALUES (23, 1, 1, 'text', '2', 0, '2026-04-06 00:24:18');
INSERT INTO `chat_message` VALUES (24, 1, 3, 'text', '3', 0, '2026-04-06 00:24:21');
INSERT INTO `chat_message` VALUES (25, 1, 1, 'text', '3444', 0, '2026-04-06 00:24:27');
INSERT INTO `chat_message` VALUES (26, 1, 3, 'text', '3123', 0, '2026-04-06 00:24:31');
INSERT INTO `chat_message` VALUES (27, 1, 3, 'text', '312', 0, '2026-04-06 00:25:57');
INSERT INTO `chat_message` VALUES (28, 1, 1, 'text', '312', 0, '2026-04-06 00:26:01');
INSERT INTO `chat_message` VALUES (29, 1, 3, 'text', '3213', 0, '2026-04-06 00:26:03');
INSERT INTO `chat_message` VALUES (30, 1, 1, 'text', '312313', 0, '2026-04-06 00:26:06');
INSERT INTO `chat_message` VALUES (31, 1, 1, 'text', '321', 0, '2026-04-06 00:37:06');
INSERT INTO `chat_message` VALUES (32, 1, 3, 'text', '11111111', 0, '2026-04-06 00:37:12');
INSERT INTO `chat_message` VALUES (33, 1, 1, 'text', '测尼玛', 0, '2026-04-06 00:37:42');
INSERT INTO `chat_message` VALUES (34, 1, 1, 'text', '吹牛逼', 0, '2026-04-06 00:37:49');
INSERT INTO `chat_message` VALUES (35, 1, 1, 'text', '1212', 0, '2026-04-06 00:38:01');
INSERT INTO `chat_message` VALUES (36, 1, 1, 'text', '你在哪买', 0, '2026-04-06 00:38:30');
INSERT INTO `chat_message` VALUES (37, 1, 1, 'text', 'hello', 0, '2026-04-06 00:38:41');
INSERT INTO `chat_message` VALUES (38, 1, 3, 'text', '111', 0, '2026-04-06 00:38:53');
INSERT INTO `chat_message` VALUES (39, 1, 3, 'text', '好好哈后', 0, '2026-04-06 00:39:29');
INSERT INTO `chat_message` VALUES (40, 1, 3, 'text', '111', 0, '2026-04-06 00:48:54');
INSERT INTO `chat_message` VALUES (41, 1, 1, 'text', '下哪六', 0, '2026-04-06 00:49:13');
INSERT INTO `chat_message` VALUES (42, 1, 1, 'text', '1', 0, '2026-04-06 00:52:06');
INSERT INTO `chat_message` VALUES (43, 1, 1, 'text', '2', 0, '2026-04-06 00:52:14');
INSERT INTO `chat_message` VALUES (44, 1, 1, 'text', '3', 0, '2026-04-06 00:52:20');
INSERT INTO `chat_message` VALUES (45, 1, 1, 'text', '测', 0, '2026-04-06 00:54:40');
INSERT INTO `chat_message` VALUES (46, 1, 1, 'text', '123', 0, '2026-04-06 00:59:43');
INSERT INTO `chat_message` VALUES (47, 1, 1, 'text', '各个', 0, '2026-04-06 01:01:05');
INSERT INTO `chat_message` VALUES (48, 1, 1, 'text', '32', 0, '2026-04-06 01:02:15');
INSERT INTO `chat_message` VALUES (49, 1, 1, 'text', '321', 0, '2026-04-06 01:02:22');
INSERT INTO `chat_message` VALUES (50, 1, 1, 'text', '321', 0, '2026-04-06 01:02:24');
INSERT INTO `chat_message` VALUES (51, 1, 1, 'text', '32', 0, '2026-04-06 01:02:26');
INSERT INTO `chat_message` VALUES (52, 1, 1, 'text', '44', 0, '2026-04-06 01:08:20');
INSERT INTO `chat_message` VALUES (53, 1, 1, 'text', '55', 0, '2026-04-06 01:08:24');
INSERT INTO `chat_message` VALUES (54, 1, 1, 'text', '66', 0, '2026-04-06 01:08:28');
INSERT INTO `chat_message` VALUES (55, 1, 1, 'text', '321', 0, '2026-04-06 01:09:14');
INSERT INTO `chat_message` VALUES (56, 1, 1, 'text', '1', 0, '2026-04-06 01:10:24');
INSERT INTO `chat_message` VALUES (57, 1, 1, 'text', '2', 0, '2026-04-06 01:10:25');
INSERT INTO `chat_message` VALUES (58, 1, 1, 'text', '3', 0, '2026-04-06 01:10:27');
INSERT INTO `chat_message` VALUES (59, 1, 1, 'text', '4', 0, '2026-04-06 01:10:28');
INSERT INTO `chat_message` VALUES (60, 1, 1, 'text', '6', 0, '2026-04-06 01:10:38');
INSERT INTO `chat_message` VALUES (61, 1, 1, 'text', '3', 0, '2026-04-06 01:10:47');
INSERT INTO `chat_message` VALUES (62, 1, 1, 'text', '1', 0, '2026-04-06 01:10:50');
INSERT INTO `chat_message` VALUES (63, 1, 1, 'text', '哈哈哈', 0, '2026-04-06 01:11:07');
INSERT INTO `chat_message` VALUES (64, 2, 3, 'text', '你好', 0, '2026-04-06 10:32:10');
INSERT INTO `chat_message` VALUES (65, 2, 3, 'text', '哈哈哈', 0, '2026-04-06 10:32:22');
INSERT INTO `chat_message` VALUES (66, 2, 2, 'text', 'nis1', 0, '2026-04-06 10:32:26');
INSERT INTO `chat_message` VALUES (67, 2, 3, 'text', '你说吧', 0, '2026-04-06 10:32:35');
INSERT INTO `chat_message` VALUES (68, 2, 2, 'text', 'nishaa1', 0, '2026-04-06 10:32:41');
INSERT INTO `chat_message` VALUES (69, 2, 3, 'text', 'nmk', 0, '2026-04-06 10:39:29');
INSERT INTO `chat_message` VALUES (70, 2, 2, 'text', '12', 0, '2026-04-06 10:39:34');
INSERT INTO `chat_message` VALUES (71, 2, 3, 'text', 'sb', 0, '2026-04-06 10:39:39');
INSERT INTO `chat_message` VALUES (72, 2, 3, 'text', '12', 0, '2026-04-06 10:39:48');
INSERT INTO `chat_message` VALUES (73, 2, 3, 'text', '21', 0, '2026-04-06 10:39:54');
INSERT INTO `chat_message` VALUES (74, 2, 3, 'text', '32', 0, '2026-04-06 10:40:04');
INSERT INTO `chat_message` VALUES (75, 2, 3, 'text', '1', 0, '2026-04-06 10:41:02');
INSERT INTO `chat_message` VALUES (76, 2, 3, 'text', '1', 0, '2026-04-06 10:41:08');
INSERT INTO `chat_message` VALUES (77, 2, 3, 'text', '3213', 0, '2026-04-06 10:41:11');
INSERT INTO `chat_message` VALUES (78, 2, 2, 'text', '12', 0, '2026-04-06 10:58:31');
INSERT INTO `chat_message` VALUES (79, 2, 2, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png\",\"price\":111,\"title\":\"李峰卖东西\",\"id\":14}', 0, '2026-04-06 10:58:34');
INSERT INTO `chat_message` VALUES (80, 2, 2, 'text', '21', 0, '2026-04-06 10:59:32');
INSERT INTO `chat_message` VALUES (81, 2, 2, 'text', '21', 0, '2026-04-06 11:00:17');
INSERT INTO `chat_message` VALUES (82, 2, 2, 'text', '21', 0, '2026-04-06 11:00:19');
INSERT INTO `chat_message` VALUES (83, 2, 2, 'text', '你哈哈哈哈', 0, '2026-04-06 11:00:29');
INSERT INTO `chat_message` VALUES (84, 2, 2, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png\",\"price\":111,\"title\":\"李峰卖东西\",\"id\":14}', 0, '2026-04-06 11:00:33');
INSERT INTO `chat_message` VALUES (85, 2, 2, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png\",\"price\":111,\"title\":\"李峰卖东西\",\"id\":14}', 0, '2026-04-06 11:01:14');
INSERT INTO `chat_message` VALUES (86, 2, 2, 'text', '我针对性地调整了以下样式细', 0, '2026-04-06 11:06:17');
INSERT INTO `chat_message` VALUES (87, 2, 2, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png\",\"price\":111,\"title\":\"李峰卖东西\",\"id\":14}', 0, '2026-04-06 11:25:14');
INSERT INTO `chat_message` VALUES (88, 2, 2, 'text', '12', 0, '2026-04-06 11:25:16');
INSERT INTO `chat_message` VALUES (89, 2, 2, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png\",\"price\":111,\"title\":\"李峰卖东西\",\"id\":14}', 0, '2026-04-06 11:28:34');
INSERT INTO `chat_message` VALUES (90, 2, 2, 'text', '哈哈哈', 0, '2026-04-06 11:28:38');
INSERT INTO `chat_message` VALUES (91, 1, 1, 'text', '31', 0, '2026-04-06 13:59:41');
INSERT INTO `chat_message` VALUES (92, 1, 1, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/f807ccb2-4c2a-4d09-b383-1abd278abc2c.jpg\",\"price\":11,\"title\":\" Repository navigation Code Issues 2  (2) Pull requests 1  (1) Actions Projects Wiki Security and quality Insights\",\"id\":16}', 0, '2026-04-06 13:59:42');
INSERT INTO `chat_message` VALUES (93, 3, 4, 'text', '3123', 0, '2026-04-06 15:07:29');
INSERT INTO `chat_message` VALUES (94, 4, 4, 'text', '3213', 0, '2026-04-06 15:10:03');
INSERT INTO `chat_message` VALUES (95, 4, 4, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/f807ccb2-4c2a-4d09-b383-1abd278abc2c.jpg\",\"price\":11,\"title\":\" Repository navigation Code Issues 2  (2) Pull requests 1  (1) Actions Projects Wiki Security and quality Insights\",\"id\":16}', 0, '2026-04-06 15:10:04');
INSERT INTO `chat_message` VALUES (96, 4, 3, 'text', 'ok', 0, '2026-04-06 15:10:27');
INSERT INTO `chat_message` VALUES (97, 4, 3, 'text', '会话', 0, '2026-04-06 15:10:36');
INSERT INTO `chat_message` VALUES (98, 4, 3, 'text', '321', 0, '2026-04-06 15:11:30');
INSERT INTO `chat_message` VALUES (99, 4, 4, 'text', '321', 0, '2026-04-06 15:11:32');
INSERT INTO `chat_message` VALUES (100, 4, 3, 'text', '32131', 0, '2026-04-06 15:11:42');
INSERT INTO `chat_message` VALUES (101, 4, 3, 'text', '3213', 0, '2026-04-06 15:12:27');
INSERT INTO `chat_message` VALUES (102, 4, 3, 'text', '312', 0, '2026-04-06 15:12:32');
INSERT INTO `chat_message` VALUES (103, 4, 3, 'text', '3123', 0, '2026-04-06 15:12:55');
INSERT INTO `chat_message` VALUES (104, 4, 3, 'text', '3123', 0, '2026-04-06 15:13:16');
INSERT INTO `chat_message` VALUES (105, 4, 4, 'text', '312', 0, '2026-04-06 15:13:18');
INSERT INTO `chat_message` VALUES (106, 4, 3, 'text', '3231', 0, '2026-04-06 15:13:24');
INSERT INTO `chat_message` VALUES (107, 4, 3, 'text', '32131313', 0, '2026-04-06 15:13:35');
INSERT INTO `chat_message` VALUES (108, 4, 3, 'text', '32', 0, '2026-04-06 15:15:37');
INSERT INTO `chat_message` VALUES (109, 4, 4, 'text', '3123', 0, '2026-04-06 15:15:40');
INSERT INTO `chat_message` VALUES (110, 4, 3, 'text', '312', 0, '2026-04-06 15:16:55');
INSERT INTO `chat_message` VALUES (111, 4, 4, 'text', '312', 0, '2026-04-06 15:16:57');
INSERT INTO `chat_message` VALUES (112, 4, 3, 'text', '312', 0, '2026-04-06 15:18:41');
INSERT INTO `chat_message` VALUES (113, 4, 4, 'text', '312', 0, '2026-04-06 15:18:43');
INSERT INTO `chat_message` VALUES (114, 4, 3, 'text', '3123', 0, '2026-04-06 15:18:45');
INSERT INTO `chat_message` VALUES (115, 4, 4, 'text', '31231', 0, '2026-04-06 15:18:47');
INSERT INTO `chat_message` VALUES (116, 4, 4, 'text', '321', 0, '2026-04-06 15:18:58');
INSERT INTO `chat_message` VALUES (117, 4, 4, 'text', '312313', 0, '2026-04-06 15:19:01');
INSERT INTO `chat_message` VALUES (118, 4, 4, 'text', '哈哈哈哈哈哈哈哈哈', 0, '2026-04-06 15:19:21');
INSERT INTO `chat_message` VALUES (119, 4, 4, 'text', '我喜欢', 0, '2026-04-06 15:19:30');
INSERT INTO `chat_message` VALUES (120, 4, 4, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/f807ccb2-4c2a-4d09-b383-1abd278abc2c.jpg\",\"price\":11,\"title\":\" Repository navigation Code Issues 2  (2) Pull requests 1  (1) Actions Projects Wiki Security and quality Insights\",\"id\":16}', 0, '2026-04-06 15:19:32');
INSERT INTO `chat_message` VALUES (121, 4, 4, 'text', '321313', 0, '2026-04-06 15:25:09');
INSERT INTO `chat_message` VALUES (122, 4, 4, 'text', '321', 0, '2026-04-06 15:26:37');
INSERT INTO `chat_message` VALUES (123, 4, 4, 'text', '3123', 0, '2026-04-06 15:26:43');
INSERT INTO `chat_message` VALUES (124, 4, 4, 'text', '31', 0, '2026-04-06 15:27:24');
INSERT INTO `chat_message` VALUES (125, 4, 4, 'text', '3213313', 0, '2026-04-06 15:27:28');
INSERT INTO `chat_message` VALUES (126, 4, 4, 'text', '321', 0, '2026-04-06 15:28:54');
INSERT INTO `chat_message` VALUES (127, 4, 4, 'text', '312', 0, '2026-04-06 15:28:59');
INSERT INTO `chat_message` VALUES (128, 4, 4, 'text', '1', 0, '2026-04-06 15:29:45');
INSERT INTO `chat_message` VALUES (129, 4, 4, 'text', '2', 0, '2026-04-06 15:29:46');
INSERT INTO `chat_message` VALUES (130, 4, 4, 'text', '3', 0, '2026-04-06 15:29:46');
INSERT INTO `chat_message` VALUES (131, 4, 4, 'text', '1', 0, '2026-04-06 15:30:02');
INSERT INTO `chat_message` VALUES (132, 4, 4, 'text', '2', 0, '2026-04-06 15:30:03');
INSERT INTO `chat_message` VALUES (133, 4, 4, 'text', '3', 0, '2026-04-06 15:30:04');
INSERT INTO `chat_message` VALUES (134, 4, 4, 'text', '1', 0, '2026-04-06 15:30:22');
INSERT INTO `chat_message` VALUES (135, 4, 4, 'text', '2', 0, '2026-04-06 15:30:23');
INSERT INTO `chat_message` VALUES (136, 4, 4, 'text', '3', 0, '2026-04-06 15:30:24');
INSERT INTO `chat_message` VALUES (137, 4, 3, 'text', '1', 0, '2026-04-06 15:30:32');
INSERT INTO `chat_message` VALUES (138, 4, 4, 'text', '2', 0, '2026-04-06 15:30:39');
INSERT INTO `chat_message` VALUES (139, 5, 5, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png\",\"price\":32,\"title\":\"3213\",\"id\":17}', 0, '2026-04-06 18:52:05');
INSERT INTO `chat_message` VALUES (140, 5, 5, 'text', 'ip', 0, '2026-04-06 18:52:09');
INSERT INTO `chat_message` VALUES (141, 2, 2, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png\",\"price\":111,\"title\":\"李峰卖东西\",\"id\":14}', 0, '2026-04-06 20:19:53');
INSERT INTO `chat_message` VALUES (142, 2, 2, 'text', '买', 0, '2026-04-06 20:19:56');
INSERT INTO `chat_message` VALUES (143, 2, 2, 'text', '1', 0, '2026-04-06 20:20:27');
INSERT INTO `chat_message` VALUES (144, 2, 3, 'text', '22', 0, '2026-04-06 20:20:31');
INSERT INTO `chat_message` VALUES (145, 2, 2, 'text', 'cnm', 0, '2026-04-06 20:24:34');
INSERT INTO `chat_message` VALUES (146, 2, 2, 'text', '1', 0, '2026-04-06 20:24:42');
INSERT INTO `chat_message` VALUES (147, 2, 2, 'text', '2', 0, '2026-04-06 20:24:42');
INSERT INTO `chat_message` VALUES (148, 2, 2, 'text', '3', 0, '2026-04-06 20:24:43');
INSERT INTO `chat_message` VALUES (149, 6, 2, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png\",\"price\":32,\"title\":\"3213\",\"id\":17}', 0, '2026-04-06 20:37:04');
INSERT INTO `chat_message` VALUES (150, 2, 2, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/yan0.jpg\",\"price\":39,\"title\":\"考研政治冲刺\",\"id\":35}', 0, '2026-04-11 00:42:14');
INSERT INTO `chat_message` VALUES (151, 2, 2, 'text', '我很喜欢这本书', 0, '2026-04-11 00:42:20');
INSERT INTO `chat_message` VALUES (152, 2, 3, 'text', '好的', 0, '2026-04-11 00:42:50');
INSERT INTO `chat_message` VALUES (153, 7, 2, 'text', '我需要购买', 0, '2026-04-11 00:43:14');
INSERT INTO `chat_message` VALUES (154, 7, 2, 'text', '我需要购买', 0, '2026-04-11 00:43:37');
INSERT INTO `chat_message` VALUES (155, 8, 2, 'text', '321', 0, '2026-04-11 00:43:43');
INSERT INTO `chat_message` VALUES (156, 2, 3, 'goods', '{\"imageUrls\":\"https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/3ae63ab1-054c-40db-afeb-84fce6fd1b9d.jpg\",\"price\":288.8,\"title\":\"二手耳机\",\"id\":76}', 0, '2026-04-11 00:46:50');
INSERT INTO `chat_message` VALUES (157, 2, 3, 'text', '耳机是崭新的嘛', 0, '2026-04-11 00:46:59');
INSERT INTO `chat_message` VALUES (158, 2, 2, 'text', '是的', 0, '2026-04-11 00:47:27');

-- ----------------------------
-- Table structure for donation_items
-- ----------------------------
DROP TABLE IF EXISTS `donation_items`;
CREATE TABLE `donation_items`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '价格（0表示免费）',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图片URL，逗号分隔',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布人昵称',
  `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `campus` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '校区',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'available' COMMENT '状态：available / traded / donated',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_campus`(`campus` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '捐赠物品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of donation_items
-- ----------------------------
INSERT INTO `donation_items` VALUES (1, '大学英语教材', '全新未使用的大学英语四级教材，包含听力光盘', 0.00, '书籍', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/yan0.jpg', '小明', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/ed7a420a-98aa-4e5b-b01f-bb751301ebe0.jpg', '主校区', 'available', '2026-04-10 23:40:18', '2026-04-11 00:23:39');
INSERT INTO `donation_items` VALUES (2, '笔记本电脑', '使用一年的笔记本电脑，配置良好，适合学习使用', 0.00, '电子产品', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/xmn0.jpg', '小红', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/ed7a420a-98aa-4e5b-b01f-bb751301ebe0.jpg', '东校区', 'available', '2026-04-10 23:40:18', '2026-04-11 00:23:58');
INSERT INTO `donation_items` VALUES (4, '蓝牙耳机', '全新的蓝牙耳机，音质清晰', 0.00, '电子产品', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/erji.jpg', '小张', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/ed7a420a-98aa-4e5b-b01f-bb751301ebe0.jpg', '西校区', 'available', '2026-04-10 23:40:18', '2026-04-11 00:24:22');
INSERT INTO `donation_items` VALUES (5, '考研资料', '全套考研复习资料，包含真题和笔记', 0.00, '书籍', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/sssss8.jpg', '小王', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/ed7a420a-98aa-4e5b-b01f-bb751301ebe0.jpg', '主校区', 'donated', '2026-04-10 23:40:18', '2026-04-11 00:24:36');
INSERT INTO `donation_items` VALUES (7, '书籍', '高等数学书籍', 0.00, '书籍', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/sssss8.jpg', NULL, NULL, NULL, 'available', '2026-04-11 00:21:42', '2026-04-11 00:24:41');
INSERT INTO `donation_items` VALUES (8, '高等数学书', '捐助衣物和书籍', 0.00, '书籍', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/729f0ef4-339d-4166-b20a-40eed5c40abc.jpg', NULL, NULL, NULL, 'available', '2026-04-11 00:33:20', '2026-04-11 00:33:20');
INSERT INTO `donation_items` VALUES (9, '高等数学书', '捐赠书籍', 0.00, '书籍', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/b7efd29d-4159-4d57-9bb0-3b20c9e0836e.jpg', NULL, NULL, NULL, 'available', '2026-04-11 00:41:06', '2026-04-11 00:41:06');
INSERT INTO `donation_items` VALUES (10, '高数书籍', '捐赠书籍', 0.00, '书籍', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/9c8020aa-8c3d-419d-91f4-8e418e5aed0d.jpg', NULL, NULL, NULL, 'available', '2026-04-11 00:49:22', '2026-04-11 00:49:22');

-- ----------------------------
-- Table structure for forum_post
-- ----------------------------
DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE `forum_post`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布人用户ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子标题',
  `image_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '帖子封面图片URL',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子正文内容',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态 (0:草稿, 1:已发布, 2:已删除)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '论坛帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_post
-- ----------------------------
INSERT INTO `forum_post` VALUES (1, 1, '哈哈哈哈哈哈哈哈', NULL, '嘻嘻嘻嘻嘻嘻嘻嘻', 1, '2026-04-06 13:36:47', '2026-04-06 13:37:08');
INSERT INTO `forum_post` VALUES (2, 2, '哈哈哈', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/717fcefa-2a0d-4b76-b854-0542a1a1dcaa.png', '好好哈后', 1, '2026-04-06 13:38:24', '2026-04-06 13:51:07');
INSERT INTO `forum_post` VALUES (3, 2, '测测', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/856f2bb1-60e5-4db9-b474-08b0c6985131.jpg,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/48c939b2-9728-4bfc-88d4-59ebc1322336.png', '水水水水水·', 1, '2026-04-06 13:54:09', '2026-04-06 13:59:13');
INSERT INTO `forum_post` VALUES (4, 2, '122222222222222', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/732d0945-1155-40d5-85e9-5c8272c202ba.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/e4756992-8af9-42d8-9650-a405e1c3b417.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/2b3e80ee-b79e-474f-a41e-f94eb1305a1e.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/00d43890-ffe1-473c-9c02-273e36048b18.jpg,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/91722496-7815-4843-9ab5-8af318030dd9.png', '222222222222222222222222', 1, '2026-04-06 13:57:17', '2026-04-06 13:59:13');
INSERT INTO `forum_post` VALUES (5, 1, '3213', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/528fac31-f0d1-43a2-8089-a6f5250a7c83.png', '2113', 1, '2026-04-06 14:03:11', '2026-04-06 14:03:11');
INSERT INTO `forum_post` VALUES (6, 2, '键盘有人需要吗', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/90d200cf-ad24-4e03-b5bb-f37d4bc40772.jpg', '效果很好，希望大家看看', 1, '2026-04-11 00:32:28', '2026-04-11 00:32:28');
INSERT INTO `forum_post` VALUES (7, 2, '有人需要考研资料吗', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/2ae1d4bc-6f32-4003-aa91-833a358c0d5e.jpg', '书籍是崭新的', 1, '2026-04-11 00:40:24', '2026-04-11 00:40:24');
INSERT INTO `forum_post` VALUES (8, 3, '需要鼠标嘛', 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/c3759264-d11d-4b0b-bd85-e00a354b0f93.jpg', '崭新的', 1, '2026-04-11 00:48:47', '2026-04-11 00:48:47');

-- ----------------------------
-- Table structure for goods_topic
-- ----------------------------
DROP TABLE IF EXISTS `goods_topic`;
CREATE TABLE `goods_topic`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `topic` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '话题名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品话题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_topic
-- ----------------------------
INSERT INTO `goods_topic` VALUES (11, '全站热搜');
INSERT INTO `goods_topic` VALUES (12, '9.9元包邮');
INSERT INTO `goods_topic` VALUES (13, '每日必抢');
INSERT INTO `goods_topic` VALUES (14, '开学季大促');
INSERT INTO `goods_topic` VALUES (15, '毕业季清仓');
INSERT INTO `goods_topic` VALUES (16, '学长学姐推荐');
INSERT INTO `goods_topic` VALUES (17, '全新未拆封');
INSERT INTO `goods_topic` VALUES (18, '急出回血');
INSERT INTO `goods_topic` VALUES (19, '免费赠送');
INSERT INTO `goods_topic` VALUES (20, '捡漏专区');

-- ----------------------------
-- Table structure for goods_type
-- ----------------------------
DROP TABLE IF EXISTS `goods_type`;
CREATE TABLE `goods_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类名称/标签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_type
-- ----------------------------
INSERT INTO `goods_type` VALUES (1, '数码好物');
INSERT INTO `goods_type` VALUES (2, '宿舍神器');
INSERT INTO `goods_type` VALUES (3, '考研资料');
INSERT INTO `goods_type` VALUES (4, '毕业清仓');
INSERT INTO `goods_type` VALUES (5, '教材课本');
INSERT INTO `goods_type` VALUES (6, '骑行代步');
INSERT INTO `goods_type` VALUES (7, '美妆护肤');
INSERT INTO `goods_type` VALUES (8, '运动健身');
INSERT INTO `goods_type` VALUES (9, '游戏电竞');
INSERT INTO `goods_type` VALUES (10, '生活日用');

-- ----------------------------
-- Table structure for message_thread
-- ----------------------------
DROP TABLE IF EXISTS `message_thread`;
CREATE TABLE `message_thread`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `participant_a_id` bigint(20) NOT NULL COMMENT '参与者A的用户ID',
  `participant_b_id` bigint(20) NOT NULL COMMENT '参与者B的用户ID',
  `good_id` bigint(20) NULL DEFAULT NULL COMMENT '关联商品ID  ',
  `last_message` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '最后一条消息摘要',
  `last_message_time` timestamp NULL DEFAULT NULL COMMENT '最后消息时间',
  `participant_a_unread_count` int(11) NULL DEFAULT 0 COMMENT '参与者A的未读数',
  `participant_b_unread_count` int(11) NULL DEFAULT 0 COMMENT '参与者B的未读数',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '1-正常 0-删除',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_participants`(`participant_a_id` ASC, `participant_b_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_thread
-- ----------------------------
INSERT INTO `message_thread` VALUES (1, 1, 3, 14, '{\"imageUrls\":\"https:...', '2026-04-06 13:59:42', 0, 0, 1, '2026-04-05 23:25:32', '2026-04-05 23:25:32');
INSERT INTO `message_thread` VALUES (2, 2, 3, 15, '是的', '2026-04-11 00:47:28', 0, 0, 1, '2026-04-06 10:32:10', '2026-04-06 10:32:10');
INSERT INTO `message_thread` VALUES (3, 1, 4, 13, '3123', '2026-04-06 15:07:29', 1, 0, 1, '2026-04-06 15:07:29', '2026-04-06 15:07:29');
INSERT INTO `message_thread` VALUES (4, 3, 4, 16, '2', '2026-04-06 15:30:40', 0, 15, 1, '2026-04-06 15:10:03', '2026-04-06 15:10:03');
INSERT INTO `message_thread` VALUES (5, 1, 5, 17, 'ip', '2026-04-06 18:52:09', 2, 0, 1, '2026-04-06 18:52:05', '2026-04-06 18:52:05');
INSERT INTO `message_thread` VALUES (6, 1, 2, 17, '{\"imageUrls\":\"https:...', '2026-04-06 20:37:04', 1, 0, 1, '2026-04-06 20:37:04', '2026-04-06 20:37:04');
INSERT INTO `message_thread` VALUES (7, 2, 2, 15, '我需要购买', '2026-04-11 00:43:38', 0, 0, 1, '2026-04-11 00:43:14', '2026-04-11 00:43:14');
INSERT INTO `message_thread` VALUES (8, 2, 4, 16, '321', '2026-04-11 00:43:43', 0, 1, 1, '2026-04-11 00:43:43', '2026-04-11 00:43:43');

-- ----------------------------
-- Table structure for order_comments
-- ----------------------------
DROP TABLE IF EXISTS `order_comments`;
CREATE TABLE `order_comments`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `good_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '关联商品ID',
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '评论用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `star_rating` tinyint(3) UNSIGNED NULL DEFAULT 5 COMMENT '评分(1-5星)',
  `create_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_comments
-- ----------------------------
INSERT INTO `order_comments` VALUES (3, 15, 2, '哈哈哈哈哈哈', 5, 2, '2026-04-06 12:10:31', NULL, '2026-04-06 12:37:24', '芜湖市');
INSERT INTO `order_comments` VALUES (7, 15, 2, '我在哪', 5, 2, '2026-04-06 12:24:57', NULL, '2026-04-06 12:37:24', '芜湖市');
INSERT INTO `order_comments` VALUES (8, 15, 2, '11', 5, 2, '2026-04-06 12:29:00', NULL, '2026-04-06 12:37:24', '芜湖市');
INSERT INTO `order_comments` VALUES (9, 15, 2, '测试', 5, 2, '2026-04-06 12:36:31', NULL, '2026-04-06 12:36:31', '芜湖市');
INSERT INTO `order_comments` VALUES (10, 14, 2, '11', 5, 2, '2026-04-06 12:43:55', NULL, '2026-04-06 12:43:55', '芜湖市');
INSERT INTO `order_comments` VALUES (11, 16, 3, '11', 5, 3, '2026-04-06 13:16:05', NULL, '2026-04-06 13:16:05', '芜湖市');
INSERT INTO `order_comments` VALUES (12, 14, 4, 'ces1', 5, 4, '2026-04-06 14:56:36', NULL, '2026-04-06 14:56:36', '芜湖市');
INSERT INTO `order_comments` VALUES (13, 16, 4, '231', 5, 4, '2026-04-06 15:09:58', NULL, '2026-04-06 15:09:58', '芜湖市');
INSERT INTO `order_comments` VALUES (14, 11, 2, '12', 5, 2, '2026-04-06 17:08:31', NULL, '2026-04-06 17:08:31', '芜湖市');
INSERT INTO `order_comments` VALUES (15, 17, 5, 'hjkh', 5, 5, '2026-04-06 18:51:59', NULL, '2026-04-06 18:51:59', '芜湖市');
INSERT INTO `order_comments` VALUES (16, 17, 2, '你好', 5, 2, '2026-04-06 20:19:32', NULL, '2026-04-06 20:19:32', '芜湖市');
INSERT INTO `order_comments` VALUES (17, 17, 2, '231231', 5, 2, '2026-04-06 20:22:40', NULL, '2026-04-06 20:22:40', '芜湖市');
INSERT INTO `order_comments` VALUES (18, 17, 2, '31233', 5, 2, '2026-04-06 20:22:43', NULL, '2026-04-06 20:22:43', '芜湖市');
INSERT INTO `order_comments` VALUES (19, 17, 2, '222', 5, 2, '2026-04-06 20:37:00', NULL, '2026-04-06 20:37:00', '芜湖市');
INSERT INTO `order_comments` VALUES (20, 21, 2, 'hhhh1', 5, 2, '2026-04-06 20:41:51', NULL, '2026-04-06 20:41:51', '芜湖市');
INSERT INTO `order_comments` VALUES (21, 13, 2, '11', 5, 2, '2026-04-06 21:32:32', NULL, '2026-04-06 21:32:32', '芜湖市');
INSERT INTO `order_comments` VALUES (22, 13, 3, '测试', 5, 3, '2026-04-10 22:56:22', NULL, '2026-04-10 22:56:22', '芜湖市');
INSERT INTO `order_comments` VALUES (23, 44, 2, '我很喜欢', 5, 2, '2026-04-11 00:37:04', NULL, '2026-04-11 00:37:04', '芜湖市');
INSERT INTO `order_comments` VALUES (24, 44, 2, '这个书籍很棒', 5, 2, '2026-04-11 00:38:45', NULL, '2026-04-11 00:38:45', '芜湖市');
INSERT INTO `order_comments` VALUES (25, 35, 2, '书籍很棒嘛', 5, 2, '2026-04-11 00:42:08', NULL, '2026-04-11 00:42:08', '芜湖市');
INSERT INTO `order_comments` VALUES (26, 36, 2, '我很喜欢', 5, 2, '2026-04-11 00:45:04', NULL, '2026-04-11 00:45:04', '芜湖市');
INSERT INTO `order_comments` VALUES (27, 76, 3, '我喜欢', 5, 3, '2026-04-11 00:46:46', NULL, '2026-04-11 00:46:46', '芜湖市');
INSERT INTO `order_comments` VALUES (28, 44, 3, '我也很喜欢', 5, 3, '2026-04-11 00:50:12', NULL, '2026-04-11 00:50:12', '芜湖市');

-- ----------------------------
-- Table structure for order_goods
-- ----------------------------
DROP TABLE IF EXISTS `order_goods`;
CREATE TABLE `order_goods`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `image_urls` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片URL',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容详情',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '价格',
  `topic` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '话题标签',
  `is_published` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否发布 (0:未发布, 1:已发布)',
  `type_id` int(10) UNSIGNED NOT NULL COMMENT '所属类型ID (关联类型表)',
  `view_permission` tinyint(4) NOT NULL DEFAULT 1 COMMENT '查看权限 (1:公开, 2:仅好友用户, 3:仅自己)',
  `delivery_method` tinyint(4) NOT NULL DEFAULT 1 COMMENT '发货方式 (1:快递, 2:自提, 3:无需物流)',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '商品状态 (0:下架, 1:上架, 2:售罄, 3:审核中)',
  `create_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_type_id`(`type_id` ASC) USING BTREE,
  INDEX `idx_is_published`(`is_published` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_goods
-- ----------------------------
INSERT INTO `order_goods` VALUES (10, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png', '呱呱呱呱呱呱呱呱呱', '冯绍峰士大夫十分', 212.00, '12,13,11', 1, 1, 1, 1, 0, '3', '2026-04-03 23:28:54', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (11, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png', '321312331', '31231', 313.00, '11,12', 1, 1, 1, 1, 0, '3', '2026-04-03 23:29:36', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (12, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png', '直到我尝试了 图书馆8小时原则直到我尝试 ', '真的很喜欢在图书馆学习呀大学独处挺好的，不过大学里有严重的信息差还是很难过我也经历过，我身边厉害的大学生每个假期都在努力提升自己，我也给上进的大学生宝拉了个大学生自律成长的quan子，分享实习，大学规划，经济独立的经验，打破信息差，还给里面的宝分享7000字大学规划表，有一起来的宝吗，笔记下面可以找到哦', 98.00, '11,12,13', 1, 1, 1, 1, 0, '3', '2026-04-03 23:30:31', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (13, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png', '售卖苹果', '经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。', 144.00, '', 1, 1, 1, 1, 0, '3', '2026-04-04 22:50:33', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (14, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png', '李峰卖东西', '经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。', 111.00, '13,12,11', 1, 1, 1, 1, 0, '3', '2026-04-05 16:19:42', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (15, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/a29ee5e5-1534-4159-9bfb-377df523892d.png', '理然速干木质香发胶定型喷雾男士发泥定型发', '对比了好几种发胶，大牌子的发胶就是比杂牌小店的好太多了，首先味道不是那种廉价香精味，是很高级的木质香，很像爱马仕的一款香水。定型的时候不会结块，喷完头发很哑光，颅顶能立住一整天，哈尔滨的春天kuku刮大风头发也顶的住。之前买20几块钱的杂牌用久', 123.00, '12,13', 1, 1, 1, 1, 0, '3', '2026-04-06 10:31:00', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (16, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/f807ccb2-4c2a-4d09-b383-1abd278abc2c.jpg', ' Repository navigation Code Issues 2  (2) Pull requests 1  (1) Actions Projects Wiki Security and quality Insights', '\nRepository navigation\nCode\nIssues\n2\n (2)\nPull requests\n1\n (1)\nActions\nProjects\nWiki\nSecurity and quality\nInsights\nRepository navigation\nCode\nIssues\n2\n (2)\nPull requests\n1\n (1)\nActions\nProjects\nWiki\nSecurity and quality\nInsights\nRepository navigation\nCode\nIssues\n2\n (2)\nPull requests\n1\n (1)\nActions\nProjects\nWiki\nSecurity and quality\nInsights\nRepository navigation\nCode\nIssues\n2\n (2)\nPull requests\n1\n (1)\nActions\nProjects\nWiki\nSecurity and quality\nInsights', 11.00, '12,13', 1, 1, 1, 1, 0, '3', '2026-04-06 13:15:55', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (17, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/98a19236-63c1-47a6-a57b-4e248e33b6c2.png', '3213', '2133\n经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。', 32.00, '12,13', 1, 1, 1, 1, 0, '3', '2026-04-06 14:37:20', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (18, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/34d44971-47f4-46b2-9f15-d123cc3f3f82.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/c83ac4b5-f64e-4e3d-819f-dfdb7318f57f.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/b9b18270-4e0e-421e-a82e-0b5320f6f839.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/cdabfa8d-bf47-4cc0-8ccb-670e89f9f854.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/feeb2de5-ec60-41f6-93f4-d0fd4ff1a82b.png', '1', '1', 111.00, '11,12,13', 1, 1, 1, 1, 0, '3', '2026-04-06 20:27:31', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (19, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/62e50208-3894-4b70-816a-508b10baf6c8.png', '111111111111111111111111111111111111111', '经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。', 111.00, '11', 1, 1, 1, 1, 0, '3', '2026-04-06 20:39:14', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (20, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/2872bd39-70c0-4103-9c07-a895977d14d5.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/4462d980-21a0-4727-8526-b8cd72991826.png', '3213', '经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。', 312.00, '12,13,11', 1, 1, 1, 1, 0, '3', '2026-04-06 20:40:41', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (21, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/c84ed113-3174-40e8-aab1-7a961fcde2b6.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/c8d5fa98-12b0-4dff-a48b-ff043d95ccd0.png,https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/71733858-64e1-4e90-89f8-a9ab267b968d.png', '32331', '经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。', 32.00, '11,12,13,14,15', 1, 1, 1, 1, 0, '3', '2026-04-06 20:41:42', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (22, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/09a81be5-6c66-4074-b54f-c7ff629adb4b.jpg', '校园考研桌椅', '屁股坐穿，成功上岸！出一套陪伴我度过无数个日夜的“战友”——考研专用桌椅。', 45.00, '11,13,14', 1, 1, 1, 1, 0, '3', '2026-04-07 22:22:41', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (23, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/sssss8.jpg', '二手英语六级资料出手', '英语渣逆袭！出陪伴我考研全程的英语全家桶。买资料送备考思路，让你少走半年弯路。', 12.50, '11,14,15', 1, 1, 1, 1, 0, '3', '2026-04-07 22:25:15', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (24, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/O1CN01myRcBL1oRGzTUKnGO_%21%212219435195221.jpg', '机械键盘', 'cherry轴机械键盘，手感超棒', 299.00, '数码好物', 1, 1, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (25, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/sb0.jpg', '无线鼠标', '罗技无线鼠标，续航久', 99.00, '数码好物', 1, 2, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (26, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/erji.jpg', '蓝牙耳机', '降噪蓝牙耳机，音质清晰', 199.00, '数码好物', 1, 3, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (27, 'https://example.com/image4.jpg', '移动硬盘', '1T移动硬盘，存储无忧', 399.00, '数码好物', 1, 4, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (28, 'https://example.com/image5.jpg', '平板电脑', '10.4英寸平板，学习办公两不误', 1299.00, '数码好物', 1, 5, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (29, 'https://example.com/image6.jpg', '床上书桌', '可折叠床上书桌，宿舍必备', 59.00, '宿舍神器', 1, 6, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (30, 'https://example.com/image7.jpg', '收纳盒', '多层收纳盒，整理宿舍杂物', 39.00, '宿舍神器', 1, 7, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (31, 'https://example.com/image8.jpg', '台灯', '护眼台灯，宿舍学习好伴侣', 79.00, '宿舍神器', 1, 8, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (32, 'https://example.com/image9.jpg', '晾衣架', '伸缩晾衣架，宿舍阳台适用', 29.00, '宿舍神器', 1, 9, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (33, 'https://example.com/image10.jpg', '小风扇', 'USB小风扇，夏日宿舍清凉', 19.00, '宿舍神器', 1, 10, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (34, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/sssss8.jpg', '考研英语真题', '近10年考研英语真题详解', 49.00, '考研资料', 1, 1, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (35, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/yan0.jpg', '考研政治冲刺', '肖秀荣考研政治冲刺背诵手册', 39.00, '考研资料', 1, 2, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (36, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/shuxue3609.jpg', '考研数学复习全书', '李永乐考研数学复习全书', 59.00, '考研资料', 1, 3, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (37, 'https://example.com/image14.jpg', '考研专业课笔记', '某高校计算机专业课笔记', 29.00, '考研资料', 1, 4, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (38, 'https://example.com/image15.jpg', '考研复试指南', '考研复试流程与技巧指南', 19.00, '考研资料', 1, 5, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (39, 'https://example.com/image16.jpg', '二手自行车', '九成新山地自行车，毕业低价出', 199.00, '毕业清仓', 1, 6, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (40, 'https://example.com/image17.jpg', '旧教材', '大学四年教材打包出售', 99.00, '毕业清仓', 1, 7, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (41, 'https://example.com/image18.jpg', '宿舍用品', '床上四件套+收纳箱，毕业带不走', 129.00, '毕业清仓', 1, 8, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (42, 'https://example.com/image19.jpg', '电子产品', '旧手机+旧平板，低价处理', 299.00, '毕业清仓', 1, 9, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (43, 'https://example.com/image20.jpg', '运动器材', '哑铃+瑜伽垫，毕业闲置', 89.00, '毕业清仓', 1, 10, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (44, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/shuxue3609.jpg', '高等数学', '同济大学高等数学第七版', 45.00, '教材课本', 1, 1, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (45, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/sssss8.jpg', '大学英语', '新视野大学英语第三版', 35.00, '教材课本', 1, 2, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (46, 'https://example.com/image23.jpg', '大学物理', '张三慧大学物理第二版', 40.00, '教材课本', 1, 3, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (47, 'https://example.com/image24.jpg', '计算机基础', '大学计算机基础教程', 30.00, '教材课本', 1, 4, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (48, 'https://example.com/image25.jpg', '线性代数', '同济线性代数第六版', 38.00, '教材课本', 1, 5, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (49, 'https://example.com/image26.jpg', '折叠自行车', '16寸折叠自行车，通勤方便', 399.00, '骑行代步', 1, 6, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (50, 'https://example.com/image27.jpg', '电动滑板车', '迷你电动滑板车，续航20km', 899.00, '骑行代步', 1, 7, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (51, 'https://example.com/image28.jpg', '平衡车', '智能平衡车，儿童成人可用', 699.00, '骑行代步', 1, 8, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (52, 'https://example.com/image29.jpg', '自行车头盔', '专业骑行头盔，安全防护', 99.00, '骑行代步', 1, 9, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (53, 'https://example.com/image30.jpg', '骑行手套', '透气骑行手套，防滑减震', 49.00, '骑行代步', 1, 10, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (54, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/xmn0.jpg', '洗面奶', '氨基酸洗面奶，温和清洁', 59.00, '美妆护肤', 1, 1, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (55, 'https://example.com/image32.jpg', '面霜', '保湿面霜，滋润不油腻', 89.00, '美妆护肤', 1, 2, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (56, 'https://example.com/image33.jpg', '口红', '迪奥999口红，经典正红', 299.00, '美妆护肤', 1, 3, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (57, 'https://example.com/image34.jpg', '眼影盘', '12色眼影盘，日常百搭', 129.00, '美妆护肤', 1, 4, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (58, 'https://example.com/image35.jpg', '防晒霜', 'SPF50+防晒霜，户外必备', 79.00, '美妆护肤', 1, 5, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (59, 'https://example.com/image36.jpg', '哑铃', '可调节哑铃，居家健身', 129.00, '运动健身', 1, 6, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (60, 'https://example.com/image37.jpg', '瑜伽垫', '加厚瑜伽垫，防滑舒适', 59.00, '运动健身', 1, 7, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (61, 'https://example.com/image38.jpg', '跑步鞋', '耐克跑步鞋，减震透气', 499.00, '运动健身', 1, 8, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (62, 'https://example.com/image39.jpg', '健身手环', '智能健身手环，监测运动数据', 199.00, '运动健身', 1, 9, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (63, 'https://example.com/image40.jpg', '跳绳', '计数跳绳，燃脂利器', 29.00, '运动健身', 1, 10, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (64, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/sb0.jpg', '游戏鼠标', '雷蛇游戏鼠标，精准定位', 199.00, '游戏电竞', 1, 1, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (65, 'https://example.com/image42.jpg', '电竞椅', '人体工学电竞椅，久坐不累', 599.00, '游戏电竞', 1, 2, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (66, 'https://example.com/image43.jpg', '游戏键盘', 'RGB背光游戏键盘，手感出色', 299.00, '游戏电竞', 1, 3, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (67, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/erji.jpg', '游戏耳机', '7.1声道游戏耳机，沉浸音效', 399.00, '游戏电竞', 1, 4, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (68, 'https://example.com/image45.jpg', '游戏主机', '二手PS5游戏主机，配件齐全', 2999.00, '游戏电竞', 1, 5, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (69, 'https://example.com/image46.jpg', '洗衣液', '蓝月亮洗衣液，深层洁净', 39.00, '生活日用', 1, 6, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (70, 'https://example.com/image47.jpg', '抽纸', '维达抽纸，三层加厚', 29.00, '生活日用', 1, 7, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (71, 'https://example.com/image48.jpg', '牙刷', '电动牙刷，清洁更彻底', 99.00, '生活日用', 1, 8, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (72, 'https://example.com/image49.jpg', '垃圾桶', '分类垃圾桶，宿舍家用', 19.00, '生活日用', 1, 9, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (73, 'https://example.com/image50.jpg', '拖鞋', '防滑拖鞋，居家必备', 15.00, '生活日用', 1, 10, 1, 1, 1, '3', '2026-04-07 22:37:43', '1', '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (74, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/ff28a8f3-1497-49bc-b2da-0bb925a5f244.jpg', '二手耳机', '经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。', 500.00, '11,12', 1, 1, 1, 1, 0, '3', '2026-04-11 00:31:50', NULL, '2026-04-11 00:37:35');
INSERT INTO `order_goods` VALUES (75, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/4040e88e-30c8-41a1-bf91-25b2db426585.jpg', '二手二级售卖', '经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。', 588.00, '11,13', 1, 1, 1, 1, 0, '2', '2026-04-11 00:39:33', NULL, '2026-04-11 00:39:33');
INSERT INTO `order_goods` VALUES (76, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/3ae63ab1-054c-40db-afeb-84fce6fd1b9d.jpg', '二手耳机', '经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。', 288.80, '11,13,14', 1, 1, 1, 1, 0, '2', '2026-04-11 00:46:15', NULL, '2026-04-11 00:46:15');

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `quantity` int(11) NULL DEFAULT 1 COMMENT '商品数量',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '加入购物车时的商品单价',
  `is_checked` tinyint(1) NULL DEFAULT 1 COMMENT '是否选中 0-未选中 1-选中',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `adr_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'String' COMMENT '地址表d',
  `pay_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shopping_cart
-- ----------------------------
INSERT INTO `shopping_cart` VALUES (2, 4, 12, 5, NULL, 1, '2026-04-04 19:39:12', '2026-04-06 14:48:03', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (3, 4, 12, 1, NULL, 1, '2026-04-04 21:21:40', '2026-04-06 14:48:03', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (4, 4, 13, 6, NULL, 1, '2026-04-04 23:39:36', '2026-04-06 14:48:03', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (5, 4, 13, 1, NULL, 1, '2026-04-04 23:44:12', '2026-04-06 14:48:03', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (6, 4, 14, 1, NULL, 1, '2026-04-05 20:04:19', '2026-04-06 14:48:03', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (7, 4, 15, 1, NULL, 1, '2026-04-06 10:31:58', '2026-04-06 14:48:03', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (8, 4, 16, 1, NULL, 1, '2026-04-06 13:23:47', '2026-04-06 14:48:03', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (9, 4, 16, 1, NULL, 1, '2026-04-06 15:10:01', '2026-04-06 15:10:01', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (10, 5, 13, 3, NULL, 1, '2026-04-06 18:52:47', '2026-04-06 18:52:47', 'mock_123', 'wechat');
INSERT INTO `shopping_cart` VALUES (11, 2, 17, 5, NULL, 1, '2026-04-06 20:19:40', '2026-04-06 20:19:40', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (12, 2, 13, 1, NULL, 1, '2026-04-06 20:21:34', '2026-04-06 20:21:34', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (13, 2, 17, 1, NULL, 1, '2026-04-06 20:22:50', '2026-04-06 20:22:50', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (14, 2, 17, 1, NULL, 1, '2026-04-06 20:37:07', '2026-04-06 20:37:07', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (15, 2, 23, 1, NULL, 1, '2026-04-09 22:23:06', '2026-04-09 22:23:06', 'mock_123', 'alipay');
INSERT INTO `shopping_cart` VALUES (16, 2, 22, 1, NULL, 1, '2026-04-09 23:14:31', '2026-04-09 23:14:31', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (17, 2, 22, 1, NULL, 1, '2026-04-09 23:26:18', '2026-04-09 23:26:18', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (18, 2, 22, 1, NULL, 1, '2026-04-09 23:27:12', '2026-04-09 23:27:12', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (19, 2, 13, 1, NULL, 1, '2026-04-09 23:28:14', '2026-04-09 23:28:14', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (20, 2, 22, 1, NULL, 1, '2026-04-09 23:28:14', '2026-04-09 23:28:14', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (21, 3, 13, 1, NULL, 1, '2026-04-10 22:55:49', '2026-04-10 22:55:49', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (22, 2, 13, 1, NULL, 1, '2026-04-10 22:57:26', '2026-04-10 22:57:26', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (23, 2, 13, 1, NULL, 1, '2026-04-10 23:01:19', '2026-04-10 23:01:19', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (24, 2, 13, 1, NULL, 1, '2026-04-10 23:06:29', '2026-04-10 23:06:29', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (25, 2, 13, 1, NULL, 1, '2026-04-10 23:08:46', '2026-04-10 23:08:46', 'String', 'DEFAULT');
INSERT INTO `shopping_cart` VALUES (26, 3, 76, 1, NULL, 1, '2026-04-11 00:47:58', '2026-04-11 00:47:58', 'String', 'alipay');
INSERT INTO `shopping_cart` VALUES (27, 3, 44, 1, NULL, 1, '2026-04-11 00:50:23', '2026-04-11 00:50:23', 'String', 'wechat');
INSERT INTO `shopping_cart` VALUES (28, 3, 44, 1, NULL, 1, '2026-04-11 00:52:50', '2026-04-11 00:52:50', 'String', 'DEFAULT');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名，登录账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码，存储加密后的密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `student_card_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学生校园卡ID',
  `user_tag_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户标签ID',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `gender` tinyint(4) NULL DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最后登录IP地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录token',
  `token_expire_time` datetime NULL DEFAULT NULL COMMENT 'token过期时间',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `student_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `score` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '信誉分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, NULL, '$2a$10$WSFg8zBpolXk1r4VDXgKSOSYnjboHwXvt2G7iEmLgE2w.8RNQH8z2', '2712303959@qq.com', '13966308096', NULL, NULL, NULL, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/8f85325e-fa65-4786-951b-d9cfc2b81cdc.png', 0, '2026-04-06', 1, NULL, NULL, '2026-03-31 22:36:06', NULL, '2026-04-06 14:36:49', NULL, 'adc684678b084394ad785e9df0da3aea', '2026-04-07 14:03:04', '伍六一', '20260001', '90');
INSERT INTO `sys_user` VALUES (2, NULL, '$2a$10$dUcbFpTBSQzshI/x.OH1..9e6ONUZekUsLqdqARNafcfeKhJYEp5G', '2712303959@qq.com', '13966308093', NULL, NULL, NULL, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/aa36e9d4-5995-463c-b55d-257d5d6f1d0c.png', 0, '2026-07-06', 1, NULL, NULL, '2026-04-04 18:01:17', NULL, '2026-04-11 21:38:46', NULL, '54c4ed59cd9745bb96dbd573cef1b9ee', '2026-04-12 21:38:46', '来晓璞', '25122101140', '90');
INSERT INTO `sys_user` VALUES (3, NULL, '$2a$10$Pi5nxVFCTKW5EyJTZD//Uu6EX6LTZ3ilOTUPGZxzADjc3SDtORXsG', '2712303959@qq.com', '15256717343', NULL, NULL, NULL, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/e5e7ef78-e7e4-4faf-8455-586a6c8ce8f9.jpg', 0, '2026-04-11', 1, NULL, NULL, '2026-04-05 00:51:37', NULL, '2026-04-11 00:51:41', NULL, 'c60b7c3f3f004daba935c5bd7a0e8ae2', '2026-04-12 00:47:42', '李峰', '25122101139', '90');
INSERT INTO `sys_user` VALUES (4, NULL, '$2a$10$luWw4lXRq1WOxi.pNNpMPu4Sa9pzFz29u3IGiMVmOu7NJ8lEa0qBK', '2712303959@qq.com', '13966308093', NULL, NULL, NULL, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/36fcac6f-4faa-4ccf-93a0-03f6bdba8835.png', 1, '2026-04-06', 1, NULL, NULL, '2026-04-06 14:46:54', NULL, '2026-04-06 15:08:40', NULL, 'f34edbdb47dd4c8a97acef41885fbd32', '2026-04-07 14:46:54', '张三', '20260001', NULL);
INSERT INTO `sys_user` VALUES (5, NULL, '$2a$10$JwUK4EHDTa35MSknJEtjPeDZB2OBVOv8qLLPb3YWV1uFYewGYv5qi', NULL, NULL, NULL, NULL, NULL, 'https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/ed7a420a-98aa-4e5b-b01f-bb751301ebe0.jpg', 0, NULL, 1, NULL, NULL, '2026-04-06 18:51:45', NULL, '2026-04-06 18:53:44', NULL, '8b8941525b6d4edb85adfdf146bdc619', '2026-04-07 18:51:45', '尤志文', '25122101135', NULL);

-- ----------------------------
-- Table structure for t_address
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省市区/详细地址',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区县',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细街道地址',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认地址: 0-否, 1-是',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户地址信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_address
-- ----------------------------
INSERT INTO `t_address` VALUES (1, '来晓璞', '13966308093', '', '2', '2026-04-07 18:42:07', NULL, NULL, NULL, NULL, 0, '北京市朝阳区', '某某街道10号院', '公司');
INSERT INTO `t_address` VALUES (3, '王五', '138886950685', NULL, '2', '2026-04-07 19:19:16', NULL, NULL, NULL, NULL, 0, '北京市朝阳区', '某某街道10号院', '学校');
INSERT INTO `t_address` VALUES (4, '来晓璞', '2321313213231', NULL, '2', '2026-04-07 20:10:12', NULL, NULL, NULL, NULL, 0, '安徽省芜湖市', '安徽师范大学', '学校');
INSERT INTO `t_address` VALUES (5, '王五', '138', NULL, '3', '2026-04-11 00:51:01', NULL, NULL, NULL, NULL, 1, '芜湖市', '安师大', '学校');

SET FOREIGN_KEY_CHECKS = 1;
