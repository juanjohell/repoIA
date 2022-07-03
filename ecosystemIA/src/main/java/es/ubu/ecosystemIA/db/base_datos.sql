CREATE DATABASE bbdd_modelos_neuronales;

CREATE USER 'ecosystemIA_AU'@'ecosystemIA_AU' IDENTIFIED BY 'ecosystem';
GRANT ALTER, SHOW VIEW, SHOW DATABASES, SELECT, PROCESS, EXECUTE, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE VIEW, DELETE, DROP, EVENT, INDEX, INSERT, REFERENCES, TRIGGER, UPDATE, CREATE USER, FILE, LOCK TABLES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SHUTDOWN, SUPER  ON *.* TO 'ecosystemIA_AU'@'ecosystemIA_AU' WITH GRANT OPTION;
FLUSH PRIVILEGES;

USE bbdd_modelos_neuronales;

CREATE TABLE `modelos` (
	`ID_MODELO` INT(10) UNSIGNED NOT NULL,
	`NOMBRE` VARCHAR(255) NOT NULL DEFAULT '' COLLATE 'utf8mb4_spanish2_ci',
	`DESCRIPCION` VARCHAR(500) NULL DEFAULT '' COLLATE 'utf8mb4_spanish2_ci',
	`FICHEROH5` LONGBLOB NULL DEFAULT NULL,
	`INPUT_HEIGHT` INT(10) UNSIGNED NULL DEFAULT NULL,
	`INPUT_WIDTH` INT(10) UNSIGNED NULL DEFAULT NULL,
	`INPUT_CHANNELS` INT(10) UNSIGNED NULL DEFAULT NULL,
	PRIMARY KEY (`ID_MODELO`) USING BTREE
)
COMMENT='almacena modelos de redes neuronales, sus características y ficheros asociados'
COLLATE='utf8mb4_spanish2_ci'
ENGINE=InnoDB
;

INSERT INTO `bbdd_modelos_neuronales`.`modelos` (`ID_MODELO`, `NOMBRE`, `DESCRIPCION`) VALUES ('1', 'cifar10', 'modelo de clasificación de imágenes entrenado con DATASET Cifar10, 10 Epochs');
SELECT `ID_MODELO`, `NOMBRE`, `DESCRIPCION`, `FICHEROH5`, `INPUT_HEIGHT`, `INPUT_WIDTH`, `INPUT_CHANNELS` FROM `bbdd_modelos_neuronales`.`modelos` WHERE  `ID_MODELO`=1;
INSERT INTO `bbdd_modelos_neuronales`.`modelos` (`ID_MODELO`, `NOMBRE`, `DESCRIPCION`) VALUES ('2', 'r-cnn_vgg16', 'modelo de reconocimiento de objetos en imágenes');
SELECT `ID_MODELO`, `NOMBRE`, `DESCRIPCION`, `FICHEROH5`, `INPUT_HEIGHT`, `INPUT_WIDTH`, `INPUT_CHANNELS` FROM `bbdd_modelos_neuronales`.`modelos` WHERE  `ID_MODELO`=2;

CREATE USER 'ecosystemIA_AU'@'localhost' IDENTIFIED BY 'DeGaMa3E';
GRANT ALTER, SHOW VIEW, SHOW DATABASES, SELECT, PROCESS, EXECUTE, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE VIEW, DELETE, DROP, EVENT, INDEX, INSERT, REFERENCES, TRIGGER, UPDATE, CREATE USER, FILE, LOCK TABLES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SHUTDOWN, SUPER  ON *.* TO 'ecosystemIA_AU'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;
SHOW GRANTS FOR 'ecosystemIA_AU'@'localhost';
