package com.example.puntos

import kotlin.math.sqrt
import kotlin.random.Random

data class Individuo(val cromosoma: MutableList<Int>, var distancia: Float = 0f) {
    fun calcularDistancia(puntos: List<Posiciones>): Float {
        var total = 0f
        for (i in 0 until cromosoma.size - 1) {
            total += distanciaEntre(puntos[cromosoma[i]], puntos[cromosoma[i + 1]])
        }
        total += distanciaEntre(puntos[cromosoma.last()], puntos[cromosoma.first()])
        distancia = total
        return total
    }

    private fun distanciaEntre(a: Posiciones, b: Posiciones): Float {
        return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
    }
}

object AlgoritmoGenetico {
    fun ejecutar(
        puntos: List<Posiciones>,
        tamañoPoblacion: Int,
        generaciones: Int,
        probMutacion: Double
    ): Individuo {
        var poblacion = List(tamañoPoblacion) {
            val cromosoma = (puntos.indices).shuffled().toMutableList()
            Individuo(cromosoma).apply { calcularDistancia(puntos) }
        }

        repeat(generaciones) {
            poblacion = seleccionTorneo(poblacion).chunked(2).flatMap { pareja ->
                val padre1 = pareja[0]
                val padre2 = if (pareja.size > 1) pareja[1] else pareja[0]

                val hijo1 = mutar(cruzar(padre1, padre2), probMutacion).apply { calcularDistancia(puntos) }
                val hijo2 = mutar(cruzar(padre2, padre1), probMutacion).apply { calcularDistancia(puntos) }

                listOf(hijo1, hijo2)
            }

        }

        return poblacion.minByOrNull { it.distancia }!!
    }

    private fun cruzar(p1: Individuo, p2: Individuo): Individuo {
        val start = Random.nextInt(p1.cromosoma.size)
        val end = Random.nextInt(start, p1.cromosoma.size)
        val hijo = mutableListOf<Int>()
        val sub = p1.cromosoma.subList(start, end)
        hijo.addAll(sub)
        for (gen in p2.cromosoma) {
            if (!hijo.contains(gen)) hijo.add(gen)
        }
        return Individuo(hijo)
    }

    private fun mutar(ind: Individuo, prob: Double): Individuo {
        if (Math.random() < prob) {
            val i = Random.nextInt(ind.cromosoma.size)
            var j = Random.nextInt(ind.cromosoma.size)
            while (i == j) j = Random.nextInt(ind.cromosoma.size)
            ind.cromosoma[i] = ind.cromosoma[j].also { ind.cromosoma[j] = ind.cromosoma[i] }
        }
        return ind
    }

    private fun seleccionTorneo(poblacion: List<Individuo>): List<Individuo> {
        return List(poblacion.size) {
            val a = poblacion.random()
            val b = poblacion.random()
            if (a.distancia < b.distancia) a else b
        }
    }
}
