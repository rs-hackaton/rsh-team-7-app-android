package com.example.rsh_team_7_app.room

import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import com.example.rsh_team_7_app.R
import com.example.rsh_team_7_app.room.data.DataMessage
import com.example.rsh_team_7_app.room.data.roomMap
import kotlinx.android.synthetic.main.activity_room.view.*
import java.util.zip.Inflater

class RoomPresenter(roomActivity: RoomActivity) {
    //private val mModel = TODO: Add model
    private val mView = roomActivity
      private val messages = mutableListOf<DataMessage>(DataMessage("test1",""),DataMessage("test2",""),DataMessage("test3",""),DataMessage("test4",""))

   fun addString(position: Int?,isChecked: Boolean?) {
       if (position != null){
           //TODO create data  messages[position].message, active=isChecked
           Log.d("addString", "addString: ${messages[position].message} $isChecked")
       }
            mView.setItems(messages)
    }
    //TODO: add real message from model

}

