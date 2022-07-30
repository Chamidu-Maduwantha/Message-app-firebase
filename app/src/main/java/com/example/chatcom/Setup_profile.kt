package com.example.chatcom

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import com.example.chatcom.databinding.ActivitySetupProfileBinding
import com.example.chatcom.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Setup_profile : AppCompatActivity() {

    var binding : ActivitySetupProfileBinding? = null
    var auth:FirebaseAuth? = null
    var database:FirebaseDatabase?= null
    var storage:FirebaseStorage? = null
    var selectedImage: Uri? = null
    //var dialog:ProgressDialog?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_setup_profile)

        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        //dialog!!.setMessage("Set Profile...")
        //dialog!!.setCancelable(false)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()


        binding!!.profileimg.setOnClickListener{

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,45)

        }

        binding!!.save.setOnClickListener{

            val bio :String = binding!!.edtBio.text.toString()
            val name :String = binding!!.edtname.text.toString()
            if(selectedImage != null ){

                val reference = storage!!.reference.child("User").child(auth!!.uid!!)

                reference.putFile(selectedImage!!).addOnCompleteListener{ task ->
                    if (task.isSuccessful){

                        reference.downloadUrl.addOnCompleteListener{uri->
                            val imageUrl = uri.toString()

                            val uid = auth!!.uid
                            val gmail = auth!!.currentUser!!.email
                            val bio:String = binding!!.edtBio.text.toString()
                            val user = User(uid, name ,gmail , imageUrl, bio)

                            database!!.reference.child("Users").child(uid!!).setValue(user).addOnCompleteListener{
                               // dialog!!.dismiss()
                                val intent = Intent(this@Setup_profile,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                    }

                    else{
                        val uid = auth!!.uid
                        val gmail = auth!!.currentUser!!.email
                        val bio:String = binding!!.edtBio.text.toString()
                        val user = User(uid,name,gmail,"No Image",bio)
                        database!!.reference.child("Users").child(uid!!).setValue(user).addOnCanceledListener {
                            //dialog!!.dismiss()
                            val intent = Intent(this@Setup_profile,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }
                }

            }

        }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){

            if (data.data != null){

                val uri = data.data //filePath
                val storage = FirebaseStorage.getInstance()
                val time = Date().time
                val reference = storage.reference.child("User").child(time.toString() + "")
                reference.putFile(uri!!).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        reference.downloadUrl.addOnCompleteListener { uri->
                            val filePath = uri.toString()
                            val obj =HashMap<String,Any>()
                            obj["image"] = filePath
                            database!!.reference.child("Users").child(FirebaseAuth.getInstance().uid!!).updateChildren(obj).addOnSuccessListener {  }
                        }
                    }
                }
                binding!!.profileimg.setImageURI(data.data)
                selectedImage = data.data

            }

        }
    }



}