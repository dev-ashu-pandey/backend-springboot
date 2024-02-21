-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema seeder
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema seeder
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `seeder` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema seeder
-- -----------------------------------------------------
USE `seeder` ;

-- -----------------------------------------------------
-- Table `seeder`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `seeder`.`user` (
  `id` BINARY(16) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NULL,
  `cashkick_balance` INT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seeder`.`payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `seeder`.`payment` (
  `id` BINARY(16) NOT NULL,
  `due_date` DATETIME NULL,
  `term` VARCHAR(45) NOT NULL,
  `total_payback_amount` VARCHAR(45) NOT NULL,
  `status` ENUM('UPCOMING') NULL,
  `expected_amount` VARCHAR(45) NULL,
  `outstanding_amount` VARCHAR(45) NULL,
  `user_id` BINARY(16) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_payment_user_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_payment_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `seeder`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seeder`.`cashkick`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `seeder`.`cashkick` (
  `id` BINARY(16) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `status` ENUM('Pending', 'Active', 'Completed', 'Cancelled') NOT NULL,
  `maturity` DATETIME NOT NULL,
  `total_received_amount` VARCHAR(45) NOT NULL,
  `total_financed_amount` VARCHAR(45) NOT NULL,
  `user_id` BINARY(16) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_cashkick_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_cashkick_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `seeder`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seeder`.`contract`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `seeder`.`contract` (
  `id` BINARY(16) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `type` ENUM('Monthly, Yearly') NOT NULL,
  `monthly_payment` VARCHAR(45) NOT NULL,
  `term_length` VARCHAR(45) NOT NULL,
  `monthly_intreset_rate` VARCHAR(45) NULL,
  `payment_amount` VARCHAR(45) NOT NULL,
  `status` ENUM('Available', 'Unavailable') NOT NULL,
  `total_available_amount` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seeder`.`cashkick_has_contract`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `seeder`.`cashkick_has_contract` (
  `cashkick_id` BINARY(16) NOT NULL,
  `contract_id` BINARY(16) NOT NULL,
  PRIMARY KEY (`cashkick_id`, `contract_id`),
  INDEX `fk_cashkick_has_contract_contract1_idx` (`contract_id` ASC) VISIBLE,
  INDEX `fk_cashkick_has_contract_cashkick1_idx` (`cashkick_id` ASC) VISIBLE,
  CONSTRAINT `fk_cashkick_has_contract_cashkick1`
    FOREIGN KEY (`cashkick_id`)
    REFERENCES `seeder`.`cashkick` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cashkick_has_contract_contract1`
    FOREIGN KEY (`contract_id`)
    REFERENCES `seeder`.`contract` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
