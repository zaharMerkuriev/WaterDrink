package ru.nsu.merkuriev.waterbalance.presentation.common.custom

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class NonFilterableAdapter<T>(context: Context, resource: Int, objects: List<T>) :
    ArrayAdapter<T>(context, resource, objects) {

    private val filter: Filter = NonFilterableFilter()
    private val items: List<T> = objects

    override fun getFilter(): Filter {
        return filter
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