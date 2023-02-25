package com.skoove.challenge.data

import com.skoove.challenge.data.response.ApiResponse
import com.skoove.challenge.data.response.ManifestModel
import retrofit2.Retrofit
import retrofit2.http.GET

class Repository(private val retrofit: Retrofit) {

    private val skooveAPI = retrofit.create(SkooveAPI::class.java)
    suspend fun getAudioEntries(): ApiResponse<ManifestModel> =
        ApiResponse(data = skooveAPI.getManifest())
}

interface SkooveAPI {
    @GET("manifest.json")
    suspend fun getManifest(): ManifestModel
}
