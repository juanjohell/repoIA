# AUTOR: juan José Hellín Belmonte
# FUNCIONALIDAD:  funciones de gestión de los modelos neuronales
import sys
import os

from pathlib import Path
from tensorflow import keras
from tensorflow.keras.models import load_model
from flask import jsonify
import app
from flask import Flask, url_for

# Función para extraccion de metadatos de un modelo h5 pasado por parámetro
# Los ficheros deben estar en la subcarpeta modelos del proyecto.
#
app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = f'{sys.path[0]}/modelos/'


def extrae_info_de_modelo(nombre_fichero):

    # Comprobar existencia
    ruta_fichero = app.config['UPLOAD_FOLDER'] + nombre_fichero

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
    ruta_completa = app.config['UPLOAD_FOLDER'] + (nombre_fichero)
    archivo.save(ruta_completa)


    ruta_completa = str(app.config['UPLOAD_FOLDER'] + (nombre_fichero))
    ruta_completa = "../modelos/" + nombre_fichero
    print(ruta_completa)
    #ANTES DE ALMACENAR SE DEBE COMPROBAR QUE NO ESTÁ EN EL SISTEMA DE ARCHIVOS
    if os.path.exists(ruta_completa):
        return False
    else:
        archivo.save(ruta_completa)
        return True

