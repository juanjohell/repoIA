# -*- coding: utf-8 -*-
"""
Created on Thu May 11 13:32:34 2023

@author: jjhb01
"""
import sqlite3
from sqlite3 import Error
import sys
import os

# Obtener la ruta absoluta a la carpeta que contiene el archivo de base de datos
folder_path = os.path.abspath('bbdd')
# Combinar la ruta absoluta de la carpeta con el nombre del archivo de base de datos
path = os.path.join(folder_path, 'gestion_modelos.sqlite')

def create_connection():
    connection = None
    try:
        print(path)
        connection = sqlite3.connect(path)
        print("Conectado a la base de datos SQLite3")
    except Error as e:
        print(f"Error '{e}' conectando a la base de datos")

    return connection

def existe_bbdd():
    return os.path.exists(path)

crear_tabla_modelos = '''CREATE TABLE "Modelos" (
    "id_modelo" INTEGER PRIMARY KEY AUTOINCREMENT,
    "id_uso" INTEGER,
    "id_optimizer"	INTEGER,
	"nombre"	TEXT NOT NULL,
	"id_familia"	TEXT,
    "descripcion"	TEXT,
	"depth"	INTEGER,
	"input_shape"	TEXT,
	FOREIGN KEY("id_optimizer") REFERENCES "Optimizador"("id_optimizador"),
	FOREIGN KEY("id_familia") REFERENCES "FamiliaModelo"("id_familia"),
    FOREIGN KEY("id_uso") REFERENCES "Usos"("id_uso")
);'''

crear_tabla_optimizador = '''CREATE TABLE "Optimizador" (
	"id_optimizador" INTEGER PRIMARY KEY AUTOINCREMENT,
    "optimizer_type" TEXT NOT NULL,
	"learning_rate"	REAL,
	"amsgrad" INTEGER,
	"decay"	REAL,
	"b1"	REAL,
	"b2"	REAL,
	"epsilon"	REAL
);'''

crear_tabla_usos = '''CREATE TABLE "Usos" (
	"id_uso"	INTEGER PRIMARY KEY AUTOINCREMENT,
    "nombre"	TEXT NOT NULL,
    "descripcion"	TEXT
);'''

crear_tabla_familia_modelo = '''CREATE TABLE "FamiliaModelo" (
    "id_familia" INTEGER PRIMARY KEY AUTOINCREMENT,
	"nombre"	TEXT NOT NULL,
    "descripcion"	TEXT,
	"depth"	INTEGER,
	"input_shape"	TEXT
);'''
# REALIZA UNA INSERCIÓN EN LA TABLA DE optimizadores Y
# RETORNA EL ID ASIGNADO AL REGISTRO.

def insert_tabla_usos(params):
    sql = '''INSERT OR IGNORE INTO USOS (NOMBRE, DESCRIPCION)
    VALUES (?,?)'''
    conn = create_connection()
    cursor_obj = conn.cursor()
    cursor_obj.execute(sql,params)
    conn.commit()
    conn.close()
    return cursor_obj.lastrowid
    
# REALIZA INERCIÓN EN TABLA DE OPTIMIZADORES

def insert_tabla_optimizador(params):
    sql = '''INSERT OR IGNORE INTO OPTIMIZADOR (OPTIMIZER_TYPE, LEARNING_RATE, AMSGRAD,
    DECAY, B1, B2, EPSILON) VALUES (?,?,?,?,?,?,?)'''
    conn = create_connection()
    cursor_obj = conn.cursor()

    cursor_obj.execute(sql,params)
    conn.commit()
    conn.close()
    return cursor_obj.lastrowid

# REALIZA UNA INSERCIÓN EN LA TABLA DE MODELOS Y
# RETORNA EL ID ASIGNADO AL REGISTRO.

def insert_tabla_modelos(params):
    sql = '''INSERT OR IGNORE INTO MODELOS (NOMBRE, DESCRIPCION, DEPTH, INPUT_SHAPE)
             VALUES (?, ?, ?, ?)'''
    # Conectar a la base de datos SQLite3
    conn = create_connection()
    cursor_obj = conn.cursor()
    cursor_obj.execute(sql, params)
    conn.commit()
    # Cerrar la conexión a la base de datos
    conn.close()
    return cursor_obj.lastrowid

def editar_tabla_modelo(params):
    sql = '''UPDATE MODELOS SET  
    NOMBRE=:nombre,
    DESCRIPCION=:descripcion, 
    DEPTH=:depth, 
    INPUT_SHAPE=:input_shape
    WHERE ID_MODELO=:id_modelo'''
    print(sql)
    # Conectar a la base de datos SQLite3
    conn = create_connection()

    cursor_obj = conn.cursor()
    cursor_obj.execute(sql, params)
    conn.commit()
    # Cerrar la conexión a la base de datos
    conn.close()
    if cursor_obj.rowcount > 0:
        return True  # La operación de actualización fue exitosa
    else:
        return False  # No se modificó ninguna fila, la operación falló

def insert_tabla_familia_modelo(params):
    sql = '''INSERT OR IGNORE INTO FAMILIAMODELO (NOMBRE, DESCRIPCION, DEPTH, INPUT_SHAPE) VALUES (?,?,?,?)'''

    conn = create_connection()
    cursor_obj = conn.cursor()
    cursor_obj.execute(sql,params)
    conn.commit()
    conn.close()
    return cursor_obj.lastrowid

# DEVUELVE UN LISTADO DE TODOS LOS MODELOS
def listado_modelos():
    sql = '''SELECT id_modelo, nombre, descripcion FROM Modelos'''
    conn = create_connection()
     # como objetos de tipo Row, lo que permitirá acceder
     # a los campos por nombre en lugar de por índice.
    conn.row_factory = sqlite3.Row
    cursor = conn.execute(sql)
    modelos = cursor.fetchall()
    # Cerrar la conexión a la base de datos
    conn.close()
    return modelos

