package com.example.mytodoapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.mytodoapp.App
import com.example.mytodoapp.R
import com.example.mytodoapp.databinding.ActivityMainBinding
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import com.example.mytodoapp.presentation.featureTodoList.MainTodoListFragment
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
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        component.injectMainActivity(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.rootContainer, MainTodoListFragment.newInstance()).commit()
    }

    fun updateAppTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        delegate.applyDayNight()
    }
}