package com.hyperone.assignment.network

import com.hyperone.assignment.models.Source
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * APIClient is a singleton class that provides the Retrofit instance.
 *
 * @since 11/05/2022
 * @author Abdelaziz Daoud
 */
class APIClient {

    /**
     * The Retrofit instance.
     */
    private var sourceInterface: APIService? = null
    private var instance: APIClient? = null

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        sourceInterface = retrofit.create(APIService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://elsayedmustafa.github.io/"
    }

    /**
     * Returns the Retrofit instance.
     *
     * @return APIClient?
     */
    fun getInstance(): APIClient? {
        if (null == instance) {
            instance = APIClient()
        }
        return instance
    }


    /**
     * Returns the Observable of Source.
     *
     * @return Observable<Source>
     */
    fun getSources(): Observable<List<Source>>? {
        return sourceInterface?.getSources()
    }
}