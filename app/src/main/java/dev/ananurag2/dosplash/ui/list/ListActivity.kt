package dev.ananurag2.dosplash.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
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

    //[Boolean] flag to avoid pagination while in searchMode
    private var inSearchMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (!inSearchMode) {
                if (binding.random == null)
                    viewModel.getRandomImage()
                viewModel.getLatestImages(false)
            } else
                binding.swipeRefreshLayout.isRefreshing = false
        }

        observeData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val search = menu?.findItem(R.id.toolbar_search)
        val searchView = (search?.actionView as? SearchView)
        searchView?.setOnQueryTextListener(
            DebouncingQueryTextListener(this.lifecycle, { query ->
                val queryLength = query?.length ?: 0
                if (queryLength > 3) {
                    inSearchMode = true
                    viewModel.searchForQuery(query!!)
                } else if (queryLength == 0) {
                    inSearchMode = false
                    viewModel.resetSearch()
                }
            }, {
                inSearchMode = false
                searchView.clearFocus()
            })
        )
        return true
    }

    override fun onItemCLicked(imageResponse: ImageResponse, v1: View, v2: View) {
        navigateToDetailsActivity(imageResponse, v1, v2)
    }

    override fun onBottomReached() {
        if (!inSearchMode) {
            binding.moreProgressBar.show()
            viewModel.getLatestImages(true)
        }
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
                        if (it.data.size > 0) {
                            /**
                             * Known issue
                             * List adapter doesn't update list item, if same list object is passed again. Hence [toMutableList] is used
                             * https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-recyclerview
                             */
                            mAdapter.submitList(it.data.toMutableList())
                            tvEmpty.hide()
                            rvImageList.show()
                        } else {
                            rvImageList.hide()
                            tvEmpty.show()
                        }
                    }

                    is Resource.Error -> {
                        if (!this@ListActivity::mAdapter.isInitialized || (mAdapter.currentList.size ?: 0) == 0){
                            rvImageList.hide()
                            tvEmpty.show()
                        }
                        showSnackBar(it.message)
                    }
                }
            }
        })

        NetworkHelper.getNetworkLiveData(this).observe(this, {
            if (!it)
                showSnackBar(getString(R.string.connection_lost))
        })
    }

    /**
     *  It would be invoked when any itemView is clicked.
     *  Opens [DetailsActivity] with sharedElementTransitions
     */
    private fun navigateToDetailsActivity(data: ImageResponse, v1: View, v2: View) {
        val p1 = Pair.create(v1, ViewCompat.getTransitionName(v1))
        val p2 = Pair.create(v2, ViewCompat.getTransitionName(v2))
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2)
        val bundle = bundleOf(DetailsActivity.IMAGE_DATA_EXTRAS to data)
        Intent(this, DetailsActivity::class.java).also {
            it.putExtras(bundle)
        }.let {
            startActivity(it, options.toBundle())
        }
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
}