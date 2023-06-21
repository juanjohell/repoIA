# -*- coding: utf-8 -*-
"""
Created on Wed Feb 22 18:01:38 2023

@author: jjhb01
"""
from flask import Flask, render_template, request, jsonify, session, redirect, url_for
import json
import io
import sys
import base64
import logging
import os
from PIL import Image, ImageDraw
from backend.gestion_modelos import extrae_info_de_modelo, almacenar_fichero
from backend.inferencia import clasificar_imagen, obtener_estructura
from sql.modelo_bbdd import insert_tabla_modelos, listado_modelos, editar_tabla_modelo, listado_datasets, listado_usos, listado_familia_modelos
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


#ruta al recurso de los modelos
path_modelos = f'{sys.path[0]}/modelos/'

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/menu_inicial')
def menu_inicial():
    return render_template('menuInicial.html')

@app.route('/realizar_inferencia', methods=['GET'])
def realizar_inferencia():
    id_modelo = request.args.get('id_modelo')
    modelo_seleccionado = Modelo.devuelve_modelo_por_id(id_modelo)
    session['modelo_seleccionado'] = modelo_seleccionado.to_json()
    return render_template('realizarInferencia.html', modelo=modelo_seleccionado, mostrar_barra_progreso=True)

@app.route('/ver_estructura', methods=['GET'])
def ver_estructura():
    id_modelo = request.args.get('id_modelo')
    modelo_seleccionado = Modelo.devuelve_modelo_por_id(id_modelo)
    session['modelo_seleccionado'] = modelo_seleccionado.to_json()
    img_str = obtener_estructura()
    return render_template('infoModelo.html', modelo=modelo_seleccionado, img_str=img_str)

@app.route('/administrar_modelos', methods=['GET'])
def administrar_modelos():
    return render_template('administrarModelos.html')

@app.route('/cargar_modelo')
def cargar_modelo():
    return render_template('cargarFichero.html')

@app.route('/salvar_fichero', methods=['GET', 'POST'])
def salvar_fichero():
    # Guardar el archivo cargado en el servidor
    archivo = request.files['file']
    nombre_fichero = archivo.filename
    ruta_completa = path_modelos+nombre_fichero
    archivo.save(ruta_completa)
    session['nombre_fichero'] = nombre_fichero
    datasets = listado_datasets()
    familias = listado_familia_modelos()
    usos = listado_usos()
    return render_template('resultadoCargaFichero.html', datasets = datasets, familias=familias, usos=usos)

@app.route('/salvar_y_extraer_config_modelo', methods=['GET', 'POST'])
def salvar_y_extraer_config_modelo():
    datos_modelo = extrae_info_de_modelo(session['nombre_fichero'])
    #datos_json = json.dumps(datos_modelo)  # Convertir a cadena JSON
    #print(datos_json)

    # Convertir los valores float32 a float64 para la serialización JSON
    datos_modelo_converted = {}
    for key, value in datos_modelo.items():
        if isinstance(value, np.float32):
            datos_modelo_converted[key] = np.float64(value)
        else:
            datos_modelo_converted[key] = value

    return jsonify(datos_modelo_converted)

@app.route('/insertar_modelo', methods=['POST'])
def insertar_modelo():
    # Obtener los datos del formulario
    # carga de datos editados en formulario:

    modelo = Modelo(nombre=request.form.get('nombre'),
                    descripcion=request.form.get('descripcion'),
                    depth=request.form.get('depth'),
                    input_shape=request.form.get('input_shape'),
                    id_dataset=request.form.get('dataset-select'),
                    id_uso=request.form.get('uso-select'),
                    id_familia=request.form.get('familia-select')
                    )
    modelo.guardar()

    # Retornar una respuesta al usuario
    return redirect(url_for('seleccionar_modelo'))

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

@app.route('/datos_modelo', methods=['GET'])
def datos_modelo():
    id_modelo = request.args.get('id_modelo')
    modelo_seleccionado = Modelo.devuelve_modelo_por_id(id_modelo)
    return render_template('datosModelo.html', modelo=modelo_seleccionado)

# Se muestran los datos asociados a un modelo en
# un formulario listos para su edición
@app.route('/editar_modelo', methods=['GET'])
def editar_modelo():
    id_modelo = request.args.get('id_modelo')
    modelo_seleccionado = Modelo.devuelve_modelo_por_id(id_modelo)
    session['modelo_seleccionado'] = modelo_seleccionado.to_json()
    datasets = listado_datasets()
    usos = listado_usos()
    familias = listado_familia_modelos()
    return render_template('edicionModelo.html', modelo=modelo_seleccionado, datasets=datasets, usos=usos, familias=familias)

# toma los datos editados y realiza la actualización en bbdd
@app.route('/editar_modelo_bbdd', methods=['POST'])
def editar_modelo_bbdd():
    # carga de datos editados en formulario:
    modelo = Modelo(id_modelo=request.form.get('id_modelo'),
                    nombre=request.form.get('nombre'),
                    descripcion=request.form.get('descripcion'),
                    depth=request.form.get('depth'),
                    input_shape=request.form.get('input_shape'),
                    id_dataset=request.form.get('dataset-select'),
                    id_uso=request.form.get('uso-select'),
                    id_familia=request.form.get('familia-select')
                    )
    modelo.guardar()
    mensaje = 'La actualización se ha realizado de forma correcta'
    resultado = {
        'mensaje': mensaje,
        'url': 'lista_editar_modelos'
    }
    return render_template('mensaje.html', resultado=resultado)


@app.route('/eliminar_modelo', methods=['GET'])
def eliminar_modelo():
    id_modelo = request.args.get('id_modelo')
    modelo_seleccionado = Modelo.devuelve_modelo_por_id(id_modelo)
    modelo_seleccionado.eliminar_modelo()
    # Retornar al listado de modelos
    return redirect(url_for('seleccionar_modelo'))

# INFERENCIA
@app.route('/inferir_con_modelo', methods=['POST'])
def inferir_con_modelo():
    # Obtener la imagen enviada por el usuario
    img = request.files['image'].read()
    img_str, resultado = clasificar_imagen(img)
    return render_template('realizarInferencia.html', prediction=img_str, modelo=session['modelo_seleccionado'], mostrar_barra_progreso=False, resultado=resultado)

@app.route('/mostrar_mensaje', methods=['GET'])
def mostrar_mensaje():
    resultado = {
        'mensaje': request.args.get('mensaje'),
        'url': request.args.get('url')
    }
    return render_template('mensaje.html', resultado=resultado)

if __name__ == '__main__':
    app.run(debug=True)
