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

-- Volcando datos para la tabla bbdd_modelos_neuronales.categorias: ~12 rows (aproximadamente)
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
	(10, 1, 10, 'Camión'),
	(11, 12, 1, 'Persona'),
	(13, 12, 2, 'Bicicleta'),
	(14, 12, 3, 'Coche');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;

-- Volcando datos para la tabla bbdd_modelos_neuronales.config_modelo: ~0 rows (aproximadamente)
DELETE FROM `config_modelo`;
/*!40000 ALTER TABLE `config_modelo` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_modelo` ENABLE KEYS */;

-- Volcando datos para la tabla bbdd_modelos_neuronales.elementos_config: ~2 rows (aproximadamente)
DELETE FROM `elementos_config`;
/*!40000 ALTER TABLE `elementos_config` DISABLE KEYS */;
INSERT INTO `elementos_config` (`idElemento`, `nombreElemento`) VALUES
	(1, 'colorCaja'),
	(2, 'grosorCaja');
/*!40000 ALTER TABLE `elementos_config` ENABLE KEYS */;

-- Volcando datos para la tabla bbdd_modelos_neuronales.ficheros: ~0 rows (aproximadamente)
DELETE FROM `ficheros`;
/*!40000 ALTER TABLE `ficheros` DISABLE KEYS */;
/*!40000 ALTER TABLE `ficheros` ENABLE KEYS */;

-- Volcando datos para la tabla bbdd_modelos_neuronales.modelos: ~6 rows (aproximadamente)
DELETE FROM `modelos`;
/*!40000 ALTER TABLE `modelos` DISABLE KEYS */;
INSERT INTO `modelos` (`ID_MODELO`, `NOMBRE`, `DESCRIPCION`, `INPUT_HEIGHT`, `INPUT_WIDTH`, `INPUT_CHANNELS`, `FORMATO_MATRIZ_IMG`, `PATH_FICHERO`, `TIPO_ALMACENAMIENTO`, `TIPO_FICHERO`, `TIPO_PREDICCION`, `TIPO_SALIDA`) VALUES
	(1, 'Cifar10', 'modelo de clasificación de imágenes entrenado con DATASET Cifar10, 100 Epochs', 32, 32, 3, NULL, 'modelos\\\\modelo_sencillo_cifar10_100epochs.h5', 2, 1, 1, 1),
	(2, 'r-cnn_vgg16 entrenado con UAV-DB', 'modelo de reconocimiento de objetos en imágenes, basado en el modelo preentrenado VGG16', 224, 224, 3, NULL, 'modelos\\\\rcnn_VGG16_reconocimiento_botellas.h5', 2, 1, 2, 2),
	(12, 'yolov5s', 'modelo YOLO 5 con uav-bd', 320, 320, 3, NULL, 'modelos\\\\yolov5s-last.h5', 2, 1, 2, 2),
	(14, 'VGG19 Imagenet', 'VGG19 modelo de clasificación de imágenes entrenado con el dataset ImageNet', 224, 224, 3, NULL, 'modelos\\\\model_imagenet_vgg19.h5', 2, 1, 1, 2),
	(15, 'r-cnn-vgg16 revisado', 'reconocimiento solo 10', 224, 224, 3, NULL, 'modelos\\\\detector.h5', 2, 1, 2, 2),
	(21, 'Cifar10 remoto 100 epochs', 'Cifar 10 modelo en Git', 32, 32, 3, NULL, 'https://github.com/juanjohell/repoIA/blob/a6d07f8c6d71805d1d9776fe16d84aeb3179e53e/modelos_convolucionales/modelos_entrenados/cifar10_entrenado_10epochs.h5', 3, 1, 1, 1);
/*!40000 ALTER TABLE `modelos` ENABLE KEYS */;

-- Volcando datos para la tabla bbdd_modelos_neuronales.tipos_almacenamiento: ~3 rows (aproximadamente)
DELETE FROM `tipos_almacenamiento`;
/*!40000 ALTER TABLE `tipos_almacenamiento` DISABLE KEYS */;
INSERT INTO `tipos_almacenamiento` (`idTipoAlmacenamiento`, `nombre`) VALUES
	(1, 'Base de datos'),
	(2, 'Sistema ficheros servidor'),
	(3, 'Google Drive o similar');
/*!40000 ALTER TABLE `tipos_almacenamiento` ENABLE KEYS */;

-- Volcando datos para la tabla bbdd_modelos_neuronales.tipos_fichero: ~3 rows (aproximadamente)
DELETE FROM `tipos_fichero`;
/*!40000 ALTER TABLE `tipos_fichero` DISABLE KEYS */;
INSERT INTO `tipos_fichero` (`idTipoFichero`, `nombreCorto`, `nombreLargo`, `descripcion`) VALUES
	(1, 'h5', 'kerash5', 'formato de fichero H5 de modelos implementados en Keras'),
	(2, 'tf', 'tensorFlowPB', 'formato pb protobuffer de TensorFlow'),
	(3, 'on', 'onnx', 'formato onnx');
/*!40000 ALTER TABLE `tipos_fichero` ENABLE KEYS */;

-- Volcando datos para la tabla bbdd_modelos_neuronales.tipo_prediccion: ~2 rows (aproximadamente)
DELETE FROM `tipo_prediccion`;
/*!40000 ALTER TABLE `tipo_prediccion` DISABLE KEYS */;
INSERT INTO `tipo_prediccion` (`idTipoPrediccion`, `nombre`, `descripcion`) VALUES
	(1, 'clasificacion', 'Clasificacion de imagenes'),
	(2, 'deteccion', 'bounding boxes, deteccion de imagenes');
/*!40000 ALTER TABLE `tipo_prediccion` ENABLE KEYS */;

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
