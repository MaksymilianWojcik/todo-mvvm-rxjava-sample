package com.mw.todo_mvvm_jetpack_reactive_sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.FragmentNewTaskBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel.NewTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : Fragment() {

    private lateinit var dataBinding: FragmentNewTaskBinding
    private val newTaskViewModel: NewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentNewTaskBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = newTaskViewModel
        }
        return dataBinding.root
    }
}
