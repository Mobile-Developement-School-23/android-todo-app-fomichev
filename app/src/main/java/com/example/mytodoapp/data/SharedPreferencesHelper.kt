package com.example.mytodoapp.data

import android.content.Context
import android.content.SharedPreferences
import com.example.mytodoapp.data.network.Token
import java.util.UUID
import javax.inject.Inject

/**
 * This class provides a helper for managing shared preferences related to application states.
 * It allows storing and retrieving various states, such as network access errors, online status,
 * and the last revision number.
 */
class SharedPreferencesHelper @Inject constructor(context: Context){
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
        Token.updated_by = sharedPreferences.getString("UID", "-1").toString()
    }

    fun putRevision(revision:Int){
        editor.putInt("REVISION", revision)
        editor.apply()
    }
    fun getLastRevision():Int = sharedPreferences.getInt("REVISION", 1)
}