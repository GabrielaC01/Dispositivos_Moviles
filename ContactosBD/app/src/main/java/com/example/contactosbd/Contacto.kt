package com.example.contactosbd

class Contacto {
    var nombre:String = ""
    var alias:String = ""
    var clave:String = ""
    var codigo:String = ""
    var url:String = ""
    var respuesta:String = ""

    constructor(nombre: String, alias: String, clave: String, codigo: String,url:String,respuesta:String) {
        this.nombre = nombre
        this.alias = alias
        this.clave = clave
        this.codigo = codigo
        this.url = url
        this.respuesta = respuesta
    }
    //constructor(){}
}
