package com.example.rsh_team_7_app.room

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionBarContainer
import androidx.appcompat.widget.ActionBarContextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsh_team_7_app.R
import com.example.rsh_team_7_app.room.adapter.OnCheckboxClickListener
import com.example.rsh_team_7_app.room.adapter.RoomAdapter
import kotlinx.android.synthetic.main.activity_room.*

class RoomActivity: AppCompatActivity(), OnCheckboxClickListener {
    private val itemAdapter = RoomAdapter(this)
    private val mPresenter = RoomPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val intent = intent
        val nameRoom = intent.getStringExtra(getString(R.string.room_id))
        /*if (nameRoom == null){
            Toast.makeText(this, "Сan't enter the room", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }else{
           title = nameRoom
        }*/

        recycler_view.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this@RoomActivity)
        }

     //   mPresenter.addString(nameRoom)
    }

    override fun onItemClick(item: String, position: Int) {
        Log.d("RoomActivity", item)
    }

    fun setItems(items: List<String>) {
        itemAdapter.addMessage(items)
    }
}