# -*- coding: utf-8 -*-
"""
Created on Thu May 11 13:22:12 2023

@author: jjhb01
"""
from sql.modelo_bbdd import *

# Obtener la ruta absoluta a la carpeta que contiene el archivo de base de datos
folder_path = os.path.abspath('bbdd')
# Combinar la ruta absoluta de la carpeta con el nombre del archivo de base de datos
path = os.path.join(folder_path, 'gestion_modelos.sqlite')

def crea_base_de_datos(path):
    connection = create_connection(path)
    # Crear las tablas de la base de datos
    cursor_obj = connection.cursor()
    print('creación de modelo')
    cursor_obj.execute(crear_tabla_usos)
    cursor_obj.execute(crear_tabla_optimizador)
    cursor_obj.execute(crear_tabla_familia_modelo)
    cursor_obj.execute(crear_tabla_modelos)
    connection.commit()

    #INSERCION DE DATOS MAESTROS:
    # Tabla de usos:
    print('insercion de datos')
    fila = ("Clasificación","Modelo diseñado para clasificación de imágenes")
    insert_tabla_usos(fila)
    fila = ("Detección","Modelo diseñado para detección de objetos en imágenes")
    insert_tabla_usos(fila)

    # Tabla de optimizadores
    fila = ("Adam",0.001,1,0.0,0.9,0.999,0.00000001)
    insert_tabla_optimizador(fila)

    # Tabla familia_modelo
    fila = ("VGG19","Modelo vgg19", 26, "(224,224,3)")
    insert_tabla_familia_modelo(fila)

    # Tabla Modelos, se inserta el modelo de ejemplo de la aplicación
    fila = ("vgg19_imagenet.h5", "Modelo VGG9 de Keras entrenado en Imagenet", 26, "(224,224,3)")
    insert_tabla_modelos(fila)

if not existe_bbdd(path):
    crea_base_de_datos(path)
