package com.hqy.commonlibrary.utils.qr

import android.graphics.Bitmap
import android.graphics.Color

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

import java.util.Hashtable

/**
 * citsin
 * 2019/4/1
 */
object QrCodeUtil {

    fun create(text: String, w: Int, h: Int): Bitmap {
        val hints = Hashtable<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        hints[EncodeHintType.MARGIN] = 1
        val m = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, w, h)
        val matrix = deleteWhitePadding(m)//删除白边
        val width = matrix.width
        val height = matrix.height

        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = Color.BLACK
                } else {
                    pixels[y * width + x] = Color.WHITE
                }
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap

    }


    private fun deleteWhitePadding(matrix: BitMatrix): BitMatrix {
        val rec = matrix.enclosingRectangle
        val resWidth = rec[2] + 1
        val resHeight = rec[3] + 1

        val resMatrix = BitMatrix(resWidth, resHeight)
        resMatrix.clear()

        for (i in 0 until resWidth) {
            for (j in 0 until resHeight) {
                if (matrix.get(i + rec[0], j + rec[1])) {
                    resMatrix.set(i, j)
                }
            }
        }
        return resMatrix
    }

}
