# -*- coding: utf-8 -*-
"""
Created on Sat Jul  9 02:01:58 2022

@author: juanjo
"""

# import the necessary packages

from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.preprocessing.image import load_img
from tensorflow.keras.models import load_model
import numpy as np
import imutils
import cv2
import os
import matplotlib.pyplot as plt

image_height = 342
image_width = 342

imagen_prueba = "1_Sand_000020_71_1"
extension = "jpg"
img_dir = '../../datasets/uav-bd/val/images/'   # directorios con las imagenes



image_height = 224
image_width = 224

sep="."
imagen = sep.join((imagen_prueba,extension))
sep=""
imagenPath = sep.join((img_dir,imagen))
print("[INFO] imagen: " + imagenPath)
print("[INFO] loading object detector...")
model = load_model('rcnn_VGG16_reconocimiento_botellas_coord_no_rotadas.h5')

image = load_img(imagenPath, target_size=(224, 224))
image = img_to_array(image) / 255.0

image = np.expand_dims(image, axis=0)

# make bounding box predictions on the input image

preds = model.predict(image)[0]
print (preds)
(startX, startY, endX, endY) = preds
#(endX, endY,startX, startY) = preds
img = cv2.imread(os.path.join(img_dir,imagen))
#image = imutils.resize(img, width=600)
print (img.shape[:2])

(w,h) = img.shape[:2]

# scale the bounding box coordinates relative to the spatial
#dimensions of the input image
#load the image and preprocess it
startX = int(startX * w)
startY = int(startY * h)
endX = int(endX * w)
endY = int(endY * h)
    
print(startX, startY, endX, endY)

# draw the predicted bounding box on the image
cv2.rectangle(img, (startX,startY), (endX,endY),
		(255, 255, 0), 3)
    
# load the input image (in OpenCV format), resize it such that it
# fits on our screen, and grab its dimensions
   
# show the output image
plt.figure()
plt.imshow(img)