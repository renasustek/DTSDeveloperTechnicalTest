CREATE DATABASE IF NOT EXISTS `mind_map`
USE `mind_map`;

CREATE TABLE IF NOT EXISTS `task`
(
    `uuid` CHAR(36) NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `description` VARCHAR(1000),
    `status` VARCHAR(255) NOT NULL,
    `due_date` DATETIME(6) NOT NULL,
    `task_status`    ENUM ('TODO', 'INPROGRESS', 'DONE'),
    );
