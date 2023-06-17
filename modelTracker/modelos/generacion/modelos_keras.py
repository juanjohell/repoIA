from keras.preprocessing import image
from keras.applications.vgg19 import VGG19, preprocess_input, decode_predictions
from keras.applications.inception_v3 import InceptionV3, decode_predictions, preprocess_input
from keras.applications.mobilenetv2 import MobileNetV2, decode_predictions, preprocess_input
from keras.models import load_model
import numpy as np
import os

# GENERACIÓN DE FICHEROS H5 DE DIFERENTES MODELOS NEURONALES
# KERAS PREENTRENADOS EN IMAGENET.
def get_model(model_file_path, model_name):
    model = None
    if os.path.exists(model_file_path):
        # Load model from file
        model = load_model(model_file_path)
    else:
        # Create new model with pre-trained weights based on model_name
        if model_name == 'vgg19':
            model = VGG19(weights='imagenet', include_top=True)
        elif model_name == 'inceptionv3':
            model = InceptionV3(weights='imagenet', include_top=True)
        elif model_name == 'mobilenetv2':
            model = MobileNetV2(weights='imagenet', include_top=True)
        else:
            raise ValueError(f"Invalid model name: {model_name}")

        # Save model to file
        model.save(model_file_path)

    return model


# Esta funcion es solo para predecir por fuera de la aplicación y debuggear
# los ficheros generados:
def predecir(img_path):
    # Importar el modelo pre-entrenado VGG19

   # model = get_vgg19_model('../vgg19_imagenet.h5')
   # model = get_inceptionV3('../Inceptionv3_imagenet.h5')
    model = get_inceptionV3('../MobileNetV2.h5')
    # Cargar una imagen y preprocesarla para que sea compatible con el modelo
    #VGG19
    #img = image.load_img(img_path, target_size=(224, 224))
    #InceptionV3
    #img = image.load_img(img_path, target_size=(139, 139))
    #MobileNetV2
    img = image.load_img(img_path, target_size=(224, 224))
    x = image.img_to_array(img)
    x = np.expand_dims(x, axis=0)
    x = preprocess_input(x)

    # Clasificar la imagen cargada con el modelo pre-entrenado
    preds = model.predict(x)
    print('Predicción:', decode_predictions(preds, top=3)[0])


predecir('../imagenes_test/gato.jpg')

def datos_modelo(model):
    config = model.get_config()
    print 
