package dev.ananurag2.dosplash.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dev.ananurag2.dosplash.R
import dev.ananurag2.dosplash.databinding.ActivityDetailsBinding
import dev.ananurag2.dosplash.model.ImageResponse
import dev.ananurag2.dosplash.utils.loadWithCompletionCallBack

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()               //Stop transition until loading is done.
        val binding = DataBindingUtil.setContentView<ActivityDetailsBinding>(this, R.layout.activity_details)

        with(binding) {
            image = intent?.getParcelableExtra<ImageResponse>(IMAGE_DATA_EXTRAS)
            ivClose.setOnClickListener {
                onBackPressed()
            }

            //Manually load Image to observe completion
            ivPost.loadWithCompletionCallBack(image?.urls?.thumb, image?.urls?.regular) {
                //Resume transition after loading is completed
                startPostponedEnterTransition()
            }
        }
    }

    companion object {
        const val IMAGE_DATA_EXTRAS = "image_data"
    }
}