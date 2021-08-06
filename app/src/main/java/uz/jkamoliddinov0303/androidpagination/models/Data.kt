package uz.jkamoliddinov0303.androidpagination.models

import com.google.gson.annotations.SerializedName

data class Data(

    @field:SerializedName("next_page")
    val nextPage: String? = null,

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("photos")
    val photos: List<Photo?>? = null,

    @field:SerializedName("total_results")
    val totalResults: Int? = null
)


