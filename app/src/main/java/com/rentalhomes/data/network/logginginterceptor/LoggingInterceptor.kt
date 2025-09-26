package com.rentalhomes.data.network.logginginterceptor

import android.text.TextUtils
import android.util.Log
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class LoggingInterceptor(private val builder: Builder) : Interceptor {
    private val loggable: Boolean = builder.isDebug

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = if (builder.headers.size() > 0) {
            chain.request().newBuilder().headers(builder.headers).build()
        } else {
            chain.request()
        }
        if (!loggable || builder.level == Level.NONE) {
            return chain.proceed(request)
        }
        Logger.printJsonRequest(builder, request)
        val st = System.nanoTime()
        val response = chain.proceed(request)
        val chainMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - st)
        val headers = response.headers().toString()
        val code = response.code()
        val isSuccessful = response.isSuccessful
        val bodyString = Logger.getJsonString(
            response.body()!!.string()
        )
        Logger.printJsonResponse(builder, chainMs, isSuccessful, code, headers, bodyString)
        val cloneRequest = chain.request()
        var contentType: MediaType? = null
        if (cloneRequest.body() != null) contentType = cloneRequest.body()!!.contentType()
        val body = ResponseBody.create(contentType, bodyString)
        return response.newBuilder().body(body).build()
    }

    class Builder {
        var tag = TAG_JSON
            private set
        var isDebug = false
        var type = Log.ERROR
            private set
        var requestTag: String? = null
            private set
        var responseTag: String? = null
            private set
        var level = Level.BASIC
            private set
        private val builder: Headers.Builder = Headers.Builder()
        val headers: Headers
            get() = builder.build()

        fun getTag(isRequest: Boolean): String {
            return if (isRequest) {
                if (TextUtils.isEmpty(requestTag)) tag else requestTag!!
            } else {
                if (TextUtils.isEmpty(responseTag)) tag else responseTag!!
            }
        }

        fun addHeader(name: String?, value: String?): Builder {
            builder.add(name, value)
            return this
        }

        fun setLevel(level: Level): Builder {
            this.level = level
            return this
        }

        fun tag(tag: String): Builder {
            this.tag = tag
            return this
        }

        fun loggable(isDebug: Boolean): Builder {
            this.isDebug = isDebug
            return this
        }

        fun build(): LoggingInterceptor {
            return LoggingInterceptor(this)
        }

        fun log(type: Int): Builder {
            this.type = type
            return this
        }

        fun request(tag: String?): Builder {
            requestTag = tag
            return this
        }

        fun response(tag: String?): Builder {
            responseTag = tag
            return this
        }

        companion object {
            private const val TAG_JSON = "LoggingI"
        }

    }

}