package ru.nsu.merkuriev.waterbalance.presentation.common.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class NonFilterableAdapter<T, Binding : ViewDataBinding>(
    context: Context,
    resource: Int,
    objects: List<T>
) :
    ArrayAdapter<T>(context, resource, objects) {

    private val filter: Filter = NonFilterableFilter()
    private val items: List<T> = objects
    private val resourceLayout = resource

    override fun getFilter(): Filter {
        return filter
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = getBinding(convertView, parent)
        bind(binding, items[position])
        return binding.root
    }

    protected open fun bind(binding: Binding, item: T) {}

    protected open fun createBinding(parent: ViewGroup): Binding {
        return DataBindingUtil.inflate(LayoutInflater.from(context), resourceLayout, parent, false)
    }

    private fun getBinding(convertView: View?, parent: ViewGroup): Binding {
        return convertView?.let { DataBindingUtil.getBinding<Binding>(it) } ?: createBinding(parent)
    }

    private inner class NonFilterableFilter : Filter() {
        override fun performFiltering(arg0: CharSequence?): FilterResults {
            val result = FilterResults()
            result.values = items
            result.count = items.size
            return result
        }

        override fun publishResults(arg0: CharSequence?, arg1: FilterResults?) {
            notifyDataSetChanged()
        }
    }
}