package com.lijie.techtest.entity

import com.google.gson.annotations.SerializedName

data class AmountData(

    @SerializedName("volume_of_mobile_data")
    var volumeOfMobileData: String? = null,
    var quarter: String? = null,

    @SerializedName("_id")
    var id: Int? = null

)