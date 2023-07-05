//package com.example.mytodoapp.presentation.viewmodels
//
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//
//import com.example.mytodoapp.App
//
///**
// * This class represents a ViewModelFactory that is responsible for creating instances of ViewModels.
// * It implements the ViewModelProvider.Factory interface.
// * The factory creates specific instances of ViewModels based on the requested modelClass.
// */
//class ViewModelFactory(
//    private val app: App
//) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val viewModel = when (modelClass) {
//            MainViewModel::class.java -> {
//                MainViewModel(locale(), locale(), locale())
//            }
//            TodoItemViewModel::class.java ->
//            {
//                TodoItemViewModel(locale(), locale(), locale())
//            }
//            else -> {
//                throw IllegalStateException("Unknown ViewModel")
//            }
//        }
//        return viewModel as T
//    }
//}
//fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)