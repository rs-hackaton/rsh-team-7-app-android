package com.example.rsh_team_7_app.room

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsh_team_7_app.R
import com.example.rsh_team_7_app.room.adapter.OnCheckboxClickListener
import com.example.rsh_team_7_app.room.adapter.RoomAdapter
import com.example.rsh_team_7_app.room.data.Topic
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RoomActivity: AppCompatActivity(), OnCheckboxClickListener {

    private val itemAdapter = RoomAdapter(this)

    private lateinit var roomReference: DatabaseReference
    private lateinit var roomID: String
    private val db = Firebase.database
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val intent = intent
        val roomName = intent.getStringExtra("nameRoom")
        roomID = intent.getStringExtra("nameId")
        val title = findViewById<TextView>(R.id.name_room)
        title.text = roomName
        roomReference = Firebase.database.reference
            .child("topics")

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@RoomActivity)
            adapter = itemAdapter
        }

        getItems()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_room_activity,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.copy_id_element_menu -> copyIdRoom()
            R.id.add_message_element_menu -> addMessage()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMessage() {
        val layoutInflaterAndroid: LayoutInflater = LayoutInflater.from(this)
        val view = layoutInflaterAndroid.inflate(R.layout.layout_alert_add_message, null)
        val alertDialogBuilderUserInput: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilderUserInput.setView(view)

        val messageEditText = view.findViewById<EditText>(R.id.messageEditText)

        alertDialogBuilderUserInput.setCancelable(false)
            .setPositiveButton("Create") { _, _ -> }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("HH:mma")
            formatter.format(date)
        }
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            when {
                TextUtils.isEmpty(messageEditText.text) -> {
                    Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else -> {
                    val topic = Topic(roomID, time, messageEditText.text.toString(), "User ID")
                    val job = GlobalScope.launch(Dispatchers.IO) {
                        addMessageToDB(topic)
                    }
                    job.start()
                    alertDialog.dismiss()
                }
            }
        }
    }

    private fun copyIdRoom() {
        //TODO
    }

    override fun onItemClick( position: Int, isChecked: Boolean, name: String) {
        val job = GlobalScope.launch(Dispatchers.IO) {
            updateTopicActive(name, isChecked)
        }
        job.start()
    }

    private fun getItems() {
        roomReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val topic = dataSnapshot.children
                val topics = topic.filter {
                    it.child("roomId").value == roomID
                }
                val message = topics.map {
                    Topic(
                        it.child("roomId").value as String,
                        it.child("time").value as String,
                        it.child("title").value as String,
                        it.child("userId").value as String,
                        it.child("order").value as Long,
                        it.child("active").value as Boolean
                    )
                }
                setItems(message)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("getRoomFromDB", "Failed to read value.", error.toException())
            }
        })
    }

    fun setItems (items: List<Topic>){
        itemAdapter.addMessage(items)
    }

    private suspend fun addMessageToDB(topic: Topic) {
        val messageId = (1..20)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        withContext(Dispatchers.IO) {
            val myRef = db.getReference("topics").child(messageId)
            myRef.setValue(topic)
                .addOnSuccessListener {
                    getItems()
                }
                .addOnFailureListener { e ->
                    Log.w("addRoomToDB", "Error adding document", e)
                }
        }
    }

    private suspend fun updateTopicActive(name: String, active: Boolean) {
        withContext(Dispatchers.IO) {
            roomReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val topics = dataSnapshot.children.filter {
                        it.child("roomId").value == roomID
                    }
                    topics.forEach {
                        if (it.child("title").value == name)
                            db.getReference("topics")
                                .child("${it.key}")
                                .child("active")
                                .setValue(active)
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("getRoomFromDB", "Failed to read value.", error.toException())
                }
            })
        }
//        withContext(Dispatchers.IO) {
//            db.getReference("topics").chi()
//
//                .child(roomID)
//                .child("active")
//                .setValue(active)
//                .addOnSuccessListener {
//                    Log.d("updateTopicActive", active.toString())
//                }
//                .addOnFailureListener { e ->
//                    Log.w("updateTopicActive", "Error adding document", e)
//                }
//        }
    }
}