package com.rcaetano.marvelheroes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.rcaetano.marvelheroes.data.model.Response


inline fun <T> LiveData<T>.subscribe(
    lifecycle: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
) {
    observe(lifecycle, Observer { it?.run(onChanged) })
}

inline fun <T : Any> apiCall(call: () -> T) =
    try {
        Response.Success(call())
    } catch (e: Exception) {
        Response.Error(e)
    }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
