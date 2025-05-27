package com.example.contactossqlite

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.widget.Toast
import android.content.ContentValues

class Conexion(var context: Context) : SQLiteOpenHelper(context, "ContactoBD", null, 5) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE Contacto ("+
                " idcontacto INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nombre VARCHAR(256)," +
                " alias VARCHAR(256),"+
                " codigo VARCHAR(256))"
        db.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
    fun insertar(c: Contacto): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", c.nombre)
            put("alias", c.alias)
            put("codigo", c.codigo)
        }
        return db.insert("Contacto", null, valores)
    }
    fun ListaContactos() : ArrayList<Contacto>{
        var list:ArrayList<Contacto> = ArrayList<Contacto>()

        val db = this.readableDatabase
        val query = "Select * from Contacto"
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var c = Contacto()
                c.idcontacto = result.getInt(0).toInt()
                c.nombre = result.getString(1).toString()
                c.alias = result.getString(2).toString()
                c.codigo = result.getString(3).toString()
                list.add(c)
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }
    fun EliminarContacto(idcontacto:Int){
        val db = this.writableDatabase
        db.delete("Contacto","idcontacto=?",arrayOf(idcontacto.toString()))
        db.close()
    }
    fun ActualizarContacto(c: Contacto) {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", c.nombre)
            put("alias", c.alias)
            put("codigo", c.codigo)
        }
        // Actualiza SOLO el contacto con el ID recibido
        db.update(
            "Contacto",
            valores,
            "idcontacto=?",  // Condición WHERE
            arrayOf(c.idcontacto.toString())  // Argumentos para el WHERE
        )
        db.close()
    }
}