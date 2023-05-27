# -*- coding: utf-8 -*-
"""
Created on Wed Feb 22 18:01:38 2023

@author: jjhb01
"""
from flask import Flask, render_template, request, jsonify
from keras.preprocessing import image
from keras.applications.vgg19 import preprocess_input, decode_predictions
import numpy as np
import io
import sys
import base64
import logging
import json
from PIL import Image, ImageDraw, ImageFont
#from modelos.vgg19_imagenet import get_vgg19_model
from gestion_modelos import extrae_info_de_modelo


app = Flask(__name__)

# Configuración de los logs en la consola
app.logger.addHandler(logging.StreamHandler(sys.stdout))
app.logger.setLevel(logging.DEBUG)

# Cargar el modelo VGG19 pre-entrenado
#model = get_vgg19_model()


@app.route('/')
def index():
    return render_template('index.html')
@app.route('/seleccionar_modelo', methods=['GET'])
def seleccionar_modelo():
    return render_template('realizarInferencia.html')

@app.route('/cargar_modelo', methods=['GET'])
def cargar_modelo():
    #inicialmente no tenemos datos del modelo
    mostrar_datos = False  # Variable que indica si se deben mostrar los datos o no
    datos_modelo = {}  # Define una variable datos_modelo vacía o con valores predeterminados
    return render_template('cargarModelo.html', datos_modelo=datos_modelo, mostrar_datos=mostrar_datos)

@app.route('/extraer_config_modelo', methods=['POST'])
def extraer_config_modelo():
    fichero = request.files['file']
    datos_modelo = extrae_info_de_modelo(fichero)
    datos_modelo_json = jsonify(datos_modelo)
    mostrar_datos = True  # Variable que indica si se deben mostrar los datos o no
    print(datos_modelo_json)
    return render_template('cargarModelo.html', datos_modelo=datos_modelo_json, mostrar_datos=mostrar_datos)

@app.route('/clasificar', methods=['POST'])
def predict():
    # Obtener la imagen enviada por el usuario
    img = request.files['image'].read()

    # Convertir la imagen a un objeto de imagen PIL
    pil_img = Image.open(io.BytesIO(img))

    # Preprocesar la imagen para que sea compatible con el modelo VGG19
    pil_img = Image.open(io.BytesIO(img))
    pil_img = pil_img.resize((224, 224))  # Cambiar dimensiones de la imagen
    x = image.img_to_array(pil_img)
    x = np.expand_dims(x, axis=0)
    x = preprocess_input(x)

    # Realizar la clasificación utilizando el modelo VGG19 pre-entrenado
    preds = model.predict(x)
    result = decode_predictions(preds, top=3)[0]

    # Crear una imagen con la clasificación
    img_with_text = pil_img.copy()
    img_draw = ImageDraw.Draw(img_with_text)
    img_draw.text((10, 10), str(result), fill=(255, 0, 0))

    # Codificar la imagen con la clasificación en formato base64 para mostrarla en la página web
    buffered = io.BytesIO()
    img_with_text.save(buffered, format="JPEG")
    img_str = base64.b64encode(buffered.getvalue()).decode('ascii')

    return render_template('realizarInferencia.html', prediction=img_str)
@app.route('/cargamodelo', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        # Guardar el archivo cargado en el servidor
        archivo = request.files['file']
        nombre_fichero = archivo.filename
        ruta_completa = app.config['UPLOAD_FOLDER'].joinpath(nombre_fichero)
        archivo.save(ruta_completa)

        # Extraer la información del modelo y guardarla en la tabla SQLite
        conn = sqlite3.connect('nombre_db.sqlite')
        params = {}
        params = extrae_info_de_modelo(ruta_completa)
        insert_tabla_modelos(conn, params)
        conn.close()

        return 'Archivo cargado con éxito'

    return render_template('upload.html')

if __name__ == '__main__':
    app.run(debug=True)
