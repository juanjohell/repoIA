# -*- coding: utf-8 -*-
"""
Created on Sat Sep  3 09:46:21 2022

@author: jjhb01
"""
# CODIGO ADAPTADO DE:
# https://github.com/jbagnato/machine-learning/blob/master/Ejercicio_Object_Detection.ipynb

import sys
sys.path.append('yolo_v2')

import numpy as np
from config import *
from modelo_yolo_v2 import YOLO
from utilidades import avg_IOU, print_anchors, run_kmeans



from utilidades import leer_anotaciones
from config import *

#  cargamos anotaciones de entrenamineto
#train_imgs, train_labels = leer_anotaciones(train_xml_dir,train_img_dir, LABELS)
#print('imagenes',len(train_imgs), 'labels',len(train_labels))

#  cargamos anotaciones de validacion
#val_imgs, val_labels = leer_anotaciones(val_xml_dir,val_img_dir, LABELS)
#print('imagenes',len(val_imgs), 'labels',len(val_labels))

train_imgs, train_labels = leer_anotaciones(val_xml_dir,val_img_dir, LABELS)
print('imagenes',len(train_imgs), 'labels',len(train_labels))

#### SI HUBIERA QUE OBTENER EL SUBCONJUNTO DE VALIDACION DEL MISMO
#### DATASET DEL ENTRENAMIENTO HAY QUE PREPARAR DOS SUBCONJUNTOS
#### DE DATOS, en nuestro caso ya tenemos dos conjuntos distintos y no nos hace falta

train_valid_split = int(0.8*len(train_imgs))
np.random.shuffle(train_imgs)
val_imgs = train_imgs[train_valid_split:]
train_imgs = train_imgs[:train_valid_split]
print('train:',len(train_imgs), 'validate:',len(val_imgs))

####

# creamos anclas (anchors)

grid_w = int(image_size/32)
grid_h = int(image_size/32)
#grid_w = 13.0
#grid_h = 13.0

# run k_mean to find the anchors
annotation_dims = []
for image in train_imgs:
    cell_w = imagen_dataset_size/grid_w
    cell_h = imagen_dataset_size/grid_h

    for obj in image['object']:
        relative_w = (float(obj['xmax']) - float(obj['xmin']))/cell_w
        relatice_h = (float(obj["ymax"]) - float(obj['ymin']))/cell_h
        annotation_dims.append(tuple(map(float, (relative_w,relatice_h))))
print ('annotation_dims')
#print (annotation_dims)

annotation_dims = np.array(annotation_dims)
centroids = run_kmeans(annotation_dims, num_anchors)

# write anchors to file
print('\naverage IOU for', num_anchors, 'anchors:', '%0.2f' % avg_IOU(annotation_dims, centroids))
print_anchors(centroids)
anchors = []
for x in centroids:
    anchors.append(x[0])
    anchors.append(x[1])
anchors

######  ENTRENAMIENTO

# instanciamos un objeto yolo
# instanciamos al modelo
yolo = YOLO(input_size          = image_size,
            grid_h              = grid_h,
            grid_w              = grid_w,        
            labels              = LABELS, 
            max_box_per_image   = 5,
            anchors             = anchors)

yolo.train(train_imgs         = train_imgs,
           valid_imgs         = val_imgs,
           train_times        = 6,
           valid_times        = 1,
           nb_epochs          = NUM_EPOCHS, 
           learning_rate      = 1e-4, 
           batch_size         = BATCH_SIZE,
           warmup_epochs      = 2,
           object_scale       = 5,
           no_object_scale    = 1,
           coord_scale        = 1,
           class_scale        = 1,
           saved_weights_name = mejores_pesos,
           debug              = True)


