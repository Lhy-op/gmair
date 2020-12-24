#2018.04.08 add city_daily_aqi_detail to store aqi data for the
CREATE TABLE `gmair_airquality`.`city_daily_aqi_detail`(
  `city_id` VARCHAR(20) NOT NULL,
  `aqi_index` INT NOT NULL,
  `aqi_level` VARCHAR(45) NOT NULL,
  `pri_pollut` VARCHAR(45) NOT NULL,
  `pm_2_5` DOUBLE NOT NULL DEFAULT 0,
  `pm_10` DOUBLE NOT NULL DEFAULT 0,
  `co` DOUBLE NOT NULL DEFAULT 0,
  `no_2` DOUBLE NOT NULL DEFAULT 0,
  `o_3` DOUBLE NOT NULL DEFAULT 0,
  `so_2` DOUBLE NOT NULL DEFAULT 0,
  `record_time` DATETIME NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL);

create table `gmair_airquality`.`city_url` (
	  city_id varchar(20) not null UNIQUE,
    city_url varchar(255) not null,
    block_flag tinyint(1) default 0,
    create_time datetime not null,
    PRIMARY KEY (city_id));


# 2018-05-11 create obscure city table
CREATE TABLE `gmair_airquality`.`obscure_city` (
  `oc_id` CHAR(20) NOT NULL,
  `city_name` VARCHAR(31) NOT NULL ,
  `city_id` VARCHAR(31) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`oc_id`),
  UNIQUE INDEX `oc_id_UNIQUE` (`oc_id` ASC));

# 2018-05-17 create machine air quality
CREATE TABLE `gmair_airquality`.`machine_airquality` (
  `ma_id` CHAR(31) NOT NULL,
  `codeValue` CHAR (31) NOT NULL ,
  `pm_25` DECIMAL(10, 4) NOT NULL,
  `temperature` DECIMAL(10, 4) NOT NULL,
  `humidity` DECIMAL(10, 4) NOT NULL,
  `co2` DECIMAL(10, 4) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`ma_id`));

# 2018-05-28 rename table city_daily_aqi_sum
DROP TABLE `gmair_airquality`.`city_latest_aqi`;

DROP TABLE `gmair_airquality`.`city_daily_aqi_sum`;
CREATE TABLE `gmair_airquality`.`city_daily_aqi` (
  `city_id` int(11) NOT NULL,
  `pm_2_5` double NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table `gmair_airquality`.`city_monthly_aqi`;
CREATE TABLE `gmair_airquality`.`city_monthly_aqi` (
  `city_id` varchar(20) NOT NULL,
  `pm_2_5` double NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `gmair_airquality`.`city_hourly_aqi` (
  `city_id` varchar(20) NOT NULL,
  `pm_2_5` double NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

rename TABLE `gmair_airquality`.`city_daily_aqi_detail` to `gmair_airquality`.`city_aqi_full`;

#2018-05-29 rename table COLUMN
ALTER TABLE `gmair_airquality`.`city_daily_aqi`
CHANGE COLUMN `pm_2_5` `pm_25` DOUBLE NOT NULL ;

ALTER TABLE `gmair_airquality`.city_hourly_aqi
CHANGE COLUMN `pm_2_5` `pm_25` DOUBLE NOT NULL ;


ALTER TABLE `gmair_airquality`.city_monthly_aqi
CHANGE COLUMN `pm_2_5` `pm_25` DOUBLE NOT NULL ;

#2018-05-31
ALTER TABLE `gmair_airquality`.`city_daily_aqi`
DROP PRIMARY KEY;

ALTER TABLE `gmair_airquality`.`city_hourly_aqi`
DROP PRIMARY KEY;

ALTER TABLE `gmair_airquality`.`city_monthly_aqi`
DROP PRIMARY KEY;

#2018-06-22
CREATE TABLE `gmair_airquality`.`station_aqi_full` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `station_id` varchar(20) NOT NULL,
  `aqi_index` int(11) NOT NULL,
  `aqi_level` varchar(45) NOT NULL,
  `pri_pollut` varchar(45) NOT NULL,
  `pm_2_5` double NOT NULL DEFAULT '0',
  `pm_10` double NOT NULL DEFAULT '0',
  `co` double NOT NULL DEFAULT '0',
  `no_2` double NOT NULL DEFAULT '0',
  `o_3` double NOT NULL DEFAULT '0',
  `so_2` double NOT NULL DEFAULT '0',
  `record_time` datetime NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `gmair_airquality`.`station_aqi_full` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `station_id` varchar(20) NOT NULL,
  `aqi_index` int(11) NOT NULL,
  `aqi_level` varchar(45) NOT NULL,
  `pri_pollut` varchar(45) NOT NULL,
  `pm_2_5` double NOT NULL DEFAULT '0',
  `pm_10` double NOT NULL DEFAULT '0',
  `co` double NOT NULL DEFAULT '0',
  `no_2` double NOT NULL DEFAULT '0',
  `o_3` double NOT NULL DEFAULT '0',
  `so_2` double NOT NULL DEFAULT '0',
  `record_time` datetime NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3929 DEFAULT CHARSET=utf8;

CREATE TABLE `gmair_airquality`.`province_airquality` (
  `province_id` int(11) NOT NULL,
  `province_name` varchar(45) NOT NULL,
  `pm_2_5` double NOT NULL,
  `aqi_index` double NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`province_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# add primary key
ALTER TABLE `gmair_airquality`.`city_aqi_full`
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY (`id`);

ALTER TABLE `gmair_airquality`.`city_daily_aqi`
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY (`id`);

ALTER TABLE `gmair_airquality`.`city_hourly_aqi`
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY (`id`);

ALTER TABLE `gmair_airquality`.`city_monthly_aqi`
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY (`id`);

CREATE TABLE `gmair_airquality`.`moji_aqi_cities` (
  `moji_id` INT NOT NULL,
  `region_id` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`moji_id`));

#2019-11-07
CREATE TABLE `gmair_airquality`.`moji_token` (
  `token_id` VARCHAR(45) NOT NULL,
  `token` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `url` VARCHAR(45) NOT NULL,
  `base` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`token_id`));


