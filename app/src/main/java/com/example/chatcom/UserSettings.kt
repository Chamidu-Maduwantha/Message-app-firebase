package com.example.chatcom

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.chatcom.adapter.UserAdapter
import com.example.chatcom.databinding.ActivityMainBinding
import com.example.chatcom.databinding.ActivitySetupProfileBinding
import com.example.chatcom.databinding.ActivityUserSettingsBinding
import com.example.chatcom.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserSettings : AppCompatActivity() {

    private lateinit var binding: ActivityUserSettingsBinding
    private lateinit var Kauth: FirebaseAuth
    private lateinit var databasereferenc: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var user: User
    private lateinit var dialog: Dialog
    private lateinit var uid: String
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Kauth = FirebaseAuth.getInstance()
        uid = Kauth.currentUser?.uid.toString()

        databasereferenc = FirebaseDatabase.getInstance().getReference("Users")

        if (uid.isNotEmpty()) {
            getUserData()

        }

        binding.del.setOnClickListener {
            deleteUser()
            Intent(this@UserSettings,LogIn::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        binding.chat1.setOnClickListener{
            val intent =Intent (this,MainActivity::class.java)
            startActivity(intent)
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

        binding!!.logout.setOnClickListener {
            Kauth.signOut()
            Intent(this@UserSettings,LogIn::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }


    }


    private fun getUserData() {

        databasereferenc.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                binding.gmli.setText(user.gmail)
                binding.uname.setText(user.name)
                binding.biol.setText(user.bio)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun deleteUser() {
        databasereferenc = FirebaseDatabase.getInstance().getReference("Users")
        databasereferenc.child(uid).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()


        }
    }



    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        databasereferenc.child("presence").child(currentId!!).setValue("Online")

    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        databasereferenc.child("presence").child(currentId!!).setValue("Offline")
    }
}