package uz.jkamoliddinov0303.androidpagination

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import uz.jkamoliddinov0303.androidpagination.adapters.ForResult
import uz.jkamoliddinov0303.androidpagination.adapters.ImageAdapter
import uz.jkamoliddinov0303.androidpagination.viewModels.PagingImageViewModel

class MainActivity : AppCompatActivity() {
    lateinit var imageViewModel: PagingImageViewModel
    lateinit var imageAdapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageAdapter = ImageAdapter(this)
        imageViewModel = ViewModelProviders.of(this)[PagingImageViewModel::class.java]

        btn_search.setOnClickListener {
            val query = et_search.text.toString()
            ForResult.listOfUrls.clear()
            imageViewModel.getSearchImages(query).observe(this, Observer {
                imageAdapter.submitList(it)
            })
        }

        recyclerview.layoutManager = GridLayoutManager(this, 2)
        recyclerview.adapter = imageAdapter
    }
}