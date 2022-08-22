# -*- coding: utf-8 -*-
"""
Created on Tue Aug 16 12:09:04 2022

@author: juanjo
"""

#CONVERSION DE MODELO EN FORMATO PB DE TENSORFLOW
#A FORMATO H5 DE KERAS
import os
import tensorflow as tf
from tensorflow.keras.models import Model
from tensorflow.keras.preprocessing import image


pb_model_dir = "..\\..\\modelos_compilados\\formatos_pb\\yolov5_keras"
h5_model_pesos = "..\\..\\modelos_compilados\\formato_h5\\yolov5_residuos_keras_weights.h5"
h5_model = "..\\..\\modelos_compilados\\formato_h5\\yolov5_residuos_keras.h5"
# Loading the Tensorflow Saved Model (PB)
model = tf.keras.models.load_model(pb_model_dir)
print(model.summary())
print(model.input)
print(model.outputs)
print(model.optimizer)

model = Model(model.input, model.outputs)

model.compile()
print(model.summary())
# Saving the Model in H5 Format
model.save_weights(h5_model_pesos)
tf.keras.models.save_json('..\\..\\modelos_compilados\\modelo.json')
model.save(h5_model)
#tf.keras.models.save_model(model, h5_model)

# Loading the H5 Saved Model
#loaded_model_from_h5 = tf.keras.models.load_model(h5_model)
#print(loaded_model_from_h5.summary())

