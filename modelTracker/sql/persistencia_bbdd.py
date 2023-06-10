import sqlite3
import sys
import json

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

class Modelo:
    def __init__(self, id_modelo=None, id_uso=None, id_optimizer=None, nombre=None, id_familia=None, descripcion=None, depth=None, input_shape=None):
        self.id_modelo = id_modelo
        self.id_uso = id_uso
        self.id_optimizer = id_optimizer
        self.nombre = nombre
        self.id_familia = id_familia
        self.descripcion = descripcion
        self.depth = depth
        self.input_shape = input_shape

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
            input_shape=row['input_shape']
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
            cursor.execute('INSERT INTO Modelos (id_uso, id_optimizer, nombre, id_familia, descripcion, depth, input_shape) VALUES (?, ?, ?, ?, ?, ?, ?)', (self.id_uso, self.id_optimizer, self.nombre, self.id_familia, self.descripcion, self.depth, self.input_shape))
            self.id_modelo = cursor.lastrowid
        else:
            # Actualizar registro existente
            cursor.execute('UPDATE Modelos SET id_uso=?, id_optimizer=?, nombre=?, id_familia=?, descripcion=?, depth=?, input_shape=? WHERE id_modelo=?', (self.id_uso, self.id_optimizer, self.nombre, self.id_familia, self.descripcion, self.depth, self.input_shape, self.id_modelo))

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
