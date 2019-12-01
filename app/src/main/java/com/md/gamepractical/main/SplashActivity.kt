package com.md.gamepractical.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.md.gamepractical.R
import com.md.gamepractical.Utils
import com.md.gamepractical.activity.MainActivity

class SplashActivity : AppCompatActivity() {
    @BindView(R.id.bulb)
    lateinit var bulb: ImageView;

    @BindView(R.id.letsgo)
    lateinit var letsgo: Button;

    @BindView(R.id.playdemo)
    lateinit var playdemo: Button;

    private var mUnBinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ButterKnife.bind(this)
        startAnimation()

        letsgo.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Utils.SOURCE, Utils.EXPLORE)
            startActivity(intent)
        })

        playdemo.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Utils.SOURCE, Utils.DEMO)
            startActivity(intent)
        })
    }

    private fun startAnimation() {
        val animation = ObjectAnimator.ofFloat(bulb, "rotationY", 0.0f, 360f)
        animation.duration = 5000
        animation.repeatCount = ObjectAnimator.INFINITE
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mUnBinder != null) {
            mUnBinder?.unbind();
        }
    }
}