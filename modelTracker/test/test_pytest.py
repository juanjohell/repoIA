from backend.inferencia import hex_to_rgb


# Probamos funcion que convierte colores desde
# el formato hexadecimal a rgb para dibujar
# predicciones con PIL u OpenCV
def test_conversion_colores():
    negro_hexadecimal = "#000000"
    blanco_hexadecimal = "FFFFFF"
    assert hex_to_rgb(negro_hexadecimal) == "(0,0,0)"
    assert hex_to_rgb(blanco_hexadecimal) == "(100,100,100)"
