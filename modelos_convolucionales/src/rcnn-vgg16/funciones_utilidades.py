# -*- coding: utf-8 -*-
"""
Created on Fri Jun 10 13:13:49 2022

@author: juanjo
"""


import cv2
import xml.etree.ElementTree as ET
import math

#funciones para extraer anotaciones de XML
def read_content_from_center_wh(xml_file: str):

    tree = ET.parse(xml_file)
    root = tree.getroot()
    list_with_all_boxes = []

    for boxes in root.iter('object'):
        filename = root.find('filename').text
        yc, xc, h, w, angle = None, None, None, None, None
        yc = int(round(float(boxes.find("robndbox/cy").text)))
        xc = int(round(float(boxes.find("robndbox/cx").text)))
        h = int(round(float(boxes.find("robndbox/h").text)))
        w = int(round(float(boxes.find("robndbox/w").text)))
        angle = float(boxes.find("robndbox/angle").text)
        list_with_single_boxes = [xc, yc, w, h, angle]
        list_with_all_boxes.append(list_with_single_boxes)
    return filename, list_with_all_boxes

#funcion para extraer coordenadas de los puntos
# de una caja conociendo cx, cy, h, w, angulo
def return_coordinates_of_xyhw(x0, y0, width, height):
    
    pt0 = (int(x0),int(y0))
    pt1 = (int(x0),int(y0 + height))
    pt2 = (int(x0 + width), int(y0 + height))
    pt3 = (int(x0 + width), int(y0))
    coordenadas = [pt0, pt1, pt2, pt3]
    return coordenadas

#funcion para extraer coordenadas de los puntos
# de una caja conociendo cx, cy, h, w, angulo
def return_coordinates_of_angled_rec(x0, y0, width, height, angle):
    # si viene en grados se convierte a radianes
    #_angle = angle * math.pi / 180.0
    #si viene en radianes se deja tal cual
    _angle = angle
    b = math.cos(_angle) * 0.5
    a = math.sin(_angle) * 0.5

    pt0 = (int(x0 - a * height - b * width),
           int(y0 + b * height - a * width))
    pt1 = (int(x0 + a * height - b * width),
           int(y0 - b * height - a * width))
    pt2 = (int(2 * x0 - pt0[0]), int(2 * y0 - pt0[1]))
    pt3 = (int(2 * x0 - pt1[0]), int(2 * y0 - pt1[1]))

    coordenadas = [pt0, pt1, pt2, pt3]
    return coordenadas


def return_two_point_coordinates(punto0,punto1,punto2,punto3):
    startX = min(punto0[0],punto1[0],punto2[0],punto3[0])
    startY = max(punto0[1],punto1[1],punto2[1],punto3[1])
    endX = max(punto0[0],punto1[0],punto2[0],punto3[0])
    endY = min(punto0[1],punto1[1],punto2[1],punto3[1])

    coordenadas = (startX, startY, endX, endY)
    return coordenadas

#funcion para rotar anotaciones con un angulo
def draw_angled_rec(x0, y0, width, height, angle, img):
    # si viene en grados se convierte a radianes
    #_angle = angle * math.pi / 180.0
    #si viene en radianes se deja tal cual
    _angle = angle
    b = math.cos(_angle) * 0.5
    a = math.sin(_angle) * 0.5

    pt0 = (int(x0 - a * height - b * width),
           int(y0 + b * height - a * width))
    pt1 = (int(x0 + a * height - b * width),
           int(y0 - b * height - a * width))
    pt2 = (int(2 * x0 - pt0[0]), int(2 * y0 - pt0[1]))
    pt3 = (int(2 * x0 - pt1[0]), int(2 * y0 - pt1[1]))

    cv2.line(img, pt0, pt1, (255, 255, 255), 3)
    cv2.line(img, pt1, pt2, (255, 255, 255), 3)
    cv2.line(img, pt2, pt3, (255, 255, 255), 3)
    cv2.line(img, pt3, pt0, (255, 255, 255), 3)
  
#funcion para el calculo de intersection over union
def get_iou(bb1, bb2):
    assert bb1['x1'] < bb1['x2']
    assert bb1['y1'] < bb1['y2']
    assert bb2['x1'] < bb2['x2']
    assert bb2['y1'] < bb2['y2']

    x_left = max(bb1['x1'], bb2['x1'])
    y_top = max(bb1['y1'], bb2['y1'])
    x_right = min(bb1['x2'], bb2['x2'])
    y_bottom = min(bb1['y2'], bb2['y2'])

    if x_right < x_left or y_bottom < y_top:
        return 0.0

    intersection_area = (x_right - x_left) * (y_bottom - y_top)

    bb1_area = (bb1['x2'] - bb1['x1']) * (bb1['y2'] - bb1['y1'])
    bb2_area = (bb2['x2'] - bb2['x1']) * (bb2['y2'] - bb2['y1'])

    iou = intersection_area / float(bb1_area + bb2_area - intersection_area)
    assert iou >= 0.0
    assert iou <= 1.0
    return iou

from shapely.geometry import Polygon
#funcion sencilla para el calculo de iou para cajas en cualquier angulo
def get_generic_iou(bb1, bb2):
    bb1 = Polygon([bb1[0], bb1[1], bb1[2], bb1[3]])
    bb2 = Polygon([bb2[0], bb2[1], bb2[2], bb2[3]])
    iou = bb1.intersection(bb2).area / float(bb1.union(bb2).area)
    assert iou >= 0.0
    assert iou <= 1.0
    return iou