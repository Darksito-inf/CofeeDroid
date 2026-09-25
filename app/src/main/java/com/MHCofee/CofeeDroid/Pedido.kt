package com.MHCofee.CofeeDroid

data class Pedido(
    val id: Int,
    val cafe: String,
    val tipoPedido: String, // "Mesa" o "Para llevar"
    val mesa: String?       // número si es Mesa, null si es Para llevar
)