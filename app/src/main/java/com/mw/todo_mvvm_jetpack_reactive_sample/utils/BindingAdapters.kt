package com.mw.todo_mvvm_jetpack_reactive_sample.utils

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.adapters.TasksAdapter

// TODO: Consider renaming this to be more generic
@BindingAdapter("app:completedTask")
fun setStyle(textView: TextView, enabled: Boolean) {
    if (enabled) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

// TODO: Consider moving this to TasksAdapter
@BindingAdapter("app:items")
fun setItems(recyclerView: RecyclerView, items: List<Task>) {
    // This is not necessary anymore, as we provide the default emptyList in TasksViewModel for _allTasks
//    items?.let { (recyclerView.adapter as TasksAdapter).submitList(items) }
    (recyclerView.adapter as TasksAdapter).submitList(items)
}
