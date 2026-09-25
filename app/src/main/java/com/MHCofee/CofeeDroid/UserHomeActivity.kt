package com.MHCofee.CofeeDroid

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

class UserHomeActivity : AppCompatActivity() {

    private lateinit var spinnerCafe: Spinner
    private lateinit var radioGroupTipoPedido: RadioGroup
    private lateinit var spinnerMesa: Spinner
    private lateinit var listaPedidos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarUser)
        setSupportActionBar(toolbar)

        spinnerCafe = findViewById(R.id.spinnerCafeUser)
        radioGroupTipoPedido = findViewById(R.id.radioGroupTipoPedidoUser)
        spinnerMesa = findViewById(R.id.spinnerMesaUser)
        val btnRealizarPedido = findViewById<Button>(R.id.btnRealizarPedido)
        listaPedidos = findViewById(R.id.listaPedidosUser)

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
            spinnerMesa.visibility = if (checkedId == R.id.radioMesaUser) View.VISIBLE else View.GONE
        }

        renderizarLista()

        btnRealizarPedido.setOnClickListener {
            val cafeSeleccionado = spinnerCafe.selectedItem?.toString().orEmpty()
            val esParaMesa = radioGroupTipoPedido.checkedRadioButtonId == R.id.radioMesaUser
            val mesa = if (esParaMesa) spinnerMesa.selectedItem?.toString() else null

            OrdenesRepository.agregar(cafeSeleccionado, if (esParaMesa) "Mesa" else "Para llevar", mesa)

            renderizarLista()
            Toast.makeText(this, getString(R.string.order_saved), Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderizarLista() {
        listaPedidos.removeAllViews()
        OrdenesRepository.pedidos.forEach { pedido ->
            val item = TextView(this).apply {
                text = "• ${OrdenesRepository.textoPedido(pedido)}"
                textSize = 14f
                setPadding(0, 8, 0, 8)
            }
            listaPedidos.addView(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 1, 0, getString(R.string.menu_cerrar_sesion))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}