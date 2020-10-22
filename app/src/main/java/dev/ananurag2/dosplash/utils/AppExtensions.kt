package dev.ananurag2.dosplash.utils

import android.content.Context
import android.view.View
import android.widget.Toast

/**
 * created by ankur on 22/10/20
 */
fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}