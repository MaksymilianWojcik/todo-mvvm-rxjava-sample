package com.mw.todo_mvvm_jetpack_reactive_sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.FragmentTasksBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TasksNavigationDestination
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var dataBinding: FragmentTasksBinding
    private val tasksViewModel: TasksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentTasksBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = tasksViewModel
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        tasksViewModel.navigationDestination.observe(viewLifecycleOwner) {
            when (it) {
                is TasksNavigationDestination.NewTask -> navigateToNewTask()
                else -> Timber.w("Navigation destination not handled")
            }
        }
    }

    private fun navigateToNewTask() {
        // TODO: Pass id for task edit and pass null for new task
        val action = TasksFragmentDirections.actionTasksFragmentToNewTaskFragment()
        findNavController().navigate(action)
    }
}
