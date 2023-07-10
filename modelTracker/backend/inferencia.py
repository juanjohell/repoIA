from flask import Flask, render_template, request, jsonify, session, redirect, url_for
import io
import os
import base64
from PIL import Image, ImageDraw, ImageFont
from sql.persistencia_bbdd import Modelo
from keras.preprocessing import image
from tensorflow.keras.preprocessing.image import img_to_array
from keras.applications.vgg19 import preprocess_input, decode_predictions as decode_vgg19
from keras.applications.imagenet_utils import decode_predictions as decode_any_model_imagenet
from keras.models import load_model
from keras.utils.vis_utils import plot_model
from backend.datasets_labels import *
import numpy as np
import cv2
import sys

# CONFIGURACION
# Obtener la ruta absoluta a la carpeta que contiene los ficheros de modelos
path_modelos = os.path.abspath('modelos')
path_fuente = path = f'{sys.path[0]}/static/fonts/arial.ttf'

# Recibe una imagen en formato de bytes
# Realiza clasificación y devuelve la imagen con el resultado
# y el propio resultado como cadena de texto
def clasificar_imagen(imagen, hex_color):

    rgb_color = hex_to_rgb(hex_color)
    # Preprocesar la imagen para que sea compatible con el modelo
    # seleccionado actualmente
    pil_img_orig = Image.open(io.BytesIO(imagen))
    modelo_seleccionado = Modelo.from_json(session.get('modelo_seleccionado'))
    # aqui hay que obtener el input_shape del modelo
    input_shape = modelo_seleccionado.input_shape
    print(input_shape)
    #TRATAMOS LAS DIMENSIONES DE INPUT_SHAPE POR SEPARADO:
    # Eliminar los paréntesis del texto
    dimensiones_sin_parentesis = input_shape.strip("()")
    # Dividir la cadena en función de las comas
    dimensiones = dimensiones_sin_parentesis.split(",")

    # Extraer las tres dimensiones
    canales = int(dimensiones[2])
    ancho = int(dimensiones[0])
    alto = int(dimensiones[1])


    pil_img = pil_img_orig.resize((alto, ancho))  # Cambiar dimensiones de la imagen
    x = image.img_to_array(pil_img)
    x = np.expand_dims(x, axis=0)
    x = preprocess_input(x)

    # Realizar la clasificación utilizando el modelo
    path_fichero_modelo = os.path.join(path_modelos, modelo_seleccionado.nombre)
    print(path_fichero_modelo)
    #CARGA DEL MODELO
    model = load_model(path_fichero_modelo)
    preds = model.predict(x)
    print(preds)

    if modelo_seleccionado.devuelve_dataset().nombre =="Imagenet":
        if modelo_seleccionado.devuelve_familia().nombre == "VGG19":
            result = decode_vgg19(preds,top=3)
        else:
            result = decode_any_model_imagenet(preds,top=3)
    else:
        result = decode_predictions(preds,modelo_seleccionado.devuelve_dataset().nombre)

    # Crear una imagen con la clasificación
    img_with_text = pil_img_orig.copy()
    img_draw = ImageDraw.Draw(img_with_text)

    # Configuramos la fuente y su tamaño
    # Especificar el tamaño de la fuente
    font_size = 20

    # Cargar una fuente predeterminada del sistema con el tamaño de fuente ajustado
    font = ImageFont.truetype("C:/Windows/Fonts/Arial.ttf", size=font_size)

    # Escribimos texto con la predicción
    img_draw.text((10, 10), str(result), fill=rgb_color, font=font)

    # Codificar la imagen con la clasificación en formato base64 para mostrarla en la página web
    buffered = io.BytesIO()
    img_with_text.save(buffered, format="JPEG")
    img_str = base64.b64encode(buffered.getvalue()).decode('ascii')

    return (img_str, str(result))

def obtener_estructura():
    modelo_seleccionado = Modelo.from_json(session.get('modelo_seleccionado'))
    path_fichero_modelo = os.path.join(path_modelos, modelo_seleccionado.nombre)
    print(path_fichero_modelo)
    #CARGA DEL MODELO
    model = load_model(path_fichero_modelo)

    # Guardar la imagen en un archivo temporal
    temp_file = "model_plot.png"
    plot_model(model, to_file=temp_file, show_shapes=True, show_layer_names=True)

    # Leer el contenido del archivo en una cadena
    with open(temp_file, "rb") as file:
        img_bytes = file.read()

    # Convertir a base64
    img_str = base64.b64encode(img_bytes).decode("ascii")

    # Eliminar el archivo temporal
    os.remove(temp_file)

    return (img_str)

# FUNCIÓN GENÉRICA A LA QUE SE PASAN UNAS PREDICCIONES
# Y LAS ETIQUETAS DEL CONJUNTO DE DATOS EN EL QUE SE HA
# ENTRENADO
def decode_predictions(predictions, nombre_dataset):
    predicted_indices = np.argmax(predictions, axis=1)
    class_labels = getattr(nombre_dataset)
    result = [class_labels[idx] for idx in predicted_indices]
    return result

def detectar_objetos_h5(imagen, hex_color):
    # Preprocesar la imagen para que sea compatible con el modelo seleccionado actualmente
    rgb_color = hex_to_rgb(hex_color)
    pil_img_orig = Image.open(io.BytesIO(imagen))
    w_imagen, h_imagen = pil_img_orig.size
    print(w_imagen,h_imagen)
    modelo_seleccionado = Modelo.from_json(session.get('modelo_seleccionado'))
    input_shape = modelo_seleccionado.input_shape

    # Obtenemos las dimensiones de entrada que necesita el modelo
    dimensiones_sin_parentesis = input_shape.strip("()")
    dimensiones = dimensiones_sin_parentesis.split(",")
    canales = int(dimensiones[2])
    w_modelo = int(dimensiones[0])
    h_modelo = int(dimensiones[1])

    # Redimensionar la imagen a (w, h)
    pil_img = pil_img_orig.resize((w_modelo, h_modelo))

    # Convertir la imagen redimensionada a un arreglo numpy
    img_array = np.array(pil_img)

    # Expandir dimensiones para que coincida con la forma esperada por el modelo
    img_array = np.expand_dims(img_array, axis=0)

    # Preprocesar la imagen utilizando la función preprocess_input adecuada para el modelo
    img_preprocessed = preprocess_input(img_array)

    # Realizar la clasificación utilizando el modelo
    path_fichero_modelo = os.path.join(path_modelos, modelo_seleccionado.nombre)
    model = load_model(path_fichero_modelo)
    preds = model.predict(img_preprocessed)[0]

    # Convertir la imagen original a un arreglo numpy
    img_orig_array = np.array(pil_img_orig)

    if preds.size % 4 == 0:
        # Reshape para dividir los valores en grupos de 4
        boxes = preds.reshape(-1, 4)
        # Procesar cada caja
        for box in boxes:
            startX, startY, endX, endY = box

            startX = int(startX * w_imagen)
            startY = int(startY * h_imagen)
            endX = int(endX * w_imagen)
            endY = int(endY * h_imagen)

            # Dibujar el rectángulo en la imagen original
            cv2.rectangle(img_orig_array, (startX, startY), (endX, endY), rgb_color, 3)

    # Convertir la imagen resultante de vuelta a PIL.Image.Image
    pil_img_resultante = Image.fromarray(img_orig_array)

    # Codificar la imagen con la clasificación en formato base64 para mostrarla en la página web
    buffered = io.BytesIO()
    pil_img_resultante.save(buffered, format="JPEG")
    img_str = base64.b64encode(buffered.getvalue()).decode('ascii')

    return (img_str, "")

def detectar_objetos_pb(imagen, hex_color):
    # Decodificar los bytes y convertirlos en una imagen en formato OpenCV
    nparr = np.frombuffer(imagen, np.uint8)
    image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # Verificar si la imagen se cargó correctamente
    if image is None:
        print("Error al cargar la imagen")
    else:

        rgb_color = rgb_to_bgr(hex_to_rgb(hex_color))
        modelo_seleccionado = Modelo.from_json(session.get('modelo_seleccionado'))
        path_fichero_modelo = os.path.join(path_modelos, modelo_seleccionado.nombre)
        path_fichero_clases = os.path.join(path_modelos, modelo_seleccionado.nombre+'txt')
        input_shape = modelo_seleccionado.input_shape
        print('input_shape '+input_shape)
        # convertimos input_shape que viene como cadena a array
        input_shape = tuple(map(int, input_shape.strip('()').split(',')))
        # carga del modelo con openCV
        net = cv2.dnn.readNetFromTensorflow(path_fichero_modelo, path_fichero_clases)
        # establecemos shape de entrada
        blobimg = cv2.dnn.blobFromImage(image, 1.0, (input_shape[0], input_shape[1]), (0, 0, 0), True)
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
                cv2.rectangle(image, rec, rgb_color, 2, 8, 0)
                cv2.putText(image, f'{coco[det_index]}', (int(x1), int(y1 - 5)), cv2.FONT_HERSHEY_SIMPLEX, 0.5, rgb_color, 1, 8, 0)

        # Después de dibujar las cajas y antes de convertir la imagen a PIL.Image.Image
        # Cambiar el orden de los canales de color de BGR a RGB
        # ya que en en PIL se utiliza el orden RGB (rojo, verde, azul), en OpenCV se utiliza el orden BGR (azul, verde, rojo)
        image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        # Convertir la imagen resultante de vuelta a PIL.Image.Image
        pil_img_resultante = Image.fromarray(image_rgb)

        # Codificar la imagen con la clasificación en formato base64 para mostrarla en la página web
        buffered = io.BytesIO()
        pil_img_resultante.save(buffered, format="JPEG")
        img_str = base64.b64encode(buffered.getvalue()).decode('ascii')

        return (img_str, "")

def hex_to_rgb(hex_color):
    # Eliminar el carácter '#' si está presente
    hex_color = hex_color.lstrip('#')

    # Dividir el valor hexadecimal en componentes R, G y B
    r = int(hex_color[0:2], 16)
    g = int(hex_color[2:4], 16)
    b = int(hex_color[4:6], 16)

    # Devolver la tupla RGB
    return (r, g, b)

def rgb_to_bgr(rgb_color):
    # Convertir la cadena a una lista de enteros
    rgb_list = list(map(int, rgb_color))

    # Obtener los componentes R, G y B
    r = rgb_list[0]
    g = rgb_list[1]
    b = rgb_list[2]

    # Devolver la tupla BGR
    return (b, g, r)
