# -*- coding: utf-8 -*-
"""
Created on Tue Aug 16 12:09:04 2022

@author: juanjo
"""



#CONVERSION DE MODELO EN FORMATO PB DE TENSORFLOW
#A FORMATO H5 DE KERAS
import os
import tensorflow as tf
from tensorflow.keras.models import Model, save_model
from tensorflow.keras.preprocessing import image

#from layers import nms, YoloHead
import numpy as np
import tensorflow as tf
#from layers import Conv, C3, SPPF, Concat
import keras.backend as K
from tensorflow.keras.optimizers import Adam
import sys
sys.path.append('..\\YOLO-versiones\\keras-todas-versiones-yolo-master')

from yolo3.utils import get_random_data
from yolo3.model import preprocess_true_boxes, yolo_body, tiny_yolo_body, yolo_loss
from keras.callbacks import TensorBoard, ModelCheckpoint, ReduceLROnPlateau, EarlyStopping
#sys.path.append('./')

pb_model_dir = "..\\..\\modelos_entrenados\\yolov5_keras"
h5_model_pesos = "..\\..\\modelos_entrenados\\yolov5_residuos_keras_pesos.h5"
h5_model = "..\\..\\modelos_entrenados\\yolov5_residuos_keras.h5"

log_dir = 'logs/000/'
annotation_path ="..\\..\\datasets\\uav-bd\\train\\labels_txt\\etiquetas_train_espacios.txt"
yolov5_type = "5m"
#shape para el tamaño 5m
input_shape = (640, 640, 3)
num_classes = 3

#datos conocidos del entrenamiento :
batch_size = 20
num_val = 4064
num_train = 16253
EPOCH=10

 
anchors = np.array([[10, 13], [16, 30], [33, 23],
                    [30, 61], [62, 45], [59, 119],
                    [116, 90], [156, 198], [373, 326]]) / 640.
anchors = np.array(anchors, dtype=np.float32)
anchor_masks = np.array([[0, 1, 2], [3, 4, 5], [6, 7, 8]], dtype=np.int8)

model=tf.keras.models.load_model(pb_model_dir)

#WARNING:tensorflow:No training configuration found in save file, 
#so the model was *not* compiled. Compile it manually.

def totalloss(y_true, y_pred):
    return K.sum(y_pred)/K.cast(K.shape(y_pred)[0],K.dtype(y_pred))

def data_generator_wrapper(annotation_lines, batch_size, input_shape, anchors, num_classes):
    n = len(annotation_lines)
    if n==0 or batch_size<=0: return None
    return data_generator(annotation_lines, batch_size, input_shape, anchors, num_classes)

def data_generator(annotation_lines, batch_size, input_shape, anchors, num_classes):
    '''data generator for fit_generator'''
    n = len(annotation_lines)
    i = 0
    while True:
        image_data = []
        box_data = []
        for b in range(batch_size):
            if i==0:
                np.random.shuffle(annotation_lines)
            image, box = get_random_data(annotation_lines[i], input_shape, random=True)
            print (image)
            image_data.append(image)
            box_data.append(box)
            i = (i+1) % n
        image_data = np.array(image_data)
        box_data = np.array(box_data)
        y_true = preprocess_true_boxes(box_data, input_shape, anchors, num_classes)
        yield [image_data, *y_true], np.zeros(batch_size)

model.compile(optimizer=Adam(lr=1e-3), loss=totalloss)

val_split = 0.1
with open(annotation_path) as f:
    lines = f.readlines()
np.random.seed(10101)
np.random.shuffle(lines)
np.random.seed(None)
num_val = int(len(lines)*val_split)
num_train = len(lines) - num_val
logging = TensorBoard(log_dir=log_dir)
checkpoint = ModelCheckpoint(log_dir + 'ep{epoch:03d}-loss{loss:.3f}-val_loss{val_loss:.3f}.h5',
    monitor='val_loss', save_weights_only=True, save_best_only=True, save_freq=3)


print('Train on {} samples, val on {} samples, with batch size {}.'.format(num_train, num_val, batch_size))
model.fit_generator(data_generator_wrapper(lines[:num_train], batch_size, input_shape, anchors, num_classes),
        steps_per_epoch=max(1, num_train//batch_size),
        validation_data=data_generator_wrapper(lines[num_train:], batch_size, input_shape, anchors, num_classes),
        validation_steps=max(1, num_val//batch_size),
        epochs=EPOCH,
        initial_epoch=0,
        callbacks=[logging, checkpoint])

type(model)
model._is_graph_network

#model = Model
#model.load_weights(h5_model_pesos)
#model.load_weights(self=model,filepath=h5_model_pesos)

#model.summary()
print('cargados pesos')

print('creado modelo')
#model.save(h5_model)












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
