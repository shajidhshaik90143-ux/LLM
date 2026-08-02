package com.example.llm.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun String.truncate(maxLength: Int = 100): String {
    return if (this.length > maxLength) "${this.take(maxLength)}..." else this
}