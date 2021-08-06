package uz.jkamoliddinov0303.androidpagination.adapters


import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso

class ViewPagerAdapter(private var context: Context, private var listOfUrls: ArrayList<String>) :
    PagerAdapter() {
    override fun getCount(): Int {
        return listOfUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): ImageView {
        val imageView = ImageView(context)
        Picasso.get().load(listOfUrls[position])
            .into(imageView)

        container.addView(imageView)
        return imageView
    }
}