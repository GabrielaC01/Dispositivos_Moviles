package com.example.contactossqlite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contactossqlite.databinding.ActivityMainAgregarBinding
import android.view.View
import android.widget.Toast
import android.content.Intent

class MainAgregar : AppCompatActivity() {
    lateinit var binding : ActivityMainAgregarBinding
    var idcontacto:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_agregar)
        binding = ActivityMainAgregarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var db = Conexion(this)

        intent?.extras?.let { bundle ->
            idcontacto = bundle.getInt("idcontacto", 0)
            bundle.getString("nombre")?.let { binding.txtnombre.setText(it) }
            bundle.getString("alias")?.let { binding.txtalias.setText(it) }
            bundle.getString("codigo")?.let { binding.txtcodigo.setText(it) }
            binding.btnagregar.text = "Modificar"
        }

        binding.btnagregar.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                var nombre:String = binding.txtnombre.text.toString()
                var alias:String = binding.txtalias.text.toString()
                var codigo:String = binding.txtcodigo.text.toString()
                var c = Contacto(idcontacto,nombre,alias,codigo)
                if (nombre.isEmpty() || alias.isEmpty() || codigo.isEmpty()) {
                    Toast.makeText(applicationContext, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                }
                if (binding.btnagregar.text == "Agregar") {
                    if (db.insertar(c) != -1L) {
                        Toast.makeText(applicationContext, "Contacto agregado", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    db.ActualizarContacto(c)
                    Toast.makeText(applicationContext, "Contacto modificado", Toast.LENGTH_SHORT).show()
                }
                binding.txtnombre.setText("")
                binding.txtalias.setText("")
                binding.txtcodigo.setText("")
            }
        })
        binding.btnlistar.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                intent = Intent(applicationContext, MainListar::class.java)
                startActivity(intent)
            }
        })
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }
}