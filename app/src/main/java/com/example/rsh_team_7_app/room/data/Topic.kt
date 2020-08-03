package com.example.rsh_team_7_app.room.data

data class Topic (
    val roomId: String,
    val time: String,
    val title: String,
    val userId: String,
    val order: Long = 0,
    val active: Boolean = false
)