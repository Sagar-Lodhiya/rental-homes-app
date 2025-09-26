package com.rentalhomes.data.network.logginginterceptor

import android.text.TextUtils
import com.rentalhomes.data.network.Keys.AUTHORIZATION
import com.rentalhomes.data.network.logginginterceptor.I.Log
import okhttp3.Request
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class Logger internal constructor() {
    companion object {
        private const val JSON_INDENT = 4
        private val LINE_SEPARATOR = System.getProperty("line.separator")
        private val DOUBLE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR
        private fun isEmpty(line: String): Boolean {
            return TextUtils.isEmpty(line) || line == "\n" || line == "\t" || TextUtils.isEmpty(line.trim { it <= ' ' })
        }

        fun printJsonRequest(builder: LoggingInterceptor.Builder, request: Request) {
            val requestBody = LINE_SEPARATOR + "Body:" + LINE_SEPARATOR + bodyToString(request)
            val tag = builder.getTag(true)
            Log(
                builder.type, tag,
                "╔══════ Request ════════════════════════════════════════════════════════════════════════"
            )
            logLines(builder.type, tag, getRequest(request))
            if (builder.level === Level.BASIC || builder.level === Level.HEADERS || builder.level === Level.BODY) {
                logLines(
                    builder.type,
                    tag,
                    requestBody.split(LINE_SEPARATOR.toRegex()).toTypedArray()
                )
            }
            Log(
                builder.type, tag,
                "╚═══════════════════════════════════════════════════════════════════════════════════════"
            )
        }

        fun printJsonResponse(
            builder: LoggingInterceptor.Builder,
            chainMs: Long,
            isSuccessful: Boolean,
            code: Int,
            headers: String,
            bodyString: String
        ) {
            val responseBody = LINE_SEPARATOR + "Body:" + LINE_SEPARATOR + getJsonString(bodyString)
            val tag = builder.getTag(false)
            Log(
                builder.type, tag,
                "╔══════ Response ═══════════════════════════════════════════════════════════════════════"
            )
            logLines(
                builder.type,
                tag,
                getResponse(headers, chainMs, code, isSuccessful, builder.level)
            )
            if (builder.level === Level.BASIC || builder.level === Level.HEADERS || builder.level === Level.BODY) {
                logLines(
                    builder.type,
                    tag,
                    responseBody.split(LINE_SEPARATOR.toRegex()).toTypedArray()
                )
            }
            Log(
                builder.type, tag,
                "╚═══════════════════════════════════════════════════════════════════════════════════════"
            )
        }

        private fun getRequest(request: Request): Array<String> {
            val message: String =
                "URL: " + request.url() + DOUBLE_SEPARATOR + "HEADER: " + request.headers()
                    .get(AUTHORIZATION)
            return message.split(LINE_SEPARATOR.toRegex()).toTypedArray()
        }

        private fun getResponse(
            header: String,
            tookMs: Long,
            code: Int,
            isSuccessful: Boolean,
            level: Level
        ): Array<String> {
            val message: String = "Result is Successful: $isSuccessful"
            return message.split(LINE_SEPARATOR.toRegex()).toTypedArray()
            //        String message;
//        boolean loggableHeader = level == Level.HEADERS || level == Level.BASIC;
//        message = ("Result is Successful: " + isSuccessful + DOUBLE_SEPARATOR + "Status Code: " +
//                code + DOUBLE_SEPARATOR + (isEmpty(header) ? "" : loggableHeader ? "Headers:" + LINE_SEPARATOR +
//                dotHeaders(header) : "") + LINE_SEPARATOR + "Received in: " + tookMs + "ms");
//        return message.split(LINE_SEPARATOR);
        }

        private fun dotHeaders(header: String): String {
            val headers = header.split(LINE_SEPARATOR.toRegex()).toTypedArray()
            val builder = StringBuilder()
            for (item in headers) {
                builder.append("- ").append(item).append("\n")
            }
            return builder.toString()
        }

        private fun logLines(type: Int, tag: String, lines: Array<String>) {
            for (line in lines) {
                Log(type, tag, "║ $line")
            }
        }

        private fun bodyToString(request: Request): String {
            return try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                if (copy.body() == null) return ""
                copy.body()!!.writeTo(buffer)
                getJsonString(buffer.readUtf8())
            } catch (e: IOException) {
                "{\"err\": \"" + e.message + "\"}"
            }
        }

        fun getJsonString(msg: String): String {
            val message: String = try {
                when {
                    msg.startsWith("{") -> {
                        val jsonObject = JSONObject(msg)
                        jsonObject.toString(JSON_INDENT)
                    }
                    msg.startsWith("[") -> {
                        val jsonArray = JSONArray(msg)
                        jsonArray.toString(JSON_INDENT)
                    }
                    else -> {
                        msg
                    }
                }
            } catch (e: JSONException) {
                msg
            }
            return message
        }
    }

    init {
        throw UnsupportedOperationException("you can't instantiate me")
    }
}