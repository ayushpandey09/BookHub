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


class SignUpActivity : AppCompatActivity() {
    lateinit var etemail :EditText
    lateinit var etpassword : EditText
    lateinit var etconPass : EditText
    lateinit var btnSignIn : Button
    lateinit var tvLogin : TextView

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etemail = findViewById(R.id.etEmail)
        etpassword = findViewById(R.id.etPassword)
        etconPass = findViewById(R.id.etConfirmPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        tvLogin = findViewById(R.id.tvDontHaveAnAcc)

        firebaseAuth = FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener {
            signUp()
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signUp(){
       val email = etemail.text.toString()
       val password = etpassword.text.toString()
       val conPassword = etconPass.text.toString()

        if(password.isBlank() || conPassword.isBlank() || email.isBlank()){
            Toast.makeText(this,"Error! \n Check the credentials",Toast.LENGTH_LONG).show()
            return
        }

        if(password != conPassword)
        {
            Toast.makeText(this,"Password or Confirm Password is wrong",Toast.LENGTH_LONG).show()
            return
        }

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) {
                if (it.isSuccessful){
                    Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
                   val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }

                else
                    Toast.makeText(this,"Error !!",Toast.LENGTH_SHORT).show()
            }
    }
}