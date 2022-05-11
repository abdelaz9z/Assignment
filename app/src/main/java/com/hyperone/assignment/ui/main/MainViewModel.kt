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
 * @property _sourceList MutableLiveData<ArrayList<Source>>
 * @property sourceList LiveData<ArrayList<Source>>
 */
class MainViewModel() : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    /**
     * Get the list of sources from the API
     */
    private val _sourceList = MutableLiveData<ArrayList<Source>>().apply {

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
                    value = sources as ArrayList<Source>
                }
            }
        })
    }

    /**
     * Get the list of sources
     */
    val sourceList: LiveData<ArrayList<Source>> = _sourceList

}