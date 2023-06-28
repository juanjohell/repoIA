import unittest
from unittest.mock import MagicMock, patch
from backend.gestion_modelos import extrae_info_de_modelo


class TestExtraeInfoDeModelo(unittest.TestCase):
    @patch('tensorflow.keras.models.load_model')  # Mock para la función load_model
    def test_extrae_info_de_modelo(self):
        # Simula el nombre del archivo y la configuración
        nombre_fichero = 'model_imagenet_vgg19.h5'
        config = {
            'layers': [
                {'config': {'batch_input_shape': (None, 32, 32, 3)}},
                {'config': {}}
            ]
        }

        # Simula la función load_model y sus resultados
        load_model = MagicMock()
        load_model.return_value.get_config.return_value = config

        # Llama a la función y verifica los resultados
        parametros = extrae_info_de_modelo(nombre_fichero)
        self.assertEqual(parametros['nombre'], nombre_fichero)
        self.assertEqual(parametros['depth'], 2)
        self.assertEqual(parametros['input_shape'], (None, 32, 32, 3))
        # Continúa con las demás aserciones para los otros valores extraídos

        # Verifica las llamadas a load_model
        load_model.assert_called_once_with('UPLOAD_FOLDERmodelo.h5')
        load_model.return_value.get_config.assert_called_once()
