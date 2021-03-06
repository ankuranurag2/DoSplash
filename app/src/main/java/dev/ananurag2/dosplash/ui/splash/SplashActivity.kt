package dev.ananurag2.dosplash.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dev.ananurag2.dosplash.R
import dev.ananurag2.dosplash.databinding.ActivitySplashBinding
import dev.ananurag2.dosplash.ui.list.ListActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIMEOUT = 800L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        binding.ivSplash.postDelayed({
            Intent(this, ListActivity::class.java).also {
                //clear all the previous activities in the activity stack
                it.flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }.let {
                startActivity(it)
            }
        }, SPLASH_TIMEOUT)
    }
}