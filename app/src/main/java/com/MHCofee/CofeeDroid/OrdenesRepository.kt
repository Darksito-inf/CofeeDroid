package com.MHCofee.CofeeDroid

// Repositorio único en RAM compartido por Admin y Usuario.
object OrdenesRepository {

    private var siguienteId = 1
    val pedidos = mutableListOf<Pedido>()

    init {
        agregar("Latte", "Mesa", "4")
        agregar("Espresso", "Para llevar", null)
    }

    fun agregar(cafe: String, tipoPedido: String, mesa: String?): Pedido {
        val nuevo = Pedido(siguienteId++, cafe, tipoPedido, mesa)
        pedidos.add(nuevo)
        return nuevo
    }

    fun actualizar(id: Int, cafe: String, tipoPedido: String, mesa: String?) {
        val index = pedidos.indexOfFirst { it.id == id }
        if (index != -1) {
            pedidos[index] = Pedido(id, cafe, tipoPedido, mesa)
        }
    }

    fun eliminar(id: Int) {
        pedidos.removeAll { it.id == id }
    }

    fun textoPedido(p: Pedido): String {
        return if (p.tipoPedido == "Mesa") "${p.cafe} (Mesa ${p.mesa})" else "${p.cafe} (Para llevar)"
    }
}