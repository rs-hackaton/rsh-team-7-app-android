package com.example.rsh_team_7_app.room

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rsh_team_7_app.R
import com.example.rsh_team_7_app.room.data.roomMap

class CreateAndLogInRoom(private val context: Context) {

    fun createRoom() {
        val layoutInflaterAndroid: LayoutInflater = LayoutInflater.from(context.applicationContext)
        val view = layoutInflaterAndroid.inflate(R.layout.layout_alert_create_room, null)
        val alertDialogBuilderUserInput: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilderUserInput.setView(view)

        val nameRoomEditText = view.findViewById<EditText>(R.id.nameRoomEditText)

        alertDialogBuilderUserInput.setCancelable(false)
            .setPositiveButton("Create") { _, _ -> }
            .setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            when {
                TextUtils.isEmpty(nameRoomEditText.text) -> {
                    Toast.makeText(context, "Please enter name room", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else -> {
                    alertDialog.dismiss()
                }
            }
            if (roomMap.containsKey(nameRoomEditText.text.toString().trim())) {
                Toast.makeText(context, "The room has already been created", Toast.LENGTH_SHORT)
                    .show()
            } else {
                roomMap.put(nameRoomEditText.text.toString().trim(), mutableListOf())
                val intent = Intent(context, RoomActivity::class.java)
                /*intent.putExtra(
                    context.getString(R.string.room_id),
                    nameRoomEditText.text.toString().trim()
                )*/
                context.startActivity(intent)
            }
        }
    }

    fun logInRoom(enterNameRoomEditText: EditText) {
        when {
            TextUtils.isEmpty(enterNameRoomEditText.text) -> {
                Toast.makeText(context, "Please enter name room", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val list = roomMap[enterNameRoomEditText.text.toString().trim()]
                if (list == null) {
                    Toast.makeText(context, "Room not found", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(context, RoomActivity::class.java)
                    /*intent.putExtra(
                        context.getString(R.string.room_id),
                        enterNameRoomEditText.text.toString().trim()
                    )*/
                    context.startActivity(intent)
                }
            }
        }
    }

}
