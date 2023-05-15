# -*- coding: utf-8 -*-
"""
Created on Wed Feb 22 18:01:38 2023

@author: jjhb01
"""
from flask import Flask, render_template, request
from keras.preprocessing import image
from keras.applications.vgg19 import preprocess_input, decode_predictions
import numpy as np
import io
import base64
from PIL import Image, ImageDraw, ImageFont
from modelos.vgg19_imagenet import get_vgg19_model


app = Flask(__name__)



# Cargar el modelo VGG19 pre-entrenado
model = get_vgg19_model()


@app.route('/')
def index():
    return render_template('index.html')


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

    return render_template('index.html', prediction=img_str)
@app.route('/cargamodelo', methods=['GET'])
def carga_modelo():
    return render_template('cargamodelo.html')

if __name__ == '__main__':
    app.run(debug=True)
