CREATE TABLE IF NOT EXISTS `seeder`.`contract` (
  `id` BINARY(16) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `type` ENUM('Monthly, Yearly') NOT NULL,
  `monthly_payment` VARCHAR(45) NOT NULL,
  `term_length` VARCHAR(45) NOT NULL,
  `monthly_interest_rate` VARCHAR(45) NULL,
  `payment_amount` VARCHAR(45) NOT NULL,
  `status` ENUM('Available') NOT NULL,
  `total_available_amount` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
