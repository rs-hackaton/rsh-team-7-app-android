package com.example.rsh_team_7_app.room

class RoomPresenter(roomActivity: RoomActivity) {
    //private val mModel = TODO: Add model
    private val mView = roomActivity
    private val messages = listOf<String>("Test 0", "Test 1", "Test 2", "Test 3")

    fun addString() {
        //TODO: add real message from model
        mView.setItems(messages)
    }
}
