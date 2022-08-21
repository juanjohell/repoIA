# -*- coding: utf-8 -*-
"""
Created on Sat Aug 20 18:57:12 2022

@author: juanjo
"""

FICHERO_A_DIVIDIR='coco\\labels\\etiquetas_train.txt'

with open(FICHERO_A_DIVIDIR) as bigfile:
    for lineno, line in enumerate(bigfile):
            #obtener nombre fichero
            una_linea_filename = line.split('. ',1)[0]+".txt"
            contenido = line.split('. ',1)[1]
            smallfile = open(una_linea_filename, "w")
            smallfile.write(contenido)
            smallfile.close()
    
        