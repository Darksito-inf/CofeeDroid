package com.MHCofee.CofeeDroid

// Repositorio único en RAM para los insumos de inventario.
object InventarioRepository {

    private var siguienteId = 1
    val insumos = mutableListOf<InsumoInventario>()

    init {
        agregar("Grano Arábica", 12.0)
        agregar("Grano Robusta", 4.0)
    }

    fun agregar(nombre: String, cantidadKg: Double): InsumoInventario {
        val nuevo = InsumoInventario(siguienteId++, nombre, cantidadKg)
        insumos.add(nuevo)
        return nuevo
    }

    fun actualizar(id: Int, nombre: String, cantidadKg: Double) {
        val index = insumos.indexOfFirst { it.id == id }
        if (index != -1) {
            insumos[index] = InsumoInventario(id, nombre, cantidadKg)
        }
    }

    fun eliminar(id: Int) {
        insumos.removeAll { it.id == id }
    }

    fun textoInsumo(i: InsumoInventario): String {
        return "${i.nombre}: ${i.cantidadKg} kg"
    }
}