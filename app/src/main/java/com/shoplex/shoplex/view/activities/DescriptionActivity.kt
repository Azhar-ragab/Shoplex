package com.shoplex.shoplex.view.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.MediaController
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.ActivityDescriptionBinding
import com.shoplex.shoplex.databinding.ActivityHomeBinding

class DescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDescriptionBinding
    private var position =0
    private var mediaControls :MediaController ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnEnglish?.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnArabic?.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (mediaControls == null){
            mediaControls = MediaController(this@DescriptionActivity)
        }
        try {
          binding. vvVideoView.setMediaController(mediaControls)
            binding.vvVideoView.setVideoURI(Uri.parse("android.resource://"+packageName+"/"+R.raw.videodesc))
        }catch (e:Exception){
            e.message?.let { Log.e("Error", it) }
        }
        binding.vvVideoView.requestFocus()
        binding.vvVideoView.setOnPreparedListener{
            binding.vvVideoView.seekTo(position)
            if (position==0){
                binding.vvVideoView.start()
            }else{
                binding.vvVideoView.pause()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (outState!=null){
            outState.putInt("Position",binding.vvVideoView.currentPosition)
        }
        binding.vvVideoView.pause()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("Position")
        }

        binding.vvVideoView.seekTo(position)
    }

}