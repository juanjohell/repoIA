# -*- coding: utf-8 -*-
"""
Created on Thu May 11 13:32:34 2023

@author: jjhb01
"""
import sqlite3
from sqlite3 import Error
import sys
import os

# Obtener la ruta absoluta del archivo actual
archivo_actual = os.path.abspath(__file__)
#Obtener la ruta al nodo padre del archivo actual
nodo_padre = os.path.dirname(archivo_actual)
# Obtener la ruta al abuelo del archivo actual
modelTracker_path = os.path.dirname(nodo_padre)
modelTracker_path = os.path.join(modelTracker_path, 'bbdd')
path = os.path.join(modelTracker_path, 'gestion_modelos.sqlite')
def create_connection(path = path):
    print(path)
    connection = None
    try:
        print(path)
        connection = sqlite3.connect(path)
        print("Conectado a la base de datos SQLite3")
    except Error as e:
        print(f"Error '{e}' conectando a la base de datos")

    return connection


crear_tabla_modelos = '''CREATE TABLE "Modelos" (
    "id_modelo" INTEGER PRIMARY KEY AUTOINCREMENT,
    "id_uso" INTEGER,
    "id_optimizer"	INTEGER,
	"nombre"	TEXT NOT NULL,
	"id_familia"	INTEGER,
	"id_dataset"    INTEGER,
    "descripcion"	TEXT,
	"depth"	INTEGER,
	"input_shape"	TEXT,
	"output_format" TEXT,
	FOREIGN KEY("id_optimizer") REFERENCES "Optimizador"("id_optimizador"),
	FOREIGN KEY("id_familia") REFERENCES "FamiliaModelo"("id_familia"),
	FOREIGN KEY("id_dataset") REFERENCES "Datasets"("id_dataset"),
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

crear_tabla_datasets = '''CREATE TABLE "Datasets" (
	"id_dataset"	INTEGER PRIMARY KEY AUTOINCREMENT,
    "nombre"	TEXT NOT NULL,
    "descripcion"	TEXT,
    "num_items"    INTEGER
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
    print("path insertar_tabla_usos")
    print(modelTracker_path)
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
    sql = '''INSERT OR IGNORE INTO MODELOS (NOMBRE, DESCRIPCION, DEPTH, INPUT_SHAPE, ID_DATASET, ID_USO, ID_FAMILIA)
             VALUES (?, ?, ?, ?, ?, ?, ?)'''
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

def insert_tabla_datasets(params):
    sql = '''INSERT OR IGNORE INTO DATASETS (NOMBRE, DESCRIPCION, NUM_ITEMS) VALUES (?,?,?)'''

    conn = create_connection()
    cursor_obj = conn.cursor()
    cursor_obj.execute(sql,params)
    conn.commit()
    conn.close()
    return cursor_obj.lastrowid

# DEVUELVE UN LISTADO DE TODOS LOS MODELOS
def listado_modelos():
    sql = '''SELECT id_modelo, nombre, descripcion, id_uso FROM Modelos'''
    conn = create_connection()
     # como objetos de tipo Row, lo que permitirá acceder
     # a los campos por nombre en lugar de por índice.
    conn.row_factory = sqlite3.Row
    cursor = conn.execute(sql)
    modelos = cursor.fetchall()
    # Cerrar la conexión a la base de datos
    conn.close()
    return modelos

def listado_datasets():
    sql = '''SELECT id_dataset, nombre, descripcion, num_items FROM Datasets'''
    conn = create_connection()
     # como objetos de tipo Row, lo que permitirá acceder
     # a los campos por nombre en lugar de por índice.
    conn.row_factory = sqlite3.Row
    cursor = conn.execute(sql)
    datasets = cursor.fetchall()
    # Cerrar la conexión a la base de datos
    conn.close()
    return datasets

def listado_usos():
    sql = '''SELECT id_uso, nombre, descripcion FROM Usos'''
    conn = create_connection()
     # como objetos de tipo Row, lo que permitirá acceder
     # a los campos por nombre en lugar de por índice.
    conn.row_factory = sqlite3.Row
    cursor = conn.execute(sql)
    usos = cursor.fetchall()
    # Cerrar la conexión a la base de datos
    conn.close()
    return usos

def listado_familia_modelos():
    sql = '''SELECT id_familia, nombre, descripcion, depth, input_shape FROM FamiliaModelo'''
    conn = create_connection()
     # como objetos de tipo Row, lo que permitirá acceder
     # a los campos por nombre en lugar de por índice.
    conn.row_factory = sqlite3.Row
    cursor = conn.execute(sql)
    familias = cursor.fetchall()
    # Cerrar la conexión a la base de datos
    conn.close()
    return familias
