# -*- coding: utf-8 -*-
"""
Created on Thu Jul 21 16:50:40 2022

@author: juanjo
"""

import os
import funciones_utilidades as utils

xml_train_dir = "../../../datasets/uav-bd/train/labels/"  # directorio que contiene los xml
img_train_dir = "../../../datasets/uav-bd/train/images/"   # directorios con las imagenes
img_val_dir = "../../../datasets/uav-bd/val/images/"
xml_val_dir = "../../../datasets/uav-bd/val/labels/"
extension='.jpg'
#clase del objeto botella
class_id = "0"
delimiter=","
#abrimos un fichero para escritura
etiquetas_val = open('etiquetas_val.txt', 'w')
for e,i in enumerate(os.listdir(xml_val_dir)) :
    sep=""
    filename = sep.join((i.split('.')[0],extension))
    xmlpath = sep.join((xml_val_dir,i))
    imagePath = os.path.join(img_val_dir, filename)
    etiquetas_val.write(imagePath+' ')
    name, boxes = utils.read_content_from_center_wh(xmlpath)
    for i, box in (enumerate(boxes)):
        nbox = utils.return_coordinates_of_angled_rec(box[0],box[1], box[2], box[3], box[4])
        (startX, startY,endX, endY) = utils.return_two_point_coordinates(nbox[0],nbox[1],nbox[2], nbox[3])
        etiquetas_val.write("%d,%d,%d,%d,%s " % (startX,startY,endX,endY,class_id))
    etiquetas_val.write('\n')
etiquetas_val.close()

etiquetas_train = open('etiquetas_train.txt', 'w')
for e,i in enumerate(os.listdir(xml_train_dir)) :
    sep=""
    filename = sep.join((i.split('.')[0],extension))
    xmlpath = sep.join((xml_train_dir,i))
    imagePath = os.path.join(img_train_dir, filename)
    etiquetas_train.write(imagePath+' ')
    name, boxes = utils.read_content_from_center_wh(xmlpath)
    for i, box in (enumerate(boxes)):
        nbox = utils.return_coordinates_of_angled_rec(box[0],box[1], box[2], box[3], box[4])
        (startX, startY,endX, endY) = utils.return_two_point_coordinates(nbox[0],nbox[1],nbox[2], nbox[3])
        etiquetas_train.write("%d,%d,%d,%d,%s " % (startX,startY,endX,endY,class_id))
    etiquetas_train.write('\n')
etiquetas_train.close()