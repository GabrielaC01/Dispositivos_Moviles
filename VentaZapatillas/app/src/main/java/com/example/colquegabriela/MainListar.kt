package com.example.colquegabriela

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.colquegabriela.databinding.ActivityMainListarBinding
import android.content.Intent
import android.app.AlertDialog
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Toast

class MainListar : AppCompatActivity() {
    lateinit var binding: ActivityMainListarBinding
    var adapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_listar)
        binding = ActivityMainListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MyAdapter(applicationContext)
        val db = Conexion(this)
        MiGlobal.misproductos = db.ListarProductos()
        binding.lvmisproductos.adapter = adapter

        binding.lvmisproductos.setOnItemClickListener { _, _, position, _ ->
            val producto = MiGlobal.misproductos[position]
            Intent(this, MainAgregar::class.java).apply {
                putExtra("idproducto", producto.idproducto)
                putExtra("marca", producto.marca)
                putExtra("talla", producto.talla)
                putExtra("precio", producto.precio)
                putExtra("pares", producto.numpares)
            }.also { startActivity(it) }
        }

        binding.lvmisproductos.setOnItemLongClickListener { _, _, position, _ ->
            val producto = MiGlobal.misproductos[position]
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar producto")
                .setMessage("¿Deseas eliminar esta venta?")
                .setPositiveButton("Sí") { _, _ ->
                    db.EliminarProducto(producto.idproducto)
                    MiGlobal.misproductos.removeAt(position)
                    adapter?.notifyDataSetChanged()
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
            true
        }
    }
}

