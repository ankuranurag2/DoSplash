package dev.ananurag2.dosplash.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dev.ananurag2.dosplash.model.ErrorResponse
import dev.ananurag2.dosplash.model.ImageResponse
import dev.ananurag2.dosplash.model.Resource
import dev.ananurag2.dosplash.repository.ImageRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.net.ConnectException
import java.net.UnknownHostException
import javax.crypto.AEADBadTagException

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
        imageListLiveData.postValue(Resource.error(handleNetworkError(throwable)))
    }

    //[CoroutineExceptionHandler] for Random Image API call
    private val coroutineExceptionHandlerRandom = CoroutineExceptionHandler { _, throwable ->
        _randomImageLiveData.postValue(Resource.error(handleNetworkError(throwable)))
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
                imageListLiveData.postValue(Resource.error(handleApiError(response.errorBody())))
        }
    }

    fun getRandomImage() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandlerRandom) {
            val response = repository.getRandomImage()
            if (response.isSuccessful && response.body() != null) {
                _randomImageLiveData.postValue(Resource.success(response.body()!!))
            } else
                _randomImageLiveData.postValue(Resource.error(handleApiError(response.errorBody())))
        }
    }

    fun resetSearch() {
        if (_imageStore.isNullOrEmpty())
            return
        imageListLiveData.postValue(Resource.success(_imageStore))
    }

    fun searchForQuery(query: String) {
        if (_imageStore.isNullOrEmpty())
            return
        viewModelScope.launch {
            val filteredList = repository.getFilterList(query, _imageStore)
            imageListLiveData.postValue(Resource.success(ArrayList(filteredList)))
        }
    }

    private fun handleApiError(errorBody: ResponseBody?): String {
        errorBody ?: return "Something went wrong!"
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<ErrorResponse> = moshi.adapter(ErrorResponse::class.java)
        val errorResponse = adapter.fromJson(errorBody.string())
        return errorResponse?.errorList?.first() ?: "Something went wrong!"
    }

    private fun handleNetworkError(throwable: Throwable): String {
        return when (throwable) {
            is ConnectException -> "Connection Interrupted."
            is UnknownHostException -> "Please check you internet connection."
            is AEADBadTagException -> "Decryption key not valid."
            else -> "Failed to fetch images!"
        }
    }
}