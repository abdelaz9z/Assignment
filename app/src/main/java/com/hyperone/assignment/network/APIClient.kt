package com.hyperone.assignment.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * APIClient is a singleton class that provides the Retrofit instance.
 *
 * @since 11/05/2022
 * @author Abdelaziz Daoud
 */
class APIClient {

    /**
     * Get the Retrofit instance
     *
     * @return Retrofit
     */
    fun getRetrofitClient(): Retrofit {

        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    companion object {
        private const val BASE_URL = "https://elsayedmustafa.github.io/"
    }
}