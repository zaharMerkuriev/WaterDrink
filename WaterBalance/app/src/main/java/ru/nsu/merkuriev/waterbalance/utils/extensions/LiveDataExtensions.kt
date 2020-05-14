package ru.nsu.merkuriev.waterbalance.utils.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T1 : Any, T2 : Any, T3 : Any, R> MediatorLiveData<R>.combine(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    condition: (t1: T1?, t2: T2?, t3: T3?) -> R
) {
    addSource(source1) { t: T1? ->
        value = condition(t, source2.value, source3.value)
    }

    addSource(source2) { t: T2? ->
        value = condition(source1.value, t, source3.value)
    }

    addSource(source3) { t: T3? ->
        value = condition(source1.value, source2.value, t)
    }
}
