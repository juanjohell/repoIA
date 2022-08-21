# -*- coding: utf-8 -*-
"""
Created on Sat Jul  9 13:31:54 2022

@author: juanjo
"""

# -*- coding: utf-8 -*-
"""
Created on Fri Jul  8 13:14:11 2022

@author: juanjo
"""
#ENTRENAR UN MODELO

#https://pyimagesearch.com/2020/10/05/object-detection-bounding-box-regression-with-keras-tensorflow-and-deep-learning/

# import the necessary packages

from tensorflow.keras.applications import VGG16
from tensorflow.keras.layers import Flatten
from tensorflow.keras.layers import Dense
from tensorflow.keras.layers import Input
from tensorflow.keras.models import Model
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.preprocessing.image import load_img

import funciones_utilidades as utils
import matplotlib.pyplot as plt
import numpy as np
import cv2
import os


xml_train_dir = "../../datasets/uav-bd/train/labels/"  # directorio que contiene los xml
img_train_dir = "../../datasets/uav-bd/train/images/"   # directorios con las imagenes
img_val_dir = "../../datasets/uav-bd/val/images/"
xml_val_dir = "../../datasets/uav-bd/val/labels/"

# initialize our initial learning rate, number of epochs to train
# for, and the batch size

<<<<<<< Upstream, based on branch 'master' of https://github.com/juanjohell/repoIA.git
NUM_EPOCHS = 100
BATCH_SIZE = 10
=======
NUM_EPOCHS = 10
BATCH_SIZE = 100
>>>>>>> 28c4567 fuentes python-keras (sobre TensorFlow) para la generacion del modelo rcnn-vgg16

extension=".jpg"

# initialize the list of data (images), our target output predictions
# (bounding box coordinates), along with the filenames of the
# individual images
train_images = []
train_targets = []
train_filenames = []
train_name_rows = []
train_coord_rows = []

test_images = []
test_targets = []
test_filenames = []
test_name_rows = []
test_coord_rows = []

# load the contents of the CSV annotations file
print("[INFO] loading dataset...")
#en rows debe haber: (filename, startX, startY, endX, endY)
for e,i in enumerate(os.listdir(xml_train_dir)) :
    sep=""
    name_file = sep.join((i.split('.')[0],extension))
    xmlpath = sep.join((xml_train_dir,i))
    name, boxes = utils.read_content_from_center_wh(xmlpath)
    for x, box in (enumerate(boxes)):
        nbox = utils.return_coordinates_of_angled_rec(box[0],box[1], box[2], box[3], box[4])
        train_name_rows.append(name_file)
        train_coord_rows.append(nbox)
    #print(name_file)    
#ya tenemos todas las imagenes y coordenadas

print(len(train_name_rows))
print(len(train_coord_rows))
for filename,nbox in zip(train_name_rows, train_coord_rows):
    #print(name)
    #print(coords)
    # break the row into the filename and bounding box coordinates
    sep="."
    (startX, startY,endX, endY) = utils.return_two_point_coordinates(nbox[0],nbox[1],nbox[2], nbox[3])
    imagePath = os.path.join(img_train_dir, filename)
    #print(imagePath)
    image = cv2.imread(imagePath)
    (h, w) = image.shape[:2]
    cv2.rectangle(image, (startX, startY), (endX, endY),
    		(255, 255, 0), 3)
    #plt.figure()
    #plt.imshow(image) 
    
	# scale the bounding box coordinates relative to the spatial
	# dimensions of the input image
	# load the image and preprocess it
    startX = float(startX) / w
    startY = float(startY) / h
    endX = float(endX) / w
    endY = float(endY) / h
    
    image = load_img(imagePath, target_size=(224, 224))
    if image is None:
        break
    
    image = img_to_array(image)
    #update our list of data, targets, and filenames
    train_images.append(image)
    train_targets.append((startX,startY,endX,endY))
    train_filenames.append(filename)
#print(imagePath)
#print(len(train_images))
#print(len(train_targets))

#ahora los datos de validacion, no hay que hacer split
#pues ya vienen separados
for e,i in enumerate(os.listdir(xml_val_dir)) :
    sep=""
    name_file = sep.join((i.split('.')[0],extension))
    xmlpath = sep.join((xml_val_dir,i))
    name, boxes = utils.read_content_from_center_wh(xmlpath)
    for i, box in (enumerate(boxes)):
        nbox = utils.return_coordinates_of_angled_rec(box[0],box[1], box[2], box[3], box[4])
        test_name_rows.append(name_file)
        test_coord_rows.append(nbox)
        
#ya tenemos todas las imagenes y coordenadas
for filename,nbox in zip(test_name_rows, test_coord_rows):
#for i, row in enumerate(test_rows) :
    # break the row into the filename and bounding box coordinates
    sep="."
    (startX, startY,endX, endY) = utils.return_two_point_coordinates(nbox[0],nbox[1],nbox[2], nbox[3])
    
    imagePath = os.path.join(img_val_dir, filename)
    
    # derive the path to the input image, load the image (in OpenCV
    # format), and grab its dimensions
    image = cv2.imread(imagePath)
    (h, w) = image.shape[:2]
     
    
	# scale the bounding box coordinates relative to the spatial
	# dimensions of the input image
	# load the image and preprocess it
    startX = float(startX) / w
    startY = float(startY) / h
    endX = float(endX) / w
    endY = float(endY) / h
    
    
    image = load_img(imagePath, target_size=(224, 224))
    if image is None:
        break
    image = img_to_array(image)
    #update our list of data, targets, and filenames
    test_images.append(image)
    test_targets.append((startX,startY,endX,endY))
    test_filenames.append(filename)    
    
    
# convert the data and targets to NumPy arrays, scaling the input
# pixel intensities from the range [0, 255] to [0, 1]
train_data = np.array(train_images, dtype="float32") / 255.0
#print(train_targets)
train_targ = np.array(train_targets, dtype="float32")

test_data = np.array(test_images, dtype="float32") / 255.0
test_targ = np.array(test_targets, dtype="float32")

# load the VGG16 network, ensuring the head FC layers are left off
vgg = VGG16(weights="imagenet", include_top=False,
	input_tensor=Input(shape=(224, 224, 3)))
# freeze all VGG layers so they will *not* be updated during the
# training process
vgg.trainable = False
# flatten the max-pooling output of VGG
flatten = vgg.output
flatten = Flatten()(flatten)

# construct a fully-connected layer header to output the predicted
# bounding box coordinates
bboxHead = Dense(128, activation="relu")(flatten)
bboxHead = Dense(64, activation="relu")(bboxHead)
bboxHead = Dense(32, activation="relu")(bboxHead)
bboxHead = Dense(4, activation="sigmoid")(bboxHead)
# construct the model we will fine-tune for bounding box regression
model = Model(inputs=vgg.input, outputs=bboxHead)

# initialize the optimizer, compile the model, and show the model
# summary
opt = Adam()
model.compile(loss="mse", optimizer=opt)
print(model.summary())
# train the network for bounding box regression
print("[INFO] training bounding box regressor...")
H = model.fit(
	train_data, train_targ,
	validation_data=(test_data, test_targ),
	batch_size=BATCH_SIZE,
	epochs=NUM_EPOCHS,
	verbose=1)

# serialize the model to disk
print("[INFO] saving object detector model...")
model.save('rcnn_VGG16_reconocimiento_botellas_coord_no_rotadas.h5')
# plot the model training history
N = NUM_EPOCHS
plt.style.use("ggplot")
plt.figure()
plt.plot(np.arange(0, N), H.history["loss"], label="train_loss")
plt.plot(np.arange(0, N), H.history["val_loss"], label="val_loss")
plt.title("Bounding Box Regression Loss on Training Set")
plt.xlabel("Epoch #")
plt.ylabel("Loss")
plt.legend(loc="lower left")
plt.savefig()

