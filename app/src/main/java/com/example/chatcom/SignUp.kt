package com.example.chatcom

//import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatcom.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    //private lateinit var Uname:EditText
    private lateinit var Gmail:EditText
    private lateinit var Password:EditText
    private lateinit var Sign:Button
    private lateinit var LogI:Button
    private lateinit var Mauth: FirebaseAuth
    //private lateinit var mDbref:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Uname = findViewById(R.id.edtName)
        Gmail =findViewById(R.id.edtGmail)
        Password = findViewById(R.id.edtPassword)
        Sign = findViewById(R.id.btnSign)
        LogI = findViewById(R.id.btnlog)

        Mauth = Firebase.auth


        LogI.setOnClickListener{
            //Go back to login page after clicking login button
            val intent = Intent(this,LogIn::class.java)
            startActivity(intent)
        }


        Sign.setOnClickListener{
            //pass the data to firebase and store  gmail , password
            val email = Gmail.text.toString()
            val password = Password.text.toString()


            signIn(email,password)
        }



    }

    private fun signIn(email:String,password:String){

        Mauth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                  //  addUserToDataBase()
                    val intent = Intent(this@SignUp, Setup_profile::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUp,"Some error" , Toast.LENGTH_SHORT).show()
                }
            }


    }




}