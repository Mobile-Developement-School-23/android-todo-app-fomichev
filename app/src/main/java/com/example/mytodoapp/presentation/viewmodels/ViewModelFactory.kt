package com.example.mytodoapp.presentation.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.data.network.locale
import com.example.mytodoapp.presentation.App

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(locale(), locale(), locale())
            }
            TodoItemViewModel::class.java ->
            {
                TodoItemViewModel(locale(), locale(), locale())
            }
            else -> {
                throw IllegalStateException("Unknown ViewModel")
            }
        }
        return viewModel as T
    }
}
fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)