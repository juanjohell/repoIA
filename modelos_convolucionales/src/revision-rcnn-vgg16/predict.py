# -*- coding: utf-8 -*-
"""
Created on Sat Aug 27 18:44:41 2022

@author: jjhb01
"""

# import the necessary packages
import config
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.preprocessing.image import load_img
from tensorflow.keras.models import load_model
import numpy as np
import mimetypes
import argparse
#import imutils
import cv2
import os
import matplotlib.pyplot as plt

image_height = 224
image_width = 224

imagen_prueba = "1_Sand_000020_71_1"
imagen_prueba = "1_Sand_000011_52_1"
#imagen_prueba = "1_Sand_000001_78_2"
extension = "jpg"
sep="."
img_dir = '../../datasets/uav-bd/val/images/'   # directorios con las imagenes

filename= sep.join((imagen_prueba,extension))

sep=""
imagenPath = sep.join((img_dir,filename))
# determine the input file type, but assume that we're working with
# single input image
input = imagen_prueba
filetype = extension

# if the file type is a text file, then we need to process *multiple*
# images


# load our trained bounding box regressor from disk
print("[INFO] loading object detector...")
model = load_model(config.MODEL_PATH)
# loop over the images that we'll be testing using our bounding box

	# load the input image (in Keras format) from disk and preprocess
	# it, scaling the pixel intensities to the range [0, 1]
image = load_img(imagenPath, target_size=(224, 224))
image = img_to_array(image) / 255.0
image = np.expand_dims(image, axis=0)
	# make bounding box predictions on the input image
	
preds = model.predict(image)[0]
print (preds)
(startX, startY, endX, endY) = preds
#(endX, endY,startX, startY) = preds
img = cv2.imread(os.path.join(img_dir,filename))
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
cv2.rectangle(image, (startX, startY), (endX, endY),
		(0, 255, 0), 2)
cv2.rectangle(img, (startX,startY), (endX,endY),
		(255, 255, 0), 3)
	# show the output image
plt.figure()
plt.imshow(img)