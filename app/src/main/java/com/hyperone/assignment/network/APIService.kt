package com.hyperone.assignment.network

import com.hyperone.assignment.models.Source
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * This is the interface for the API service.
 *
 * @author Abdelaziz Daoud
 * @since 11/05/2022
 */
interface APIService {

    /**
     * This method is used to get the list of sources.
     * - The response is an observable of [Source]
     *
     * @return Observable<List<Source>>
     */
    @GET(VALUE_SOURCES)
    fun getSources(): Observable<List<Source>>

    companion object {
        private const val VALUE_SOURCES = "/HyperoneWebservice/getListOfFilesResponse.json"
    }
}