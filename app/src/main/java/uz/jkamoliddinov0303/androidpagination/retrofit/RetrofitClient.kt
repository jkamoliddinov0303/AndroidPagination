package uz.jkamoliddinov0303.androidpagination.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private const val BASE_URL = "https://api.pexels.com/v1/"
        const val API_KEY = "563492ad6f917000010000012c2048d5012548eb9c7b6cb67db12e40"
        fun getRetrofit(): Retrofit {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }
    }
}