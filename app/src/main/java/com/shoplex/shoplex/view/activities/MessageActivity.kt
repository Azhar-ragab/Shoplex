package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityMessageBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.LeftMessageItem
import com.shoplex.shoplex.model.adapter.RightMessageItem
import com.shoplex.shoplex.model.enumurations.keys.ChatMessageKeys
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Message
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var phoneNumber: String
    private val messageAdapter = GroupAdapter<GroupieViewHolder>()
    lateinit var storeID: String
    lateinit var productID: String
    lateinit var chatID: String
    var firstTime = true
    private var firstUnread: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMessage)
        supportActionBar?.title = ""
        val userName = intent.getStringExtra(ChatMessageKeys.CHAT_TITLE_KEY.name)
        val productImg = intent.getStringExtra(ChatMessageKeys.CHAT_IMG_KEY.name)
        Glide.with(this).load(productImg).into(binding.imgToolbarChat)

        productID = intent.getStringExtra(ChatMessageKeys.PRODUCT_ID.name).toString()
        storeID = intent.getStringExtra(ChatMessageKeys.STORE_ID.name).toString()
        chatID = intent.getStringExtra(ChatMessageKeys.CHAT_ID.name).toString()
        // phoneNumber = intent.getStringExtra(ChatMessageKeys.PHONE.name).toString()
        binding.imgToolbarChat.setImageResource(R.drawable.placeholder)
        binding.tvToolbarUserChat.text = userName
        binding.imgToolbarback.setOnClickListener { finish() }
        getAllMessage()
        binding.btnSendMessage.setOnClickListener {
            performSendMessage()
        }

        binding.edSendMesssage.setOnFocusChangeListener { v, hasFocus ->
            /*
            if(hasFocus){
                Toast.makeText(this, "Read", Toast.LENGTH_SHORT).show()
            }
            */
        }

    }

    private fun performSendMessage() {
        val messageText = binding.edSendMesssage.text
        var message = Message(
            toId = storeID, message = messageText.toString()
        )

        messageAdapter.add(RightMessageItem(message))
        FirebaseReferences.chatRef.document(chatID).collection(getString(R.string.messages))
            .document(message.messageID)
            .set(message).addOnSuccessListener {
                messageText.clear()
            }
    }

    private fun getAllMessage() {
        FirebaseReferences.chatRef.document(chatID).collection(getString(R.string.messages))
            .get().addOnSuccessListener { result ->
                for ((index, message) in result.withIndex()) {
                    var msg: Message = message.toObject()
                    if (msg.toId == UserInfo.userID) {
                        messageAdapter.add(
                            LeftMessageItem(
                                chatID,
                                Message(
                                    msg.messageID,
                                    msg.messageDate,
                                    msg.toId,
                                    msg.message,
                                    msg.isSent,
                                    msg.isRead
                                )
                            )
                        )

                        if (!msg.isSent) {
                            FirebaseReferences.chatRef.document(chatID).collection("messages")
                                .document(msg.messageID).update("isSent", true)
                            if (firstUnread == -1)
                                firstUnread = index
                        }
                    } else if (msg.toId != UserInfo.userID) {
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
                    if (firstUnread != -1)
                        binding.rvMessage.scrollToPosition(firstUnread)
                    else
                        binding.rvMessage.scrollToPosition(result.size() - 1)
                }
                binding.rvMessage.adapter = messageAdapter
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.message_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.call -> {
                val intent = Intent(Intent.ACTION_DIAL);
                intent.data = Uri.parse(getString(R.string.tel) + phoneNumber)
                startActivity(intent)
            }
        }
        return false
    }
}