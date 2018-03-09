package com.example.test

import android.os.Bundle
import android.support.v4.app.Fragment

class Pp : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): Pp {
            return Pp().apply {
                arguments = Bundle().apply {
                    putInt("ds", 213)
                    putInt("ds", 33)
                }
                setHasOptionsMenu(true)
            }
        }
    }

    var ds: Int = 0;
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        ds = state?.getInt("ds") ?: 0

    }

}


class foo {

    fun dd() {
        Pp.newInstance()
    }

}