package com.shoplex.shoplex.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityMessageBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.LeftMessageItem
import com.shoplex.shoplex.model.adapter.RightMessageItem
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Chat
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.model.pojo.Store

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var phoneNumber:String
    val messageAdapter = GroupAdapter<GroupieViewHolder>()
    lateinit var storeID: String
    lateinit var productID:String
    lateinit var chatID:String
    var firstTime = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMessage)
        supportActionBar?.setTitle("")
        val userName = intent.getStringExtra(ChatHeadAdapter.CHAT_TITLE_KEY)
        val productImg = intent.getStringExtra(ChatHeadAdapter.CHAT_IMG_KEY)
        Glide.with(this).load(productImg).into(binding.imgToolbarChat)
        storeID = intent.getStringExtra(getString(R.string.storeID)).toString()
        phoneNumber= intent.getStringExtra(getString(R.string.phones)).toString()
        productID= intent.getStringExtra(getString(R.string.productID)).toString()
        chatID=intent.getStringExtra(getString(R.string.chatID)).toString()
        binding.imgToolbarChat.setImageResource(R.drawable.placeholder)
        binding.tvToolbarUserChat.text = userName
        binding.imgToolbarback.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                finish()
            }}
            )

//        messageAdapter.add(LeftMessageItem(Message(toId = "1",message = "hi")))
//        messageAdapter.add(LeftMessageItem(Message(toId = "1", message = "hello Koky")))
//        messageAdapter.add(RightMessageItem(Message(toId = "0", message = "send hello")))
//        messageAdapter.add(LeftMessageItem(Message(toId = "1", message = "hello Azhar")))
//        messageAdapter.add(RightMessageItem(Message(toId = "0", message = "hello Heba")))
//
//        binding.rvMessage.adapter = messageAdapter
        getAllMessage()
        binding.btnSendMessage.setOnClickListener{
            performSendMessage()
        }

    }

    private fun performSendMessage() {
        val messageID = Timestamp.now().toDate().time.toString()
        val messageText = binding.edSendMesssage.text
        messageAdapter.add(RightMessageItem(Message(message = messageText.toString())))
        var message = Message(
            messageID, Timestamp.now().toDate(),
            storeID, messageText.toString()
        )
            FirebaseReferences.chatRef.document(chatID).collection(getString(R.string.messages))
                .document(messageID)
                .set(message).addOnSuccessListener {
                    messageText.clear()
                }
    }
    fun getAllMessage() {

        FirebaseReferences.chatRef.document(chatID).collection(getString(R.string.messages))
            .get().addOnSuccessListener { result ->
                for (message in result) {
                    var msg: Message = message.toObject<Message>()
                    if (msg.toId.equals(UserInfo.userID)){
                        messageAdapter.add(
                            LeftMessageItem(
                                Message(
                                    msg.messageID,
                                    msg.messageDate,
                                    msg.toId,
                                    msg.message
                                )
                            )
                        )
                    }else if (!msg.toId.equals(UserInfo.userID)){
                        messageAdapter.add(
                            RightMessageItem(
                                Message(
                                    msg.messageID,
                                    msg.messageDate,
                                    msg.toId,
                                    msg.message
                                )
                            )
                        )
                    }
                    binding.rvMessage.scrollToPosition(result.size() -1);

                }
                binding.rvMessage.adapter=messageAdapter


            }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.message_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.call ->{val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse(getString(R.string.tel)+phoneNumber)
                    startActivity(intent)}

        }

        return false

    }
}