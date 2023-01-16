package com.lomamo.lomamo3.models

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class ProductionCountries (

  @SerializedName("iso_3166_1" ) var iso31661 : String? = null,
  @SerializedName("name"       ) var name     : String? = null

)