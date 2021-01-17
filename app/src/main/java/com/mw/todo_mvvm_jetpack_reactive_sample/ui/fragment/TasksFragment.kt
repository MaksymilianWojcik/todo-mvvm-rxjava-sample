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

    //    @SuppressLint("RestrictedApi")
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
//        (activity as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.filterButton.setOnClickListener {
            showFiltersMenu2()
        }
        dataBinding.sortingButton.setOnClickListener {
            showSortingMenu2()
        }
        dataBinding.accountIV.setOnClickListener {
            tasksViewModel.showProfile()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.tasks_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_clear_completed -> {
//                tasksViewModel.clearCompletedTasks()
//                true
//            }
//            R.id.menu_clear_active -> {
//                tasksViewModel.clearActiveTasks()
//                true
//            }
//            R.id.menu_filter -> {
//                showFiltersMenu()
//                true
//            }
//            R.id.menu_sort -> {
//                showSortingMenu()
//                true
//            }
//            else -> false
//        }
//    }

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

    private fun showFiltersMenu2() {
        val view = activity?.findViewById<View>(R.id.filterButton) ?: return
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

    private fun showSortingMenu2() {
        val view = activity?.findViewById<View>(R.id.sortingButton) ?: return
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
