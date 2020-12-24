-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_airquality
-- -----------------------------------------------------
-- city air quality in china

-- -----------------------------------------------------
-- Schema gmair_airquality
--
-- city air quality in china
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_airquality`
  DEFAULT CHARACTER SET utf8;
USE `gmair_airquality`;

-- -----------------------------------------------------
-- Table `gmair_airquality`.`monitor_station`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_airquality`.`monitor_station` (
  `station_id`     VARCHAR(20) NOT NULL,
  `belong_city_id` VARCHAR(45) NOT NULL,
  `station_name`   VARCHAR(45) NULL,
  `block_flag`     TINYINT(1)  NULL,
  PRIMARY KEY (`station_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_airquality`.`city_latest_aqi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_airquality`.`city_latest_aqi` (
  `city_id`     VARCHAR(20) NOT NULL,
  `aqi_index`   INT         NOT NULL,
  `aqi_level`   VARCHAR(45) NOT NULL,
  `pri_pollut`  VARCHAR(45) NOT NULL,
  `pm_2_5`      DOUBLE      NOT NULL DEFAULT 0,
  `pm_10`       DOUBLE      NOT NULL DEFAULT 0,
  `co`          DOUBLE      NOT NULL DEFAULT 0,
  `no_2`        DOUBLE      NOT NULL DEFAULT 0,
  `o_3`         DOUBLE      NOT NULL DEFAULT 0,
  `so_2`        DOUBLE      NOT NULL DEFAULT 0,
  `block_flag`  TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`city_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_airquality`.`city_daily_aqi_sum`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_airquality`.`city_daily_aqi_sum` (
  `city_id`     INT        NOT NULL,
  `pm_2_5`      DOUBLE     NOT NULL,
  `pm_10`       DOUBLE     NOT NULL,
  `co`          DOUBLE     NOT NULL,
  `no_2`        DOUBLE     NOT NULL,
  `o_3`         DOUBLE     NOT NULL,
  `so_2`        DOUBLE     NOT NULL,
  `date`        DATE       NOT NULL,
  `block_flag`  TINYINT(1) NOT NULL,
  `create_time` DATETIME   NOT NULL,
  PRIMARY KEY (`city_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_airquality`.`city_monthly_aqi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_airquality`.`city_monthly_aqi` (
  `city_id`     VARCHAR(20) NOT NULL,
  `aqi_index`   INT         NOT NULL,
  `aqi_level`   VARCHAR(45) NOT NULL,
  `pri_pollut`  VARCHAR(45) NOT NULL,
  `pm_2_5`      DOUBLE      NOT NULL DEFAULT 0,
  `pm_10`       DOUBLE      NOT NULL DEFAULT 0,
  `co`          DOUBLE      NOT NULL DEFAULT 0,
  `no_2`        DOUBLE      NOT NULL DEFAULT 0,
  `o_3`         DOUBLE      NOT NULL DEFAULT 0,
  `so_2`        DOUBLE      NOT NULL DEFAULT 0,
  `block_flag`  TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`city_id`)
)
  ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
