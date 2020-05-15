package ru.nsu.merkuriev.waterbalance.presentation.common.view.adapter

import android.content.Context
import ru.nsu.merkuriev.waterbalance.databinding.ItemActiveTypeBinding
import ru.nsu.merkuriev.waterbalance.domain.model.ActiveType
import ru.nsu.merkuriev.waterbalance.presentation.common.custom.NonFilterableAdapter

class ActiveTypeAdapter(
    context: Context,
    resource: Int,
    objects: List<ActiveType>
) : NonFilterableAdapter<ActiveType, ItemActiveTypeBinding>(context, resource, objects) {

    override fun bind(binding: ItemActiveTypeBinding, item: ActiveType) {
        super.bind(binding, item)

        binding.text.text = context.getString(item.title)
    }
}