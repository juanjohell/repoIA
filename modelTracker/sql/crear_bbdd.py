# -*- coding: utf-8 -*-
"""
Created on Thu May 11 13:22:12 2023

@author: jjhb01
"""
from sql.modelo_bbdd import *
import os

#Ruta a modelTracker
# Obtener la ruta absoluta del archivo actual
archivo_actual = os.path.abspath(__file__)

# Obtener la ruta al nodo padre del archivo actual
nodo_padre = os.path.dirname(archivo_actual)
# Obtener la ruta al abuelo del archivo actual
modelTracker_path = os.path.dirname(nodo_padre)
modelTracker_path = os.path.join(modelTracker_path, 'bbdd')

# Combinar la ruta absoluta de la carpeta con el nombre del archivo de base de datos
path = os.path.join(modelTracker_path, 'gestion_modelos.sqlite')

def crea_base_de_datos(path):
    print(modelTracker_path)
    connection = create_connection(path)
    # Crear las tablas de la base de datos
    cursor_obj = connection.cursor()
    print('creación de modelo')
    cursor_obj.execute(crear_tabla_usos)
    cursor_obj.execute(crear_tabla_optimizador)
    cursor_obj.execute(crear_tabla_familia_modelo)
    cursor_obj.execute(crear_tabla_datasets)
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
    fila = ("InceptionV3","Modelo InceptionV3", 26, "(299,299,3)")
    insert_tabla_familia_modelo(fila)
    fila = ("Sin determinar","Modelo específico", 0, "(0,0,0)")
    insert_tabla_familia_modelo(fila)

    #Tabla datasets
    fila = ("Imagenet","dataset imagenet", 1000)
    insert_tabla_datasets(fila)
    fila = ("Cifar10","dataset cifar10", 10)
    insert_tabla_datasets(fila)
    fila = ("PascalVOC","dataset pascalVOC", 80)
    insert_tabla_datasets(fila)
    fila = ("Específico","dataset específico", 0)
    insert_tabla_datasets(fila)

    # Tabla Modelos, se inserta el modelo de ejemplo de la aplicación
    fila = ("vgg19_imagenet.h5", "Modelo VGG9 de Keras entrenado en Imagenet", 26, "(224,224,3)",1,1,1)
    insert_tabla_modelos(fila)

if not os.path.exists(path):
    crea_base_de_datos(path)
