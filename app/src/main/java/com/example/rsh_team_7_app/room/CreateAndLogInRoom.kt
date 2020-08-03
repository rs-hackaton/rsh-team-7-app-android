package com.example.rsh_team_7_app.room

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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateAndLogInRoom(private val context: Context) {

    private val db = Firebase.database
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private lateinit var roomName: String

    fun createRoom() {
        val layoutInflaterAndroid: LayoutInflater = LayoutInflater.from(context.applicationContext)
        val view = layoutInflaterAndroid.inflate(R.layout.layout_alert_create_room, null)
        val alertDialogBuilderUserInput: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilderUserInput.setView(view)

        val nameRoomEditText = view.findViewById<EditText>(R.id.nameRoomEditText)
        roomName = nameRoomEditText.text.toString()
        val time = System.currentTimeMillis()

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
                    val room = Room(nameRoomEditText.text.toString(), time, "User ID")
                    val job = GlobalScope.launch(Dispatchers.IO) {
                        addRoomToDB(room, nameRoomEditText.text.toString())
                    }
                    job.start()
                }
            }
        }
    }

    fun logInRoom(enterNameRoomEditText: EditText) {
        when {
            TextUtils.isEmpty(enterNameRoomEditText.text) -> {
                Toast.makeText(context, "Please enter name room", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val job = GlobalScope.launch(Dispatchers.IO) {
                    getRoomFromDB(enterNameRoomEditText.text.toString())
                }
                job.start()
            }
        }
    }

    private suspend fun addRoomToDB (room: Room, name: String) {
        val roomId = (1..20)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        withContext(Dispatchers.IO) {
            val myRef = db.getReference("rooms").child(roomId)
            myRef.setValue(room)
                .addOnSuccessListener {
                    goToRoomActivity(name, roomId)
                }
                .addOnFailureListener { e ->
                    Log.w("addRoomToDB", "Error adding document", e)
                }
        }
    }

    private fun goToRoomActivity(name: String, roomId: String) {
        val intent = Intent(context, RoomActivity::class.java)
        intent.putExtra("nameRoom", name)
        intent.putExtra("nameId", roomId)
        context.startActivity(intent)
    }

    private suspend fun getRoomFromDB(roomId: String){
        withContext(Dispatchers.IO) {
            val myRef = db.getReference("rooms").child(roomId)
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        goToRoomActivity(dataSnapshot.child("title").value as String, roomId)
                    } else {
                       Toast.makeText(context, "Room not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("getRoomFromDB", "Failed to read value.", error.toException())
                }
            })
        }
    }
}
