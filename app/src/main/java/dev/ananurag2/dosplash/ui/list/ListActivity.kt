package dev.ananurag2.dosplash.ui.list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import dev.ananurag2.dosplash.R
import dev.ananurag2.dosplash.databinding.ActivityListBinding
import dev.ananurag2.dosplash.model.ImageResponse
import dev.ananurag2.dosplash.model.Resource
import dev.ananurag2.dosplash.ui.adapters.ImageListAdapter
import dev.ananurag2.dosplash.ui.details.DetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    //Lazy dependency injection
    private val viewModel: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        val adapter = ImageListAdapter { navigateToDetailsActivity(it) }
        adapter.let {
            binding.rvImageList.adapter = it
            observeData(it)
        }
    }

    private fun observeData(adapter: ImageListAdapter) {
        viewModel.randomImageLiveData.observe(this, {
            when (it) {
                is Resource.Success -> {
                    binding.random = it.data
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.imageListLiveData.observe(this, {
            when (it) {
                is Resource.Success -> {
                    adapter.submitList(it.data)
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //A function that would be passed to @{@link ListAdapter}. It would be invoked when any itemView is clicked.
    private fun navigateToDetailsActivity(data: ImageResponse) {
        val bundle = bundleOf(DetailsActivity.IMAGE_DATA_EXTRAS to data)
        Intent(this, DetailsActivity::class.java).also {
            it.putExtras(bundle)
        }.let {
            startActivity(it)
        }
    }
}