package com.example.puntos

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.puntos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val puntos = mutableListOf<Posiciones>() // Lista para almacenar los puntos
    private var cantPuntos: Int = 0 // Contador de puntos
    private var canAddNode = false // Indicador para permitir agregar nodos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crear un Bitmap para el canvas
        val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true) // Guardamos una copia del bitmap original
        canvas.drawColor(Color.GRAY)
        binding.myImg.setImageBitmap(bitmap)

        // Configuración del Paint para los puntos y las líneas
        val paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 2f // Grosor de línea
            isAntiAlias = true
        }

        // Configuración del Paint para el texto (coordenadas de los puntos)
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 20f // Tamaño del texto
            isAntiAlias = true
        }

        // Obtener las dimensiones del canvas
        val height = canvas.height.toFloat()
        val width = canvas.width.toFloat()


        // Obtener las métricas de pantalla para ajustar el toque en la vista
        val displayMetrics = DisplayMetrics().also {
            windowManager.defaultDisplay.getMetrics(it)
        }

        // Evento del botón "Agregar Punto"
        binding.btnAgregarPunto.setOnClickListener {
            canAddNode = true // Permitir agregar un nodo en el próximo toque
        }

        // Evento de toque en la imagen
        binding.myImg.setOnTouchListener { _, event ->
            if (canAddNode) {
                // Ajuste de las coordenadas según el tamaño de la pantalla
                val screenWith = displayMetrics.widthPixels
                val x = event.x * 500 / screenWith
                val y = event.y * 500 / screenWith

                // Añadir el punto a la lista
                puntos.add(Posiciones(x, y)) // Añadir punto a la lista
                cantPuntos++

                // Limpiar el canvas y redibujar
                val newBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
                val newCanvas = Canvas(newBitmap)
                newCanvas.drawColor(Color.GRAY) // Limpiar el fondo

                // Dibujar los puntos
                for ((index, p) in puntos.withIndex()) {
                    newCanvas.drawCircle(p.x, p.y, 5f, paint)
                    val letra = ('A' + index)  // Convierte 0→A, 1→B, 2→C, ...
                    val mensaje = "$letra (${p.x.toInt()}, ${p.y.toInt()})"
                    newCanvas.drawText(mensaje, p.x + 10, p.y - 10, textPaint)
                }

                // Conectar todos los puntos entre sí como un grafo completo
                if (puntos.size > 1) {
                    for (i in 0 until puntos.size) {
                        for (j in i + 1 until puntos.size) {
                            val p1 = puntos[i]
                            val p2 = puntos[j]
                            newCanvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint)
                        }
                    }
                }

                // Actualizar el ImageView con el nuevo bitmap
                binding.myImg.setImageBitmap(newBitmap)

                // Deshabilitar el modo de agregar puntos
                canAddNode = false
            }
            return@setOnTouchListener true
        }

        binding.btnBuscarRuta.setOnClickListener {
            val poblacion = binding.editPoblacion.text.toString().toIntOrNull() ?: 100  // Valor por defecto si está vacío
            val generaciones = binding.editGeneraciones.text.toString().toIntOrNull() ?: 200  // Valor por defecto si está vacío
            val probabilidad = binding.editProbabilidad.text.toString().toDoubleOrNull() ?: 0.05  // Valor por defecto si está vacío


            if (puntos.size >= 2) {
                val mejor = AlgoritmoGenetico.ejecutar(puntos, poblacion, generaciones, probabilidad)

                val newBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
                val newCanvas = Canvas(newBitmap)
                newCanvas.drawColor(Color.GRAY)

                for ((index, p) in puntos.withIndex()) {
                    newCanvas.drawCircle(p.x, p.y, 5f, paint)
                    val letra = ('A' + index)
                    val mensaje = "$letra (${p.x.toInt()}, ${p.y.toInt()})"
                    newCanvas.drawText(mensaje, p.x + 10, p.y - 10, textPaint)
                }

                for (i in 0 until mejor.cromosoma.size - 1) {
                    val a = puntos[mejor.cromosoma[i]]
                    val b = puntos[mejor.cromosoma[i + 1]]
                    newCanvas.drawLine(a.x, a.y, b.x, b.y, paint)
                }

                val inicio = puntos[mejor.cromosoma.first()]
                val fin = puntos[mejor.cromosoma.last()]
                newCanvas.drawLine(fin.x, fin.y, inicio.x, inicio.y, paint)

                binding.myImg.setImageBitmap(newBitmap)

                val rutaTexto = mejor.cromosoma.joinToString(" -> ") { i ->
                    ('A' + i).toString()
                }

                // Crear el mensaje con la ruta y la distancia
                val mensaje = "Ruta: $rutaTexto\nDistancia: ${mejor.distancia}"

                // Mostrar el mensaje en el TextView
                binding.lblmejordistancia.text = mensaje

            }
        }

    }

}

data class Posiciones (val x: Float, val y: Float)

