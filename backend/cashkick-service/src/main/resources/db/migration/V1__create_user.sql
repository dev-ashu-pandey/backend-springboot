CREATE TABLE IF NOT EXISTS `seeder`.`user` (
  `id` BINARY(16) NOT NULL,
  `name` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(80) NULL,
  `cashkick_balance` INT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
