package com.example.colquegabriela

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues

class Conexion(var context: Context) : SQLiteOpenHelper(context, "BDZAPATILLA,", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE Producto ("+
                " idproducto INTEGER PRIMARY KEY AUTOINCREMENT," +
                " marca INTEGER," +
                " talla INTEGER,"+
                " precio INTEGER," +
                " numpares INTEGER)"
        db.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
    fun InsertarProducto(p: Producto): String {
        val db = writableDatabase
        val values = ContentValues()
        values.put("marca", p.marca)
        values.put("talla", p.talla)
        values.put("precio", p.precio)
        values.put("numpares", p.numpares)

        val res = db.insert("Producto", null, values)

        db.close()
        return if (res == -1L) "Error al insertar" else "Registro insertado"
    }

    fun ListarProductos() : ArrayList<Producto>{
        var list:ArrayList<Producto> = ArrayList<Producto>()

        val db = this.readableDatabase
        val query = "Select * from Producto"
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var p = Producto()
                p.idproducto = result.getInt(0).toInt()
                p.marca = result.getInt(1).toInt()
                p.talla = result.getInt(2).toInt()
                p.precio = result.getInt(3).toInt()
                p.numpares = result.getInt(4).toInt()
                list.add(p)
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }
    fun EliminarProducto(idproducto:Int){
        val db = this.writableDatabase
        db.delete("Producto","idproducto=?",arrayOf(idproducto.toString()))
        db.close()
    }
    fun ModificarProducto(p: Producto): String {
        val db = writableDatabase
        val values = ContentValues()
        values.put("marca", p.marca)
        values.put("talla", p.talla)
        values.put("precio", p.precio)
        values.put("numpares", p.numpares)
        val res = db.update("Producto", values, "idproducto=?", arrayOf(p.idproducto.toString()))
        db.close()
        return if (res == 0) "Error al modificar" else "Modificado correctamente"
    }
}