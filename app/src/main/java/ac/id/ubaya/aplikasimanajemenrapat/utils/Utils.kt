package ac.id.ubaya.aplikasimanajemenrapat.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun reduceImage(bitmap: Bitmap): ByteArrayOutputStream {
    val bmpStreamFinal = ByteArrayOutputStream()
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 500000 && compressQuality >= 0)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStreamFinal)
    return bmpStreamFinal
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = reduceImage(bitmap)
    val imageByte = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(imageByte, Base64.DEFAULT)
}

fun convertDateFormat(date: Date): String {
    val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}

fun convertDateFormatWithoutTime(date: Date): String {
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(date)
}

fun convertTimeFormat(date: Date): String {
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}

fun getFile(context: Context, uri: Uri): File {
    val destinationFilename = File(
        (context.filesDir.path + File.separatorChar) + queryName(
            context,
            uri
        )
    )
    try {
        context.contentResolver.openInputStream(uri).use { ins ->
            if (ins != null) {
                createFileFromStream(
                    ins,
                    destinationFilename
                )
            }
        }
    } catch (ex: Exception) {
        Log.e("Save File", ex.message.toString())
        ex.printStackTrace()
    }
    return destinationFilename
}

fun createFileFromStream(ins: InputStream, destination: File?) {
    try {
        FileOutputStream(destination).use { os ->
            val buffer = ByteArray(4096)
            var length: Int
            while (ins.read(buffer).also { length = it } > 0) {
                os.write(buffer, 0, length)
            }
            os.flush()
        }
    } catch (ex: Exception) {
        Log.e("Save File", ex.message.toString())
        ex.printStackTrace()
    }
}

private fun queryName(context: Context, uri: Uri): String {
    val returnCursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
    val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name: String = returnCursor.getString(nameIndex)
    returnCursor.close()
    return name
}

fun convertNumber(number: Int): String {
    val numberText = number.toString()
    val numberSize = numberText.length

    if (numberSize >= 2) {
        return if (numberText[numberSize - 1].toString() == "1" && numberText[numberSize - 2].toString() != "1") {
            numberText + "st"
        } else if (numberText[numberSize - 1].toString() == "2" && numberText[numberSize - 2].toString() != "1") {
            numberText + "nd"
        } else if (numberText[numberSize - 1].toString() == "3" && numberText[numberSize - 2].toString() != "1") {
            numberText + "rd"
        } else {
            numberText + "th"
        }
    } else {
        return if (numberText[numberSize - 1].toString() == "1") {
            numberText + "st"
        } else if (numberText[0].toString() == "2") {
            numberText + "nd"
        } else if (numberText[0].toString() == "3") {
            numberText + "rd"
        } else {
            numberText + "th"
        }
    }
}