package com.example.mytodoapp.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.App
import com.example.mytodoapp.R
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.presentation.ui.MainTodoListFragment
import com.example.mytodoapp.presentation.viewmodels.MainViewModel
import com.example.mytodoapp.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * This class represents the main activity of the application.
 * It extends AppCompatActivity and serves as the entry point for the application's UI.
 * The activity sets the content view and replaces the root container with the MainTodoListFragment.
 */
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (application as App).appComponent
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        component.injectMainActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appComponent.injectMainActivity(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.root_container, MainTodoListFragment.newInstance()).commit()
    }
}