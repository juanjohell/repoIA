# -*- coding: utf-8 -*-
"""
Created on Tue Aug 16 12:09:04 2022

@author: juanjo
"""

#CONVERSION DE MODELO EN FORMATO PB DE TENSORFLOW
#A FORMATO H5 DE KERAS
input_shape = (416,416)
ruta_modelo_pb = '../weights/yolov5'
nombre_modelo_h5 = './yolov5_residuos.h5'
nombre_modelo_onnx = './yolov5.onnx'

import os
import tensorflow as tf
from tensorflow.keras.preprocessing import image
import numpy as np
from tensorflow.keras.optimizers import Adam
import tensorflow_hub as hub 
import onnx2keras
from onnx2keras import onnx_to_keras
from tf2onnx import convert
import keras
import onnx

#convertimos de pb a ONNX
#convert(ruta_modelo_pb, nombre_modelo_onnx)

onnx_model = onnx.load(nombre_modelo_onnx)
#convertimos a H5
k_model = onnx_to_keras(onnx_model, ['input1'])

keras.models.save_model(k_model,nombre_modelo_h5,overwrite=True,include_optimizer=True)

