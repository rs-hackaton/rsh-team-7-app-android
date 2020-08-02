package com.example.rsh_team_7_app.room.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.rsh_team_7_app.R
import com.example.rsh_team_7_app.room.data.DataMessage

class RoomAdapter(
    private val mOnNoteListener: OnCheckboxClickListener
) :
    RecyclerView.Adapter<RoomAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        view: View,
        var onCheckBox: OnCheckboxClickListener
    ) : RecyclerView.ViewHolder(view) {
        val messageRoomTextView: TextView = view.findViewById(R.id.message_room)
        private val checkboxRoom :CheckBox = view.findViewById(R.id.checkbox_room)

        init {
            checkboxRoom.setOnClickListener {
                onCheckBox.onItemClick(adapterPosition,checkboxRoom.isChecked)
                Log.d("onClick", "$adapterPosition --- ${checkboxRoom.isChecked}")
            }
        }
    }

    private var items = listOf<DataMessage>()

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
        holder.messageRoomTextView.text = message.message
    }

    fun addMessage(newItems: List<DataMessage>) {
        items = newItems
        notifyDataSetChanged()
    }

}

interface OnCheckboxClickListener {
    fun onItemClick(position: Int,isChecked:Boolean)
}