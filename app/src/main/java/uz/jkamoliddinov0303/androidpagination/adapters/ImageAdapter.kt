package uz.jkamoliddinov0303.androidpagination.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_image.view.*
import uz.jkamoliddinov0303.androidpagination.R
import uz.jkamoliddinov0303.androidpagination.ViewPagerActivity
import uz.jkamoliddinov0303.androidpagination.models.Photo

class ImageAdapter(private val context: Context) :
    PagedListAdapter<Photo, ImageAdapter.ViewHolder>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(photo: Photo) {
            Picasso.get().load(photo.src?.large).into(itemView.image_view)
            ForResult.addPhotosUrl(photo.src?.large!!)
            Log.d("TAGTESTURL", "${photo.src.medium}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            ForResult.setImgPosition(position)
            context.startActivity(Intent(context, ViewPagerActivity::class.java))
        }
        holder.onBind(getItem(position)!!)
    }
}

interface ForResult {
    companion object {
        private var position = 0
        var listOfUrls = ArrayList<String>()
        fun getPhotosUrl(): ArrayList<String> {
            return listOfUrls
        }

        fun addPhotosUrl(data: String) {
            listOfUrls.add(data)
        }

        fun setImgPosition(position: Int) {
            this.position = position
        }

        fun getImgPosition(): Int {
            return position
        }
    }

}

