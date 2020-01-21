package com.example.kotlincamerasample

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.mylibrary.FirstClass
import com.example.mylibrary.Sample


class MainActivity : AppCompatActivity(), Sample {
    private var txt: TextView? = null
    private var enble: CheckBox? = null
    private var btn: Button? = null
    private var img: ImageView? = null
    private var thumbnailimg: ImageView? = null
    private var video: VideoView? = null
    private var activity: Activity? = null
    private var context: Context? = null
    private var wantVideo: Boolean? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        context = this

        btn = findViewById<View>(R.id.btn) as Button
        txt = findViewById<View>(R.id.txt) as TextView
        img = findViewById<View>(R.id.img) as ImageView
        thumbnailimg = findViewById<View>(R.id.thumbnailimg) as ImageView
        video = findViewById<View>(R.id.video) as VideoView
        enble = findViewById<View>(R.id.enble) as CheckBox


        btn!!.setOnClickListener(View.OnClickListener {
            FirstClass(activity, context, wantVideo)
        })

        enble!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                wantVideo = true
            }else{
                wantVideo = false
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        FirstClass.ConnectivityUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, activity, context, wantVideo)

    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        FirstClass.ConnectivityUtils.onActivityResult(requestCode, resultCode, data, activity, this)

    }


    override fun geturi(
        uri: Uri?,
        isvideo: Boolean?,
        thumbnailuri: Uri?
    ) {


        thumbnailimg!!.setImageURI(thumbnailuri)



        if (isvideo!!) {

            video!!.visibility = View.VISIBLE
            img!!.visibility = View.GONE
            txt!!.setText(uri.toString())

            video!!.setVideoURI(uri);
            video!!.requestFocus();
            video!!.start();
        } else {
            video!!.visibility = View.GONE
            img!!.visibility = View.VISIBLE

            txt!!.setText(uri.toString())
            img!!.setImageURI(uri)
        }
    }
}
