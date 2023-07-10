import unittest
from unittest.mock import MagicMock, patch
from gestion_modelos import extrae_info_de_modelo

class TestExtraeInfoDeModelo(unittest.TestCase):
    def test_extrae_info_de_modelo(self):
        # Crear un MagicMock para el método get_config()
        mock_get_config = MagicMock(return_value={'layers': [{'config': {'batch_input_shape': [None, 32, 32, 3]}}]})

        # Llamar a la función con el MagicMock
        with patch('gestion_modelos.load_model') as mock_load_model:
            mock_load_model.return_value.get_config = mock_get_config
            parametros = extrae_info_de_modelo('modelo.h5')

        # Verificar que el MagicMock se haya llamado correctamente
        mock_load_model.assert_called_once_with('ruta/a/modelo.h5')
        mock_get_config.assert_called_once()

        # Verificar los valores de los parámetros
        self.assertEqual(parametros['depth'], 1)
        self.assertEqual(parametros['input_shape'], '(32,32,3)')
        # Verificar otros parámetros si es necesario

if __name__ == '__main__':
    unittest.main()
