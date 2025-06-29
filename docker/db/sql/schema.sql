CREATE DATABASE IF NOT EXISTS `test`;
USE `test`;

CREATE TABLE IF NOT EXISTS `task`
(
    `uuid` CHAR(36) NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `description` VARCHAR(1000),
    `due_date` DATE NOT NULL,
    `task_status`    ENUM ('COMPLETE', 'INCOMPLETE')
    );
