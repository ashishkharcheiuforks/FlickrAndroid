package com.hucet.flickr.utils

import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.mock
import org.mockito.ArgumentCaptor

inline fun <reified T> createPairObserverCaptor(): Pair<Observer<T>, KArgumentCaptor<T>> {
    val captor = KArgumentCaptor(ArgumentCaptor.forClass(T::class.java), T::class)
    val observer = mock<Observer<T>>()
    return observer to captor
}