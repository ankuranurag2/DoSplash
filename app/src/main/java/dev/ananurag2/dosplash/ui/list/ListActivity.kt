package dev.ananurag2.dosplash.ui.list

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dev.ananurag2.dosplash.R
import dev.ananurag2.dosplash.databinding.ActivityListBinding
import dev.ananurag2.dosplash.model.Resource
import dev.ananurag2.dosplash.ui.adapters.ImageListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    private val viewModel: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        ImageListAdapter().let {
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
                is Resource.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.imageListLiveData.observe(this, {
            when (it) {
                is Resource.Success -> {
                    adapter.submitList(it.data)
                }
                is Resource.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}