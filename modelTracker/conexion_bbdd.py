# -*- coding: utf-8 -*-
"""
Created on Thu May 11 13:22:12 2023

@author: jjhb01
"""
from sql.modelo_bbdd import *
import sqlite3
from sqlite3 import Error
import sys

#ruta a la base de datos de aplicación
path = f'{sys.path[0]}/bbdd/gestion_modelos.sqlite'

def create_connection():
    connection = None
    try:
        connection = sqlite3.connect(path)
        print("Conectado a la base de datos SQLite3")
    except Error as e:
        print(f"Error '{e}' conectando a la base de datos")

    return connection

def crea_base_de_datos():
    connection = create_connection()
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
    insert_tabla_usos(connection,fila)
    fila = ("Detección","Modelo diseñado para detección de objetos en imágenes")
    insert_tabla_usos(connection,fila)

    # Tabla de optimizadores
    fila = ("Adam",0.001,1,0.0,0.9,0.999,0.00000001)
    insert_tabla_optimizador(connection,fila)

    # Tabla familia_modelo
    fila = ("VGG19","Modelo vgg19", 26, "(224,224,3)")
    insert_tabla_familia_modelo(connection,fila)

    # Tabla Modelos, se inserta el modelo de ejemplo de la aplicación
    fila = (1,1, "vgg19_imagenet.h5", 1, "Modelo VGG9 de Keras entrenado en Imagenet", 26, "(224,224,3)")
    insert_tabla_modelos(connection,fila)

#crea_base_de_datos()
