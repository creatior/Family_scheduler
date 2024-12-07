package com.example.family_scheduler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    private val API = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val registrationTextView: TextView = findViewById(R.id.registrationTextView)
        registrationTextView.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        val loginEditText : EditText = findViewById(R.id.login)
        val passwordEditText : EditText = findViewById(R.id.password)

        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener{
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (API.login(this, login, password))
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}