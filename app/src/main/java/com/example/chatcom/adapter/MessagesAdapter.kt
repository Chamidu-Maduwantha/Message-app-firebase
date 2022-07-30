package com.example.chatcom.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatcom.R
import com.example.chatcom.databinding.DeleteLayoutBinding
import com.example.chatcom.databinding.ReciveMsgBinding
import com.example.chatcom.databinding.SendMsgBinding
import com.example.chatcom.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MessagesAdapter(
    var context:Context,
    messages:ArrayList<Message>?,
    senderRoom:String,
    reciverRoom:String
):RecyclerView.Adapter<RecyclerView.ViewHolder?>()
{

    lateinit var messages: ArrayList<Message>
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    val senderRoom:String
    val reciverRoom:String


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT){
            val view = LayoutInflater.from(context).inflate(R.layout.send_msg,parent,false)
            SentMsgHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.recive_msg,parent,false)
            RecieveMsgHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val messages = messages[position]
        return if (FirebaseAuth.getInstance().uid == messages.senderId ){
            ITEM_SENT
        }else{
            ITEM_RECEIVE
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder.javaClass == SentMsgHolder::class.java) {
            val viewHolder = holder as SentMsgHolder
            if (message.message.equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLiner.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(viewHolder.binding.image)
            }
            viewHolder.binding.message.text = message.message
            viewHolder.itemView.setOnLongClickListener {

                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout,null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.everyone.setOnClickListener {
                    message.message = "This message is removed"
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(message)

                    }

                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(reciverRoom)
                            .child("messages")
                            .child(it1!!).setValue(message)

                    }

                    dialog.dismiss()


                }

                binding.delete.setOnClickListener {
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(reciverRoom)
                            .child("messages")
                            .child(it1!!).setValue(message)

                    }
                    dialog.dismiss()
                }

                binding.delete.setOnClickListener { dialog.dismiss() }

                dialog.show()

                false
            }
        } else {
            val viewHolder = holder as RecieveMsgHolder
            if (message.message.equals("photo")) {

                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLiner.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(viewHolder.binding.image)

            }

            viewHolder.binding.message.text = message.message
            viewHolder.itemView.setOnLongClickListener {

                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.everyone.setOnClickListener {
                    message.message = "This message is removed"
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(message)

                    }

                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(reciverRoom)
                            .child("messages")
                            .child(it1!!).setValue(message)

                    }

                    dialog.dismiss()


                }

                binding.delete.setOnClickListener {
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(reciverRoom)
                            .child("messages")
                            .child(it1!!).setValue(message)

                    }
                    dialog.dismiss()
                }

                binding.delete.setOnClickListener { dialog.dismiss() }

                dialog.show()
                false

            }
        }
    }

    override fun getItemCount(): Int = messages.size


    inner class SentMsgHolder (itmView:View):RecyclerView.ViewHolder(itmView) {
        var binding: SendMsgBinding = SendMsgBinding.bind(itmView)
    }

    inner class RecieveMsgHolder (itmView:View):RecyclerView.ViewHolder(itmView) {
        var binding: SendMsgBinding = SendMsgBinding.bind(itmView)
    }

    init {
        if (messages != null){
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.reciverRoom = reciverRoom

    }




}