package com.akgarg.newsapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingletonClass constructor(context: Context) {

    companion object {

        @Volatile
        private var INSTANCE: VolleySingletonClass? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingletonClass(context).also {
                    INSTANCE = it
                }
            }
    }


    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }


    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}