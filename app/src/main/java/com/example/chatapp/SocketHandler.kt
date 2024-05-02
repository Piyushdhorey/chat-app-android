package com.example.chatapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketHandler {

    private var socket: Socket? = null

    private val _onNewChat = MutableLiveData<Chat>()
    val onNewChat: LiveData<Chat> get() = _onNewChat

    init {
        try {
            socket = IO.socket(SOCKET_URL)
            socket?.connect()

            registerOnNewChat()
        }catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    private fun registerOnNewChat() {
        socket?.on(CHAT_KEYS.NEW_MESSAGE) {args ->
            args?.let { d ->
                if (d.toString().isNotEmpty()) {
                    val chat = Gson().fromJson(d.toString(), Chat::class.java)
                    _onNewChat.postValue(chat)
                }
            }
        }
    }

    fun disconnectSocket() {
        socket?.disconnect()
        socket?.off()
    }

    fun emitChat(chat: Chat) {
        val jsonStr = Gson().toJson(chat, Chat::class.java)
        socket?.emit(CHAT_KEYS.NEW_MESSAGE, jsonStr)
    }

    private object CHAT_KEYS {
        const val NEW_MESSAGE = "new_message"
    }

    companion object {
        private const val SOCKET_URL = "http://10.0.2.2:3000/"
    }
}