package ru.nsu.merkuriev.waterbalance.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val name: String,
    val weight: Double,
    val age: Int,
    val activeType: ActiveType
) : Parcelable