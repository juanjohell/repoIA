from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import time

# Configurar el servicio del controlador de Chrome
s = Service(ChromeDriverManager().install())
# Configurar las opciones del navegador
options = webdriver.ChromeOptions()
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
#Para que las funciones se ejecuten sólo desde aquí
if __name__ == '__main__':
    configuracion_basica()
    abre_aplicacion()
