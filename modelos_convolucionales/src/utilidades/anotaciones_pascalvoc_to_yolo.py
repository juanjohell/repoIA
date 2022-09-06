# -*- coding: utf-8 -*-
"""
Created on Thu Jul 21 16:50:40 2022

@author: juanjo
"""
#######################################################
# EN ESTE SCRIPT SE APLICAN TRANSFORMACIONES A LOS    #
# BOUNDING BOXES PORQUE ESTAN ROTADOS Y SE TRANS-     #
# FORMAN A CAJAS NO ROTADAS                           #
# read_content_from_center_wh                         #
# return_coordinates_of_angled_rec                    #
#######################################################
import os
import funciones_utilidades as utils
import math

xml_train_dir = "../../datasets/uav-bd/train/labels/"  # directorio que contiene los xml
txt_train_dir = "../../datasets/uav-bd/train/labels_txt/nombre_xywh/"
img_train_dir = "../../datasets/uav-bd/train/images/"   # directorios con las imagenes

img_val_dir = "../../datasets/uav-bd/val/images/"
xml_val_dir = "../../datasets/uav-bd/val/labels/"
txt_val_dir = "../../datasets/uav-bd/val/labels_txt/nombre_xywh/"

width_imagenes_dataset = 342
height_imagenes_dataset = 342

path_relativo_de_labels_a_imagenes = "../images/"
extension='.jpg'
#clase del objeto botella
class_id = "0"
delimiter=","
#abrimos un fichero para escritura
#etiquetas_val = open('etiquetas_val_botellas_r-cnn-vgg16.txt', 'w')
'''
for e,i in enumerate(os.listdir(xml_val_dir)) :
    sep=""
    xmlfilename = i
    imagefilename = sep.join((i.split('.')[0],extension))
    txtfilename = sep.join((i.split('.')[0],'.txt'))
    print(imagefilename)
    etiquetas_val = open(os.path.join(txt_val_dir,txtfilename), 'w')
    xmlpath = sep.join((xml_val_dir,xmlfilename))
    imagePath = os.path.join(path_relativo_de_labels_a_imagenes, imagefilename)
    #etiquetas_val.write(imagefilename+' ')  # path a la imagen
    # leemos coordenadas cx, cy, w, h, angulo
    name, boxes = utils.read_content_from_center_wh(xmlpath)
    for i, box in (enumerate(boxes)):
        nbox = utils.return_coordinates_of_angled_rec(box[0],box[1], box[2], box[3], box[4])
        (startX, startY,endX, endY) = utils.return_two_point_coordinates(nbox[0],nbox[1],nbox[2], nbox[3])

        #normalizacion de coordenadas si se precisan
        #widthnorm = abs (endX - startX)
        #widthnorm /= width_imagenes_dataset
        #heightnorm = abs (startY - endY)
        #heightnorm /= height_imagenes_dataset
        #xcenternorm = box[0]
        #xcenternorm /= width_imagenes_dataset
        #ycenternorm = box[1]
        #ycenternorm /= height_imagenes_dataset
        #etiquetas_val.write("%d,%d,%d,%d,%s " % (startX,startY,endX,endY,class_id))
        #etiquetas_val.write("%s,%s,%s,%s,%s" % (class_id,xcenternorm,ycenternorm,widthnorm,heightnorm))
        #etiquetas_val.write("%s %s %s %s %s" % (class_id,xcenternorm,ycenternorm,widthnorm,heightnorm))
        etiquetas_val.write("%s,%s,%s,%s " % (startX,startY,endY,endX))
        etiquetas_val.write('\n')
    #etiquetas_val.write('\n')
    etiquetas_val.close()
#etiquetas_val.close()

#etiquetas_train = open('etiquetas_train_botellas_r-cnn-vgg16.txt', 'w')
'''

for e,i in enumerate(os.listdir(xml_train_dir)) :
    sep=""
    xmlfilename = i
    imagefilename = sep.join((i.split('.')[0],extension))
    print(imagefilename)
    txtfilename = sep.join((i.split('.')[0],'.txt'))
    etiquetas_train = open(os.path.join(txt_train_dir,txtfilename), 'w')
    xmlpath = sep.join((xml_train_dir,xmlfilename))
    imagePath = os.path.join(path_relativo_de_labels_a_imagenes, imagefilename)
    #etiquetas_train.write(imagefilename+' ')
    name, boxes = utils.read_content_from_center_wh(xmlpath)
    for i, box in (enumerate(boxes)):
        nbox = utils.return_coordinates_of_angled_rec(box[0],box[1], box[2], box[3], box[4])
        (startX, startY,endX, endY) = utils.return_two_point_coordinates(nbox[0],nbox[1],nbox[2], nbox[3])
        widthnorm = abs (endX - startX)
        widthnorm /= width_imagenes_dataset
        heightnorm = abs (startY - endY)
        heightnorm /= height_imagenes_dataset
        xcenternorm = box[0]
        xcenternorm /= width_imagenes_dataset
        ycenternorm = box[1]
        ycenternorm /= height_imagenes_dataset
        #etiquetas_train.write("%d,%d,%d,%d,%s " % (startX,startY,endX,endY,class_id))
        #etiquetas_train.write("%s %s %s %s %s" % (class_id,xcenternorm,ycenternorm,widthnorm,heightnorm))
        etiquetas_train.write("%s,%s,%s,%s " % (startX,startY,endY,endX))
        etiquetas_train.write('\n')
    #etiquetas_train.write('\n')
    etiquetas_train.close()
#etiquetas_train.close()
