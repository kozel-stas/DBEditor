CREATE DATABASE `PBZ`;

CREATE TABLE `PBZ`.`correspondents` (
  `id`       INT(11)     NOT NULL  AUTO_INCREMENT,
  `surname`  VARCHAR(50) NOT NULL,
  `name`     VARCHAR(50) NOT NULL,
  `lastName` VARCHAR(50) NOT NULL,
  `position` TEXT        NOT NUll,
  `division` TEXT        NOT NULL,
  `chief`     TINYINT(1) NOT NULL  DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`surname`, `name`, lastName)
)
  CHARSET = 'utf8';


CREATE TABLE `PBZ`.`orders` (
  `id`        INT(11)   NOT NULL AUTO_INCREMENT,
  `content`   TEXT      NOT NULL,
  `startDate` timestamp NOT NUll,
  `owner` INT(11) NOT NULL,
  CONSTRAINT `fk_owner` FOREIGN KEY (`owner`) REFERENCES `PBZ`.`correspondents` (`id`),
  PRIMARY KEY (`id`)
)
  CHARSET = 'utf8';

CREATE TABLE `PBZ`.`events` (
  `id`        INT(11)     NOT NULL          AUTO_INCREMENT,
  `name`      TEXT        NOT NULL,
  `startDate` timestamp   NOT NUll,
  `endDate`   timestamp   NULL,
  `completed` tinyint(1)  NOT NULL          DEFAULT '0',
  `doer`      INT(11)     NOT NULL,
  `orderID`     INT(11)     NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_doer` FOREIGN KEY (`doer`) REFERENCES `PBZ`.`correspondents` (`id`),
  CONSTRAINT `fk_order` FOREIGN KEY (`orderID`) REFERENCES `PBZ`.`orders` (`id`)
)
  CHARSET = 'utf8';

