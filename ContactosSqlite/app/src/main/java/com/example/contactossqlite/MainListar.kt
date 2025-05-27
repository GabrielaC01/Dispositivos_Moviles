package com.example.contactossqlite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contactossqlite.databinding.ActivityMainListarBinding
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
        var db = Conexion(this)
        MiGlobal.miscontactos = db.ListaContactos()
        binding.lvmiscontactos.adapter = adapter
        binding.lvmiscontactos.setOnItemClickListener { parent, view, position, id ->
            val contacto = MiGlobal.miscontactos[position]
            Intent(this, MainAgregar::class.java).apply {
                putExtra("idcontacto", contacto.idcontacto)  // ID real de la BD
                putExtra("nombre", contacto.nombre)
                putExtra("alias", contacto.alias)
                putExtra("codigo", contacto.codigo)
            }.also { startActivity(it) }
        }

        binding.lvmiscontactos.setOnItemLongClickListener(OnItemLongClickListener { parent, view, position, id ->
            val idcontacto:Int=MiGlobal.miscontactos[position].idcontacto
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setMessage("Confirma Eliminar este Contacto ? ")
                .setTitle("Eliminar contacto")
                .setPositiveButton("Si") { dialog, which ->
                    db.EliminarContacto(idcontacto)
                    Toast.makeText(applicationContext,"Contacto eliminado",Toast.LENGTH_LONG).show()
                    MiGlobal.miscontactos = db.ListaContactos()
                    binding.lvmiscontactos.adapter = adapter
                }
                .setNegativeButton("No") { dialog, which ->
                    // Do something else.
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            true
        })
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }
}