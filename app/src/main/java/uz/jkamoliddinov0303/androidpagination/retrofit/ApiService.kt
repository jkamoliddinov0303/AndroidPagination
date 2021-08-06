package uz.jkamoliddinov0303.androidpagination.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import uz.jkamoliddinov0303.androidpagination.models.Data

interface ApiService {
    @GET("search")
    fun getImages(
        @Header("Authorization") auth: String = RetrofitClient.API_KEY,
        @Query("query") query: String,
        @Query("page") page: Int
    ):
            Call<Data>
}