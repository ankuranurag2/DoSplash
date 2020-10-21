package dev.ananurag2.dosplash.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dev.ananurag2.dosplash.R
import dev.ananurag2.dosplash.databinding.ActivityDetailsBinding
import dev.ananurag2.dosplash.model.ImageResponse

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityDetailsBinding>(this, R.layout.activity_details)

        with(binding) {
            image = intent?.getParcelableExtra<ImageResponse>(IMAGE_DATA_EXTRAS)
            ivClose.setOnClickListener {
                onBackPressed()
            }
        }
    }

    companion object {
        const val IMAGE_DATA_EXTRAS = "image_data"
    }
}