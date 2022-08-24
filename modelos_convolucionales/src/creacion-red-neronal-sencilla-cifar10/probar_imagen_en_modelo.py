# -*- coding: utf-8 -*-
"""
Created on Sat May  7 19:19:39 2022

@author: juanjo
"""
import sys
from os import path

from tensorflow.keras.preprocessing.image import load_img, img_to_array
from tensorflow.keras.models import load_model

import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import numpy as np

IMAGEN_PATH = "../../datasets/cifar/coche.jpg"
MODELO_PATH = "../../modelos_entrenados/cifar10_entrenado_10epochs.h5"


labels = ['Airplane', 'Automobile', 'Bird', 'Cat', 'Deer', 'Dog', 'Frog', 'Horse', 'Ship', 'Truck']

#funcion para carga de la imagen resolucion 32x32 pixels
def load_image(filename):
	image = load_img(filename, target_size=(32, 32))
	image = img_to_array(image)
	image = image.reshape(1, 32, 32, 3)
	image = image.astype('float32')/255.0
	return image

#funcion para cargar modelo y probar imagen indicada
def predict():
	
	image = load_image(IMAGEN_PATH)
	model = load_model(MODELO_PATH)
	result = np.argmax(model.predict(image), axis=-1)
	plt.imshow(mpimg.imread(IMAGEN_PATH))
	plt.axis('off')
	plt.title('Predicted label: {}'.format(labels[result[0]]))
	plt.show()

 
predict()