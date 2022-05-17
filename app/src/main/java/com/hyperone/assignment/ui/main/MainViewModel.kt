package com.hyperone.assignment.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hyperone.assignment.const.SourceType.VIDEO
import com.hyperone.assignment.models.Source
import com.hyperone.assignment.network.APIClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
 * @property _sourceList MutableLiveData<ArrayList<Source>>
 * @property sourceList LiveData<ArrayList<Source>>
 */
class MainViewModel : ViewModel() {

    /**
     * CompositeDisposable to dispose all the disposables in one place
     */
    private lateinit var compositeDisposable: CompositeDisposable

    /**
     * MutableStateFlow to hold the state of the loading process in the view model
     * and to be used in the view to show the loading progress
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> = _isLoading

    /**
     * MutableStateFlow for the list of video and pdf.
     * Returns the list of videos.
     *
     * @return MutableStateFlow<ArrayList<Source>>
     */
    private val _sourceList: MutableStateFlow<ArrayList<Source>> =
        MutableStateFlow(ArrayList())

    /**
     * Get the list of sources from the API for video and pdf
     */
    val sourceList: MutableStateFlow<ArrayList<Source>> = _sourceList

    /**
     * Get the list of videos from the API
     * @return ArrayList<Source> list of videos from the API
     */
    var videoSources: ArrayList<Source> = ArrayList()
    var pdfSources: ArrayList<Source> = ArrayList()

    /**
     * Get the list of sources from the API for video and pdf
     */
    fun getSourceList() {
        with(_isLoading) { value = true }

        val retro = APIClient().getInstance()

        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            retro?.getSources()!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    with(_isLoading) { value = false }
                    val sources: List<Source?>? = it

                    // Get the list of videos and pdf
                    sources?.let {
                        for (source in it) {
                            if (source?.type == VIDEO) {
                                // Add the video source to the list
                                videoSources.add(source)
                            } else {
                                // Add the pdf source to the list
                                pdfSources.add(source!!)
                            }
                        }
                        // Add the video and pdf sources to the list
                        with(_sourceList) { value = videoSources }
                        with(_sourceList) { value = pdfSources }
                    }
                }, { it ->
                    with(_isLoading) { value = false }
                    val message = it.message
                    message?.let { Log.e("MainViewModel", "Error: $it") }
                })
        )
    }
}