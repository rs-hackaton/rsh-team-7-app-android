package com.example.rsh_team_7_app.room

import android.widget.Toast
import com.example.rsh_team_7_app.R
import com.example.rsh_team_7_app.room.data.roomMap
import kotlinx.android.synthetic.main.activity_room.view.*

class RoomPresenter(roomActivity: RoomActivity) {
    //private val mModel = TODO: Add model
    private val mView = roomActivity
    //  private val messages = listOf<String>("Test 0", "Test 1", "Test 2", "Test 3")

    fun addString(nameRoom: String) {
        val messageList = roomMap[nameRoom]
        if (messageList != null) {
            mView.setItems(messageList.toList())
        } else {
            Toast.makeText(mView.applicationContext, "Сan't enter the room", Toast.LENGTH_SHORT)
                .show()
        }
    }
    //TODO: add real message from model

}

