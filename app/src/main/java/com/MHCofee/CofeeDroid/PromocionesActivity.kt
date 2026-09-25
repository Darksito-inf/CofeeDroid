package com.MHCofee.CofeeDroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class PromocionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promociones)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarPromos)
        toolbar.setNavigationOnClickListener { finish() }
    }
}