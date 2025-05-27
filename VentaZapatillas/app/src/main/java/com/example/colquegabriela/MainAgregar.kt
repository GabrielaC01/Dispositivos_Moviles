package com.example.colquegabriela

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.colquegabriela.databinding.ActivityMainAgregarBinding
import android.view.View
import android.widget.Toast
import android.content.Intent
import android.widget.AdapterView
import android.widget.ArrayAdapter

class MainAgregar : AppCompatActivity() {
    lateinit var binding: ActivityMainAgregarBinding
    var idproducto: Int = 0
    val tallas = listOf(0, 38, 40, 42)
    val tallasText = listOf("Seleccione talla", "38", "40", "42")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_agregar)
        binding = ActivityMainAgregarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Conexion(this)

        // Configurar los Spinners
        val marcas = arrayOf("Seleccione marca", "Nike", "Adidas", "Fila")
        binding.spnmarca.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, marcas)
        binding.spntalla.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tallasText)

        // Calcular el precio automáticamente según marca y talla
        val calcularPrecio = {
            val marcaPos = binding.spnmarca.selectedItemPosition
            val tallaPos = binding.spntalla.selectedItemPosition
            val talla = tallas[tallaPos]

            if (marcaPos == 0 || talla == 0) {
                binding.txtprecio.text = ""
            } else {
                val precio = obtenerPrecio(talla, marcaPos)
                binding.txtprecio.text = precio.toString()
            }
        }

        binding.spnmarca.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) = calcularPrecio()
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spntalla.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) = calcularPrecio()
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Modo edición
        intent.extras?.let { bundle ->
            idproducto = bundle.getInt("idproducto", 0)
            val marca = bundle.getInt("marca", 1)
            val talla = bundle.getInt("talla", 38)
            val pares = bundle.getInt("pares", 1)
            val precio = bundle.getInt("precio", 0)

            binding.spnmarca.setSelection(marca )
            binding.spntalla.setSelection(tallas.indexOf(talla))
            binding.txtpares.setText(pares.toString())
            binding.txtprecio.text = precio.toString()
            binding.btnagregar.text = "Modificar"
        }

        // Botón agregar/modificar
        binding.btnagregar.setOnClickListener {
            val marcaPos = binding.spnmarca.selectedItemPosition
            val tallaPos = binding.spntalla.selectedItemPosition
            val talla = tallas[tallaPos]
            val pares = binding.txtpares.text.toString().toIntOrNull()
            val precio = binding.txtprecio.text.toString().toIntOrNull()

            if (marcaPos == 0 || talla == 0 || pares == null || pares <= 0 || precio == null) {
                Toast.makeText(this, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val producto = Producto(idproducto, marcaPos, talla, precio, pares)

            val mensaje = if (binding.btnagregar.text == "Agregar") {
                db.InsertarProducto(producto)
            } else {
                db.ModificarProducto(producto)
            }

            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            limpiar()
        }

        // Botón listar
        binding.btnlistar.setOnClickListener {
            startActivity(Intent(this, MainListar::class.java))
        }
    }

    private fun limpiar() {
        binding.spnmarca.setSelection(0)
        binding.spntalla.setSelection(0)
        binding.txtpares.setText("")
        binding.txtprecio.text = ""
        idproducto = 0
        binding.btnagregar.text = "Agregar"
    }

    private fun obtenerPrecio(talla: Int, marca: Int): Int {
        return when (talla) {
            38 -> when (marca) { 1 -> 150; 2 -> 140; 3 -> 80; else -> 0 }
            40 -> when (marca) { 1 -> 160; 2 -> 150; 3 -> 85; else -> 0 }
            42 -> when (marca) { 1 -> 160; 2 -> 150; 3 -> 90; else -> 0 }
            else -> 0
        }
    }
}
