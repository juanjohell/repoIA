# AUTOR: juan José Hellín Belmonte
# FUNCIONALIDAD:  funciones de gestión de los modelos neuronales
import sys
import os
import requests
from tensorflow.keras.models import load_model

# Función para extraccion de metadatos de un modelo h5 pasado por parámetro
# Los ficheros deben estar en la subcarpeta modelos del proyecto.

# Obtener la ruta absoluta del archivo actual
archivo_actual = os.path.abspath(__file__)
#Obtener la ruta al nodo padre del archivo actual
nodo_padre = os.path.dirname(archivo_actual)
# Obtener la ruta al abuelo del archivo actual
modelTracker_path = os.path.dirname(nodo_padre)
modelos_path = os.path.join(modelTracker_path, 'modelos')

def extrae_info_de_modelo(app, nombre_fichero):
    #Inicializamos datos
    optimizer = None
    input_shape = None
    optimizer_type = None
    learning_rate = None
    depth = None
    b1 = None
    b2 = None
    epsilon = None
    amsgrad = None
    decay = None
    layer_config = None
    parametros = {'nombre': nombre_fichero, 'depth': depth, 'input_shape': input_shape,
                  'optimizer_type': optimizer_type, 'optimizer_id': None,
                  'lc_id': None, 'decay': decay,
                  'learning_rate': learning_rate, 'b1': b1, 'b2': b2, 'epsilon': epsilon,
                  'amsgrad': amsgrad, 'lc_config': layer_config}

    # Comprobar existencia de fichero
    ruta_fichero = os.path.join(modelos_path, nombre_fichero)
    # Comprobar existencia del fichero
    if not os.path.exists(ruta_fichero):
        mensaje_error = "El fichero {} no existe".format(ruta_fichero)
        invocar_mostrar_mensaje(app, mensaje_error)
        return parametros
    try:
        # Carga el modelo Keras en formato h5
        saved_model = load_model(ruta_fichero)
        # traemos configuracion
        config = saved_model.get_config()
    except Exception as e:
        mensaje_error = "Error al cargar el modelo o extraer su configuración: {}".format(str(e))
        invocar_mostrar_mensaje(app, mensaje_error)
        return parametros

    # Recuperamos valores que definen el modelo
    depth = len(config['layers'])
    #input_shape con formato
    input_shape_inic = config['layers'][0]['config']['batch_input_shape']
    input_shape = '({})'.format(','.join(str(dim) for dim in input_shape_inic[1:]))

    if saved_model.optimizer:
        optimizer = saved_model.optimizer.get_config()
        optimizer_type = optimizer['name']
        try:
            learning_rate = optimizer['learning_rate']
        except KeyError:
            learning_rate = None
        try:
            b1 = optimizer['beta_1']
        except KeyError:
            b1 = None
        try:
            b2 = optimizer['beta_2']
        except KeyError:
            b2 = None
        try:
            epsilon = optimizer['epsilon']
        except KeyError:
            epsilon = None
        try:
            amsgrad = optimizer['amsgrad']
        except KeyError:
            amsgrad = None
        try:
            decay = optimizer['decay']
        except KeyError:
            decay = None
        try:
            layer_config = config['layers']
        except KeyError:
            layer_config = None

    parametros = {'nombre': nombre_fichero, 'depth': depth, 'input_shape': input_shape,
                  'optimizer_type': optimizer_type, 'optimizer_id': None,
                  'lc_id': None, 'decay': decay,
                  'learning_rate': learning_rate, 'b1': b1, 'b2': b2, 'epsilon': epsilon,
                  'amsgrad': amsgrad, 'lc_config': layer_config}
    print(parametros)
    return parametros

# SALVA FICHERO EN SISTEMA DE FICHEROS DE LA APLICACION
def almacenar_fichero(config, archivo):
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

def invocar_mostrar_mensaje(app, mensaje):
    with app.test_client() as client:
        client.get('/mostrar_mensaje?mensaje={}'.format(mensaje))
