package uz.jkamoliddinov0303.androidpagination.dataSource

import androidx.paging.DataSource
import uz.jkamoliddinov0303.androidpagination.models.Photo


class ImageDataSourceFactory(private val search:String):DataSource.Factory<Int,Photo>() {
    lateinit var imageDataSource: ImageDataSource
    override fun create(): DataSource<Int, Photo> {
        imageDataSource = ImageDataSource(search)
        return imageDataSource
    }
}