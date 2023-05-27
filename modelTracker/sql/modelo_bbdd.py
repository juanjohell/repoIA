# -*- coding: utf-8 -*-
"""
Created on Thu May 11 13:32:34 2023

@author: jjhb01
"""

crear_tabla_modelos = '''CREATE TABLE "Modelos" (
    "id_modelo" INTEGER NOT NULL UNIQUE,
    "id_uso" INTEGER NOT NULL,
    "id_optimizer"	INTEGER NOT NULL,
	"nombre"	TEXT NOT NULL,
	"id_familia"	TEXT NOT NULL,
    "descripcion"	TEXT,
	"depth"	INTEGER,
	"input_shape"	TEXT,
	PRIMARY KEY("id_modelo"),
	FOREIGN KEY("id_optimizer") REFERENCES "Optimizer"("id_optimizador"),
	FOREIGN KEY("id_familia") REFERENCES "FamiliaModelo"("id_familia"),
    FOREIGN KEY("id_uso") REFERENCES "Usos"("id_uso")
);'''

crear_tabla_optimizador = '''CREATE TABLE "Optimizador" (
	"id_optimizador"	INTEGER NOT NULL UNIQUE,
    "optimizer_type"	TEXT NOT NULL,
	"learning_rate"	REAL NOT NULL,
	"amsgrad"	INTEGER NOT NULL,
	"decay"	REAL,
	"b1"	REAL,
	"b2"	REAL,
	"epsilon"	REAL,
	PRIMARY KEY("id_optimizer"),
	UNIQUE("learning_rate","amsgrad","decay","b1","b2","epsilon")
);'''

crear_tabla_usos = '''CREATE TABLE "Usos" (
	"id_uso"	INTEGER NOT NULL UNIQUE,
    "nombre"	TEXT NOT NULL,
    "descripcion"	TEXT,
	PRIMARY KEY("id_uso")
);'''

crear_tabla_familia_modelo = '''CREATE TABLE "FamiliaModelo" (
    "id_familia" INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT NOT NULL,
    "descripcion"	TEXT,
	"depth"	INTEGER,
	"input_shape"	TEXT,
	PRIMARY KEY("id_familia")
);'''
# REALIZA UNA INSERCIÓN EN LA TABLA DE optimizadores Y
# RETORNA EL ID ASIGNADO AL REGISTRO.

def insert_tabla_usos(conn, params):
    sql = '''INSERT OR IGNORE INTO USOS (NOMBRE, DESCRIPCION)
    VALUES (?,?)'''
    
    cursor_obj = conn.cursor()

    cursor_obj.execute(sql,params)
    conn.commit()

    return cursor_obj.lastrowid
    
# REALIZA INERCIÓN EN TABLA DE OPTIMIZADORES

def insert_tabla_optimizers(conn, params):
    sql = '''INSERT OR IGNORE INTO OPTIMIZER (OPTIMIZER_TYPE, LEARNING_RATE, AMSGRAD,
    DECAY, B1, B2, EPSILON) VALUES (?,?,?,?,?,?,?)'''
    
    cursor_obj = conn.cursor()

    cursor_obj.execute(sql,params)
    conn.commit()

    return cursor_obj.lastrowid

# REALIZA UNA INSERCIÓN EN LA TABLA DE MODELOS Y
# RETORNA EL ID ASIGNADO AL REGISTRO.

def insert_tabla_modelos(conn, params):
    sql = '''INSERT OR IGNORE INTO MODELOS (ID_USO, ID_OPTIMIZER, NOMBRE, FAMILIA, 
    DESCRIPCION, DEPTH, INPUT_SHAPE) VALUES (?,?,?,?,?,?,?)'''
    
    cursor_obj = conn.cursor()

    cursor_obj.execute(sql,params)
    conn.commit()

    return cursor_obj.lastrowid
