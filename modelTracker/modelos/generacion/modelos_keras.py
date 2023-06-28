from keras.preprocessing import image
from keras.applications.vgg19 import VGG19, preprocess_input, decode_predictions
from keras.applications.inception_v3 import InceptionV3, decode_predictions, preprocess_input
from keras.applications.mobilenetv2 import MobileNetV2, decode_predictions, preprocess_input
from keras.models import load_model
import numpy as np
import os, sys


# GENERACIÓN DE FICHEROS H5 DE DIFERENTES MODELOS NEURONALES
# KERAS PREENTRENADOS EN IMAGENET.
def get_model(nombre_modelo):
    model = None
    model_file_path = os.path.join(os.path.dirname(sys.path[0]), nombre_modelo + '.h5')
    print(model_file_path)
    if os.path.exists(model_file_path):
        # Load model from file
        model = load_model(model_file_path)
    else:
        # Create new model with pre-trained weights based on model_name
        if nombre_modelo == 'vgg19':
            model = VGG19(weights='imagenet', include_top=True)
        elif nombre_modelo == 'InceptionV3':
            model = InceptionV3(weights='imagenet', include_top=True)
        elif nombre_modelo == 'MobileNetV2':
            model = MobileNetV2(weights='imagenet', include_top=True)
        else:
            raise ValueError(f"Invalid model name: {nombre_modelo}")

        # Save model to file
        model.save(model_file_path)

    return model


# Esta funcion es solo para predecir por fuera de la aplicación y debuggear
# los ficheros generados:
def predecir(img_path, nombre_modelo):

    model = get_model(nombre_modelo)
    # Cargar una imagen y preprocesarla para que sea compatible con el modelo
    datos_modelo(model)

    if nombre_modelo == 'vgg19':
        input_shape = (224, 224, 3)
    elif nombre_modelo == 'InceptionV3':
        input_shape = (299, 299, 3)
    else:
        input_shape = (299, 299, 3)

    img = image.load_img(img_path, target_size=input_shape)
    x = image.img_to_array(img)
    x = np.expand_dims(x, axis=0)
    x = preprocess_input(x)

    # Clasificar la imagen cargada con el modelo pre-entrenado
    preds = model.predict(x)
    print('Predicción:', decode_predictions(preds, top=3)[0])

def datos_modelo(model):
    config = model.get_config()
    print 

predecir('../imagenes_test/fruta.jpg', 'MobileNetV2')
