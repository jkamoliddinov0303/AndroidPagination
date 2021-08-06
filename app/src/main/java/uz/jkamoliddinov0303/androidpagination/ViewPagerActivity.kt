package uz.jkamoliddinov0303.androidpagination

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_view_pager.*
import uz.jkamoliddinov0303.androidpagination.adapters.ForResult
import uz.jkamoliddinov0303.androidpagination.adapters.ViewPagerAdapter


class ViewPagerActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var photosUrls: ArrayList<String>
    private var referenceID: Long = 0
    private var downloadManager: DownloadManager? = null

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)


        viewPager = findViewById(R.id.view_pager)
        photosUrls = ForResult.getPhotosUrl()
        val viewPagerAdapter = ViewPagerAdapter(this, photosUrls)
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = ForResult.getImgPosition()

        btn_download.setOnClickListener {
            btn_download.isEnabled = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkPermission()) {
                    /*** If Storage Permission Is Given, Check External storage is available for read and write */
                    val imageUri =
                        Uri.parse(photosUrls[viewPager.currentItem])
                    referenceID = downloadImage(imageUri)
                } else {
                    requestPermission()
                }
            } else {
                Toast.makeText(
                    this@ViewPagerActivity,
                    "Permission Is Granted..",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val imageDownloadQuery = DownloadManager.Query()
                //set the query filter to our previously Enqueued download
                imageDownloadQuery.setFilterById(referenceID)

                //Query the download manager about downloads that have been requested.
                val cursor = downloadManager!!.query(imageDownloadQuery)
                if (cursor.moveToFirst()) {
                    Toast.makeText(
                        this@ViewPagerActivity,
                        downloadStatus(cursor),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun downloadStatus(cursor: Cursor): String {

        //column for download  status
        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val status = cursor.getInt(columnIndex)
        //column for reason code if the download failed or paused
        val columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
        val reason = cursor.getInt(columnReason)
        var statusText = ""
        var reasonText = ""
        when (status) {
            DownloadManager.STATUS_FAILED -> {
                statusText = "STATUS_FAILED"
                when (reason) {
                    DownloadManager.ERROR_CANNOT_RESUME -> reasonText = "ERROR_CANNOT_RESUME"
                    DownloadManager.ERROR_DEVICE_NOT_FOUND -> reasonText = "ERROR_DEVICE_NOT_FOUND"
                    DownloadManager.ERROR_FILE_ALREADY_EXISTS -> reasonText =
                        "ERROR_FILE_ALREADY_EXISTS"
                    DownloadManager.ERROR_FILE_ERROR -> reasonText = "ERROR_FILE_ERROR"
                    DownloadManager.ERROR_HTTP_DATA_ERROR -> reasonText = "ERROR_HTTP_DATA_ERROR"
                    DownloadManager.ERROR_INSUFFICIENT_SPACE -> reasonText =
                        "ERROR_INSUFFICIENT_SPACE"
                    DownloadManager.ERROR_TOO_MANY_REDIRECTS -> reasonText =
                        "ERROR_TOO_MANY_REDIRECTS"
                    DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> reasonText =
                        "ERROR_UNHANDLED_HTTP_CODE"
                    DownloadManager.ERROR_UNKNOWN -> reasonText = "ERROR_UNKNOWN"
                }
            }
            DownloadManager.STATUS_PAUSED -> {
                statusText = "STATUS_PAUSED"
                when (reason) {
                    DownloadManager.PAUSED_QUEUED_FOR_WIFI -> reasonText = "PAUSED_QUEUED_FOR_WIFI"
                    DownloadManager.PAUSED_UNKNOWN -> reasonText = "PAUSED_UNKNOWN"
                    DownloadManager.PAUSED_WAITING_FOR_NETWORK -> reasonText =
                        "PAUSED_WAITING_FOR_NETWORK"
                    DownloadManager.PAUSED_WAITING_TO_RETRY -> reasonText =
                        "PAUSED_WAITING_TO_RETRY"
                }
            }
            DownloadManager.STATUS_PENDING -> statusText = "STATUS_PENDING"
            DownloadManager.STATUS_SUCCESSFUL -> {
                statusText = "Image Saved Successfully"
                //reasonText = "Filename:\n" + filename;
                Toast.makeText(
                    this@ViewPagerActivity,
                    "Download Status:\n$statusText\n$reasonText",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btn_download.isEnabled = true
        return statusText + reasonText
    }

    private fun downloadImage(uri: Uri): Long {
        val downloadReference: Long
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(uri)
        //Setting title of request
        request.setTitle("Image Download")

        //Setting description of request
        request.setDescription("Image download using DownloadManager.")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "sample2.jpg")
//        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadReference = downloadManager!!.enqueue(request)
        return downloadReference
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this@ViewPagerActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@ViewPagerActivity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val imageUri =
                    Uri.parse(photosUrls[viewPager.currentItem])
                referenceID = downloadImage(imageUri)
            } else {
                Toast.makeText(
                    this@ViewPagerActivity,
                    "Permission Denied... \n You Should Allow External Storage Permission To Download Images.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}