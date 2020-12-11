package com.mw.todo_mvvm_jetpack_reactive_sample.ui.adapters

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * A generic RecyclerView adapter that uses Data Binding & DiffUtil.
 *
 * @param <T> Type of the items in the list
 * @param <V> The type of the ViewDataBinding
 * ListAdapter<T> is a type of the Lists this Adapter will receive.
 * ListAdapter<DataBoundViewHolder<V>> is a class that extends ViewHolder that will be used by the adapter.
 * This will allow us to create any list adapters in the app that will work with data binding
 */
abstract class DataBoundListAdapter<T, V : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, DataBoundViewHolder<V>>(diffCallback) {

    // this creates new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        // without databinding, we would do something like:
        // LayoutInflater.from(parent.context).inflate(layout, parent, false) etc...
        // Here we could also set view's layou params like margin, padding etc
        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    // replaces the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        // without databinding, we would do something like: holder.textView.text = "something"
        bind(holder.binding, getItem(position))
        holder.binding.executePendingBindings()
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    protected abstract fun bind(binding: V, item: T)

}
