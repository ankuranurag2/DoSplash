package dev.ananurag2.dosplash.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import dev.ananurag2.dosplash.R
import dev.ananurag2.dosplash.databinding.ActivityListBinding
import dev.ananurag2.dosplash.model.ImageResponse
import dev.ananurag2.dosplash.model.Resource
import dev.ananurag2.dosplash.ui.adapters.ImageListAdapter
import dev.ananurag2.dosplash.ui.details.DetailsActivity
import dev.ananurag2.dosplash.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListActivity : AppCompatActivity(), RecyclerViewEventListener {

    private lateinit var binding: ActivityListBinding

    private lateinit var mAdapter: ImageListAdapter

    //Lazy dependency injection
    private val viewModel: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getLatestImages(false)
        }

        observeData()
    }

    override fun onItemCLicked(imageResponse: ImageResponse) {
        navigateToDetailsActivity(imageResponse)
    }

    override fun onBottomReached() {
        binding.moreProgressBar.show()
        viewModel.getLatestImages(true)
    }

    private fun observeData() {
        viewModel.randomImageLiveData.observe(this, {
            when (it) {
                is Resource.Success -> {
                    binding.random = it.data
                }
                is Resource.Error -> {
                    showToast(it.message)
                }
            }
        })

        viewModel.imageListLiveData.observe(this, {
            with(binding) {
                moreProgressBar.hide()
                swipeRefreshLayout.isRefreshing = false

                when (it) {

                    is Resource.Success -> {
                        /**
                         *check if lateinit [mAdapter] is initialize or not
                         */
                        if (!this@ListActivity::mAdapter.isInitialized) {
                            mAdapter = ImageListAdapter(this@ListActivity)
                            rvImageList.adapter = mAdapter
                        }
                        tvError.hide()
                        /**
                         * Known issue
                         * List adapter doesn't update list item, if same list object is passed again. Hence [toMutableList] is used
                         * https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-recyclerview
                         */
                        mAdapter.submitList(it.data.toMutableList())
                        rvImageList.show()
                    }

                    is Resource.Error -> {
                        if (!this@ListActivity::mAdapter.isInitialized || (mAdapter.currentList.size ?: 0) == 0)
                            rvImageList.hide()
                        tvError.text = it.message
                        tvError.show()
                    }
                }
            }
        })

        NetworkHelper.getNetworkLiveData(this).observe(this, {
            if (!it)
                showToast(getString(R.string.connection_lost))
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