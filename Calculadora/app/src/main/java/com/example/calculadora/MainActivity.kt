package com.example.calculadora

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var numero1: Double = 0.0
    var numero2: Double = 0.0
    var operador: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val botonesNumericos = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        )

        for (boton in botonesNumericos) {
            boton.setOnClickListener {
                binding.textResultado.append(boton.text)
            }
        }

        binding.btnSumar.setOnClickListener { Operacion("+") }
        binding.btnRestar.setOnClickListener { Operacion("-") }
        binding.btnMult.setOnClickListener { Operacion("*") }
        binding.btnDiv.setOnClickListener { Operacion("/") }

        binding.btnIgual.setOnClickListener { calcularResultado() }

    }

    private fun Operacion(op: String) {
        val texto = binding.textResultado.text.toString()
        if (texto.isNotEmpty()) {
            numero1 = texto.toDouble()
            operador = op
            binding.textResultado.text = ""
        }
    }

    private fun calcularResultado() {
        val texto = binding.textResultado.text.toString()
        if (texto.isNotEmpty() && operador.isNotEmpty()) {
            numero2 = texto.toDouble()
            val resultado = when (operador) {
                "+" -> numero1 + numero2
                "-" -> numero1 - numero2
                "*" -> numero1 * numero2
                "/" -> if (numero2 != 0.0) numero1 / numero2 else "Error"
                else -> "Error"
            }
            binding.textResultado.text = resultado.toString()
            operador = ""
        }
    }

}