package com.md.gamepractical.activity

import android.app.Activity
import android.content.Context;
import android.content.Intent
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity
import android.view.View;
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.md.gamepractical.R
import java.io.IOException;
import java.io.InputStream;
import com.md.gamepractical.Utils
import com.md.gamepractical.presenters.ImageAndTextConversion
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    @BindView(R.id.image_view)
    lateinit var mImageView: ImageView;

    @BindView(R.id.explore)
    lateinit var explore: Button;

    @BindView(R.id.spinner)
    lateinit var dropdown: Spinner;

    @BindView(R.id.upload)
    lateinit var upload: Button;

    private var mSelectedImage: Bitmap? = null;

    private var GET_FROM_GALLERY = 3

    private var textConversion: ImageAndTextConversion? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        val source: String = intent.getStringExtra(Utils.SOURCE)
        initListeners();
        textConversion = ImageAndTextConversion(this);
        if (source.equals(Utils.DEMO)) {
            demoListeners()
        } else {
            exploreListeners();
        }
    }

    fun initListeners() {
        explore?.setOnClickListener(View.OnClickListener {
                textConversion.let {
                    it?.runTextRecognition()
                }
        });
    }

    fun demoListeners() {
        var items: Array<String> = arrayOf(Utils.DEMO_SIMPLE, Utils.DEMO_COMPLEX);
        var adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown?.setAdapter(adapter);
        dropdown?.setOnItemSelectedListener(this);
        upload.visibility = View.GONE
    }

    fun exploreListeners() {
        dropdown.visibility = View.GONE

        upload.setOnClickListener(View.OnClickListener {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ), GET_FROM_GALLERY
            )
        })
    }


    fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun udpateButtonView(enable: Boolean) {
        explore?.setEnabled(enable);
    }

    fun getBitmapFromAsset(context: Context, filePath: String): Bitmap? {
        val assetManager = context.assets

        val `is`: InputStream
        var bitmap: Bitmap? = null
        try {
            `is` = assetManager.open(filePath)
            bitmap = BitmapFactory.decodeStream(`is`)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                mSelectedImage = getBitmapFromAsset(this, Utils.DEMO_SIMPLE_IMAGE);
            }
            1 -> {
                mSelectedImage = getBitmapFromAsset(this, Utils.DEMO_LARGE_IMAGE);
            }
        }

        textConversion!!.updateImage(mSelectedImage);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImage = data!!.data
            try {
                Picasso.get().load(selectedImage).into(object : Target {
                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        if (e != null) {
                            showToast(e.message!!)
                        }
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        textConversion!!.updateImage(bitmap)
                        mImageView.setImageBitmap(bitmap)
                    }
                });

            } catch (e: Exception) {
                if (e != null) {
                    showToast(e.message!!)
                }
            }
        }
    }

    fun playGame(filterwords: ArrayList<String>) {
        var i = Intent(this, DragDropActivity::class.java)
        i.putExtra(Utils.DRAG_DROP, filterwords)
        startActivity(i);
    }

    fun getImageView(): ImageView {
        return mImageView;
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}
