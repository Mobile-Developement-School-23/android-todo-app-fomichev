package com.example.mytodoapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mytodoapp.R
import com.example.mytodoapp.presentation.fragments.MainTodoListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.root_container, MainTodoListFragment.newInstance()).commit()
    }
}