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
