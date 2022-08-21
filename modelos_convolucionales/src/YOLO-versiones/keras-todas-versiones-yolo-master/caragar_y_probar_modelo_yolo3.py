# -*- coding: utf-8 -*-
"""
Created on Thu Aug 18 19:22:09 2022

@author: juanjo
"""
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.preprocessing.image import load_img
from tensorflow.keras.models import load_model
import numpy as np
import imutils
import cv2
import os
import matplotlib.pyplot as plt
from PIL import Image

input_shape = (416,416) # multiple of 32, hw
imagen_prueba = "ciudad"
extension = "jpg"
img_dir = '../../datasets/pruebas/'   # directorios con las imagenes
num_classes = 1
image_height = 416
image_width = 416
sep="."
imagen = sep.join((imagen_prueba,extension))
sep=""
imagenPath = sep.join((img_dir,imagen))

classes_path = 'model_data/coco_classes.txt'
anchors_path = 'model_data/yolo_anchors.txt'

print("[INFO] loading object detector...")
model = load_model('modelo_yolo3_coco.h5')
image = Image.open(imagenPath)

preds = model.predict(image)
print (preds)