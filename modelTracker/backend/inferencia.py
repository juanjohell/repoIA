from flask import Flask, render_template, request, jsonify, session, redirect, url_for
import io
import os
import base64
from PIL import Image, ImageDraw
from backend.gestion_modelos import extrae_info_de_modelo, almacenar_fichero
from sql.modelo_bbdd import insert_tabla_modelos, listado_modelos, editar_tabla_modelo
from sql.persistencia_bbdd import Modelo
from keras.preprocessing import image
from tensorflow.keras.preprocessing.image import img_to_array
from keras.applications.vgg19 import preprocess_input, decode_predictions as decode_vgg19
from keras.models import load_model
from keras.utils.vis_utils import plot_model
from werkzeug.utils import secure_filename
import backend.datasets_labels as datasets_labels
import numpy as np
import cv2

#CONFIGURACION
# Obtener la ruta absoluta a la carpeta que contiene los ficheros de modelos
path_modelos = os.path.abspath('modelos')


# Recibe una imagen en formato de bytes
# Realiza clasificación y devuelve la imagen con el resultado
# y el propio resultado como cadena de texto
def clasificar_imagen(imagen):

    # Preprocesar la imagen para que sea compatible con el modelo
    # seleccionado actualmente
    pil_img_orig = Image.open(io.BytesIO(imagen))
    modelo_seleccionado = Modelo.from_json(session.get('modelo_seleccionado'))
    # aqui hay que obtener el input_shape del modelo
    input_shape = modelo_seleccionado.input_shape
    print (input_shape)
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

    if modelo_seleccionado.devuelve_familia().nombre == "VGG19" and modelo_seleccionado.devuelve_dataset().nombre =="Imagenet":
        result = decode_vgg19(preds,top=3)
    else:
        result = decode_predictions(preds,modelo_seleccionado.devuelve_dataset().nombre)

    # Crear una imagen con la clasificación
    img_with_text = pil_img_orig.copy()
    img_draw = ImageDraw.Draw(img_with_text)
    img_draw.text((10, 10), str(result), fill=(255, 0, 0))

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
    class_labels = getattr(datasets_labels, nombre_dataset)
    result = [class_labels[idx] for idx in predicted_indices]
    return result

def detectar_objetos(imagen):
    # Preprocesar la imagen para que sea compatible con el modelo seleccionado actualmente
    pil_img_orig = Image.open(io.BytesIO(imagen))
    modelo_seleccionado = Modelo.from_json(session.get('modelo_seleccionado'))
    input_shape = modelo_seleccionado.input_shape

    # Tratamos las dimensiones de input_shape por separado
    dimensiones_sin_parentesis = input_shape.strip("()")
    dimensiones = dimensiones_sin_parentesis.split(",")
    canales = int(dimensiones[2])
    w = int(dimensiones[0])
    h = int(dimensiones[1])

    # Redimensionar la imagen a (h, w)
    pil_img = pil_img_orig.resize((h, w))

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

    (startX, startY, endX, endY) = preds

    startX = int(startX * w)
    startY = int(startY * h)
    endX = int(endX * w)
    endY = int(endY * h)

    # Convertir la imagen original a un arreglo numpy
    img_orig_array = np.array(pil_img_orig)

    # Dibujar el rectángulo en la imagen original
    cv2.rectangle(img_orig_array, (startX, startY), (endX, endY), (255, 255, 0), 3)

    # Convertir la imagen resultante de vuelta a PIL.Image.Image
    pil_img_resultante = Image.fromarray(img_orig_array)

    # Codificar la imagen con la clasificación en formato base64 para mostrarla en la página web
    buffered = io.BytesIO()
    pil_img_resultante.save(buffered, format="JPEG")
    img_str = base64.b64encode(buffered.getvalue()).decode('ascii')

    return (img_str, "")
