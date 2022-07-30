package com.example.chatcom

//import android.support.v7.app.AppCompatActivity
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chatcom.adapter.UserAdapter
import com.example.chatcom.databinding.ActivityMainBinding
import com.example.chatcom.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var binding : ActivityMainBinding? =  null
    var database:FirebaseDatabase? = null
    var users:ArrayList<User>? = null
    var userAdapter:UserAdapter? = null
    var user:User? = null

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        database = FirebaseDatabase.getInstance()
        users = ArrayList<User>()
        userAdapter = UserAdapter(this@MainActivity,users!!)
        val layoutManager = GridLayoutManager(this@MainActivity,2)
        binding!!.mRec.layoutManager = layoutManager
        database!!.reference.child("Users").child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                   user = snapshot.getValue(User::class.java)
                }

                override fun onCancelled(error: DatabaseError) {}

        })

        binding!!.mRec.adapter = userAdapter
        database!!.reference.child("Users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users!!.clear()
                for (snapshot1 in snapshot.children){
                    val user:User? = snapshot1.getValue(User::class.java)
                    if (!user!!.uid.equals(FirebaseAuth.getInstance().uid)) users!!.add(user)

                }

                userAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        val Mauth = FirebaseAuth.getInstance()
        binding!!.logout.setOnClickListener {
            Mauth.signOut()
            Intent(this@MainActivity,LogIn::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                }
            }

        binding!!.cam.setOnClickListener {

            //intent to open camera app
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)

        }


        binding!!.settings.setOnClickListener {

            val intent =Intent (this,UserSettings::class.java)
            startActivity(intent)

        }



    }


    override fun onBackPressed(){
        Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show()

    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence").child(currentId!!).setValue("Online")

    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence").child(currentId!!).setValue("Offline")
    }
}