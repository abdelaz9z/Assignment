package com.hyperone.assignment.network

import com.hyperone.assignment.models.Source
import retrofit2.Call
import retrofit2.http.GET

/**
 * This is the interface for the API service.
 *
 * @author Abdelaziz Daoud
 * @since 11/05/2022
 */
interface APIService {

    /**
     * Get all sources  from the API server using retrofit 2
     *
     * @return Call<List<Source>>
     */
    @GET(VALUE_SOURCES)
    fun getSources(): Call<List<Source>>

    companion object {
        private const val VALUE_SOURCES = "/HyperoneWebservice/getListOfFilesResponse.json"
    }
}