package com.example.rsh_team_7_app.room

import android.util.Log
import com.example.rsh_team_7_app.room.data.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomModel {

    private val db = Firebase.database

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val randomString = (1..20)
        .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("");

    suspend fun addRoomToDB (room: Room) {
          withContext(Dispatchers.IO) {
              val myRef = db.getReference("rooms").child(randomString)
              myRef.setValue(room)
                 .addOnSuccessListener { documentReference ->
                     Log.d("addRoomToDB", "DocumentSnapshot added with ID: $documentReference")
                 }
                 .addOnFailureListener { e ->
                     Log.w("addRoomToDB", "Error adding document", e)
                 }
         }
    }

    suspend fun getRoomFromDB (id_room: String): MutableMap<String, String> {
        val roomMap = mutableMapOf<String, String>()
        withContext(Dispatchers.IO) {
            val myRef = db.getReference("rooms").child(id_room)
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = dataSnapshot.getValue<HashMap<String, String>>()
                    roomMap[id_room] = value?.get("title").toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("getRoomFromDB", "Failed to read value.", error.toException())
                }
            })
        }
        return roomMap
    }
}