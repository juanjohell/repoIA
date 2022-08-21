# -*- coding: utf-8 -*-
"""
Created on Sat May  7 23:58:34 2022

@author: juanjo
"""
import tensorflowjs as tfjs
#importando el modelo 
from keras.models import load_model 
model = load_model('.\modelos\cifar10_entrenado_10epochs.h5')

#convertimos a formato exportable a tensorflow.js
tfjs.converters.save_keras_model(model,'.\modelos\cifar10js')