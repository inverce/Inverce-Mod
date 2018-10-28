package com.inverce.mod.v2.integrations.okhttp

import android.arch.lifecycle.Lifecycle
import com.inverce.mod.v2.core.threadpool.Disposer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.enqueue(callback: SmartCallback<T>.() -> Unit): Call<T> = apply {
    this.enqueue(SmartCallback<T>().apply(callback))
}

class CallResponse<T>(val call: Call<T>, var response: Response<T>)
class CallFailure<T>(val call: Call<T>, var t: Throwable, var response: Response<T>? = null)
open class SmartCallback<T>(
        var onSuccess: CallResponse<T>.() -> Unit = {},
        var onFailure: CallFailure<T>.() -> Unit = {},
        var disposer: Disposer? = null
) : Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) {
        disposer?.remove(call)
        onFailure(CallFailure(call, t))
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        disposer?.remove(call)
        if (response.isSuccessful) {
            onSuccess(CallResponse(call, response))
        } else {
            onFailure(CallFailure(call, Exception("Code is ${response.code()}"), response))
        }
    }
}

fun <T : Any> Disposer.add(task: Call<T>, time: Lifecycle.Event = Lifecycle.Event.ON_PAUSE) {
    this.add(task, time) {
        if (!it.isCanceled) {
            it.cancel()
        }
    }
}