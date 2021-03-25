package com.lijie.techtest.entity

import com.google.gson.annotations.SerializedName

data class AmountData(

    @SerializedName("volume_of_mobile_data")
    var volumeOfMobileData: Double = 0.0,
    var quarter: String? = null,

    @SerializedName("_id")
    var id: Int? = null

)