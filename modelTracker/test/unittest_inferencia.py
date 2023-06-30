import unittest
from PIL import Image, ImageDraw, ImageFont
from backend.inferencia import  hex_to_rgb

# PRUEBAS UNITARIAS DE LAS FUNCIONES DE INFERENCIA

class ConversorColorTestCase(unittest.TestCase):
    def test_conversion_colores(self):
        negro_hexadecimal = "#000000"
        blanco_hexadecimal = "FFFFFF"
        self.assertEqual(hex_to_rgb(negro_hexadecimal), (0, 0, 0))
        self.assertEqual(hex_to_rgb(blanco_hexadecimal), (255,255,255))

#Para que las funciones se ejecuten sólo desde aquí
if __name__ == '__main__':
    unittest.main()
