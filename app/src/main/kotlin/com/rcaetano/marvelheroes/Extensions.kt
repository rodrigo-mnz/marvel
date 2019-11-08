package com.rcaetano.marvelheroes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
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

fun View.hideKeyboard() {
    context?.let {
        val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun Fragment.showToast(@StringRes message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
