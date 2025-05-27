package com.example.colquegabriela

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
    override fun getCount(): Int {
        return MiGlobal.misproductos.size
    }

    override fun getItem(position: Int): Any {
        return MiGlobal.misproductos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.misfilas, parent, false)

        val lbltalla = view.findViewById<TextView>(R.id.txtmitalla)
        val lblprecio = view.findViewById<TextView>(R.id.txtprecio)
        val lblpares = view.findViewById<TextView>(R.id.txtnumpares)
        val lbltotal = view.findViewById<TextView>(R.id.txtventatotal)
        val imgimagen = view.findViewById<ImageView>(R.id.imgmilogo)

        val producto = MiGlobal.misproductos[position]

        val marcaTexto = when (producto.marca) {
            1 -> "Nike"
            2 -> "Adidas"
            3 -> "Fila"
            else -> "Desconocido"
        }

        lbltalla.text = "Talla ${producto.talla}"
        lblprecio.text = "Precio: S/. ${producto.precio}"
        lblpares.text = "${producto.numpares} pares"
        lbltotal.text = "Total: S/. ${producto.precio * producto.numpares}"

        val imagenId = when (producto.marca) {
            1 -> context.resources.getIdentifier("nike", "drawable", context.packageName)
            2 -> context.resources.getIdentifier("adidas", "drawable", context.packageName)
            3 -> context.resources.getIdentifier("fila", "drawable", context.packageName)
            else -> android.R.drawable.ic_menu_gallery
        }
        imgimagen.setImageResource(imagenId)

        return view
    }
}
