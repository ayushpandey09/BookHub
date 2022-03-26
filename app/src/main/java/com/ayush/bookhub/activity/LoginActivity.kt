package com.ayush.bookhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ayush.bookhub.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var tvRedirectSignUp: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth

    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvRedirectSignUp = findViewById(R.id.tvDontHaveAnAcc)
        etEmail = findViewById(R.id.etLEmail)
        etPass = findViewById(R.id.etLPassword)
        btnLogin = findViewById(R.id.btnLogin)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            Toast.makeText(this,"Logged In",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            login()
        }

        tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

        backToast = Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_LONG)
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity()
        } else {
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()

    }

    private fun login() {

        val email = etEmail.text.toString()
        val pass = etPass.text.toString()

        if (email.isBlank() || pass.isBlank()) {
            Toast.makeText(this, "Email/password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                val intent1 = Intent(this, MainActivity::class.java)
                startActivity(intent1)
                finish()
            } else
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
        }
    }
}