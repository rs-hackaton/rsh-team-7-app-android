package com.example.rsh_team_7_app.room.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rsh_team_7_app.R

class RoomAdapter (private val clickListener: OnCheckboxClickListener) : RecyclerView.Adapter<RoomViewHolder>() {
    private val items = mutableListOf<String>("test", "test 1")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val message: String = items[position]
        holder.initialize(items, clickListener)
        Log.d("onBindViewHolder", message)
        holder.bind(message)
    }

    fun addMessage(message: List<String>) {
        items.addAll(message)
        notifyDataSetChanged()
    }
}

class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var messageText = view.findViewById<TextView?>(R.id.message_room)
    private val btnCheckbox = view.findViewById<ImageButton>(R.id.checkbox_button_room)

    fun initialize(items: List<String>, action: OnCheckboxClickListener) {
        btnCheckbox?.apply {
            setOnClickListener{
                action.onItemClick(items[adapterPosition], adapterPosition)
            }
        }
    }

    fun bind(message: String?) {
        messageText?.text = message ?: "Error"
    }
}

interface OnCheckboxClickListener {
    fun onItemClick(item: String, position: Int)
}
