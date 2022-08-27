# -*- coding: utf-8 -*-
"""
Created on Wed Aug 24 09:22:58 2022

@author: jjhb01
"""
##################################################
#  El formato de los txt debe ser:               #
#                                                #
#     cat_id,
#     x_center_norm,
#     y_center_norm,
#     width_norm,
#     height_norm,
#     
#     varias lineas si hay vaarias coordenadas   #
#                                                #
##################################################

#https://notebooks.githubusercontent.com/view/ipynb?browser=chrome&color_mode=auto&commit=63b4f7d9728e437479f77115975745a05e2712a4&device=unknown&enc_url=68747470733a2f2f7261772e67697468756275736572636f6e74656e742e636f6d2f70796c6162656c2d70726f6a6563742f73616d706c65732f363362346637643937323865343337343739663737313135393735373435613035653237313261342f796f6c6f32636f636f2e6970796e62&logged_in=false&nwo=pylabel-project%2Fsamples&path=yolo2coco.ipynb&platform=android&repository_id=419183045&repository_type=Repository&version=100

from pylabel import importer
from IPython.display import Image, display

path_to_annotations_train = "../../datasets/uav-bd/train/labels_txt/"
path_to_images_train = "../images/" # ATENCION IMAGENES DESDE EL FICHERO DE LABELS

path_to_annotations_val = "../../datasets/uav-bd/val/labels_txt/"
path_to_images_val = "../images/" # ATENCION IMAGENES DESDE EL FICHERO DE LABELS

#Class names are defined here https://github.com/ultralytics/yolov5/blob/master/data/coco128.yaml
'''
yoloclasses = ['person', 'bicycle', 'car', 'motorcycle', 'airplane', 'bus', 'train', 'truck', 'boat', 'traffic light',
        'fire hydrant', 'stop sign', 'parking meter', 'bench', 'bird', 'cat', 'dog', 'horse', 'sheep', 'cow',
        'elephant', 'bear', 'zebra', 'giraffe', 'backpack', 'umbrella', 'handbag', 'tie', 'suitcase', 'frisbee',
        'skis', 'snowboard', 'sports ball', 'kite', 'baseball bat', 'baseball glove', 'skateboard', 'surfboard',
        'tennis racket', 'bottle', 'wine glass', 'cup', 'fork', 'knife', 'spoon', 'bowl', 'banana', 'apple',
        'sandwich', 'orange', 'broccoli', 'carrot', 'hot dog', 'pizza', 'donut', 'cake', 'chair', 'couch',
        'potted plant', 'bed', 'dining table', 'toilet', 'tv', 'laptop', 'mouse', 'remote', 'keyboard', 'cell phone',
        'microwave', 'oven', 'toaster', 'sink', 'refrigerator', 'book', 'clock', 'vase', 'scissors', 'teddy bear',
        'hair drier', 'toothbrush']
'''
yoloclasses = ['botella']
'''
dataset_train = importer.ImportYoloV5(path=path_to_annotations_train, path_to_images=path_to_images_train, cat_names=yoloclasses,img_ext='jpg', name='uav-db')

dataset_train.df.head(5)
print(f"Number of images: {dataset_train.analyze.num_images}")
print(f"Number of classes: {dataset_train.analyze.num_classes}")
print(f"Classes:{dataset_train.analyze.classes}")
print(f"Class counts:\n{dataset_train.analyze.class_counts}")

## COMPROBAMOS QUE SE HAN IMPORTADO BIEN

display(dataset_train.visualize.ShowBoundingBoxes(100))
display(dataset_train.visualize.ShowBoundingBoxes(30))

#EXPORTAR a JSON
dataset_train.export.ExportToCoco(cat_id_index=1)
'''
dataset_val = importer.ImportYoloV5(path=path_to_annotations_val, path_to_images=path_to_images_val, cat_names=yoloclasses,img_ext='jpg', name='uav-db')

dataset_val.df.head(5)
print(f"Number of images: {dataset_val.analyze.num_images}")
print(f"Number of classes: {dataset_val.analyze.num_classes}")
print(f"Classes:{dataset_val.analyze.classes}")
print(f"Class counts:\n{dataset_val.analyze.class_counts}")

## COMPROBAMOS QUE SE HAN IMPORTADO BIEN

display(dataset_val.visualize.ShowBoundingBoxes(100))
display(dataset_val.visualize.ShowBoundingBoxes(30))

#EXPORTAR a JSON
#dataset_val.export.ExportToCoco(cat_id_index=1)