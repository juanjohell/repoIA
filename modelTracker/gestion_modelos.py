# AUTOR: juan José Hellín Belmonte
# FUNCIONALIDAD:  funciones de gestión de los modelos neuronales

# Función para extraccion de metadatos de un modelo h5 pasado por parámetro
# Los ficheros deben estar en la subcarpeta modelos del proyecto.

def extrae_info_de_modelo(nombre_fichero):
    p = Path(sys.path[0])
    filepath = Path(p.parent.joinpath('modelos')
    ruta_completa = filepath.joinpath(nombre_fichero)
    if not filepath.exists():
        return f'Error: el archivo {nombre_fichero} no existe'
