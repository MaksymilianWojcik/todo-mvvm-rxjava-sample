package com.mw.todo_mvvm_jetpack_reactive_sample.utils

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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

@BindingAdapter("app:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

// Example of settingAdapter via custom BindingAdapter. Please note, that in xml this has to be
// above the [setItems] BindingAdapter, otherwise you will get an error, because adapter is not
// yet defined
@BindingAdapter("app:setAdapter")
fun RecyclerView.adapter(adapter: RecyclerView.Adapter<*>) {
    with(this) {
        setHasFixedSize(false)
        this.adapter = adapter
    }
}

// TODO: Consider moving this to TasksAdapter
@BindingAdapter("app:items")
fun setItems(recyclerView: RecyclerView, items: List<Task>) {
    (recyclerView.adapter as TasksAdapter).submitList(items)
}


@BindingAdapter("attachFloatingButton")
fun bindRecyclerViewWithFab(recyclerView: RecyclerView, fb: ExtendedFloatingActionButton) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0 && fb.isExtended) {
                fb.shrink()
            } else if (dy < 0 && !fb.isExtended) {
                fb.extend()
            }
        }
    })
}

@Suppress("UNCHECKED_CAST")
// Warning: Unchecked cast: RecyclerView.Adapter<(raw) RecyclerView.ViewHolder!>? to ListAdapter<Any, RecyclerView.ViewHolder>
@BindingAdapter("app:itemsGeneric")
fun RecyclerView.itemsGeneric(items: List<Any>) {
    (adapter as? ListAdapter<Any, RecyclerView.ViewHolder>)?.submitList(items)
}
// We could solve unchecked casts for example with adding interfaces, maybe we will do an example
/*

/**
 * Implement on RecyclerView.Adapter that should be set by databinding
 */
interface BindableAdapter<T> {
    fun setData(data: T)
}

@BindingAdapter("data")
fun <T> setData(recyclerView: RecyclerView, data: T) {
    if (recyclerView.adapter !is BindableAdapter<*>) return
    (recyclerView.adapter as BindableAdapter<T>).setData(data)
}

 */
