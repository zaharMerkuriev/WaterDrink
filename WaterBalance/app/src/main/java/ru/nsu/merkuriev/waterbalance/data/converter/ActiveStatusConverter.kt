package ru.nsu.merkuriev.waterbalance.data.converter

import androidx.room.TypeConverter
import ru.nsu.merkuriev.waterbalance.domain.model.ActiveType

class ActiveStatusConverter {
    @TypeConverter
    fun fromActiveType(value: ActiveType): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toActiveType(value: Int): ActiveType {
        return when (value) {
            ActiveType.NO.ordinal -> ActiveType.NO
            ActiveType.LAZY.ordinal -> ActiveType.LAZY
            ActiveType.MIDDLE.ordinal -> ActiveType.MIDDLE
            ActiveType.ACTIVE.ordinal -> ActiveType.ACTIVE
            else -> ActiveType.NO
        }
    }
}