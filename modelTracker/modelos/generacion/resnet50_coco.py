from keras_retinanet import models
from keras_retinanet.utils.image import read_image_bgr, preprocess_image, resize_image
from keras_retinanet.utils.visualization import draw_boxes, draw_caption
from keras_retinanet.utils.colors import label_color
import matplotlib.pyplot as plt
import numpy as np
import sys

#ruta a la base de datos de aplicación
weights_path = f'{sys.path[0]}/modelos/pesos/resnet50_coco.py'
# Ruta al archivo de pesos preentrenados


# Carga el modelo preentrenado en Keras RetinaNet
model = models.load_model(weights_path, backbone_name='resnet50')

model.save('retinanet_model.h5')

# Clases del conjunto de datos COCO
labels_to_names = {
    0: 'person',
    1: 'bicycle',
    2: 'car',
    3: 'motorcycle',
    4: 'airplane',
    5: 'bus',
    6: 'train',
    7: 'truck',
    8: 'boat',
    9: 'traffic light',
    10: 'fire hydrant',
    11: 'stop sign',
    12: 'parking meter',
    13: 'bench',
    14: 'bird',
    15: 'cat',
    16: 'dog',
    17: 'horse',
    18: 'sheep',
    19: 'cow',
    20: 'elephant',
    21: 'bear',
    22: 'zebra',
    23: 'giraffe',
    24: 'backpack',
    25: 'umbrella',
    26: 'handbag',
    27: 'tie',
    28: 'suitcase',
    29: 'frisbee',
    30: 'skis',
    31: 'snowboard',
    32: 'sports ball',
    33: 'kite',
    34: 'baseball bat',
    35: 'baseball glove',
    36: 'skateboard',
    37: 'surfboard',
    38: 'tennis racket',
    39: 'bottle',
    40: 'wine glass',
    41: 'cup',
    42: 'fork',
    43: 'knife',
     44: 'spoon',
    45: 'bowl',
    46: 'banana',
    47: 'apple',
    48: 'sandwich',
    49: 'orange',
    50: 'broccoli',
    51: 'carrot',
    52: 'hot dog',
    53: 'pizza',
    54: 'donut',
    55: 'cake',
    56: 'chair',
    57: 'couch',
    58: 'potted plant',
    59: 'bed',
    60: 'dining table',
    61: 'toilet',
    62: 'tv',
    63: 'laptop',
    64: 'mouse',
    65: 'remote',
    66: 'keyboard',
    67: 'cell phone',
    68: 'microwave',
    69: 'oven',
    70: 'toaster',
    71: 'sink',
    72: 'refrigerator',
    73: 'book',
    74: 'clock',
    75: 'vase',
    76: 'scissors',
    77: 'teddy bear',
    78: 'hair drier',
    79: 'toothbrush'
}


# Carga la imagen de entrada
image_path = f'{sys.path[0]}/modelos/imagenes_test/camion.jpg'
image = read_image_bgr(image_path)
image = preprocess_image(image)
image, scale = resize_image(image)

# Realiza la detección de objetos en la imagen
boxes, scores, labels = model.predict_on_batch(np.expand_dims(image, axis=0))

# Ajusta las detecciones a la escala original de la imagen
boxes /= scale

# Dibuja las bounding boxes y etiquetas en la imagen
for box, score, label in zip(boxes[0], scores[0], labels[0]):
    # Ignora las detecciones con una confianza baja
    if score < 0.5:
        continue

    color = label_color(label)
    bgr_color = (color[2], color[1], color[0])
    draw_boxes(image, boxes, color=bgr_color, thickness=2)
    caption = f"{labels_to_names[label]} {score:.2f}"
    draw_caption(image, box, caption)

# Muestra la imagen con las detecciones
plt.imshow(image)
plt.show()
