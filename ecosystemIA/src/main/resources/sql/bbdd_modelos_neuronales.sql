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
DROP DATABASE IF EXISTS `bbdd_modelos_neuronales`;
CREATE DATABASE IF NOT EXISTS `bbdd_modelos_neuronales` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `bbdd_modelos_neuronales`;

-- Volcando estructura para tabla bbdd_modelos_neuronales.categorias
DROP TABLE IF EXISTS `categorias`;
CREATE TABLE IF NOT EXISTS `categorias` (
  `ID_ITEM` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Identificador general',
  `ID_MODELO` int(10) unsigned NOT NULL COMMENT 'Red neuronal a la que pertenece la categoria',
  `ID_ORDEN` int(10) unsigned NOT NULL COMMENT 'Orden de la categoría dentro de su conjunto',
  `NOMBRE_CATEGORIA` varchar(200) NOT NULL DEFAULT '' COMMENT 'Nombre de la categoría',
  PRIMARY KEY (`ID_ITEM`),
  KEY `FK_MODELOS` (`ID_MODELO`),
  CONSTRAINT `FK_MODELOS` FOREIGN KEY (`ID_MODELO`) REFERENCES `modelos` (`ID_MODELO`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1 COMMENT='categorias de clasificación de imágenes de los diferentes modelos de redes neuronales';

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
	(10, 1, 10, 'Camión');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.config_modelo
DROP TABLE IF EXISTS `config_modelo`;
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
DROP TABLE IF EXISTS `elementos_config`;
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

-- Volcando estructura para tabla bbdd_modelos_neuronales.ficheros
DROP TABLE IF EXISTS `ficheros`;
CREATE TABLE IF NOT EXISTS `ficheros` (
  `idModelo` int(10) unsigned NOT NULL DEFAULT 0,
  `fichero` longblob NOT NULL,
  PRIMARY KEY (`idModelo`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='almacena ficheros binarios de gran tamaño, correspondientes a ficheros de almacenamiento de modelos neuroanales en diferentes formatos estándar como H5, pb, onnx, etc...';

-- Volcando datos para la tabla bbdd_modelos_neuronales.ficheros: ~0 rows (aproximadamente)
DELETE FROM `ficheros`;
/*!40000 ALTER TABLE `ficheros` DISABLE KEYS */;
/*!40000 ALTER TABLE `ficheros` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.modelos
DROP TABLE IF EXISTS `modelos`;
CREATE TABLE IF NOT EXISTS `modelos` (
  `ID_MODELO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(255) COLLATE utf8mb4_spanish2_ci NOT NULL DEFAULT '',
  `DESCRIPCION` varchar(500) COLLATE utf8mb4_spanish2_ci DEFAULT '',
  `INPUT_HEIGHT` int(10) unsigned DEFAULT NULL,
  `INPUT_WIDTH` int(10) unsigned DEFAULT NULL,
  `INPUT_CHANNELS` int(10) unsigned DEFAULT NULL,
  `FORMATO_MATRIZ_IMG` varchar(4) COLLATE utf8mb4_spanish2_ci DEFAULT NULL COMMENT 'Almacena el formato wen que espera la primera capa del modelo la imagen: NHWC , NCHW',
  `PATH_FICHERO` varchar(250) COLLATE utf8mb4_spanish2_ci DEFAULT NULL COMMENT 'Ruta al fichero si se almacena de forma externa',
  `TIPO_ALMACENAMIENTO` int(11) NOT NULL DEFAULT 1 COMMENT 'Determina donde se guarda',
  `TIPO_FICHERO` int(1) unsigned NOT NULL DEFAULT 2 COMMENT 'Determina el tipo de fichero en que se encuentra almacenado el modelo neuronal',
  `TIPO_PREDICCION` int(11) unsigned NOT NULL,
  `TIPO_SALIDA` int(11) unsigned NOT NULL,
  PRIMARY KEY (`ID_MODELO`) USING BTREE,
  KEY `FK1_TIPO_ALMACENAMIENTO` (`TIPO_ALMACENAMIENTO`),
  KEY `FK2_TIPO_FICHERO` (`TIPO_FICHERO`),
  KEY `FK3_TIPO_SALIDA` (`TIPO_SALIDA`),
  KEY `FK4_TIPO_PREDICCION` (`TIPO_PREDICCION`),
  CONSTRAINT `FK1_TIPO_ALMACENAMIENTO` FOREIGN KEY (`TIPO_ALMACENAMIENTO`) REFERENCES `tipos_almacenamiento` (`idTipoAlmacenamiento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK2_TIPO_FICHERO` FOREIGN KEY (`TIPO_FICHERO`) REFERENCES `tipos_fichero` (`idTipoFichero`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK3_TIPO_SALIDA` FOREIGN KEY (`TIPO_SALIDA`) REFERENCES `tipo_salida` (`idTiposalida`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK4_TIPO_PREDICCION` FOREIGN KEY (`TIPO_PREDICCION`) REFERENCES `tipo_prediccion` (`idTipoPrediccion`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci COMMENT='almacena modelos de redes neuronales, sus características y ficheros asociados';

-- Volcando datos para la tabla bbdd_modelos_neuronales.modelos: ~5 rows (aproximadamente)
DELETE FROM `modelos`;
/*!40000 ALTER TABLE `modelos` DISABLE KEYS */;
INSERT INTO `modelos` (`ID_MODELO`, `NOMBRE`, `DESCRIPCION`, `INPUT_HEIGHT`, `INPUT_WIDTH`, `INPUT_CHANNELS`, `FORMATO_MATRIZ_IMG`, `PATH_FICHERO`, `TIPO_ALMACENAMIENTO`, `TIPO_FICHERO`, `TIPO_PREDICCION`, `TIPO_SALIDA`) VALUES
	(1, 'modelo secuencial sencillo Cifar10', 'modelo de clasificación de imágenes entrenado con DATASET Cifar10, 100 Epochs', 32, 32, 3, NULL, 'modelos\\\\modelo_sencillo_cifar10_100epochs.h5', 2, 1, 1, 1),
	(2, 'r-cnn_vgg16 entrenado con UAV-DB', 'modelo de reconocimiento de objetos en imágenes, basado en el modelo preentrenado VGG16', 224, 224, 3, NULL, 'modelos\\\\r-cnn-vgg16-detector-residuos.h5', 2, 1, 2, 2),
	(14, 'VGG19 Imagenet', 'VGG19 modelo de clasificación de imágenes entrenado con el dataset ImageNet', 224, 224, 3, NULL, 'modelos\\\\model_imagenet_vgg19.h5', 2, 1, 1, 2),
	(21, 'Cifar10 remoto 100 epochs', 'Cifar 10 modelo en Git', 32, 32, 3, NULL, 'https://github.com/juanjohell/repoIA/blob/0adc5591f599b3988feb74d8d8c8bba441d8d942/modelos_convolucionales/modelos_entrenados/cifar10_entrenado_10epochs.h5', 1, 1, 1, 1);
/*!40000 ALTER TABLE `modelos` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.tipos_almacenamiento
DROP TABLE IF EXISTS `tipos_almacenamiento`;
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
DROP TABLE IF EXISTS `tipos_fichero`;
CREATE TABLE IF NOT EXISTS `tipos_fichero` (
  `idTipoFichero` int(10) unsigned NOT NULL COMMENT 'primary',
  `nombreCorto` varchar(2) NOT NULL COMMENT 'identifica al tipo de fichero',
  `nombreLargo` varchar(50) NOT NULL,
  `descripcion` varchar(250) NOT NULL COMMENT 'Descripción del formato',
  PRIMARY KEY (`idTipoFichero`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='tipos de formatos de almacenamiento de ficheros de redes neuronales entrenadas';

-- Volcando datos para la tabla bbdd_modelos_neuronales.tipos_fichero: ~3 rows (aproximadamente)
DELETE FROM `tipos_fichero`;
/*!40000 ALTER TABLE `tipos_fichero` DISABLE KEYS */;
INSERT INTO `tipos_fichero` (`idTipoFichero`, `nombreCorto`, `nombreLargo`, `descripcion`) VALUES
	(1, 'h5', 'kerash5', 'formato de fichero H5 de modelos implementados en Keras'),
	(2, 'tf', 'tensorFlowPB', 'formato pb protobuffer de TensorFlow'),
	(3, 'on', 'onnx', 'formato onnx');
/*!40000 ALTER TABLE `tipos_fichero` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.tipo_prediccion
DROP TABLE IF EXISTS `tipo_prediccion`;
CREATE TABLE IF NOT EXISTS `tipo_prediccion` (
  `idTipoPrediccion` int(11) unsigned NOT NULL,
  `nombre` varchar(50) NOT NULL DEFAULT '',
  `descripcion` varchar(250) NOT NULL DEFAULT '',
  PRIMARY KEY (`idTipoPrediccion`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Determina si el modelo es para prediccion o deteccion de objetos';

-- Volcando datos para la tabla bbdd_modelos_neuronales.tipo_prediccion: ~2 rows (aproximadamente)
DELETE FROM `tipo_prediccion`;
/*!40000 ALTER TABLE `tipo_prediccion` DISABLE KEYS */;
INSERT INTO `tipo_prediccion` (`idTipoPrediccion`, `nombre`, `descripcion`) VALUES
	(1, 'clasificacion', 'Clasificacion de imagenes'),
	(2, 'deteccion', 'bounding boxes, deteccion de imagenes');
/*!40000 ALTER TABLE `tipo_prediccion` ENABLE KEYS */;

-- Volcando estructura para tabla bbdd_modelos_neuronales.tipo_salida
DROP TABLE IF EXISTS `tipo_salida`;
CREATE TABLE IF NOT EXISTS `tipo_salida` (
  `idTiposalida` int(11) unsigned NOT NULL,
  `nombre` varchar(50) NOT NULL DEFAULT '',
  `descripcion` varchar(250) NOT NULL DEFAULT '',
  PRIMARY KEY (`idTiposalida`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Si la salida del modelo es secuencial (matriz unidimendional) o multicapa (matriz multidimensional)';

-- Volcando datos para la tabla bbdd_modelos_neuronales.tipo_salida: ~2 rows (aproximadamente)
DELETE FROM `tipo_salida`;
/*!40000 ALTER TABLE `tipo_salida` DISABLE KEYS */;
INSERT INTO `tipo_salida` (`idTiposalida`, `nombre`, `descripcion`) VALUES
	(1, 'secuencial', 'Matriz unidimensional'),
	(2, 'multiLayer', 'Matriz multidimensional');
/*!40000 ALTER TABLE `tipo_salida` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
