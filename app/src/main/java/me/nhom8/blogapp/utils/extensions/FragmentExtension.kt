package me.nhom8.blogapp.utils.extensions

import androidx.fragment.app.Fragment

fun Fragment.clearFocus() {
    activity?.currentFocus?.clearFocus()
}
