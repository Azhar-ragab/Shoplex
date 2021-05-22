package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityMessageBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.LeftMessageItem
import com.shoplex.shoplex.model.adapter.RightMessageItem
import com.shoplex.shoplex.model.pojo.Message

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var phoneNumber:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMessage)
        supportActionBar?.setTitle("")

        val userName = intent.getStringExtra(ChatHeadAdapter.CHAT_TITLE_KEY)
        val chatId =intent.getStringExtra("chatID")
        phoneNumber= intent.getStringExtra("phoneNumber").toString()
        binding.imgToolbarChat.setImageResource(R.drawable.placeholder)
        binding.tvToolbarUserChat.text = userName
        binding.imgToolbarback.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                finish()
            }}
            )

        val messageAdapter = GroupAdapter<GroupieViewHolder>()
        messageAdapter.add(LeftMessageItem(Message(toId = "1",message = "hi")))
        messageAdapter.add(LeftMessageItem(Message(toId = "1", message = "hello Koky")))
        messageAdapter.add(RightMessageItem(Message(toId = "0", message = "send hello")))
        messageAdapter.add(LeftMessageItem(Message(toId = "1", message = "hello Azhar")))
        messageAdapter.add(RightMessageItem(Message(toId = "0", message = "hello Heba")))

        binding.rvMessage.adapter = messageAdapter

        binding.btnSendMessage.setOnClickListener{
            performSendMessage()
        }

    }

    private fun performSendMessage() {
        //send Message to Firebase
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.message_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.call ->{val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${phoneNumber}")
                    startActivity(intent)}

        }

        return false

    }
}