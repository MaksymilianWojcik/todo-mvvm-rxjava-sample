package com.mw.todo_mvvm_jetpack_reactive_sample.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.databinding.TaskItemBinding
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel.TasksViewModel

class TasksAdapter(
    private val viewModel: TasksViewModel
) : DataBoundListAdapter<Task, TaskItemBinding>(TaskDiffUtilCallback) {

    override fun createBinding(parent: ViewGroup): TaskItemBinding =
        TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: TaskItemBinding, item: Task) {
        binding.viewModel = viewModel
        binding.task = item
        binding.executePendingBindings()
    }
}

object TaskDiffUtilCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}
