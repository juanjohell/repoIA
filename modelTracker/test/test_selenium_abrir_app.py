from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import time
import os

# Obtener la ruta absoluta del directorio de la aplicación
app_dir = os.path.abspath(os.path.dirname(__file__))

# Configurar el servicio del controlador de Chrome
s = Service(ChromeDriverManager().install())

# Configurar las opciones del navegador
options = webdriver.ChromeOptions()
# Aquí puedes añadir opciones adicionales si es necesario

# Crear la instancia del navegador
driver = webdriver.Chrome(ChromeDriverManager().install(), options=options)


def configuracion_basica():
    options = webdriver.ChromeOptions()
    options.headless = False  # Cambia a True para ejecutar en modo headless


def abre_aplicacion():
    driver.get('http://127.0.0.1:5000/')
    # Encontrar el enlace con el texto "Comenzar" y hacer clic en él
    link = driver.find_element_by_link_text('Comenzar')
    # Pausar la ejecución durante 1 segundo
    time.sleep(1)
    link.click()


# SECUENCIA DE ACCIONES
#configuracion_basica()
#abre_aplicacion()
