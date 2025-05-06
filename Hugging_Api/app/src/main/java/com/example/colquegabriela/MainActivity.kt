package com.example.colquegabriela

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import android.widget.Toast
import com.example.colquegabriela.databinding.ActivityMainBinding
import android.view.View
import kotlin.random.Random
import java.util.concurrent.ThreadLocalRandom

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnaccion.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val numciudades:Float = binding.txtnumciudades.getText().toString().toFloat()
                val tampoblacion:Float = binding.txttampoblacion.getText().toString().toFloat()
                var probabilidad_mutacion:Float = binding.txtprobabilidadMutacion.getText().toString().toFloat()
                var numgeneraciones:Float = binding.txtnumciudades.getText().toString().toFloat()
                var coordenadas:Array<Punto> = generar_coordenadas(numciudades.toInt())
                var matriz:IntArray =  IntArray(3) { it  }//matriz_puntos(coordenadas)
                //var mm = List<Float>(3) { 0.0 }
                val requestData = RequestData(
                    listOf(
                        numciudades,
                        tampoblacion,
                        probabilidad_mutacion,
                        numgeneraciones
                    ),arreglo_puntos(coordenadas)
                )
                val call = RetrofitAGviajero.aGviajeroApi.predict(requestData)
                call.enqueue(object : Callback<ResponseData> {
                    override fun onResponse(
                        call: Call<ResponseData>,
                        response: Response<ResponseData>
                    ) {
                        if (response.isSuccessful) {
                            val responseData = response.body()
                            responseData?.let {
                                var mensa:String = ""+it.prediction
                                /*for(i in 0..binding.txtnumciudades.getText().toString().toInt()-1){
                                    mensa = mensa + "-"+it.prediction[i]
                                }*/
                                var mensaje = "La mejor ruta es: " + mensa.toString()
                                binding.lblmejordistancia.setText(mensaje)
                                val myToast = Toast.makeText(applicationContext,mensaje,Toast.LENGTH_SHORT)
                                myToast.show()
                            }
                        } else {
                            val myToast = Toast.makeText(applicationContext,"Error1:"+response.body().toString(),Toast.LENGTH_SHORT)
                            myToast.show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                        var mm:String = t.message.toString()
                        val myToast = Toast.makeText(applicationContext,"Error2:"+mm,Toast.LENGTH_SHORT)
                        myToast.show()
                    }
                })
            }
        })
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }
    fun arreglo_puntos(Pts:Array<Punto>):IntArray{
        val salida =  IntArray(2*Pts.size) { it  }
        var contador: Int = 0
        for(i in 0..Pts.size-1){
            salida[contador] = Pts[i].x
            salida[contador+1] = Pts[i].y
            contador = contador + 2
        }
        return salida
    }

    fun generar_coordenadas(n:Int):Array<Punto> {
        val random = Random(47)
        val miscoordenadas = Array(n) {  Punto(0,0)  }
        for (i in (0 until n)) {
            var x:Int= 5 + ThreadLocalRandom.current().nextInt(100)
            var y:Int= 5 + ThreadLocalRandom.current().nextInt(100)
            miscoordenadas[i] = Punto(x,y)
        }
        return miscoordenadas
    }
}