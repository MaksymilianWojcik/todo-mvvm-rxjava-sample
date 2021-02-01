package com.mw.todo_mvvm_jetpack_reactive_sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.FragmentNewTaskRedesignedBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel.NewTaskViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : Fragment() {

    private lateinit var dataBinding: FragmentNewTaskRedesignedBinding
    private val newTaskViewModel: NewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentNewTaskRedesignedBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = newTaskViewModel
        }
        setHasOptionsMenu(true) // TODO: Add delete menu item when in edit mode
        (activity as AppCompatActivity).setSupportActionBar(dataBinding.customToolbar)
        dataBinding.customToolbar.setupWithNavController(findNavController())
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupSnackbar()
        val itemsMock = listOf<String>("Alo", "Salut", "Vejseprej")
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.item_category, itemsMock)
        dataBinding.categorySelector.setAdapter(adapter)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, newTaskViewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    }

    private fun setupUI() {
        newTaskViewModel.closeScreen.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
}
