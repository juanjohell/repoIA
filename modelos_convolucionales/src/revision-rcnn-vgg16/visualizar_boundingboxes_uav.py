# -*- coding: utf-8 -*-
"""
Created on Sat Jul  9 12:07:10 2022

@author: juanjo
"""

# dibujar boundingboxes de dataset auv

import funciones_utilidades as utils
import os
import matplotlib.pyplot as plt
import cv2
#import imutils

imagen_prueba = "8_Playground_000280_116_2"
extension = "jpg"
bounding_box_tag='robndbox'

xml_dir = "../../datasets/uav-bd/train/labels/"  # directorio que contiene los xml
img_dir = '../../datasets/uav-bd/train/images/'   # directorios con las imagenes
img_val_dir = "../../datasets/uav-bd/val/images/"
xml_val_dir = "../../datasets/uav-bd/val/labels/"
labels = ["bottle"]
image_height = 342
image_width = 342
sep="."
fichero_xml = sep.join((imagen_prueba,"xml"))
imagen = sep.join((imagen_prueba,extension))


#mostrar coordenadas giradas
name, boxes = utils.read_content_from_center_wh(xml_dir + fichero_xml )
#transformamos coordenadas
print(imagen)
img = cv2.imread(os.path.join(img_dir,imagen))
#puede haber mas de un objeto en la imagen
for i, box in (enumerate(boxes)):
    utils.draw_angled_rec(box[0],box[1], box[2],box[3],box[4],img)
             

#mostrar coordenadas sin girar (menos ajustadas)

'''
for i, box in (enumerate(boxes)):
    print(box)
    nbox = utils.return_coordinates_of_angled_rec(box[0],box[1], box[2], box[3], box[4])
    print(nbox)
    (startX, startY,endX, endY) = utils.return_two_point_coordinates(nbox[0],nbox[1],nbox[2], nbox[3])
    print((startX, startY), (endX, endY))
    #color RGB y grosor de la caja como parámetros finales 
    cv2.rectangle(img, (startX, startY), (endX, endY),
    		(255, 255, 0), 3)
'''
plt.figure()
plt.imshow(img)  