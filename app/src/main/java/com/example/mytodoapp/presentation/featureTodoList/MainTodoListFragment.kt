package com.example.mytodoapp.presentation.featureTodoList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.databinding.FragmentMainTodoListBinding
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import javax.inject.Inject

/**
 * This class represents a fragment that displays the main todo list in an Android application.
 * It handles the user interface elements and logic for viewing, editing, and managing todo items.
 * The class follows the single responsibility principle by focusing on the specific task of managing
 * the main todo list.
 */

class MainTodoListFragment() : Fragment() {

    private lateinit var binding: FragmentMainTodoListBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel
    private var fragmentViewComponent: MainTodoListFragmentViewController? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent
            .mainTodoListComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.initLifecycleOwner(viewLifecycleOwner)
        fragmentViewComponent = MainTodoListFragmentViewController(
            this,
            binding,
            viewLifecycleOwner,
            viewModel
        ).apply {
            initViews()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentViewComponent = null
    }


    companion object {

        @JvmStatic
        fun newInstance() = MainTodoListFragment()
    }
}