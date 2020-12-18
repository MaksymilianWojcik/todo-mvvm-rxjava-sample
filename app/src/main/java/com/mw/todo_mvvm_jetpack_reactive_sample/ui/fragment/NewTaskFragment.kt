package com.mw.todo_mvvm_jetpack_reactive_sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.FragmentNewTaskBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel.NewTaskViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : BindingFragment<FragmentNewTaskBinding>(R.layout.fragment_new_task) {

    private val newTaskViewModel: NewTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        view.setupSnackbar(this, newTaskViewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    }

    private fun setupUI() {
        newTaskViewModel.closeScreen.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    override fun getViewModel(): ViewModel? = newTaskViewModel
}
