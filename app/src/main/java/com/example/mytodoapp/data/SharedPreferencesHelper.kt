package com.example.mytodoapp.data

import android.content.Context
import android.content.SharedPreferences
import com.example.mytodoapp.data.network.BaseUrl
import com.example.mytodoapp.presentation.fragments.MainTodoListFragment
import java.util.UUID

class SharedPreferencesHelper(context: Context){
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    var networkAccessError = false
    var isNotOnline = false
    init {
        sharedPreferences = context.getSharedPreferences("states", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        if(!sharedPreferences.contains("UID")){
            editor.putString("UID", UUID.randomUUID().toString())
            editor.apply()
        }
        BaseUrl.updated_by = sharedPreferences.getString("UID", "-1").toString()
    }

    fun putRevision(revision:Int){
        editor.putInt("REVISION", revision)
        editor.apply()
    }
    fun getLastRevision():Int = sharedPreferences.getInt("REVISION", 1)


}