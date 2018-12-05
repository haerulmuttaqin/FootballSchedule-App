package com.haerul.footballapp.ui.main

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.haerul.footballapp.R
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            startActivity(intentFor<HomeActivity>().singleTop())
            overridePendingTransition(R.anim.zoom_in_from_view, R.anim.zoom_out_from_view)
            finish()
        }, 1000)
    }
}