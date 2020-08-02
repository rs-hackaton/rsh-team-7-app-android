package com.example.rsh_team_7_app.room

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        val title = findViewById<TextView>(R.id.name_room)
        title.text = intent.extras?.getString("name_room")

        recycler_view.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this@RoomActivity)
        }

        mPresenter.addString()
    }

    override fun onItemClick(item: String, position: Int) {
        Log.d("RoomActivity", item)
    }

    fun setItems(items: List<String>) {
        itemAdapter.addMessage(items)
    }
}