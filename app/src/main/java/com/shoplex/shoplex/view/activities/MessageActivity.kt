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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
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
import com.shoplex.shoplex.room.viewmodel.MessageFactoryModel
import com.shoplex.shoplex.room.viewmodel.MessageViewModel
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
    private var position: Int = -1
    private lateinit var messageVM: MessageViewModel

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

        messageVM = ViewModelProvider(
            this,
            MessageFactoryModel(this, chatID)
        ).get(MessageViewModel::class.java)


        getAllMessage()
        binding.btnSendMessage.setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage() {
        //send Message to Firebase
        val messageText = binding.edSendMesssage.text
        messageAdapter.add(RightMessageItem(Message(message = messageText.toString())))
        var message = Message(toId = storeID, message = messageText.toString())
        FirebaseReferences.chatRef.document(chatID).collection("messages")
            .document(message.messageID)
            .set(message)

        messageText.clear()
    }

    /*
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
    */

    private fun listenToNewMessages(lastID: String) {
        FirebaseReferences.chatRef.document(chatID).collection("messages")
            .whereGreaterThan("messageID", lastID).addSnapshotListener { snapshots, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                for ((index,dc) in snapshots!!.documentChanges.withIndex()) {
                    if ((dc.type) == DocumentChange.Type.ADDED) {
                        val message = dc.document.toObject<Message>()
                        if (message.toId == UserInfo.userID) {
                            message.chatID = chatID
                            if (!message.isSent) {
                                FirebaseReferences.chatRef.document(chatID).collection("messages")
                                    .document(message.messageID).update("isSent", true)
                            }
                            if (!message.isRead && position == -1)
                                position = messageAdapter.groupCount + index -1

                            messageAdapter.add(LeftMessageItem(chatID, message))
                            messageVM.addMessage(message)
                        } else if (message.toId != UserInfo.userID) {
                            message.chatID = chatID
                            messageVM.addMessage(message)
                        }
                    }
                }
                if (position > 0){
                    binding.rvMessage.scrollToPosition(position)
                    position = 0
                }
            }
    }

    private fun getAllMessage() {
        binding.rvMessage.adapter = messageAdapter
        messageVM.readAllMessage.observe(this,  {
            for (message in it) {
                if (message.toId == UserInfo.userID) {
                    messageAdapter.add(LeftMessageItem(chatID, message))

                } else if (message.toId != UserInfo.userID) {
                    messageAdapter.add(RightMessageItem(message))
                }
            }
            val lastID = if (it.isEmpty()) {
                "1"
            } else {
                // position = it.count() - 1
                binding.rvMessage.scrollToPosition(it.count() - 1)
                it.last().messageID
            }
            messageVM.readAllMessage.removeObservers(this)
            listenToNewMessages(lastID)
            //getAllMessageFromFirebase(lastID)
        })
    }

    /*
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
    */

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