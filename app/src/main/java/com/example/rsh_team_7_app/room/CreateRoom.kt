package com.example.rsh_team_7_app.room

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rsh_team_7_app.R
import com.example.rsh_team_7_app.room.data.Room
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CreateRoom(private val context: Context) {

    private val modelRoom = RoomModel()

    @SuppressLint("SimpleDateFormat")
    fun createRoom() {
        val layoutInflaterAndroid:LayoutInflater = LayoutInflater.from(context.applicationContext)
        val view = layoutInflaterAndroid.inflate(R.layout.layout_alert_create_room, null)
        val alertDialogBuilderUserInput: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilderUserInput.setView(view)

        val nameRoomEditText = view.findViewById<EditText>(R.id.nameRoomEditText)

        val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("HH:mma")
            formatter.format(date)
        }

        alertDialogBuilderUserInput.setCancelable(false)
            .setPositiveButton("Create") { _, _ -> }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
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
            val room = Room(nameRoomEditText.text.toString(), time, "Take ID user")
            val job = GlobalScope.launch(Dispatchers.IO) {
                modelRoom.addRoomToDB(room)
            }
            job.start()
            transitionToRoom(nameRoomEditText.text.toString())
            Toast.makeText(context, "create room", Toast.LENGTH_SHORT).show()
        }
    }

    fun entranceToRoom(enterNameRoomEditText:EditText) {
        when {
            TextUtils.isEmpty(enterNameRoomEditText.text) -> {
                Toast.makeText(context, "Please enter name room", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val job: Job = GlobalScope.launch(Dispatchers.Main) {
                    val room = withContext(Dispatchers.IO) {
                        modelRoom.getRoomFromDB(enterNameRoomEditText.text.toString())
                    }
                    Log.d("room", room.toString())
                    if (room[enterNameRoomEditText.text.toString()] == null) {
                        Toast.makeText(context, "Room not found", Toast.LENGTH_SHORT).show()
                    } else {
                        room?.let{
                            Toast.makeText(context, "entranceToRoom", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                job.start()

//                if (room_name == null) {
//                    Toast.makeText(context, "Room not found", Toast.LENGTH_SHORT).show()
//                } else {
//                    /*todo val intent = Intent(context,RecyclerActivity::class.java)
//           intent.putExtra("NAME_ROOM",enterNameRoomEditText.text.toString().trim())
//            context.startActivity(intent)*/
//                    Toast.makeText(context, "entranceToRoom", Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }

    private fun transitionToRoom(name: String) {
        val intent = Intent(context, RoomActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("name_room", name)
        context.startActivity(intent)
    }
}
