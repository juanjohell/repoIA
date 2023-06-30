import sqlite3
import sys
import json
from sqlite3 import Error, connect, Row

#ruta a la base de datos de aplicación
import os
# Obtener la ruta absoluta del archivo actual
archivo_actual = os.path.abspath(__file__)
# Obtener la ruta al nodo padre del archivo actual
nodo_padre = os.path.dirname(archivo_actual)
# Obtener la ruta al abuelo del archivo actual
modelTracker_path = os.path.dirname(nodo_padre)
# Combinar la ruta absoluta de la carpeta con el nombre del archivo de base de datos
path = os.path.join(modelTracker_path, 'bbdd', 'gestion_modelos.sqlite')

def create_connection():
    connection = None
    try:
        print("connection "+path)
        connection = sqlite3.connect(path)
        print("Conectado a la base de datos SQLite3")
    except Error as e:
        print(f"Error '{e}' conectando a la base de datos")

    return connection

class FamiliaModelo:
    def __init__(self, id_familia=None, nombre=None, descripcion=None, depth=None, input_shape=None):
        self.id_familia = id_familia
        self.nombre = nombre
        self.descripcion = descripcion
        self.depth = depth
        self.input_shape = input_shape

    @staticmethod
    def from_row(row):
        return FamiliaModelo(
            id_familia=row['id_familia'],
            nombre=row['nombre'],
            descripcion=row['descripcion'],
            depth=row['depth'],
            input_shape=row['input_shape']
        )

class Uso:
    def __init__(self, id_uso=None, nombre=None, descripcion=None):
        self.id_uso = id_uso
        self.nombre = nombre
        self.descripcion = descripcion

    @staticmethod
    def from_row(row):
        return Uso(
            id_uso=row['id_uso'],
            nombre=row['nombre'],
            descripcion=row['descripcion']
        )

class Dataset:
    def __init__(self, id_dataset=None, nombre=None, descripcion=None, num_items=None):
        self.id_dataset = id_dataset
        self.nombre = nombre
        self.descripcion = descripcion
        self.num_items = num_items

    @staticmethod
    def from_row(row):
        return Dataset(
            id_dataset=row['id_dataset'],
            nombre=row['nombre'],
            descripcion=row['descripcion'],
            num_items=row['num_items']
        )

class Modelo:
    def __init__(self, id_modelo=None, id_uso=None, id_optimizer=None, nombre=None, id_familia=None, descripcion=None, depth=None, input_shape=None, output_format=None, id_dataset=None):
        self.id_modelo = id_modelo
        self.id_uso = id_uso
        self.id_optimizer = id_optimizer
        self.nombre = nombre
        self.id_familia = id_familia
        self.descripcion = descripcion
        self.depth = depth
        self.input_shape = input_shape
        self.output_format = output_format
        self.id_dataset = id_dataset

    @staticmethod
    def from_row(row):
        return Modelo(
            id_modelo=row['id_modelo'],
            id_uso=row['id_uso'],
            id_optimizer=row['id_optimizer'],
            nombre=row['nombre'],
            id_familia=row['id_familia'],
            descripcion=row['descripcion'],
            depth=row['depth'],
            input_shape=row['input_shape'],
            output_format=['output_format'],
            id_dataset=row['id_dataset']
        )

    @staticmethod
    def get_all():
        conn = create_connection()
        conn.row_factory = sqlite3.Row
        cursor = conn.execute('SELECT * FROM Modelos')
        rows = cursor.fetchall()
        conn.close()

        modelos = []
        for row in rows:
            modelo = Modelo.from_row(row)
            modelos.append(modelo)

        return modelos

    def guardar(self):
        conn = create_connection()
        cursor = conn.cursor()

        if self.id_modelo is None:
            # Insertar nuevo registro
            cursor.execute('INSERT INTO Modelos (id_uso, id_optimizer, nombre, id_familia, descripcion, depth, input_shape, output_format, id_dataset) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)', (self.id_uso, self.id_optimizer, self.nombre, self.id_familia, self.descripcion, self.depth, self.input_shape, self.output_format, self.id_dataset))
            self.id_modelo = cursor.lastrowid
        else:
            # Actualizar registro existente
            cursor.execute('UPDATE Modelos SET id_uso=?, id_optimizer=?, nombre=?, id_familia=?, descripcion=?, depth=?, input_shape=?, output_format=?, id_dataset=? WHERE id_modelo=?', (self.id_uso, self.id_optimizer, self.nombre, self.id_familia, self.descripcion, self.depth, self.input_shape, self.output_format, self.id_dataset, self.id_modelo))

        conn.commit()
        conn.close()

    def devuelve_modelo_por_id(id_modelo):
        conn = create_connection()
        conn.row_factory = sqlite3.Row
        cursor = conn.execute('SELECT * FROM Modelos WHERE id_modelo = ?', (id_modelo,))
        row = cursor.fetchone()
        conn.close()

        if row is not None:
            modelo = Modelo.from_row(row)
            return modelo
        else:
            return None

    def devuelve_dataset(self):
        conn = create_connection()
        conn.row_factory = sqlite3.Row
        cursor = conn.execute('SELECT * FROM Datasets WHERE id_dataset = ?', (self.id_dataset,))
        row = cursor.fetchone()
        conn.close()
        if row is not None:
            dataset = Dataset.from_row(row)
            return dataset
        else:
            return None


    def devuelve_uso(self):
        conn = create_connection()
        conn.row_factory = sqlite3.Row
        cursor = conn.execute('SELECT * FROM Usos WHERE id_uso = ?', (self.id_uso,))
        row = cursor.fetchone()
        conn.close()
        if row is not None:
            uso = Uso.from_row(row)
            return uso
        else:
            return None


    def devuelve_familia(self):
        conn = create_connection()
        conn.row_factory = sqlite3.Row
        cursor = conn.execute('SELECT * FROM FamiliaModelo WHERE id_familia = ?', (self.id_familia,))
        row = cursor.fetchone()
        conn.close()
        if row is not None:
            familia = FamiliaModelo.from_row(row)
            return familia
        else:
            return None

    # Estas funciones de serialización en json son
    # para permitir usar un objeto de esta clase en
    # variables de sesion
    def to_json(self):
        return json.dumps(self.__dict__)

    @staticmethod
    def from_json(json_str):
        model_dict = json.loads(json_str)
        return Modelo(**model_dict)
    def por_nombre(nombre):
        return Modelo(
            nombre = nombre
        )

    def eliminar_modelo(self):
        conn = create_connection()
        cursor = conn.execute('DELETE FROM Modelos WHERE id_modelo = ?', (self.id_modelo,))
        conn.commit()
        conn.close()
