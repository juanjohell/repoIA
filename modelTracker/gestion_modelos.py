# AUTOR: juan José Hellín Belmonte
# FUNCIONALIDAD:  funciones de gestión de los modelos neuronales

# Función para extraccion de metadatos de un modelo h5 pasado por parámetro
# Los ficheros deben estar en la subcarpeta modelos del proyecto.
import tensorflow as tf
from tensorflow import keras

def extrae_info_de_modelo(nombre_fichero):
    p = Path(sys.path[0])
    filepath = Path(p.parent.joinpath('modelos')
    ruta_completa = filepath.joinpath(nombre_fichero)
    if not filepath.exists():
        return f'Error: el archivo {nombre_fichero} no existe'

    # Carga el modelo Keras en formato h5
    saved_model = keras.models.load_model(
    "my_model", custom_objects={"CustomModel": CustomModel}
)
    config = saved_model.get_config()

    #RECUPERAMOS VALORES QUE DEFINEN EL MODELO
    depth = len(config['layers'])
    input_shape = config['layers'][0]['config']['batch_input_shape']
    optimizer = saved_model.optimizer.get_config()
    optimizer_type = optimizer['name']
    learning_rate = optimizer['learning_rate']
    b1 = optimizer['beta_1']
    b2 = optimizer['beta_2']
    epsilon = optimizer['epsilon']
    amsgrad = optimizer['amsgrad']
    decay = optimizer['decay']
    layer_config = config['layers']

    params[nombre_fichero] = {'DEPTH': depth, 'INPUT_SHAPE': input_shape,
                          'optimizer_type': optimizer_type, 'optimizer_id': None,
                          'lc_id': None, 'decay': decay,
                          'learning_rate': learning_rate, 'b1': b1, 'b2': b2, 'epsilon': epsilon,
                          'amsgrad': amsgrad, 'lc_config': layer_config}

    return params
