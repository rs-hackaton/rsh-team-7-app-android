package com.example.rsh_team_7_app.room

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
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
import com.example.rsh_team_7_app.room.data.DataMessage
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
            layoutManager = LinearLayoutManager(this@RoomActivity)
            adapter = itemAdapter
        }
        mPresenter.addString(null,null)
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
        //TODO
    }

    private fun copyIdRoom() {
        //TODO
    }

    override fun onItemClick( position: Int,isChecked:Boolean) {
        Log.d("RoomActivity", "onItemClick: $position ")
        mPresenter.addString(position,isChecked)
    }

    fun setItems(items: List<DataMessage>) {
        itemAdapter.addMessage(items)
    }
}