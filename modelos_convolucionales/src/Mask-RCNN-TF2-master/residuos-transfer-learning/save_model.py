# -*- coding: utf-8 -*-
"""
Created on Wed Sep 21 18:27:55 2022

@author: jjhb01
"""
import mrcnn
import mrcnn.config
import mrcnn.model
import mrcnn.visualize
import os
from tensorflow.keras.models import model_from_json

# load the class label names from disk, one label per line
# CLASS_NAMES = open("coco_labels.txt").read().strip().split("\n")

CLASS_NAMES = ['BG', 'botella']

class SimpleConfig(mrcnn.config.Config):
    # Give the configuration a recognizable name
    NAME = "coco_inference"
    
    # set the number of GPUs to use along with the number of images per GPU
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1

	# Number of classes = number of classes + 1 (+1 for the background). The background class is named BG
    NUM_CLASSES = len(CLASS_NAMES)

# Initialize the Mask R-CNN model for inference and then load the weights.
# This step builds the Keras model architecture.
model = mrcnn.model.MaskRCNN(mode="inference", 
                             config=SimpleConfig(),
                             model_dir=os.getcwd())


# Load the weights into the model.
model.load_weights(filepath="Residuos_weights_mask_rcnn_trained.h5", 
                   by_name=True)

# serialize model to JSON
model_json = model.to_json()
with open("Residuos_mask_rcnn_trained.json", "w") as json_file:
    json_file.write(model_json)

modelo = model_from_json("Residuos_mask_rcnn_trained.json")
modelo.load_weights("Residuos_weights_mask_rcnn_trained.h5")
modelo.save("Residuos_mask_rcnn_trained.h5")


