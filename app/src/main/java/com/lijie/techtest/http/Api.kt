package com.lijie.techtest.http

import com.lijie.techtest.entity.AmountData
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("action/datastore_search")
    suspend fun getAmountData(
        @Query("resource_id") resource_id: String = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): ListResult<AmountData>

}