/*DROP DATABASE IF EXISTS cellular;

CREATE DATABASE cellular
  DEFAULT CHARACTER SET "utf8";*/

USE cellular;

CREATE TABLE tariffs (
  id        BIGINT      NOT NULL AUTO_INCREMENT,
  name      VARCHAR(50) NOT NULL UNIQUE,
  is_active BOOLEAN     NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE users (
  id          BIGINT      NOT NULL AUTO_INCREMENT,
  first_name  VARCHAR(50) NOT NULL,
  last_name   VARCHAR(50) NOT NULL,
  middle_name VARCHAR(50),
  tariff_id   BIGINT      NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_TARIFF_ID FOREIGN KEY (tariff_id) REFERENCES tariffs (id)
)
  ENGINE = InnoDB;

CREATE TABLE options (
  id             BIGINT         NOT NULL AUTO_INCREMENT,
  name           VARCHAR(50)    NOT NULL,
  old_amount     DECIMAL(19, 2) NOT NULL,
  old_currency   VARCHAR(10)    NOT NULL,
  new_amount     DECIMAL(19, 2) NOT NULL,
  new_currency   VARCHAR(10)    NOT NULL,
  date_of_change DATE           NOT NULL,
  tariff_id      BIGINT         NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_OPTIONS_TARIFF_ID FOREIGN KEY (tariff_id) REFERENCES tariffs (id)
)
  ENGINE = InnoDB;