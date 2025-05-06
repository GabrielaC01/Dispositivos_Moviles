package com.example.puntos

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.puntos.databinding.ActivityMainBinding
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val puntos = mutableListOf<Posiciones>() // lista para almacenar los nodos
    var cantPuntos : Int = 0 // contador de puntos
    private  var canAddNode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true) // Guardamos una copia del bitmap original
        canvas.drawColor(Color.GRAY)
        binding.myImg.setImageBitmap(bitmap)
        // formato para lo que dibuje
        val paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 2f //grosor de linea
            isAntiAlias = true
        }
        // formato para el texto
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 20f // tamaño del texto
            isAntiAlias = true
        }
        // obtener el tamaño del escenario para dibujar la coordenada
        // dibujar los ejes x e y
        val height = canvas.height.toFloat()
        val width = canvas.width.toFloat()
        drawAxes(canvas, width, height, paint)
        binding.myImg.setImageBitmap(bitmap)

        // obtener metricas de pantalla
        val displayMetrics = DisplayMetrics().also {
            windowManager.defaultDisplay.getMetrics(it)
        }
        // evento del btnagregarnodo
        binding.btnAgregarPunto.setOnClickListener {
            canAddNode = true // Permitir agregar un nodo en el próximo toque en el bitmap
        }
        binding.myImg.setOnTouchListener { _, event ->
            if (canAddNode) {
                val screenWith = displayMetrics.widthPixels
                val x = event.x * 500 / screenWith
                val y = event.y * 500 / screenWith
                puntos.add(Posiciones(x, y)) // añadir punto a la lista
                // aumentar la cantidad de puntos
                cantPuntos++

                // LIMPIAR EL CANVAS Y REDIBUJAR
                val newBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
                val newCanvas = Canvas(newBitmap)
                newCanvas.drawColor(Color.GRAY) // limpiar el fondo
                drawAxes(newCanvas, width, height, paint) // redibujar los ejes

                // dibujar los puntos
                for (p in puntos) {
                    newCanvas.drawCircle(p.x, p.y, 5f, paint)
                    val mensaje: String =
                        "(" + Math.round(p.x * 10.0 / 10.0) + "," + Math.round(p.y * 10.0 / 10.0) + ")"
                    newCanvas.drawText(
                        mensaje,
                        p.x + 10,
                        p.y - 10,
                        textPaint
                    )  // ajuste de posicion del texto
                }

                // dibujar las lineas
                if (puntos.size >= 1) {
                    for (i in 0..puntos.size - 2) {
                        val p0: Posiciones = puntos[i]
                        val p1: Posiciones = puntos[i + 1]
                        newCanvas.drawLine(p0.x, p0.y, p1.x, p1.y, paint) // linea horizontal
                    }
                }
                // Dibujar los puntos
                if (puntos.size > 2) {
                    binding.lblmensaje.setText(puntos.size.toString() + " - " + factorial(puntos.size).toString())
                    for (j in 0..1000) {
                        var t: Float = j / 1000.0F
                        var xx = 0.0
                        var yy = 0.0
                        for (i in 0..puntos.size - 1) {
                            var B = bernstein(puntos.size - 1, i, t)
                            xx = xx + B * puntos[i].x
                            yy = yy + B * puntos[i].y
                        }
                        newCanvas.drawCircle(xx.toFloat(), yy.toFloat(), 2f, paint)
                        //val duration: Duration = 2.4.minutes
                        //delay(duration)
                    }

                }
                binding.myImg.setImageBitmap(newBitmap) // Actualizar el ImageView con el nuevo bitmap
                //binding.lblmensaje.setText("factorial(puntos.size).toString()")
                canAddNode = false
            }
            return@setOnTouchListener true
        }

        /*
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }
    // dibujar los ejes
    private  fun drawAxes( canvas: Canvas, width: Float, height: Float, paint: Paint){
        canvas.drawLine(0f, height/2, width, height/2, paint) // linea horizontal
        canvas.drawLine(width/2, 0f, width/2, height, paint) // linea vertical
    }
    private fun factorial(n:Int):Float{
        var salida:Float = 1.0F
        for (i in 2..n){
            salida = salida * i
        }
        return salida
    }
    private fun combinacion(n:Int,i:Int):Float{
        var salida:Float = 0.0F
        if (i==0 || i==n){
            return 1.0F
        }else{
            salida = factorial(n)/(factorial(i)*factorial(n-i))
        }
        return salida
    }
    private fun bernstein(n:Int,i:Int,t:Float):Float{
        return  combinacion(n,i)*t.pow(i)*(1-t).pow(n-i)
    }

}
