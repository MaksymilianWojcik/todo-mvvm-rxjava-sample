package com.mw.todo_mvvm_jetpack_reactive_sample.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.FragmentTasksBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.adapters.TasksAdapter
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TaskFilterType
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TaskSortingType
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

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
            tasksAdapter = TasksAdapter(tasksViewModel)
        }
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(dataBinding.customToolbar)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksViewModel.filterType.observe(viewLifecycleOwner) {
            dataBinding.customToolbar.title = when (it) {
                TaskFilterType.ALL_TASKS -> getText(R.string.menu_all_title)
                TaskFilterType.ACTIVE_TASKS -> getText(R.string.menu_active_title)
                TaskFilterType.COMPLETED_TASKS -> getText(R.string.menu_completed_title)
            }
        }
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
                showFiltersMenu()
                true
            }
            R.id.menu_sort -> {
                showSortingMenu()
                true
            }
            else -> false
        }
    }

    private fun showFiltersMenu() {
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

    private fun showSortingMenu() {
        val view = activity?.findViewById<View>(R.id.menu_sort) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.tasks_sort_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.ascSorting -> tasksViewModel.setSorting(TaskSortingType.ASCENDING)
                    R.id.descSorting -> tasksViewModel.setSorting(TaskSortingType.DESCENDING)
                }
                true
            }
            show()
        }
    }
}
