package com.md.gamepractical.contract

import android.graphics.Bitmap
import com.google.firebase.ml.vision.text.FirebaseVisionText

open class TextConversionContract {

    interface Presenter  {
        fun processTextRecognitionResult(texts: FirebaseVisionText): ArrayList<String>
        fun runTextRecognition()
        fun updateImage(selectedImage: Bitmap?)
    }
}