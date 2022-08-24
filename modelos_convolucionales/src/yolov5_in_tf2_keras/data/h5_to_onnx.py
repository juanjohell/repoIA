import sys

sys.path.append("../../yolov5_in_tf2_keras")

import os
import cv2
import numpy as np
import tensorflow as tf
import tf2onnx
import onnx
import onnxsim
import onnxruntime

from yolo import Yolo
from layers import nms
from data.visual_ops import draw_bounding_box

os.environ["CUDA_VISIBLE_DEVICES"] = "1"


def h5_to_onnx(model_path, output_path, save_not_simplify_onnx=False):
    yolov5_type = "5s"
    # image_shape = (640, 640, 3)
    image_shape = (320, 320, 3)
    # num_class = 91
    num_class = 2
    batch_size = 1
    # 这里anchor归一化到[0,1]区间
    anchors = np.array([[10, 13], [16, 30], [33, 23],
                        [30, 61], [62, 45], [59, 119],
                        [116, 90], [156, 198], [373, 326]]) / 640.
    anchors = np.array(anchors, dtype=np.float32)
    # 分别对应1/8, 1/16, 1/32预测输出层
    anchor_masks = np.array([[0, 1, 2], [3, 4, 5], [6, 7, 8]], dtype=np.int8)

    yolo = Yolo(
        model_path=model_path,
        image_shape=image_shape,
        batch_size=batch_size,
        num_class=num_class,
        is_training=False,
        anchors=anchors,
        anchor_masks=anchor_masks,
        net_type=yolov5_type
    )

    output_dir = os.path.dirname(output_path)
    output_name = os.path.basename(output_path)
    not_simplify_onnx_file = os.path.join(output_dir, output_name.split(".")[0] + "_not_simplify.onnx")

    # 转onnx
    onnx_model, _ = tf2onnx.convert.from_keras(
        model=yolo.yolov5, input_signature=[tf.TensorSpec([1, 320, 320, 3], dtype=tf.float32)], opset=13)
    if save_not_simplify_onnx:
        onnx.save(onnx_model, not_simplify_onnx_file)
        print("save onnx(not simplify) model:", not_simplify_onnx_file)

    # onnx优化
    model_simp, check = onnxsim.simplify(onnx_model)
    onnx.save(model_simp, output_path)
    print("save onnx(simplify) model:", output_path)


def test_onnx(model):
    image_shape = (320,320,3)
    classes = ['cat','dog']
    f = "./tmp/Cats_Test49.jpg"
    im = cv2.imread(f)
    im_shape = np.shape(im)
    im_size_max = np.max(im_shape[0:2])
    im_scale = float(image_shape[0]) / float(im_size_max)

    # resize原始图片
    im_resize = cv2.resize(im, None, None, fx=im_scale, fy=im_scale, interpolation=cv2.INTER_LINEAR)
    im_resize_shape = np.shape(im_resize)
    im_blob = np.zeros(image_shape, dtype=np.float32)
    im_blob[0:im_resize_shape[0], 0:im_resize_shape[1], :] = im_resize
    # cv2.imshow("", np.array(im_blob,dtype=np.uint8))
    # cv2.waitKey(0)
    inputs = np.array([im_blob], dtype=np.float32) / 255.

    onnx.checker.check_model(model)
    session = onnxruntime.InferenceSession(model)
    input_name = session.get_inputs()[0].name
    output_name = session.get_outputs()[0].name
    predictions = session.run([output_name], {input_name: inputs})[0]

    nms_outputs = nms(image_shape, predictions)
    if not nms_outputs:
        return
    nms_outputs = np.array(nms_outputs[0], dtype=np.float32)
    boxes = nms_outputs[:, :4]
    b0 = np.maximum(np.minimum(boxes[:, 0] / im_scale, im_shape[1] - 1), 0)
    b1 = np.maximum(np.minimum(boxes[:, 1] / im_scale, im_shape[0] - 1), 0)
    b2 = np.maximum(np.minimum(boxes[:, 2] / im_scale, im_shape[1] - 1), 0)
    b3 = np.maximum(np.minimum(boxes[:, 3] / im_scale, im_shape[0] - 1), 0)
    origin_boxes = np.stack([b0, b1, b2, b3], axis=1)
    nms_outputs[:, :4] = origin_boxes

    for box_obj_cls in nms_outputs:
        if box_obj_cls[4] > 0.5:
            label = int(box_obj_cls[5])
            class_name = classes[label]
            xmin, ymin, xmax, ymax = box_obj_cls[:4]
            im = draw_bounding_box(im, class_name, box_obj_cls[4], int(xmin), int(ymin),
                                   int(xmax), int(ymax))
    output_f = "./tmp/onnx_predicts.jpg"
    cv2.imwrite(output_f, im)
    print("write test image in: "+output_f)


if __name__ == "__main__":
    # h5_to_onnx(model_path='../logs_bn_true/yolov5s-best.h5', output_path='../yolov5_lite/yolov5s-best.onnx')
    test_onnx(model='../yolov5_lite/yolov5s-best.onnx')
