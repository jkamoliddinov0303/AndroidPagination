package uz.jkamoliddinov0303.androidpagination.dataSource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import uz.jkamoliddinov0303.androidpagination.models.Data
import uz.jkamoliddinov0303.androidpagination.models.Photo
import uz.jkamoliddinov0303.androidpagination.retrofit.ApiService
import uz.jkamoliddinov0303.androidpagination.retrofit.RetrofitClient

class ImageDataSource(private val search:String) : PageKeyedDataSource<Int, Photo>() {
    val apiService = RetrofitClient.getRetrofit().create(ApiService::class.java)
    val START_PAGE = 1
    val PAGE_SIZE = 50
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        Log.d("TAGTESTSEARCH", search)
        apiService.getImages(page = START_PAGE,query = search).enqueue(object : retrofit2.Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.photos!!, null, START_PAGE + 1)
                }

            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
            }

        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        apiService.getImages(page = params.key,query = search).enqueue(object : retrofit2.Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    val nextPageKey: Int? = if (params.key == PAGE_SIZE) {
                        params.key + 1
                    } else {
                        null
                    }
                    callback.onResult(response.body()?.photos!!, nextPageKey)
                    val result = response.body()?.photos!!
                    for (i in result){
                        Log.d("TAGRESULT", "${i.toString()}")
                    }
                }

            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
            }

        })
    }
}