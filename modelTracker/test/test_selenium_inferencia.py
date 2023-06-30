from test_selenium_abrir_app import *
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


def inferencia_vgg19():
    # Se pulsa boton realizar inferencia
    link = driver.find_element_by_link_text('Realizar inferencia')
    # Pausar la ejecución durante 1 segundo
    time.sleep(1)
    link.click()

    # SE PULSA EL BOTON DE INFERENCIA CORRESPONDIENTE AL MODELO CON ID = 1
    link = element = driver.find_element_by_xpath(
        '//a[@href="/realizar_inferencia?id_modelo=1" and contains(@class, "btn") and contains(@class, "btn-secondary") and contains(@class, "btn-sm")]')
    time.sleep(1)
    link.click()

    # Seleccionamos color negro
    color_input = driver.find_element_by_id('color-input')
    color_input.clear()  # Limpia cualquier valor preestablecido
    color_input.send_keys('#FFFFFF')  # Envía el valor blanco al campo de entrada
    time.sleep(1)

    # tratamos de realizar inferencia sin adjuntar ninguna
    # imagen y se cancela
    # se pulsa para Presentar al modelo
    link = driver.find_element_by_id('submit-button')
    time.sleep(1)
    link.click()
    link = driver.find_element_by_id('boton_cerrar')
    time.sleep(1)
    link.click()

    # escogemos imagen
    file_input = driver.find_element_by_id('image')
    # Obtener la ruta absoluta del directorio actual del script
    script_dir = os.path.abspath(os.path.dirname(__file__))
    # Retroceder en la estructura de directorios para llegar al nodo raíz de la aplicación
    app_root_dir = os.path.abspath(os.path.join(script_dir, os.pardir))
    # Ruta relativa al archivo dentro del directorio de la aplicación
    file_relative_path = 'modelos/imagenes_test/tigre.jpeg'
    # Obtener la ruta absoluta del archivo
    file_path = os.path.join(app_root_dir, file_relative_path)
    file_input.send_keys(file_path)
    time.sleep(1)

    # finalmente se pulsa para Presentar al modelo
    link = driver.find_element_by_id('submit-button')
    time.sleep(1)
    link.click()

# SECUENCIA DE ACCIONES PARA INFERENCIA CON
# UN MODELO
configuracion_basica()
abre_aplicacion()
inferencia_vgg19()
