package com.example.mytodoapp.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mytodoapp.R
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.presentation.ui.MainTodoListFragment

/**
 * This class represents the main activity of the application.
 * It extends AppCompatActivity and serves as the entry point for the application's UI.
 * The activity sets the content view and replaces the root container with the MainTodoListFragment.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appComponent.injectMainActivity(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.root_container, MainTodoListFragment.newInstance()).commit()
    }
}