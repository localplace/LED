package com.md.gamepractical.presenters
import android.graphics.Bitmap
import android.util.Pair
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.md.gamepractical.activity.MainActivity
import com.md.gamepractical.contract.TextConversionContract.Presenter
import java.lang.StringBuilder

class ImageAndTextConversion (var view: MainActivity) : Presenter {

    private var mSelectedImage: Bitmap? = null;

    private val filterwords = ArrayList<String>();

    private var mImageMaxWidth: Int? = null

    private var mImageMaxHeight: Int? = null

    override fun processTextRecognitionResult(texts: FirebaseVisionText): ArrayList<String> {
        val blocks = texts.getTextBlocks();
        val fillinBlanks = ArrayList<String>()
        if (blocks.size == 0) {
            view.showToast("No Items");
            return fillinBlanks;
        }
        for (i in blocks) {
            val builder = StringBuilder()
            val lines = i.lines
            for (j in lines) {
                val elements = j.getElements()
                for (k in elements) {
                    builder.append(k.text + " ")
                }
            }
            fillinBlanks.add(builder.toString())
        }
        return fillinBlanks
    }

    override fun runTextRecognition() {
        val image = FirebaseVisionImage.fromBitmap(mSelectedImage!!);
        filterwords.clear()
        val recognizer = FirebaseVision.getInstance()
            .getOnDeviceTextRecognizer();
        view.udpateButtonView(false);
        recognizer.processImage(image).addOnSuccessListener {

            view.udpateButtonView(true);
            val fillinblanks = processTextRecognitionResult(it);

            for (fill in fillinblanks) {
                var seperate = fill.split(".")
                for (each in seperate) {
                    var each = each.trim();
                    if (each.length > 0 && Character.isUpperCase(each.get(0)) && each.length > 30) {
                        filterwords.add(each);
                    }
                }
            }

            view.playGame(filterwords);
        }.addOnFailureListener {
            view.udpateButtonView(true);
        }

    }

    private fun getTargetedWidthHeight(): Pair<Int, Int> {
        val targetWidth: Int
        val targetHeight: Int
        val maxWidthForPortraitMode = getImageMaxWidth()
        val maxHeightForPortraitMode = getImageMaxHeight()
        targetWidth = maxWidthForPortraitMode
        targetHeight = maxHeightForPortraitMode
        return Pair(targetWidth, targetHeight)
    }

    fun getImageMaxWidth(): Int {
        if (mImageMaxWidth == null) {
            mImageMaxWidth = view.getImageView().getWidth();
        }
        return mImageMaxWidth!!;
    }

    fun getImageMaxHeight(): Int {
        if (mImageMaxHeight == null) {
            mImageMaxHeight = view.getImageView()!!.getHeight();
        }

        return mImageMaxHeight!!;
    }

    override fun updateImage(mSelectedImage :  Bitmap?) {
        this.mSelectedImage = mSelectedImage
        if (mSelectedImage != null) {
            val targetedSize = getTargetedWidthHeight();

            val targetWidth = targetedSize.first;
            val maxHeight = targetedSize.second;

            val scaleFactor: Double =
                Math.max(
                    (mSelectedImage!!.getWidth() * 1.0 / targetWidth * 1.0),
                    mSelectedImage!!.getHeight() * 1.0 / maxHeight * 1.0
                );

            val resizedBitmap =
                Bitmap.createScaledBitmap(
                    mSelectedImage,
                    (mSelectedImage!!.getWidth() / scaleFactor).toInt(),
                    (mSelectedImage!!.getHeight() / scaleFactor).toInt(),
                    true
                );

            view.mImageView.setImageBitmap(resizedBitmap);
            this.mSelectedImage = resizedBitmap;
        }
    }

}