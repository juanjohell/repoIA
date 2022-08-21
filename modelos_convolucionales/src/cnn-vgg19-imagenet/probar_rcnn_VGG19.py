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
import matplotlib.pyplot as plt
import tensorflow as tf

image_height = 342
image_width = 342

imagen_prueba = "persona"
extension = "jpg"
img_dir = '../../datasets/pruebas/'   # directorios con las imagenes


sep="."
imagen = sep.join((imagen_prueba,extension))
sep=""
imagenPath = sep.join((img_dir,imagen))
print("[INFO] imagen: " + imagenPath)
print("[INFO] loading object detector...")
#compile false porque no se guardó para entrenar
model = load_model('model_imagenet_vgg19.h5', compile=False)

image = load_img(imagenPath, target_size=(224, 224))
plt.imshow(image)
plt.show()

numpy_image = img_to_array(image)
plt.imshow(np.uint8(numpy_image))
plt.show()

print('numpy array size',numpy_image.shape)

image_batch = np.expand_dims(numpy_image, axis=0)
print('image batch size', image_batch.shape)
plt.imshow(np.uint8(image_batch[0]))

processed_image = tf.keras.applications.vgg19.preprocess_input(image_batch.copy())

# make bounding box predictions on the input image

predictions = model.predict(processed_image)


prediccion = tf.keras.applications.imagenet_utils.decode_predictions(
    predictions, top=5)


for prediction_id in range(len(prediccion[0])):
    print(prediccion[0][prediction_id])




