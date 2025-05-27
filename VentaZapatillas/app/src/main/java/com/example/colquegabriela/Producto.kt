package com.example.colquegabriela

class Producto {
    var idproducto:Int= 0
    var marca:Int = 0 // 1 = Nike, 2 = Adidas, 3 = Fila
    var talla:Int = 0
    var precio:Int = 0
    var numpares: Int = 0
    constructor(idcontacto: Int, marca: Int, talla: Int, precio: Int, numpares:Int) {
        this.idproducto = idcontacto
        this.marca = marca
        this.talla = talla
        this.precio = precio
        this.numpares = numpares
    }
    constructor(){
    }
}