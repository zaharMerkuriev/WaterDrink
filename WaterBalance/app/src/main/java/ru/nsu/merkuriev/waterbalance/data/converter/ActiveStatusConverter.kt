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
            0 -> ActiveType.NO
            1 -> ActiveType.LAZY
            2 -> ActiveType.MIDDLE
            3 -> ActiveType.ACTIVE
            else -> ActiveType.NO
        }
    }
}