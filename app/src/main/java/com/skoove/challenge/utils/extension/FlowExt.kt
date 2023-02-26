package com.skoove.challenge.utils.extension

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.mutate(mutateFunction: T.() -> T) {
    value = value.mutateFunction()
}
