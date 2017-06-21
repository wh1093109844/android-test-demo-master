package com.example.gankio.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.example.gankio.R
import kotlinx.android.synthetic.main.activity_view_image.*

/**
 * Created by za-wanghe on 2017/6/19.
 */

class ImageDetailActivity : AppCompatActivity() {

    val TAG = ImageDetailActivity::class.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        setSupportActionBar(toolbar)
        val url = intent.getStringExtra("url")
        photoView.setImageUri(url)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.i(TAG, "menuItem ID:${item?.itemId}")
        when(item?.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
