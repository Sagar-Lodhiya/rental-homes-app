package com.rentalhomes.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object FileUtils {
    @SuppressLint("NewApi")
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + split[1]
                }

// TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

// Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /*-----------------------------------------------------------------------------------------*/ //for Compression
    private const val TAG = "FileUtils"
    fun saveTempFile(fileName: String?, context: Context, uri: Uri?): File? {
        var mFile: File? = null
        val resolver = context.contentResolver
        var `in`: InputStream? = null
        var out: FileOutputStream? = null
        try {
            `in` = resolver.openInputStream(uri!!)
            mFile = File(
                Environment.getExternalStorageDirectory().path + File.separator + Constant.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + Constant.VIDEO_COMPRESSOR_TEMP_DIR,
                fileName
            )
            out = FileOutputStream(mFile, false)
            val buffer = ByteArray(1024)
            var read: Int
            while (`in`!!.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
            out.flush()
        } catch (e: IOException) {
            Log.e(TAG, "", e)
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    Log.e(TAG, "", e)
                }
            }
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    Log.e(TAG, "", e)
                }
            }
        }
        return mFile
    }

    fun createApplicationFolder() {
        var f = File(
            Environment.getExternalStorageDirectory(),
            File.separator + Constant.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME
        )
        f.mkdirs()
        f = File(
            Environment.getExternalStorageDirectory(),
            File.separator + Constant.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + Constant.VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR)

        f.mkdirs()
        f = File(
            Environment.getExternalStorageDirectory(),
            File.separator + Constant.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + Constant.VIDEO_COMPRESSOR_TEMP_DIR
        )
        f.mkdirs()
    }
}