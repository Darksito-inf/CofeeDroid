package com.MHCofee.CofeeDroid

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val usuarios = mapOf(
        "admin" to "123456",
        "admin@admin.com" to "123456",
        "usuario1" to "password123",
        "test@correo.com" to "abc12345"
    )

    private val cuentasAdmin = setOf("admin", "admin@admin.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userText = findViewById<EditText>(R.id.UserText)
        val pswText = findViewById<EditText>(R.id.pswTEXT)
        val logInButton = findViewById<Button>(R.id.Log_IN_Button)

        logInButton.setOnClickListener {
            val userInput = userText.text.toString().trim()
            val password = pswText.text.toString().trim()

            if (userInput.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isEmail = Patterns.EMAIL_ADDRESS.matcher(userInput).matches()
            val isUsername = userInput.length >= 3

            if (!isEmail && !isUsername) {
                userText.error = getString(R.string.error_invalid_user)
                return@setOnClickListener
            }

            if (password.length < 6) {
                pswText.error = getString(R.string.error_password_short)
                return@setOnClickListener
            }

            val passwordGuardada = usuarios[userInput]

            if (passwordGuardada != null && passwordGuardada == password) {
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

                val intent = if (userInput in cuentasAdmin) {
                    Intent(this, HomeActivity::class.java)
                } else {
                    Intent(this, UserHomeActivity::class.java)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}