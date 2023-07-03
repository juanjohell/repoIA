import cv2
import numpy as np
from keras_retinanet import models
from keras_retinanet.utils.image import read_image_bgr, preprocess_image, resize_image
from keras_retinanet.utils.visualization import draw_box, draw_caption
from keras_retinanet.utils.colors import label_color
from backend.datasets_labels import coco
import os, sys

# Cargar el modelo preentrenado
# Ruta al archivo resnet50_coco_best.h52
model_path = os.path.join(os.path.dirname(sys.path[0]), 'pesos', 'resnet50_coco_best.h5')
print(model_path)
model = models.load_model(model_path, backbone_name='resnet')


# Cargar y preparar la imagen de entrada
base_dir = os.path.dirname(os.path.abspath(__file__))
image_path = os.path.join(base_dir, 'imagenes_test', 'fruta.jpg')

image = read_image_bgr(image_path)
image = preprocess_image(image)
image, scale = resize_image(image)

# Realizar la detección de objetos
boxes, scores, labels = model.predict_on_batch(np.expand_dims(image, axis=0))

# Desnormalizar las coordenadas de las cajas delimitadoras
boxes /= scale

# Visualizar los resultados
for box, score, label in zip(boxes[0], scores[0], labels[0]):
    if score < 0.5:  # Ignorar detecciones con una confianza inferior a 0.5
        break
    color = label_color(label)
    draw_box(image, box, color=color)
    caption = f'{coco[label]} {score:.3f}'
    draw_caption(image, box, caption)

# Mostrar la imagen con las detecciones
cv2.imshow('Detección de objetos', image)
cv2.waitKey(0)
cv2.destroyAllWindows()
