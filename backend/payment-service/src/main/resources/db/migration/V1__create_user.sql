CREATE TABLE IF NOT EXISTS `seeder`.`user` (
  `id` BINARY(16) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NULL,
  `cashkick_balance` INT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
