package com.example.expensetrack.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.io.FileOutputStream


@SuppressLint("NewApi")
fun Context.getPath(uri: Uri): String? {

    val isKitKat = true
    var selection: String? = null
    var selectionArgs: Array<String>? = null
    if (isKitKat) {
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            val fullPath = getPathFromExtSD(split)
            return if (fullPath !== "") {
                fullPath
            } else {
                null
            }
        }
        if (isDownloadsDocument(uri)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var cursor: Cursor? = null
                try {
                    cursor = contentResolver.query(
                        uri,
                        arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                        null,
                        null,
                        null
                    )
                    if (cursor != null && cursor.moveToFirst()) {
                        val fileName = cursor.getString(0)
                        val path = Environment.getExternalStorageDirectory()
                            .toString() + "/Download/" + fileName
                        if (!TextUtils.isEmpty(path)) {
                            return path
                        }
                    }
                } finally {
                    cursor?.close()
                }
                val id: String = DocumentsContract.getDocumentId(uri)
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:".toRegex(), "")
                    }
                    val contentUriPrefixesToTry = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                    )
                    for (contentUriPrefix in contentUriPrefixesToTry) {
                        return try {
                            val contentUri = ContentUris.withAppendedId(
                                Uri.parse(contentUriPrefix),
                                java.lang.Long.valueOf(id)
                            )
                            getDataColumn(this, contentUri, null, null)
                        } catch (e: NumberFormatException) {
                            uri.path!!.replaceFirst("^/document/raw:".toRegex(), "")
                                .replaceFirst("^raw:".toRegex(), "")
                        }
                    }
                }
            } else {
                val id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                try {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(this, contentUri, null, null)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
        }
        if (isMediaDocument(uri)) {
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
            selection = "_id=?"
            selectionArgs = arrayOf(split[1])
            return getDataColumn(
                this, contentUri, selection,
                selectionArgs
            )
        }
        if (isGoogleDriveUri(uri)) {
            return getDriveFilePath(uri)
        }
        if (isWhatsAppFile(uri)) {
            return getFilePathForWhatsApp(uri)
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            if (isGooglePhotosUri(uri)) {
                return uri.lastPathSegment
            }
            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri)
            }
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                copyFileToInternalStorage(uri, "userfiles")
            } else {
                getDataColumn(this, uri, null, null)
            }
        }
        if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
    } else {
        if (isWhatsAppFile(uri)) {
            return getFilePathForWhatsApp(uri)
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA
            )
            val cursor: Cursor?
            try {
                cursor = contentResolver
                    .query(uri, projection, selection, selectionArgs, null)
                val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
                cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    return null
}

private fun fileExists(filePath: String): Boolean {
    val file = File(filePath)
    return file.exists()
}

private fun getPathFromExtSD(pathData: Array<String>): String {
    val type = pathData[0]
    val relativePath = "/" + pathData[1]
    var fullPath: String
    if ("primary".equals(type, ignoreCase = true)) {
        fullPath = Environment.getExternalStorageDirectory().toString() + relativePath
        if (fileExists(fullPath)) {
            return fullPath
        }
    }
    fullPath = System.getenv("SECONDARY_STORAGE") ?: "" + relativePath
    if (fileExists(fullPath)) {
        return fullPath
    }
    fullPath = System.getenv("EXTERNAL_STORAGE") ?: "" + relativePath
    return if (fileExists(fullPath)) {
        fullPath
    } else fullPath
}

private fun Context.getDriveFilePath(uri: Uri): String {
    val returnCursor = contentResolver.query(uri, null, null, null, null)
    val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    val size = returnCursor.getLong(sizeIndex).toString()
    val file = File(cacheDir, name)
    try {
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read: Int
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable = inputStream!!.available()
        //int bufferSize = 1024;
        val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream.read(buffers).also { read = it } != -1) {
            outputStream.write(buffers, 0, read)
        }
        Log.e("File Size", "Size " + file.length())
        inputStream.close()
        outputStream.close()
        returnCursor.close()
        Log.e("File Path", "Path " + file.path)
        Log.e("File Size", "Size " + file.length())
    } catch (e: Exception) {
        Log.e("Exception", e.message!!)
    }
    return file.path
}
private fun Context.copyFileToInternalStorage(uri: Uri, newDirName: String): String {
    val returnCursor = contentResolver.query(
        uri, arrayOf(
            OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
        ), null, null, null
    )
    val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    val size = returnCursor.getLong(sizeIndex).toString()
    val output: File = if (newDirName != "") {
        val dir = File("$filesDir/$newDirName")
        if (!dir.exists()) {
            dir.mkdir()
        }
        File("$filesDir/$newDirName/$name")
    } else {
        File("$filesDir/$name")
    }
    try {
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(output)
        var read: Int
        val bufferSize = 1024
        val buffers = ByteArray(bufferSize)
        while (inputStream!!.read(buffers).also { read = it } != -1) {
            outputStream.write(buffers, 0, read)
        }
        inputStream.close()
        outputStream.close()
        returnCursor.close()
    } catch (e: Exception) {
        Log.e("Exception", e.message!!)
    }
    return output.path
}

private fun Context.getFilePathForWhatsApp(uri: Uri): String {
    return copyFileToInternalStorage(uri, "whatsapp")
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
        cursor = context.contentResolver.query(
            uri!!, projection,
            selection, selectionArgs, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
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

private fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

private fun isWhatsAppFile(uri: Uri): Boolean {
    return "com.whatsapp.provider.media" == uri.authority
}

private fun isGoogleDriveUri(uri: Uri): Boolean {
    return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
}