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
