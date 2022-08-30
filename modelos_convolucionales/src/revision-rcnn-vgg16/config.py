

#CÓDIGO ADAPTADO DE:
#https://pyimagesearch.com/2020/10/05/object-detection-bounding-box-regression-with-keras-tensorflow-and-deep-learning/

# -*- coding: utf-8 -*-
"""
Created on Sat Aug 27 18:17:38 2022

@author: jjhb01
"""

# import the necessary packages
import os
# acceso a ficheros
BASE_PATH = "data"
IMAGES_PATH = os.path.sep.join([BASE_PATH, "images"])
ANNOTATION_PATH = os.path.sep.join([BASE_PATH, "labels"])
#formato para anotaciones:
# path_imagen box1 box2 
# donde box es = startx, starty, endx, endy
ANNOTS_PATH_TRAIN = os.path.sep.join([BASE_PATH, "etiquetas_train_botellas_r-cnn-vgg16.txt"])
ANNOTS_PATH_VAL = os.path.sep.join([BASE_PATH, "etiquetas_val_botellas_r-cnn-vgg16.txt"])

# directorio para las salidas
BASE_OUTPUT = "output"
# nombre de elementos a generar en la salida
MODEL_PATH = os.path.sep.join([BASE_OUTPUT, "detector_residuos.h5"])
PLOT_PATH = os.path.sep.join([BASE_OUTPUT, "plot.png"])
TEST_FILENAMES = os.path.sep.join([BASE_OUTPUT, "test_images.txt"])

# inicializar valores para el entrenamiento
INIT_LR = 1e-4
NUM_EPOCHS = 10
BATCH_SIZE = 50


