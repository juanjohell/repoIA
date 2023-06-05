# AUTOR: juan José Hellín Belmonte
# FUNCIONALIDAD:  funciones de gestión de los modelos neuronales
import sys
import os
from werkzeug.utils import secure_filename
from os import path
from tensorflow import keras
from tensorflow.keras.models import load_model
from flask import jsonify
import app
from flask import Flask, url_for

# Función para extraccion de metadatos de un modelo h5 pasado por parámetro
# Los ficheros deben estar en la subcarpeta modelos del proyecto.
#


app.config['UPLOAD_FOLDER'] = path(sys.path[0]).parent.joinpath('modelos')


def extrae_info_de_modelo(fichero):
    if not fichero:
        return {'error': f'Error: el archivo {fichero} no existe'}
    # Guardar el archivo en el sistema de archivos
    nombre_fichero = secure_filename(fichero.filename)
    ruta_fichero = os.path.join('./modelos/', nombre_fichero)
    fichero.save(ruta_fichero)

    # Carga el modelo Keras en formato h5
    #saved_model = keras.models.load_model(ruta_fichero)
    saved_model = load_model(ruta_fichero)
    config = saved_model.get_config()

    # Recuperamos valores que definen el modelo
    depth = len(config['layers'])
    input_shape = config['layers'][0]['config']['batch_input_shape']
    optimizer = None
    optimizer_type = None
    learning_rate = None
    b1 = None
    b2 = None
    epsilon = None
    amsgrad = None
    decay = None
    layer_config = None
    if saved_model.optimizer:
        optimizer = saved_model.optimizer.get_config()
        optimizer_type = optimizer['name']
        learning_rate = optimizer['learning_rate']
        b1 = optimizer['beta_1']
        b2 = optimizer['beta_2']
        epsilon = optimizer['epsilon']
        amsgrad = optimizer['amsgrad']
        decay = optimizer['decay']
        layer_config = config['layers']

    parametros = {'nombre': nombre_fichero, 'depth': depth, 'input_shape': input_shape,
                  'optimizer_type': optimizer_type, 'optimizer_id': None,
                  'lc_id': None, 'decay': decay,
                  'learning_rate': learning_rate, 'b1': b1, 'b2': b2, 'epsilon': epsilon,
                  'amsgrad': amsgrad, 'lc_config': layer_config}
    print(parametros)
    return parametros

# SALVA FICHERO EN SISTEMA DE FICHEROS DE LA APLICACION
def almacenar_fichero(archivo):
    nombre_fichero = archivo.filename
    ruta_completa = app.config['UPLOAD_FOLDER'].joinpath(nombre_fichero)

    #ANTES DE ALMACENAR SE DEBE COMPROBAR QUE NO ESTÁ EN EL SISTEMA DE ARCHIVOS
    if os.path.exists(ruta_completa):
     #redirigimos a la página de mensajes
        resultado = {
            'mensaje': 'El archivo ya existe en el sistema de ficheros de la aplicación con ése nombre',
            'url': 'cargar_modelo'
        }
        with app.test_client() as client:
            url = url_for('/mostrar_mensaje', resultado=resultado)
            response = client.get(url)
            print(response.data.decode())

    archivo.save(ruta_completa)
