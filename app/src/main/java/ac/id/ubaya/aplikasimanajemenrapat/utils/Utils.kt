package ac.id.ubaya.aplikasimanajemenrapat.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
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