package com.skoove.challenge.utils.extension

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * For mutating our Flows containing [com.skoove.challenge.ui.State].
 */
fun <T> MutableStateFlow<T>.mutate(mutateFunction: T.() -> T) {
    value = value.mutateFunction()
}
