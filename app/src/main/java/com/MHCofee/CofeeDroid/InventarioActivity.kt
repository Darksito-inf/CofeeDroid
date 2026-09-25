package com.MHCofee.CofeeDroid

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class InventarioActivity : AppCompatActivity() {

    private var idEnEdicion: Int? = null

    private lateinit var inputNombre: EditText
    private lateinit var inputCantidad: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelarEdicion: Button
    private lateinit var listaInsumos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarInventario)
        toolbar.setNavigationOnClickListener { finish() }

        inputNombre = findViewById(R.id.inputNombreInsumo)
        inputCantidad = findViewById(R.id.inputCantidadInsumo)
        btnGuardar = findViewById(R.id.btnGuardarInsumo)
        btnCancelarEdicion = findViewById(R.id.btnCancelarEdicionInsumo)
        listaInsumos = findViewById(R.id.listaInsumos)

        btnCancelarEdicion.setOnClickListener { salirModoEdicion() }

        renderizarLista()

        btnGuardar.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val cantidadTexto = inputCantidad.text.toString().trim()

            if (nombre.isEmpty() || cantidadTexto.isEmpty()) {
                if (nombre.isEmpty()) inputNombre.error = getString(R.string.error_empty_fields)
                if (cantidadTexto.isEmpty()) inputCantidad.error = getString(R.string.error_empty_fields)
                return@setOnClickListener
            }

            val cantidad = cantidadTexto.toDoubleOrNull()
            if (cantidad == null) {
                inputCantidad.error = getString(R.string.error_invalid_number)
                return@setOnClickListener
            }

            val idActual = idEnEdicion
            if (idActual == null) {
                InventarioRepository.agregar(nombre, cantidad)
                Toast.makeText(this, getString(R.string.insumo_saved), Toast.LENGTH_SHORT).show()
            } else {
                InventarioRepository.actualizar(idActual, nombre, cantidad)
                Toast.makeText(this, getString(R.string.insumo_updated), Toast.LENGTH_SHORT).show()
                salirModoEdicion()
            }

            inputNombre.text.clear()
            inputCantidad.text.clear()
            renderizarLista()
        }
    }

    private fun renderizarLista() {
        listaInsumos.removeAllViews()
        InventarioRepository.insumos.forEach { insumo ->
            listaInsumos.addView(crearFilaInsumo(insumo))
        }
    }

    private fun crearFilaInsumo(insumo: InsumoInventario): View {
        val fila = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, 8, 0, 8)
        }

        val texto = TextView(this).apply {
            text = "• ${InventarioRepository.textoInsumo(insumo)}"
            textSize = 14f
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val btnEditar = Button(this).apply {
            text = getString(R.string.btn_editar)
            textSize = 11f
            setOnClickListener { entrarModoEdicion(insumo) }
        }

        val btnEliminar = Button(this).apply {
            text = getString(R.string.btn_eliminar)
            textSize = 11f
            setOnClickListener {
                InventarioRepository.eliminar(insumo.id)
                if (idEnEdicion == insumo.id) salirModoEdicion()
                renderizarLista()
            }
        }

        fila.addView(texto)
        fila.addView(btnEditar)
        fila.addView(btnEliminar)
        return fila
    }

    private fun entrarModoEdicion(insumo: InsumoInventario) {
        idEnEdicion = insumo.id
        inputNombre.setText(insumo.nombre)
        inputCantidad.setText(insumo.cantidadKg.toString())
        btnGuardar.text = getString(R.string.btn_actualizar_insumo)
        btnCancelarEdicion.visibility = View.VISIBLE
    }

    private fun salirModoEdicion() {
        idEnEdicion = null
        inputNombre.text.clear()
        inputCantidad.text.clear()
        btnGuardar.text = getString(R.string.btn_guardar_insumo)
        btnCancelarEdicion.visibility = View.GONE
    }
}