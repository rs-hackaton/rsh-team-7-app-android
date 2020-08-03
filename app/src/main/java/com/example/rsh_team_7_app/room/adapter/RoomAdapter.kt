package com.example.rsh_team_7_app.room.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rsh_team_7_app.R
import com.example.rsh_team_7_app.room.data.Topic

class RoomAdapter(
    private val mOnNoteListener: OnCheckboxClickListener
) :
    RecyclerView.Adapter<RoomAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        view: View,
        var onCheckBox: OnCheckboxClickListener
    ) : RecyclerView.ViewHolder(view) {
        val messageRoomTextView: TextView = view.findViewById(R.id.message_room)
        val checkboxRoom :CheckBox = view.findViewById(R.id.checkbox_room)

        init {
            checkboxRoom.setOnClickListener {
                onCheckBox.onItemClick(adapterPosition, checkboxRoom.isChecked, items[adapterPosition].title)
                Log.d("onClick", "$adapterPosition --- ${checkboxRoom.isChecked}")
            }
        }
    }

    private var items = listOf<Topic>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room,parent,false)
        return MyViewHolder(view, mOnNoteListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = items[position]
        //Log.d("onBindViewHolder", "onBindViewHolder: $message ")
        holder.messageRoomTextView.text = message.title
        holder.checkboxRoom.isChecked = message.active
    }

    fun addMessage(newItems: List<Topic>) {
        items = newItems
        notifyDataSetChanged()
    }

}

interface OnCheckboxClickListener {
    fun onItemClick(position: Int, isChecked:Boolean, name: String)
}