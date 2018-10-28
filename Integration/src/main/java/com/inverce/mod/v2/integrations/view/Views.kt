package com.inverce.mod.v2.integrations.view

import android.support.annotation.IdRes
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun textChanged(event: (String) -> Unit) = object : TextWatcher {
    override fun afterTextChanged(p0: Editable?) = event(p0?.toString() ?: "")

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
}

fun EditText.getTextChangedListener(@IdRes res: Int) = this.getTag(res) as? TextWatcher
fun EditText.setTextChangedListener(@IdRes res: Int, watcher: TextWatcher?) {
    getTextChangedListener(res)?.let {
        removeTextChangedListener(it)
    }
    setTag(res, watcher)
    watcher?.let {
        addTextChangedListener(it)
    }
}

