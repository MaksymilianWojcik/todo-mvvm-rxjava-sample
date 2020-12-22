package com.mw.todo_mvvm_jetpack_reactive_sample.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.FragmentTasksBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.adapters.TasksAdapter
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TaskFilterType
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TasksNavigationDestination
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.RuntimeException

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var dataBinding: FragmentTasksBinding
    private val tasksViewModel: TasksViewModel by viewModels()

    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentTasksBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = tasksViewModel
        }
        setHasOptionsMenu(true)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_clear_completed -> {
                tasksViewModel.clearCompletedTasks()
                true
            }
            R.id.menu_clear_active -> {
                tasksViewModel.clearActiveTasks()
                true
            }
            R.id.menu_filter -> {
                showFilters()
                true
            }
            else -> false
        }
    }

    private fun showFilters() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.tasks_filter_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.allTasksFilter -> tasksViewModel.setFilters(TaskFilterType.ALL_TASKS)
                    R.id.activeTasksFilter -> tasksViewModel.setFilters(TaskFilterType.ACTIVE_TASKS)
                    R.id.completedTasksFilter -> tasksViewModel.setFilters(TaskFilterType.COMPLETED_TASKS)
                }
                true
            }
            show()
        }
    }

    private fun setupRecyclerView() {
        // TODO: bindingadapter for this
        tasksAdapter = TasksAdapter(tasksViewModel)
        dataBinding.tasksList.adapter = tasksAdapter
    }

    private fun setupUI() {
        tasksViewModel.navigationDestination.observe(viewLifecycleOwner) {
            when (it) {
                is TasksNavigationDestination.NewTask -> navigateToNewTask()
                else -> Timber.w("Navigation destination not handled")
            }
        }
        tasksViewModel.tasks.observe(viewLifecycleOwner) {
            // TODO: bindingadapter for this
            tasksAdapter.submitList(it)
        }
    }

    private fun navigateToNewTask() {
        // TODO: Pass id for task edit and pass null for new task
        val action = TasksFragmentDirections.actionTasksFragmentToNewTaskFragment()
        findNavController().navigate(action)
    }
}
