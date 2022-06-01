package com.mhmdawad.instabugassignment.common.extention

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View


fun View.copyToClipBoard(text: String, showToast: () -> Unit) {
    setOnClickListener {
        val clipboard: ClipboardManager? =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(text, text)
        clipboard?.setPrimaryClip(clip)
        showToast()
    }
}


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}