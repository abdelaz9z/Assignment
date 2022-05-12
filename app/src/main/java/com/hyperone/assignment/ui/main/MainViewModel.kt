package com.hyperone.assignment.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hyperone.assignment.const.SourceType.VIDEO
import com.hyperone.assignment.models.Source
import com.hyperone.assignment.network.APIClient
import com.hyperone.assignment.network.APIService
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ViewModel for the MainActivity screen.
 *
 * @author Abdelaziz Daoud
 * @since 11/05/2022
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> = _isLoading

    /**
     * MutableStateFlow for the list of video.
     * Returns the list of videos.
     *
     * @return MutableStateFlow<ArrayList<Source>>
     */
    private val _videoList: MutableStateFlow<ArrayList<Source>> =
        MutableStateFlow(ArrayList())

    /**
     * Get the list of sources from the API for video
     */
    val videoList: MutableStateFlow<ArrayList<Source>> = _videoList

    /**
     * MutableStateFlow for the list of pdf.
     * Returns the list of videos.
     *
     * @return MutableStateFlow<ArrayList<Source>>
     */
    private val _pdfList: MutableStateFlow<ArrayList<Source>> =
        MutableStateFlow(ArrayList())

    /**
     * Get the list of sources from the API for pdf
     */
    val pdfList: MutableStateFlow<ArrayList<Source>> = _pdfList

    var videoSources: ArrayList<Source> = ArrayList()
    var pdfSources: ArrayList<Source> = ArrayList()

    /**
     * MutableStateFlow for the list of pdf.
     */
    fun getPdfList() {
        with(_isLoading) { value = true }

        val retro = APIClient().getRetrofitClient().create(APIService::class.java)
        retro.getSources().enqueue(object : retrofit2.Callback<List<Source>> {
            override fun onFailure(call: retrofit2.Call<List<Source>>, throwable: Throwable) {
                with(_isLoading) { value = false }
                val message = throwable.message
                message?.let { Log.e("TAG", it) }
            }

            override fun onResponse(
                call: retrofit2.Call<List<Source>>,
                response: retrofit2.Response<List<Source>>
            ) {
                if (response.isSuccessful) {
                    with(_isLoading) { value = false }
                    val sources: List<Source?>? = response.body()

                    for (source in sources!!) {
                        if (source?.type == "PDF") {
                            pdfSources.add(source)
                        }

                        with(_pdfList) { value = pdfSources }
                    }
                }
            }
        })
    }

    /**
     * MutableStateFlow for the list of video.
     */
    fun getVideoList() {

        with(_isLoading) { value = true }

        val retro = APIClient().getRetrofitClient().create(APIService::class.java)
        retro.getSources().enqueue(object : retrofit2.Callback<List<Source>> {
            override fun onFailure(call: retrofit2.Call<List<Source>>, throwable: Throwable) {
                with(_isLoading) { value = false }

                val message = throwable.message
                message?.let { Log.e("TAG", it) }
            }

            override fun onResponse(
                call: retrofit2.Call<List<Source>>,
                response: retrofit2.Response<List<Source>>
            ) {
                if (response.isSuccessful) {
                    with(_isLoading) { value = false }
                    val sources: List<Source?>? = response.body()

                    for (source in sources!!) {
                        if (source?.type == VIDEO) {
                            videoSources.add(source)
                        }

                        with(_videoList) { value = videoSources }
                    }
                }
            }
        })
    }
}