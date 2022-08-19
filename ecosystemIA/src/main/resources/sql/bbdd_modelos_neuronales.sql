-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.6.8-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para bbdd_modelos_neuronales
CREATE DATABASE IF NOT EXISTS `bbdd_modelos_neuronales` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `bbdd_modelos_neuronales`;

-- Volcando estructura para tabla bbdd_modelos_neuronales.categorias
CREATE TABLE IF NOT EXISTS `categorias` (
  `ID_ITEM` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Identificador general',
  `ID_MODELO` int(10) unsigned NOT NULL COMMENT 'Red neuronal a la que pertenece la categoria',
  `ID_ORDEN` int(10) unsigned NOT NULL COMMENT 'Orden de la categoría dentro de su conjunto',
  `NOMBRE_CATEGORIA` varchar(200) NOT NULL DEFAULT '' COMMENT 'Nombre de la categoría',
  PRIMARY KEY (`ID_ITEM`),
  KEY `FK_MODELOS` (`ID_MODELO`),
  CONSTRAINT `FK_MODELOS` FOREIGN KEY (`ID_MODELO`) REFERENCES `modelos` (`ID_MODELO`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1 COMMENT='categorias de clasificación de imágenes de los diferentes modelos de redes neuronales';

-- Volcando datos para la tabla bbdd_modelos_neuronales.categorias: ~9 rows (aproximadamente)
DELETE FROM `categorias`;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` (`ID_ITEM`, `ID_MODELO`, `ID_ORDEN`, `NOMBRE_CATEGORIA`) VALUES
	(1, 1, 1, 'Avión'),
	(2, 1, 2, 'Coche'),
	(3, 1, 3, 'Pájaro'),
	(4, 1, 4, 'Gato'),
	(5, 1, 5, 'Ciervo'),
	(6, 1, 6, 'Perro'),
	(7, 1, 7, 'Rana'),
	(8, 1, 8, 'Caballo'),
	(9, 1, 9, 'Barco'),
	(10, 1, 10, 'Camión'),
	(11, 2, 1, 'Botella'),
	(12, 10, 1, 'Botellas');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.config_modelo
CREATE TABLE IF NOT EXISTS `config_modelo` (
  `idItem` int(11) unsigned NOT NULL,
  `idModelo` int(10) unsigned NOT NULL,
  `idElemento` int(11) unsigned NOT NULL,
  `valor` varchar(50) NOT NULL DEFAULT '',
  KEY `PK_CONFIG_MODELO` (`idItem`),
  KEY `FK2_MODELOS` (`idModelo`),
  KEY `FK2_ELEMENTOS` (`idElemento`),
  CONSTRAINT `FK2_ELEMENTOS` FOREIGN KEY (`idElemento`) REFERENCES `elementos_config` (`idElemento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK2_MODELOS` FOREIGN KEY (`idModelo`) REFERENCES `modelos` (`ID_MODELO`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Configuracion de color y grosor de los boinding boxes del modelo.';

-- Volcando datos para la tabla bbdd_modelos_neuronales.config_modelo: ~0 rows (aproximadamente)
DELETE FROM `config_modelo`;
/*!40000 ALTER TABLE `config_modelo` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_modelo` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.elementos_config
CREATE TABLE IF NOT EXISTS `elementos_config` (
  `idElemento` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `nombreElemento` varchar(50) NOT NULL DEFAULT '',
  KEY `PK_PRIMARY` (`idElemento`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COMMENT='elementos configurables en un modelo';

-- Volcando datos para la tabla bbdd_modelos_neuronales.elementos_config: ~2 rows (aproximadamente)
DELETE FROM `elementos_config`;
/*!40000 ALTER TABLE `elementos_config` DISABLE KEYS */;
INSERT INTO `elementos_config` (`idElemento`, `nombreElemento`) VALUES
	(1, 'colorCaja'),
	(2, 'grosorCaja');
/*!40000 ALTER TABLE `elementos_config` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.modelos
CREATE TABLE IF NOT EXISTS `modelos` (
  `ID_MODELO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(255) COLLATE utf8mb4_spanish2_ci NOT NULL DEFAULT '',
  `DESCRIPCION` varchar(500) COLLATE utf8mb4_spanish2_ci DEFAULT '',
  `FICHEROH5` longblob DEFAULT NULL,
  `INPUT_HEIGHT` int(10) unsigned DEFAULT NULL,
  `INPUT_WIDTH` int(10) unsigned DEFAULT NULL,
  `INPUT_CHANNELS` int(10) unsigned DEFAULT NULL,
  `FORMATO_MATRIZ_IMG` varchar(4) COLLATE utf8mb4_spanish2_ci DEFAULT NULL COMMENT 'Almacena el formato wen que espera la primera capa del modelo la imagen: NHWC , NCHW',
  `PATH_FICHERO` varchar(250) COLLATE utf8mb4_spanish2_ci DEFAULT NULL COMMENT 'Ruta al fichero si se almacena de forma externa',
  `TIPO_ALMACENAMIENTO` int(11) NOT NULL DEFAULT 1 COMMENT 'Determina donde se guarda',
  `TIPO_FICHERO` int(1) unsigned NOT NULL DEFAULT 2 COMMENT 'Determina el tipo de fichero en que se encuentra almacenado el modelo neuronal',
  `TIPO_SALIDA` varchar(50) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`ID_MODELO`) USING BTREE,
  KEY `FK1_TIPO_ALMACENAMIENTO` (`TIPO_ALMACENAMIENTO`),
  KEY `FK2_TIPO_FICHERO` (`TIPO_FICHERO`),
  CONSTRAINT `FK1_TIPO_ALMACENAMIENTO` FOREIGN KEY (`TIPO_ALMACENAMIENTO`) REFERENCES `tipos_almacenamiento` (`idTipoAlmacenamiento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK2_TIPO_FICHERO` FOREIGN KEY (`TIPO_FICHERO`) REFERENCES `tipos_fichero` (`idTipoModelo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci COMMENT='almacena modelos de redes neuronales, sus características y ficheros asociados';

-- Volcando datos para la tabla bbdd_modelos_neuronales.modelos: ~3 rows (aproximadamente)
DELETE FROM `modelos`;
/*!40000 ALTER TABLE `modelos` DISABLE KEYS */;
INSERT INTO `modelos` (`ID_MODELO`, `NOMBRE`, `DESCRIPCION`, `FICHEROH5`, `INPUT_HEIGHT`, `INPUT_WIDTH`, `INPUT_CHANNELS`, `FORMATO_MATRIZ_IMG`, `PATH_FICHERO`, `TIPO_ALMACENAMIENTO`, `TIPO_FICHERO`, `TIPO_SALIDA`) VALUES
	(1, 'Cifar10', 'modelo de clasificación de imágenes entrenado con DATASET Cifar10, 10 Epochs', NULL, 32, 32, 3, NULL, 'modelos\\\\cifar10_entrenado_10epochs.h5', 2, 1, 'texto'),
	(2, 'r-cnn_vgg16', 'modelo de reconocimiento de objetos en imágenes, basado en el modelo preentrenado VGG16', NULL, 224, 224, 3, NULL, 'modelos\\\\rcnn_VGG16_reconocimiento_botellas.h5', 2, 1, 'imagen'),
	(10, 'Yolov5_deteccion_residuos', 'Modelo en formato PB TensorFlow YOLOv5', NULL, 416, 416, 3, NULL, 'modelos\\\\yolov5\\\\saved_model.pb', 2, 1, 'imagen');
/*!40000 ALTER TABLE `modelos` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.tipos_almacenamiento
CREATE TABLE IF NOT EXISTS `tipos_almacenamiento` (
  `idTipoAlmacenamiento` int(11) NOT NULL COMMENT 'PRIMARY',
  `nombre` varchar(50) NOT NULL DEFAULT '0' COMMENT 'NOMBRE',
  PRIMARY KEY (`idTipoAlmacenamiento`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Tipos de almacenamiento que pueden usarse para grabar los ficheros de los modelos neuronales. En base de datos, en sistema de ficheros del servidor, en google drive...';

-- Volcando datos para la tabla bbdd_modelos_neuronales.tipos_almacenamiento: ~2 rows (aproximadamente)
DELETE FROM `tipos_almacenamiento`;
/*!40000 ALTER TABLE `tipos_almacenamiento` DISABLE KEYS */;
INSERT INTO `tipos_almacenamiento` (`idTipoAlmacenamiento`, `nombre`) VALUES
	(1, 'Base de datos'),
	(2, 'Sistema ficheros servidor'),
	(3, 'Google Drive o similar');
/*!40000 ALTER TABLE `tipos_almacenamiento` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.tipos_fichero
CREATE TABLE IF NOT EXISTS `tipos_fichero` (
  `idTipoModelo` int(10) unsigned NOT NULL COMMENT 'primary',
  `nombreCorto` varchar(2) NOT NULL COMMENT 'identifica al tipo de fichero',
  `nombreLargo` varchar(50) NOT NULL,
  `descripcion` varchar(250) NOT NULL COMMENT 'Descripción del formato',
  PRIMARY KEY (`idTipoModelo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='tipos de formatos de almacenamiento de ficheros de redes neuronales entrenadas';

-- Volcando datos para la tabla bbdd_modelos_neuronales.tipos_fichero: ~2 rows (aproximadamente)
DELETE FROM `tipos_fichero`;
/*!40000 ALTER TABLE `tipos_fichero` DISABLE KEYS */;
INSERT INTO `tipos_fichero` (`idTipoModelo`, `nombreCorto`, `nombreLargo`, `descripcion`) VALUES
	(1, 'h5', 'kerash5', 'formato de fichero H5 de modelos implementados en Keras'),
	(2, 'tf', 'tensorFlowPB', 'formato pb protobuffer de TensorFlow'),
	(3, 'on', 'onnx', 'formato onnx');
/*!40000 ALTER TABLE `tipos_fichero` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
