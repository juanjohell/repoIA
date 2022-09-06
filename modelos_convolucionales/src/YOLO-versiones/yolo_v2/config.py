# -*- coding: utf-8 -*-
"""
Created on Sat Sep  3 10:03:11 2022

@author: jjhb01
"""
###################################################
#   CONFIGURACION                                 #
###################################################

### Los ficheros de anotaciones tienen que estar en formato
### PASCAL-VOC (XML) y las coordenadas de la etiqueta
### bndbox en formato (xmin,ymin,xmax,ymax)

### ¿Coordenadas sin normalizar?

train_xml_dir = "../../../datasets/uav-bd/train/pascal-voc/"  # directorio que contiene los xml
train_img_dir = "../../../datasets/uav-bd/train/images/"   # directorios con las imagenes
val_xml_dir = "../../../datasets/uav-bd/val/pascal-voc/"  # directorio que contiene los xml
val_img_dir = "../../../datasets/uav-bd/val/images/"   # directorios con las imagenes
labels = ["botella"]
image_size = 416  # tamaño YOLO en pixeles para entrenar la red 
imagen_dataset_size = 342
mejores_pesos = "red_deteccion_de_residuos.h5"
FULL_YOLO_BACKEND_PATH  = "full_yolo_backend.h5"
num_anchors = 5
NUM_EPOCHS = 5
BATCH_SIZE = 50

LABELS= ['botella']