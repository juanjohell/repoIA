#! /usr/bin/env python
# coding=utf-8
# @Author: Longxing Tan, tanlongxing888@163.com

import sys
import os
filePath = os.path.abspath(os.path.dirname(__file__))
sys.path.append(os.path.split(filePath)[0])

import cv2
import time
import argparse
import numpy as np
import tensorflow as tf
from dataset.image_utils import resize_image, resize_back
from model.post_process import batch_non_max_suppression
from tools.vis_data import draw_box
from tensorflow.keras.models import load_model


def image_demo_tf(img, model, img_size=416, class_names=None, conf_threshold=0.4, iou_threshold=0.3):
    original_shape = img.shape
    
    img_input = resize_image(img, target_sizes=img_size)
    img_input = img_input[np.newaxis, ...].astype(np.float32)
    img_input = img_input / 255.

    pred_bbox = model(img_input)
    pred_bbox = [tf.reshape(x, (tf.shape(x)[0], -1, tf.shape(x)[-1])) for x in pred_bbox]
    pred_bbox = tf.concat(pred_bbox, axis=1)  # batch_size * -1 * (num_class + 5)

    bboxes = batch_non_max_suppression(pred_bbox, conf_threshold=conf_threshold, iou_threshold=iou_threshold)
    bboxes = bboxes[0].numpy()  # batch is 1 for detect
    print (bboxes)
    bboxes = resize_back(bboxes, target_sizes=img_size, original_shape=original_shape)  # adjust box to original size
    if bboxes.any():
        image = draw_box(img, np.array(bboxes), class_names)
        cv2.imwrite('./demo.jpg', cv2.cvtColor(image, cv2.COLOR_RGB2BGR))
    else:
        print('No box detected')
    
def image_demo_keras(img, model, img_size=416, class_names=None, conf_threshold=0.4, iou_threshold=0.3):
    original_shape = img.shape
    
    img_input = resize_image(img, target_sizes=img_size)
    img_input = img_input[np.newaxis, ...].astype(np.float32)
    img_input = img_input / 255.

    pred_bbox = model.predict(img_input)
    print (pred_bbox)
    pred_bbox = [tf.reshape(x, (tf.shape(x)[0], -1, tf.shape(x)[-1])) for x in pred_bbox]
    pred_bbox = tf.concat(pred_bbox, axis=1)  # batch_size * -1 * (num_class + 5)

    bboxes = batch_non_max_suppression(pred_bbox, conf_threshold=conf_threshold, iou_threshold=iou_threshold)
    bboxes = bboxes[0].numpy()  # batch is 1 for detect
    print (bboxes)
    bboxes = resize_back(bboxes, target_sizes=img_size, original_shape=original_shape)  # adjust box to original size
    if bboxes.any():
        image = draw_box(img, np.array(bboxes), class_names)
        cv2.imwrite('./demo.jpg', cv2.cvtColor(image, cv2.COLOR_RGB2BGR))
    else:
        print('No box detected')    

def video_demo(video, model, plot=False, save=False):
    pass


def test_image_demo(img_dir, model_dir, img_size=416, class_name_dir=None, conf_threshold=0.4, iou_threshold=0.3):
    img = cv2.imread(img_dir)
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    if class_name_dir:
        class_names = {idx: name for idx, name in enumerate(open(class_name_dir).read().splitlines())}
    else:
        class_names = None    
    #PARA MODELO EN FORMATO PB
    model = tf.saved_model.load(model_dir)
    model.
    image_demo_tf(img, model, img_size=img_size, class_names=class_names,
               conf_threshold=conf_threshold, iou_threshold=iou_threshold)
    #PARA MODELO EN FORMATO h5
    #model = load_model('yolov5_botellas.h5')
    #image_demo_keras(img, model, img_size=img_size, class_names=class_names,
    #           conf_threshold=conf_threshold, iou_threshold=iou_threshold)


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--img_dir', type=str, default='../../../datasets/uav-bd/test/images/6_Mixture_000210_117_2.jpg', help='detect image dir')
    parser.add_argument('--class_name_dir', type=str, default='trash_classes.txt', help='classes name dir')
    parser.add_argument('--model_dir', type=str, default='../weights/yolov5', help='saved pb model dir')
    parser.add_argument('--img_size', type=int, default=416, help='image target size')
    parser.add_argument('--conf_threshold', type=float, default=0.4, help='filter confidence threshold')
    parser.add_argument('--iou_threshold', type=float, default=0.3, help='nms iou threshold')
    opt = parser.parse_args()

    test_image_demo(opt.img_dir, opt.model_dir, opt.img_size, opt.class_name_dir, opt.conf_threshold, opt.iou_threshold)
