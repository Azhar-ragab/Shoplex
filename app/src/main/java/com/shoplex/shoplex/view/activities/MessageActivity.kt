package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityMessageBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.LeftMessageItem
import com.shoplex.shoplex.model.adapter.RightMessageItem
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.Message
import com.shoplex.shoplex.room.viewmodel.MessageFactoryModel
import com.shoplex.shoplex.room.viewmodel.MessageViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private val messageAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var chatID: String
    private lateinit var storeID: String
    private lateinit var productID: String
    private lateinit var phoneNumber: String
    private var position: Int = -1
    private lateinit var messageVM: MessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMessage)
        supportActionBar?.title = ""

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        val userName = intent.getStringExtra(ChatHeadAdapter.CHAT_TITLE_KEY)
        val productImg = intent.getStringExtra(ChatHeadAdapter.CHAT_IMG_KEY).toString()
        chatID = intent.getStringExtra(ChatHeadAdapter.CHAT_ID_KEY).toString()
        productID = intent.getStringExtra(ChatHeadAdapter.PRODUCT_ID).toString()
        storeID = intent.getStringExtra(ChatHeadAdapter.STORE_ID_KEY).toString()
        phoneNumber = intent.getStringExtra(ChatHeadAdapter.STORE_PHONE).toString()

        messageVM = ViewModelProvider(
            this,
            MessageFactoryModel(this, chatID)
        ).get(MessageViewModel::class.java)

        messageVM.isOnline.observe(this, {
            if (it) {
                binding.cardIsOnline.visibility = View.VISIBLE
            } else {
                binding.cardIsOnline.visibility = View.INVISIBLE
            }
        })

       // binding.imgToolbarback.setOnClickListener { finish() }

        binding.imgToolbarChat.setImageResource(R.drawable.placeholder)
        binding.tvToolbarUserChat.text = userName
        Glide.with(this).load(productImg).into(binding.imgToolbarChat)

        getAllMessage()

        binding.btnSendMessage.setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage() {
        //send Message to Firebase
        val messageText = binding.edSendMesssage.text

        val message = Message(toId = storeID, message = messageText.toString())
        message.chatID = chatID
        FirebaseReferences.chatRef.document(chatID).collection("messages")
            .document(message.messageID)
            .set(message).addOnSuccessListener {
                messageAdapter.add(RightMessageItem(message, messageVM))
                messageText.clear()
                binding.rvMessage.smoothScrollToPosition(messageAdapter.groupCount)
            }
    }

    private fun listenToNewMessages(lastID: String) {
        var firstTimeToLoadMessages = (lastID == "1")
        FirebaseReferences.chatRef.document(chatID).collection("messages")
            .whereGreaterThan("messageID", lastID).addSnapshotListener { snapshots, error ->
                if (error != null) return@addSnapshotListener

                for ((index, dc) in snapshots!!.documentChanges.withIndex()) {
                    if ((dc.type) == DocumentChange.Type.ADDED) {
                        val message = dc.document.toObject<Message>()
                        if (message.toId == UserInfo.userID) {
                            message.chatID = chatID
                            if (!message.isSent) {
                                FirebaseReferences.chatRef.document(chatID).collection("messages")
                                    .document(message.messageID).update("isSent", true)
                                message.isSent = true
                            }

                            if (!message.isRead && position == -1)
                                position = messageAdapter.groupCount + index - 1

                            messageAdapter.add(LeftMessageItem(chatID, message, messageVM))
                            messageVM.addMessage(message)
                        } else if (message.toId != UserInfo.userID) {
                            if (firstTimeToLoadMessages)
                                messageAdapter.add(RightMessageItem(message, messageVM))
                            message.chatID = chatID
                            messageVM.addMessage(message)
                        }
                    }
                }

                firstTimeToLoadMessages = false

                if (position > 0) {
                    binding.rvMessage.smoothScrollToPosition(position)
                    position = 0
                }
            }
    }

    private fun getAllMessage() {
        binding.rvMessage.adapter = messageAdapter
        messageVM.readAllMessage.observe(this, {
            for (message in it) {
                if (message.toId == UserInfo.userID) {
                    messageAdapter.add(LeftMessageItem(chatID, message, messageVM))

                } else if (message.toId != UserInfo.userID) {
                    messageAdapter.add(RightMessageItem(message, messageVM))
                }
            }
            val lastID = if (it.isEmpty()) {
                "1"
            } else {
                binding.rvMessage.scrollToPosition(it.count() - 1)
                it.last().messageID
            }
            messageVM.readAllMessage.removeObservers(this)
            listenToNewMessages(lastID)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.message_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.call -> {
                if (phoneNumber.length > 10) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse(getString(R.string.tel) + phoneNumber)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Phone Number is not specified", Toast.LENGTH_SHORT).show()
                }
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


}