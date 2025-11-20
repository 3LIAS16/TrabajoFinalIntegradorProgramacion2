-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb3 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`configuracionred`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`configuracionred` (
  `idConfiguracionRed` INT NOT NULL AUTO_INCREMENT,
  `eliminado` TINYINT NULL DEFAULT '0',
  `ip` VARCHAR(45) NULL DEFAULT NULL,
  `mascara` VARCHAR(45) NULL DEFAULT NULL,
  `gateway` VARCHAR(45) NULL DEFAULT NULL,
  `dnsPrimario` VARCHAR(45) NULL DEFAULT NULL,
  `dhcpHabilitado` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idConfiguracionRed`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`dispositivoiot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`dispositivoiot` (
  `idDispositivoIoT` INT NOT NULL AUTO_INCREMENT,
  `eliminado` TINYINT NULL DEFAULT '0',
  `serial` VARCHAR(50) NOT NULL,
  `modelo` VARCHAR(50) NOT NULL,
  `ubicacion` VARCHAR(120) NULL DEFAULT NULL,
  `firmwareVersion` VARCHAR(30) NULL DEFAULT NULL,
  `idConfiguracion` INT NULL,
  PRIMARY KEY (`idDispositivoIoT`),
  UNIQUE INDEX `serial_UNIQUE` (`serial` ASC) VISIBLE,
  INDEX `fk_DispositivoIoT_ConfiguracionRed_idx` (`idConfiguracion` ASC) VISIBLE,
  CONSTRAINT `fk_DispositivoIoT_ConfiguracionRed`
    FOREIGN KEY (`idConfiguracion`)
    REFERENCES `mydb`.`configuracionred` (`idConfiguracionRed`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
