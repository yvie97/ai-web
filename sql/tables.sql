-- Export database structure for yvie
DROP DATABASE IF EXISTS `yvie`;
CREATE DATABASE IF NOT EXISTS `yvie`;
USE `yvie`;

-- Export table structure for yvie.course
DROP TABLE IF EXISTS `course`;
CREATE TABLE IF NOT EXISTS `course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'Course name',
  `edu` int NOT NULL DEFAULT '0' COMMENT 'Education requirement: 0-None, 1-Junior high, 2-High school, 3-College, 4-Bachelor or above',
  `type` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT 'Course type: Programming, Design, Social Media, Others',
  `price` bigint NOT NULL DEFAULT '0' COMMENT 'Course price',
  `duration` int unsigned NOT NULL DEFAULT '0' COMMENT 'Duration in days',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Courses table';

-- Exporting data for table yvie.course: ~7 rows (approximately)
DELETE FROM `course`;
INSERT INTO `course` (`id`, `name`, `edu`, `type`, `price`, `duration`) VALUES
  (1, 'JavaEE', 4, 'Programming', 21999, 108),
  (2, 'HarmonyOS App Development', 3, 'Programming', 20999, 98),
  (3, 'AI Artificial Intelligence', 4, 'Programming', 24999, 100),
  (4, 'Python Big Data Development', 4, 'Programming', 23999, 102),
  (5, 'Cross-border E-commerce', 0, 'Social Media', 12999, 68),
  (6, 'New Media Operations', 0, 'Social Media', 10999, 61),
  (7, 'UI Design', 2, 'Design', 11999, 66);

-- Export table structure for yvie.course_reservation
DROP TABLE IF EXISTS `course_reservation`;
CREATE TABLE IF NOT EXISTS `course_reservation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'Reserved course',
  `student_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Student name',
  `contact_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Contact info',
  `school` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Reserved campus',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'Remarks',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exporting data for table yvie.course_reservation: ~0 rows (approximately)
DELETE FROM `course_reservation`;
INSERT INTO `course_reservation` (`id`, `course`, `student_name`, `contact_info`, `school`, `remark`) VALUES
  (1, 'New Media Operations', 'Zhang Sanfeng', '13899762348', 'Guangdong Campus', 'Assign a good teacher');

-- Export table structure for yvie.school
DROP TABLE IF EXISTS `school`;
CREATE TABLE IF NOT EXISTS `school` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Campus name',
  `city` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'City where campus is located',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Campuses table';

-- Exporting data for table yvie.school: ~0 rows (approximately)
DELETE FROM `school`;
INSERT INTO `school` (`id`, `name`, `city`) VALUES
  (1, 'Changping Campus', 'Beijing'),
  (2, 'Shunyi Campus', 'Beijing'),
  (3, 'Hangzhou Campus', 'Hangzhou'),
  (4, 'Shanghai Campus', 'Shanghai'),
  (5, 'Nanjing Campus', 'Nanjing'),
  (6, 'Xi\'an Campus', 'Xi\'an'),
  (7, 'Zhengzhou Campus', 'Zhengzhou'),
  (8, 'Guangdong Campus', 'Guangdong'),
  (9, 'Shenzhen Campus', 'Shenzhen');