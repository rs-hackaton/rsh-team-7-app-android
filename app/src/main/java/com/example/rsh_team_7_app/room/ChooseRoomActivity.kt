package com.example.rsh_team_7_app.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rsh_team_7_app.R
import kotlinx.android.synthetic.main.activity_choose_room.*

class ChooseRoomActivity : AppCompatActivity() {
    private val createRoom = CreateAndLogInRoom(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_room)
        createNewRoomTextView.setOnClickListener {
            createRoom.createRoom()
        }
        enterNameButton.setOnClickListener {
            createRoom.logInRoom(findViewById(R.id.enterNameRoomEditText))
        }

    }
}