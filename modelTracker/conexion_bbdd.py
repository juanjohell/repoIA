# -*- coding: utf-8 -*-
"""
Created on Thu May 11 13:22:12 2023

@author: jjhb01
"""
from application.app.sql.modelo_bbdd import *
import sqlite3
from sqlite3 import Error
import sys

path = f'{sys.path[0]}/gestion_modelos.sqlite'

def create_connection(path):
    connection = None
    try:
        connection = sqlite3.connect(path)
        print("Conectado a la base de datos SQLite3")
    except Error as e:
        print(f"Error '{e}' conectando a la base de datos")

    return connection

connection = create_connection(path)

# Crear las tablas de la base de datos

cursor_obj = connection.cursor() 
cursor_obj.execute(crear_tabla_usos)
cursor_obj.execute(crear_tabla_optimizer)
cursor_obj.execute(crear_tabla_modelos)
