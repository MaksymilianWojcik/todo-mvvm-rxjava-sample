package com.mw.todo_mvvm_jetpack_reactive_sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.FragmentNewTaskBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel.NewTaskViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.setupSnackbar
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, newTaskViewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    }
}
