import unittest

from app import app
from unittest.mock import patch, MagicMock
from flask import Flask
from flask.testing import FlaskClient
from gestion_modelos import extrae_info_de_modelo
import sys
import os

nombre_fichero = 'vgg19_imagenet.h5'

#ruta al recurso de los modelos

ruta_actual = os.path.abspath(os.path.dirname(__file__))
ruta_padre = os.path.dirname(ruta_actual)
ruta_al_archivo = os.path.join(ruta_padre, 'modelos', nombre_fichero)
ruta_al_archivo = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'modelos', nombre_fichero))
ruta_al_archivo = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), 'modelos', nombre_fichero)

class AppTestCase(unittest.TestCase):
    def setUp(self):
        app.testing = True
        self.app = app.test_client()

    # Utilizamos un mock para no hacer inserciones reales a la base de
    # datos, sólo estamos probando la navegabilidad y las respuestas
    @patch('app.insert_tabla_modelos')  # Patch a la función insert_tabla_modelos
    def test_insertar_modelo(self, mock_insert_tabla_modelos):
        # Configurar el retorno simulado de la función simulada
        mock_insert_tabla_modelos.return_value = 1

        with app.app_context():
            # Realizar una solicitud POST a la ruta '/insertar_modelo'
            response = self.app.post('/insertar_modelo', data={
                'nombre': 'Modelo_de_prueba',
                'depth': '32',
                'input_shape': '(224, 224, 3)'
            })

            # Verificar el código de respuesta HTTP
            self.assertEqual(response.status_code, 200)

            # Verificar el contenido de la respuesta
            self.assertIn('Modelo insertado correctamente', response.data.decode())

        # Verificar que la función simulada se haya llamado una vez
        mock_insert_tabla_modelos.assert_called_once()

    ## TEST PARA PROBAR LA EXTRACCION DE LA INFORMACION DE
    ## DE CONFIGURACION DE UN FICHERO H5 KERAS UTILIZANDO UN MOCK

    def test_extraer_config_modelo(self):
        # Crear un objeto simulado para el archivo
        archivo_simulado = MagicMock()

        # Configurar el comportamiento simulado del archivo
        archivo_simulado.filename = nombre_fichero

        # Simular la función extrae_info_de_modelo
        with patch('gestion_modelos.extrae_info_de_modelo') as mock_extrae_info_de_modelo:
            # Configurar el retorno simulado de la función simulada
            mock_extrae_info_de_modelo.return_value = {'nombre': vgg19_imagenet.h5}

            # Simular la solicitud POST con el archivo simulado
            response = self.app.post('/extraer_config_modelo', data={'file': archivo_simulado})

            # Verificar el código de respuesta HTTP
            self.assertEqual(response.status_code, 200)

            # Verificar el tipo de contenido de la respuesta
            self.assertEqual(response.content_type, 'application/json')

            # Verificar los datos JSON devueltos
            expected_data = {'nombre': nombre_fichero}
            actual_data = response.get_json()
            self.assertEqual(actual_data, expected_data)

if __name__ == '__main__':
    unittest.main()
