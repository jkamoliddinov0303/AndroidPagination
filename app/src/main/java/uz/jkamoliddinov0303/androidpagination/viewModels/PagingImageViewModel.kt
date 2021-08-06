package uz.jkamoliddinov0303.androidpagination.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import uz.jkamoliddinov0303.androidpagination.dataSource.ImageDataSourceFactory
import uz.jkamoliddinov0303.androidpagination.models.Photo
import java.util.concurrent.Executors

class PagingImageViewModel : ViewModel() {
    fun getSearchImages(query: String): LiveData<PagedList<Photo>> {
        val imageDataSourceFactory = ImageDataSourceFactory(query)
        val executor = Executors.newSingleThreadExecutor()
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(50)
            .build()
        return LivePagedListBuilder<Int, Photo>(imageDataSourceFactory, pagedListConfig)
            .setFetchExecutor(executor!!)
            .build()
    }
}