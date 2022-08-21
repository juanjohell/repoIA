# -*- coding: utf-8 -*-
"""
Created on Sat Jul  9 02:01:58 2022

@author: juanjo
"""

# import the necessary packages

from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.preprocessing.image import load_img
from tensorflow.keras.models import load_model, Model
from keras.layers import Input, Lambda
from yolo3.model import preprocess_true_boxes, yolo_body, yolo_loss
import keras.backend as K
import numpy as np
import imutils
import cv2
import os
import matplotlib.pyplot as plt
from yolo import YOLO, detect_video
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

print("[INFO] imagen: " + imagenPath)
print("[INFO] loading object detector...")


def get_anchors(anchors_path):
    '''loads the anchors from a file'''
    with open(anchors_path) as f:
        anchors = f.readline()
    anchors = [float(x) for x in anchors.split(',')]
    return np.array(anchors).reshape(-1, 2)

#Si solo tenemos los pesos necesitamos crear el modelo y cargarlos
def create_model(input_shape, anchors, num_classes, load_pretrained=True, freeze_body=2,
            weights_path='model_data/yolo_weights.h5'):
    '''create the training model'''
    K.clear_session() # get a new session
    image_input = Input(shape=(None, None, 3))
    h, w = input_shape
    num_anchors = len(anchors)

    y_true = [Input(shape=(h//{0:32, 1:16, 2:8}[l], w//{0:32, 1:16, 2:8}[l], \
        num_anchors//3, num_classes+5)) for l in range(3)]

    model_body = yolo_body(image_input, num_anchors//3, num_classes)
    print('Create YOLOv3 model with {} anchors and {} classes.'.format(num_anchors, num_classes))

    if load_pretrained:
        model_body.load_weights(weights_path, by_name=True, skip_mismatch=True)
        print('Load weights {}.'.format(weights_path))
        if freeze_body in [1, 2]:
            # Freeze darknet53 body or freeze all but 3 output layers.
            num = (185, len(model_body.layers)-3)[freeze_body-1]
            for i in range(num): model_body.layers[i].trainable = False
            print('Freeze the first {} layers of total {} layers.'.format(num, len(model_body.layers)))

    model_loss = Lambda(yolo_loss, output_shape=(1,), name='yolo_loss',
        arguments={'anchors': anchors, 'num_classes': num_classes, 'ignore_thresh': 0.5})(
        [*model_body.output, *y_true])
    model = Model([model_body.input, *y_true], model_loss)

    return model

anchors = get_anchors(anchors_path)
model = create_model(input_shape, anchors, num_classes,
        freeze_body=2, weights_path='.\\model_data\\yolo_weights.h5')

model.save('modelo_yolo3_coco.h5')

img = cv2.imread(os.path.join(img_dir,imagen))
print (img.shape)


#image = load_img(imagenPath, target_size=(416, 416))
#image = img_to_array(image) / 255.0

#image = np.expand_dims(image, axis=0)

