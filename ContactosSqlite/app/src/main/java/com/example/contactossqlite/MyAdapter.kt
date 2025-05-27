package com.example.contactossqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.content.Intent


class MyAdapter(private val context: Context) : BaseAdapter() {
    private lateinit var lblminombre: TextView
    private lateinit var lblmialias: TextView
    private lateinit var lblmicodigo: TextView
    private lateinit var imgimagen: ImageView
    private lateinit var btnmieditar: Button
    private lateinit var btnmieliminar: Button
    override fun getCount(): Int {
        return MiGlobal.miscontactos.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.misfilas, parent, false)
        lblminombre = convertView.findViewById(R.id.lblminombre)
        lblmialias = convertView.findViewById(R.id.lblmialias)
        lblmicodigo = convertView.findViewById(R.id.lblmicodigo)//"Fecha de Toma: " + arrayList[position].getFecha()
        imgimagen =convertView.findViewById(R.id.imgimagen)
        btnmieditar = convertView.findViewById(R.id.btnmieditar)
        btnmieliminar = convertView.findViewById(R.id.btnmieliminar)
        // Botón Eliminar
        btnmieliminar.setOnClickListener {
            val db = Conexion(context)
            db.EliminarContacto(MiGlobal.miscontactos[position].idcontacto) // Elimina de la BD
            MiGlobal.miscontactos.removeAt(position)   // Elimina de la lista global
            notifyDataSetChanged()                  // Actualiza el ListView
            Toast.makeText(context, "Contacto eliminado", Toast.LENGTH_SHORT).show()
        }
        // Botón Editar (redirige a MainAgregar con los datos del contacto)
        btnmieditar.setOnClickListener {
            val intent = Intent(context, MainAgregar::class.java).apply {
                putExtra("idcontacto", MiGlobal.miscontactos[position].idcontacto)
                putExtra("nombre", MiGlobal.miscontactos[position].nombre)
                putExtra("alias",MiGlobal.miscontactos[position].alias)
                putExtra("codigo", MiGlobal.miscontactos[position].codigo)
            }
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

        }
        lblminombre.setText(MiGlobal.miscontactos[position].nombre)
        lblmialias.setText(MiGlobal.miscontactos[position].alias)
        lblmicodigo.setText(MiGlobal.miscontactos[position].codigo)
        val primeraletra: String = lblminombre.getText().toString().toLowerCase().substring(0, 1)
        val idimagen = context.resources.getIdentifier(primeraletra, "drawable", context.packageName)
        imgimagen.setImageResource(idimagen)
        return convertView
    }
}