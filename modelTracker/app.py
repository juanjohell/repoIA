# -*- coding: utf-8 -*-
"""
Created on Wed Feb 22 18:01:38 2023

@author: jjhb01
"""
from flask import Flask, render_template, request, jsonify, session
import json
import io
import sys
import base64
import logging
import os
from PIL import Image, ImageDraw
from backend.gestion_modelos import extrae_info_de_modelo, almacenar_fichero
from sql.modelo_bbdd import insert_tabla_modelos, listado_modelos, editar_tabla_modelo
from sql.persistencia_bbdd import Modelo
from keras.preprocessing import image
from keras.applications.vgg19 import preprocess_input, decode_predictions
from keras.models import load_model
from werkzeug.utils import secure_filename
import numpy as np

app = Flask(__name__)

#clave para el manejo seguro de sesiones
app.secret_key = 'XJ9s&3u$er7M*?3Hv!Rt@3y^Z6#jGq2'

#CONFIGURACION
# Configuración de los logs en la consola
app.logger.addHandler(logging.StreamHandler(sys.stdout))
app.logger.setLevel(logging.DEBUG)

# Cargar el modelo VGG19 pre-entrenado
#model = get_vgg19_model()

#ruta al recurso de los modelos
path_modelos = f'{sys.path[0]}/modelos/'

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/realizar_inferencia', methods=['GET'])
def realizar_inferencia():
    id_modelo = request.args.get('id_modelo')
    modelo_seleccionado = Modelo.devuelve_modelo_por_id(id_modelo)
    session['modelo_seleccionado'] = modelo_seleccionado.to_json()
    return render_template('realizarInferencia.html', modelo=modelo_seleccionado)

@app.route('/administrar_modelos', methods=['GET'])
def administrar_modelos():
    return render_template('administrarModelos.html')

@app.route('/cargar_modelo')
def cargar_modelo():
    return render_template('cargarFichero.html')

@app.route('/salvar_fichero', methods=['GET', 'POST'])
def mostrar_form_datos_fichero():
    # Guardar el archivo cargado en el servidor
    archivo = request.files['file']
    nombre_fichero = archivo.filename
    ruta_completa = path_modelos+nombre_fichero
    archivo.save(ruta_completa)
    session['nombre_fichero'] = nombre_fichero
    return render_template('resultadoCargaFichero.html')

@app.route('/salvar_y_extraer_config_modelo', methods=['GET', 'POST'])
def salvar_y_extraer_config_modelo():
    datos_modelo = extrae_info_de_modelo(session['nombre_fichero'])
    datos_json = json.dumps(datos_modelo)  # Convertir a cadena JSON
    print(datos_json)
    return jsonify(datos_modelo)

@app.route('/insertar_modelo', methods=['POST'])
def insertar_modelo():
    # Obtener los datos del formulario
    # carga de datos editados en formulario:
    params = {
        #'id_uso': request.form.get('id_uso'),
        #'id_optimizer': request.form.get('id_optimizer'),
        'nombre': request.form.get('nombre'),
        #'id_familia': request.form.get('id_familia'),
        'descripcion': request.form.get('descripcion'),
        'depth': request.form.get('depth'),
        'input_shape': request.form.get('input_shape')
    }

    # Llamar a la función de inserción y obtener el último ID insertado
    last_row_id = insert_tabla_modelos(params)

    # Retornar una respuesta al cliente
    return 'Modelo insertado correctamente. Último ID insertado: {}'.format(last_row_id)

# SELECCIONAR UN MODELO DE LA TABLA DE MODELOS PARA ELLO SE
# MUESTRA UN LISTADO CON TODOS LOS DISPONIBLES
@app.route('/seleccionar_modelo')
def seleccionar_modelo():
    modelos = listado_modelos()
    # Renderizar la plantilla HTML y pasar los modelos como contexto
    return render_template('seleccionarModelo.html', modelos=modelos)

@app.route('/lista_editar_modelos')
def lista_editar_modelos():
    modelos = listado_modelos()
    # Renderizar la plantilla HTML y pasar los modelos como contexto
    return render_template('listaEdicionModelos.html', modelos=modelos)

# Se muestran los datos asociados a un modelo en
# un formulario listos para su edición
@app.route('/editar_modelo', methods=['GET'])
def editar_modelo():
    id_modelo = request.args.get('id_modelo')
    modelo_seleccionado = Modelo.devuelve_modelo_por_id(id_modelo)
    session['modelo_seleccionado'] = modelo_seleccionado.to_json()
    return render_template('edicionModelo.html', modelo=modelo_seleccionado)

# toma los datos editados y realiza la actualización en bbdd
@app.route('/editar_modelo_bbdd', methods=['POST'])
def editar_modelo_bbdd():
    # carga de datos editados en formulario:
    params = {
        'id_uso': request.form.get('id_uso'),
        'id_optimizer': request.form.get('id_optimizer'),
        'nombre': request.form.get('nombre'),
        'id_familia': request.form.get('id_familia'),
        'descripcion': request.form.get('descripcion'),
        'depth': request.form.get('depth'),
        'input_shape': request.form.get('input_shape'),
        'id_modelo': request.form.get('id_modelo')
    }

    result = editar_tabla_modelo(params)
    if result:
        mensaje = 'La actualización se ha realizado de forma correcta'
    else:
        mensaje = 'No se ha realizado ninguna operación'
    resultado = {
        'mensaje': mensaje,
        'url': 'lista_editar_modelos'
    }
    return render_template('mensaje.html', resultado=resultado)

@app.route('/inferir_con_modelo', methods=['POST'])
def inferir_con_modelo():
    # Obtener la imagen enviada por el usuario
    img = request.files['image'].read()

    # Convertir la imagen a un objeto de imagen PIL
    pil_img = Image.open(io.BytesIO(img))

    # Preprocesar la imagen para que sea compatible con el modelo
    # seleccionado actualmente
    pil_img = Image.open(io.BytesIO(img))
    # aqui hay que obtener el input_shape del modelo
    pil_img = pil_img.resize((224, 224))  # Cambiar dimensiones de la imagen
    x = image.img_to_array(pil_img)
    x = np.expand_dims(x, axis=0)
    x = preprocess_input(x)

    # Realizar la clasificación utilizando el modelo
    modelo_seleccionado = Modelo.from_json(session.get('modelo_seleccionado'))
    path_fichero_modelo = path_modelos + modelo_seleccionado.nombre
    print(path_fichero_modelo)
    model = load_model(path_fichero_modelo)
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

    return render_template('realizarInferencia.html', prediction=img_str, modelo=session.get('modelo_seleccionado'))

@app.route('/mostrar_mensaje', methods=['GET'])
def mostrar_mensaje():
    resultado = {
        'mensaje': request.args.get('mensaje'),
        'url': request.args.get('url')
    }
    return render_template('mensaje.html', resultado=resultado)

if __name__ == '__main__':
    app.run(debug=True)
