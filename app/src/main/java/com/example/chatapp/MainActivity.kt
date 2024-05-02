package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var socketHandler: SocketHandler
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        socketHandler = SocketHandler()

        binding.sendHello.setOnClickListener {
            val chat = Chat(
                username = "Doctor",
                text = "Hello, Doctor Who?"
            )
            socketHandler.emitChat(chat)
        }

        socketHandler.onNewChat.observe(this) {

        }
    }

    override fun onDestroy() {
        socketHandler.disconnectSocket()
        super.onDestroy()
    }
}