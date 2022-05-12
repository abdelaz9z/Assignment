package com.hyperone.assignment.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hyperone.assignment.models.Source
import com.hyperone.assignment.network.APIClient
import com.hyperone.assignment.network.APIService

/**
 * ViewModel for the MainActivity screen.
 *
 * @property _isLoading MutableLiveData<Boolean>
 * @property isLoading MutableLiveData<Boolean>
 * @property videoSources ArrayList<Source>
 * @property pdfSources ArrayList<Source>
 * @property _pdfList MutableLiveData<ArrayList<Source>>
 * @property pdfList LiveData<ArrayList<Source>>
 * @property _videoList MutableLiveData<ArrayList<Source>>
 * @property videoList LiveData<ArrayList<Source>>
 */
class MainViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    var videoSources: ArrayList<Source> = ArrayList()
    var pdfSources: ArrayList<Source> = ArrayList()

    /**
     * LiveData for the list of videos.
     * Returns the list of videos.
     *
     * @return LiveData<ArrayList<Source>>
     */
    private val _pdfList = MutableLiveData<ArrayList<Source>>().apply {

        _isLoading.value = true

        val retro = APIClient().getRetrofitClient().create(APIService::class.java)
        retro.getSources().enqueue(object : retrofit2.Callback<List<Source>> {
            override fun onFailure(call: retrofit2.Call<List<Source>>, throwable: Throwable) {
                _isLoading.value = false
                val message = throwable.message
                message?.let { Log.e("TAG", it) }
            }

            override fun onResponse(
                call: retrofit2.Call<List<Source>>,
                response: retrofit2.Response<List<Source>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val sources: List<Source?>? = response.body()

                    for (source in sources!!) {
                        if (source?.type == "PDF") {
                            pdfSources.add(source)
                        }
                        value = pdfSources
                    }


                }
            }
        })
    }

    /**
     * Get the list of sources from the API for pdf
     */
    val pdfList: LiveData<ArrayList<Source>> = _pdfList

    /**
     * Get the list of sources from the API for videos
     *
     * @return LiveData<ArrayList<Source>>
     */
    private val _videoList = MutableLiveData<ArrayList<Source>>().apply {

        _isLoading.value = true

        val retro = APIClient().getRetrofitClient().create(APIService::class.java)
        retro.getSources().enqueue(object : retrofit2.Callback<List<Source>> {
            override fun onFailure(call: retrofit2.Call<List<Source>>, throwable: Throwable) {
                _isLoading.value = false
                val message = throwable.message
                message?.let { Log.e("TAG", it) }
            }

            override fun onResponse(
                call: retrofit2.Call<List<Source>>,
                response: retrofit2.Response<List<Source>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val sources: List<Source?>? = response.body()

                    for (source in sources!!) {
                        if (source?.type == "VIDEO") {
                            videoSources.add(source)
                        }
                        value = videoSources
                    }
                }
            }
        })
    }

    /**
     * Get the list of sources
     */
    val videoList: LiveData<ArrayList<Source>> = _videoList
}