package com.example.contactossqlite

class Contacto {
    var idcontacto:Int=0
    var nombre:String=""
    var alias:String=""
    var codigo:String=""
    constructor(idcontacto: Int, nombre: String, alias: String, codigo: String) {
        this.idcontacto = idcontacto
        this.nombre = nombre
        this.alias = alias
        this.codigo = codigo
    }
    constructor(){
    }
}