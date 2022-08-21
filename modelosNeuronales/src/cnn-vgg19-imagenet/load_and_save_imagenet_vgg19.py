# -*- coding: utf-8 -*-
"""
Created on Sat Aug 20 16:58:06 2022

@author: juanjo
"""
import tensorflow as tf

model= tf.keras.applications.VGG19( include_top=True, weights="imagenet", input_tensor=None, input_shape=None, pooling=None, classes=1000, classifier_activation="softmax", )

model.save('model_imagenet_vgg19.h5')
