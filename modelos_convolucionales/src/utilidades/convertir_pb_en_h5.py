# -*- coding: utf-8 -*-
"""
Created on Tue Aug 16 12:09:04 2022

@author: juanjo
"""

import sys
sys.path.append('../yolov5_in_tf2_keras')

#CONVERSION DE MODELO EN FORMATO PB DE TENSORFLOW
#A FORMATO H5 DE KERAS
import os
import tensorflow as tf
from tensorflow.keras.models import Model, save_model
from tensorflow.keras.preprocessing import image
from yolov5l import Yolov5l
from yolov5x import Yolov5x
from yolov5m import Yolov5m
from yolov5s import Yolov5s
from layers import nms, YoloHead
import numpy as np
import tensorflow as tf
from layers import Conv, C3, SPPF, Concat
sys.path.append('./')

pb_model_dir = "..\\..\\modelos_entrenados\\yolov5_keras"
h5_model_pesos = "..\\..\\modelos_entrenados\\yolov5_residuos_keras_pesos.h5"
h5_model = "..\\..\\modelos_entrenados\\yolov5_residuos_keras.h5"
json_file = "..\\..\\modelos_entrenados\\modelo.json"

yolov5_type = "5m"
#shape para el tamaño 5m
image_shape = (640, 640, 3)
num_class = 1
#datos conocidos del entrenamiento :
batch_size = 1

anchors = np.array([[10, 13], [16, 30], [33, 23],
                    [30, 61], [62, 45], [59, 119],
                    [116, 90], [156, 198], [373, 326]]) / 640.
anchors = np.array(anchors, dtype=np.float32)
anchor_masks = np.array([[0, 1, 2], [3, 4, 5], [6, 7, 8]], dtype=np.int8)

########################################################################################3


##########################################################################################
# SE CONSTRUYE EL MODELO , SACADO DE yolo5m.py
##########################################################################################


model = Model
#model.load_weights(h5_model_pesos)
model.load_weights(self=model,filepath=h5_model_pesos)

model.summary()
print('cargados pesos')

print('creado modelo')
model.save(h5_model)












'''
# Loading the Tensorflow Saved Model (PB)
model = tf.keras.models.load_model(pb_model_dir)
#save_model(model,h5_model,save_format='h5')
print(model.summary())
print(model.input)
print(model.outputs)
print(model.optimizer)



model = Model(model.input, model.outputs)

model.compile()
print(model.summary())
# Saving the Model in H5 Format
model.save_weights(h5_model_pesos)

model.save(h5_model)
#tf.keras.models.save_model(model, h5_model)

# Loading the H5 Saved Model
#loaded_model_from_h5 = tf.keras.models.load_model(h5_model)
#print(loaded_model_from_h5.summary())
'''
