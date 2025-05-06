from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import numpy as np
import random
from typing import List, Tuple

class InputData(BaseModel):
    # Recibimos los puntos como una lista de tuplas (x, y)
    points: List[Tuple[float, float]]  # Lista de puntos (x, y)
    poblacion: int  # Población del algoritmo genético
    probabilidad: float  # Probabilidad de cruce
    generaciones: int  # Número de generaciones

app = FastAPI()

# ------------- algoritmo genetico -------------
# Función para generar una población inicial aleatoria
def generar_poblacion(num_individuos, num_ciudades):
    poblacion = []
    for _ in range(num_individuos):
        individuo = list(range(num_ciudades))
        random.shuffle(individuo)
        poblacion.append(individuo)
    return poblacion

# Función para calcular la distancia entre dos puntos (x, y)
def calcular_distancia(p1: Tuple[float, float], p2: Tuple[float, float]) -> float:
    return np.sqrt((p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2)

# Función para evaluar la aptitud de un individuo (distancia total del recorrido)
def calcular_aptitud(individuo, coordenadas):
    distancia_total = 0
    for i in range(len(individuo) - 1):
        ciudad_actual = individuo[i]
        siguiente_ciudad = individuo[i + 1]
        distancia_total += calcular_distancia(coordenadas[ciudad_actual], coordenadas[siguiente_ciudad])
    
    # Cerrar el ciclo (último punto con el primero)
    distancia_total += calcular_distancia(coordenadas[individuo[-1]], coordenadas[individuo[0]])

    return distancia_total

# Función para seleccionar individuos para la reproducción (torneo binario)
def seleccion_torneo(poblacion, coordenadas):
    seleccionados = []
    for _ in range(len(poblacion)):
        torneo = random.sample(poblacion, 2)
        aptitud_torneo = [
            calcular_aptitud(individuo, coordenadas) for individuo in torneo
        ]
        seleccionado = torneo[aptitud_torneo.index(min(aptitud_torneo))]
        seleccionados.append(seleccionado)
    return seleccionados

# Función para realizar el cruce de dos padres para producir un hijo
def cruzar(padre1, padre2):
    punto_cruce = random.randint(1, len(padre1) - 1)
    hijo = padre1[:punto_cruce] + [gen for gen in padre2 if gen not in padre1[:punto_cruce]]
    return hijo

# Función para realizar la mutación de un hijo
def mutar(individuo):
    i, j = random.sample(range(len(individuo)), 2)
    individuo[i], individuo[j] = individuo[j], individuo[i]

# Función principal para ejecutar el algoritmo genético
def algoritmo_genetico(coordenadas, poblacion_size, generaciones, probabilidad):
    num_ciudades = len(coordenadas)
    poblacion = generar_poblacion(poblacion_size, num_ciudades)

    for _ in range(generaciones):
        # Selección
        seleccionados = seleccion_torneo(poblacion, coordenadas)

        # Cruce y mutación
        nueva_poblacion = []
        for i in range(0, len(seleccionados), 2):
            padre1, padre2 = seleccionados[i], seleccionados[i + 1]
            hijo1 = cruzar(padre1, padre2)
            hijo2 = cruzar(padre2, padre1)
            if random.random() < probabilidad:
                mutar(hijo1)
                mutar(hijo2)
            nueva_poblacion.extend([hijo1, hijo2])

        poblacion = nueva_poblacion

    # Seleccionar el mejor individuo
    mejor_individuo = min(poblacion, key=lambda ind: calcular_aptitud(ind, coordenadas))
    return mejor_individuo

@app.post("/predict")
async def predict(input_data: InputData):
    # Obtener los puntos del frontend
    coordenadas = input_data.points
    poblacion_size = input_data.poblacion
    generaciones = input_data.generaciones
    probabilidad = input_data.probabilidad

    # Ejecutar el algoritmo genético
    mejor_ruta = algoritmo_genetico(coordenadas, poblacion_size, generaciones, probabilidad)

    # Devolver los índices de la mejor ruta encontrada
    return {"prediction": mejor_ruta}

