package com.MHCofee.CofeeDroid

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class OrdenEspecialActivity : AppCompatActivity() {

    private var idEnEdicion: Int? = null

    private lateinit var spinnerCafe: Spinner
    private lateinit var radioGroupTipoPedido: RadioGroup
    private lateinit var spinnerMesa: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelarEdicion: Button
    private lateinit var listaOrdenes: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orden_especial)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarOrden)
        toolbar.setNavigationOnClickListener { finish() }

        spinnerCafe = findViewById(R.id.spinnerCafe)
        radioGroupTipoPedido = findViewById(R.id.radioGroupTipoPedido)
        spinnerMesa = findViewById(R.id.spinnerMesa)
        btnGuardar = findViewById(R.id.btnGuardarOrden)
        btnCancelarEdicion = findViewById(R.id.btnCancelarEdicion)
        listaOrdenes = findViewById(R.id.listaOrdenes)

        val cafeAdapter = ArrayAdapter.createFromResource(
            this, R.array.coffee_menu_names, android.R.layout.simple_spinner_item
        )
        cafeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCafe.adapter = cafeAdapter

        val numerosMesa = (1..15).map { it.toString() }
        val mesaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numerosMesa)
        mesaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMesa.adapter = mesaAdapter

        radioGroupTipoPedido.setOnCheckedChangeListener { _, checkedId ->
            spinnerMesa.visibility = if (checkedId == R.id.radioMesa) View.VISIBLE else View.GONE
        }

        btnCancelarEdicion.setOnClickListener { salirModoEdicion() }

        renderizarLista()

        btnGuardar.setOnClickListener {
            val cafeSeleccionado = spinnerCafe.selectedItem?.toString().orEmpty()
            val esParaMesa = radioGroupTipoPedido.checkedRadioButtonId == R.id.radioMesa
            val mesa = if (esParaMesa) spinnerMesa.selectedItem?.toString() else null

            val idActual = idEnEdicion
            if (idActual == null) {
                OrdenesRepository.agregar(cafeSeleccionado, if (esParaMesa) "Mesa" else "Para llevar", mesa)
                Toast.makeText(this, getString(R.string.order_saved), Toast.LENGTH_SHORT).show()
            } else {
                OrdenesRepository.actualizar(idActual, cafeSeleccionado, if (esParaMesa) "Mesa" else "Para llevar", mesa)
                Toast.makeText(this, getString(R.string.order_updated), Toast.LENGTH_SHORT).show()
                salirModoEdicion()
            }
            renderizarLista()
        }
    }

    private fun renderizarLista() {
        listaOrdenes.removeAllViews()
        OrdenesRepository.pedidos.forEach { pedido ->
            listaOrdenes.addView(crearFilaPedido(pedido))
        }
    }

    private fun crearFilaPedido(pedido: Pedido): View {
        val fila = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, 8, 0, 8)
        }

        val texto = TextView(this).apply {
            text = "• ${OrdenesRepository.textoPedido(pedido)}"
            textSize = 14f
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val btnEditar = Button(this).apply {
            text = getString(R.string.btn_editar)
            textSize = 11f
            setOnClickListener { entrarModoEdicion(pedido) }
        }

        val btnEliminar = Button(this).apply {
            text = getString(R.string.btn_eliminar)
            textSize = 11f
            setOnClickListener {
                OrdenesRepository.eliminar(pedido.id)
                if (idEnEdicion == pedido.id) salirModoEdicion()
                renderizarLista()
            }
        }

        fila.addView(texto)
        fila.addView(btnEditar)
        fila.addView(btnEliminar)
        return fila
    }

    private fun entrarModoEdicion(pedido: Pedido) {
        idEnEdicion = pedido.id
        seleccionarEnSpinner(spinnerCafe, pedido.cafe)

        if (pedido.tipoPedido == "Mesa") {
            radioGroupTipoPedido.check(R.id.radioMesa)
            spinnerMesa.visibility = View.VISIBLE
            seleccionarEnSpinner(spinnerMesa, pedido.mesa)
        } else {
            radioGroupTipoPedido.check(R.id.radioLlevar)
            spinnerMesa.visibility = View.GONE
        }

        btnGuardar.text = getString(R.string.btn_actualizar_pedido)
        btnCancelarEdicion.visibility = View.VISIBLE
    }

    private fun salirModoEdicion() {
        idEnEdicion = null
        btnGuardar.text = getString(R.string.btn_guardar_orden)
        btnCancelarEdicion.visibility = View.GONE
    }

    private fun seleccionarEnSpinner(spinner: Spinner, valor: String?) {
        if (valor == null) return
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == valor) {
                spinner.setSelection(i)
                break
            }
        }
    }
}