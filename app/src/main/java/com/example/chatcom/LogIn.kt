package com.example.chatcom

//import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LogIn : AppCompatActivity() {

    private lateinit var Gmail:EditText
    private lateinit var Password:EditText
    private lateinit var btnLog:Button
    private lateinit var btnReg:Button

    private lateinit var Mauth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        Gmail = findViewById(R.id.email)
        Password = findViewById(R.id.lgpassowrd)
        btnLog = findViewById(R.id.btnlg)
        btnReg = findViewById(R.id.btnreg)



        Mauth = Firebase.auth


        btnReg.setOnClickListener{
            val intent =Intent (this,SignUp::class.java)
            startActivity(intent)
        }


       btnLog.setOnClickListener{

            val email = Gmail.text.toString()
            val password = Password.text.toString()

            login(email,password)

        }

    }


    private fun login(email:String,password:String){

        Mauth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LogIn,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LogIn, "User not exist", Toast.LENGTH_SHORT).show()
                }
            }



    }


}