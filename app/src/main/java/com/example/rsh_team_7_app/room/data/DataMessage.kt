package com.example.rsh_team_7_app.room.data

import android.os.Message

data class DataMessage (
    val message: String,
    val time: String,
    var isClick: Boolean = false
)