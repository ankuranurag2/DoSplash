package dev.ananurag2.dosplash.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ananurag2.dosplash.model.ImageResponse
import dev.ananurag2.dosplash.model.Resource
import dev.ananurag2.dosplash.repository.ImageRepository
import dev.ananurag2.dosplash.utils.NetworkHelper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * created by ankur on 21/10/20
 */
class ListViewModel(private val repository: ImageRepository) : ViewModel() {
    private val _imageStore = ArrayList<ImageResponse>()
    private val _randomImageLiveData = MutableLiveData<Resource<ImageResponse>>()

    private var pageNum = 1

    val imageListLiveData = MutableLiveData<Resource<ArrayList<ImageResponse>>>()
    val randomImageLiveData: MutableLiveData<Resource<ImageResponse>> get() = _randomImageLiveData

    //[CoroutineExceptionHandler] for Latest Images API call
    private val coroutineExceptionHandlerLatest = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        imageListLiveData.postValue(Resource.error("Please check your connection!"))
    }

    //[CoroutineExceptionHandler] for Random Image API call
    private val coroutineExceptionHandlerRandom = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    //Call the APIs for the very first time when ViewModel is instantiated
    init {
        getRandomImage()
        getLatestImages(false)
    }

    /**
     * If [isLoadMore]==true, then it is pagination call for next page
     * else it is call for fetching new images
     */
    fun getLatestImages(isLoadMore: Boolean) {
        if (!NetworkHelper.getNetworkStatus()) {
            imageListLiveData.postValue(Resource.error("It seems you are not connected to Internet!"))
            return
        }

        if (isLoadMore)
            pageNum++
        else
            pageNum = 1

        //override the Dispatcher, as default one for `viewModelScope` is {@link Dispatchers.Main}
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandlerLatest) {
            val response = repository.getLatestImages(pageNum)
            if (response.isSuccessful && response.body() != null) {
                if (!isLoadMore)
                    _imageStore.clear()
                _imageStore.addAll(response.body()!!)
                imageListLiveData.postValue(Resource.success(_imageStore))
            } else
                imageListLiveData.postValue(Resource.error(if (pageNum > 1) "Failed to fetch more images!" else "Failed to fetch images!"))
        }
    }

    fun getRandomImage() {
        if (!NetworkHelper.getNetworkStatus()) return

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandlerRandom) {
            val response = repository.getRandomImage()
            if (response.isSuccessful && response.body() != null) {
                _randomImageLiveData.postValue(Resource.success(response.body()!!))
            } else
                _randomImageLiveData.postValue(Resource.error("Failed to fetch header image!"))
        }
    }

    fun resetSearch() {
        imageListLiveData.postValue(Resource.success(_imageStore))
    }

    fun searchForQuery(query: String) {
        viewModelScope.launch {
            if (_imageStore.isNullOrEmpty())
                imageListLiveData.postValue(Resource.success(_imageStore))
            else {
                val filteredList = repository.getFilterList(query, _imageStore)
                imageListLiveData.postValue(Resource.success(ArrayList(filteredList)))
            }
        }
    }
}