package ru.nsu.merkuriev.waterbalance.domain.model

import androidx.annotation.StringRes
import ru.nsu.merkuriev.waterbalance.R

enum class ActiveType(
    @StringRes val title: Int,
    val waterSum: Float
) {
    NO(R.string.active_type_no, 0f),
    LAZY(R.string.active_type_lazy, 300f),
    MIDDLE(R.string.active_type_middle, 600f),
    ACTIVE(R.string.active_type_active, 900f);
}