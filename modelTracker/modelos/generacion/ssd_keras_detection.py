import cv2
import numpy as np
from backend.datasets_labels import coco
import os, sys

# PATH AL PB y PBTXT
model_path = os.path.dirname(sys.path[0])
pb_path = os.path.join(model_path, 'frozen_inference_graph_V2.pb')
pbtxt_path = os.path.join(model_path, 'frozen_inference_graph_V2.pbtxt')


net = cv2.dnn.readNetFromTensorflow(pb_path, pbtxt_path)
# net = cv2.dnn.readNetFromTensorflow('frozen_inference_graph_V2.pb', 'ssd_mobilenet_v2_coco_2018_03_29.pbtxt')

def detect_objects(image):
    blobimg = cv2.dnn.blobFromImage(image, 1.0, (300, 300), (0, 0, 0), True)
    net.setInput(blobimg)
    detection = net.forward('detection_out')
    detectionMat = np.array(detection[0, 0, :, :])

    confidence_threshold = 0.25
    for i in range(detectionMat.shape[0]):
        detect_confidence = detectionMat[i, 2]

        if detect_confidence > confidence_threshold:
            det_index = int(detectionMat[i, 1])
            x1 = detectionMat[i, 3] * image.shape[1]
            y1 = detectionMat[i, 4] * image.shape[0]
            x2 = detectionMat[i, 5] * image.shape[1]
            y2 = detectionMat[i, 6] * image.shape[0]
            rec = (int(x1), int(y1), int(x2 - x1), int(y2 - y1))
            cv2.rectangle(image, rec, (0, 0, 255), 1, 8, 0)
            cv2.putText(image, f'{coco[det_index]}', (int(x1), int(y1 - 5)), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 1, 8, 0)

# Cargar y preparar la imagen de entrada
base_dir = os.path.dirname(sys.path[0])
image_path = os.path.join(base_dir, 'imagenes_test', 'tigre.jpeg')

image = cv2.imread(image_path)
if image is None:
    print("Error al cargar la imagen")
    exit()

detect_objects(image)

cv2.imshow('Detected Objects', image)
cv2.waitKey(0)
cv2.destroyAllWindows()
