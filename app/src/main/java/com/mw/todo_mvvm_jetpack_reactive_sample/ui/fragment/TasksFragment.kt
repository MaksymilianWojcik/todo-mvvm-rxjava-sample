package com.mw.todo_mvvm_jetpack_reactive_sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.filterButton.setOnClickListener { showFiltersMenu() }
        dataBinding.sortingButton.setOnClickListener { showSortingMenu() }
    }

    private fun showFiltersMenu() {
        PopupMenu(requireContext(), dataBinding.filterButton).run {
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
        PopupMenu(requireContext(), dataBinding.sortingButton).run {
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
