package com.example.kotlincamerasample

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.mylibrary.FirstClass
import com.example.mylibrary.Sample


class MainActivity : AppCompatActivity(), Sample {
    private var txt: TextView? = null
    private var btn: Button? = null
    private var img: ImageView? = null
    private var activity: Activity? = null
    private var context: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        context = this

        btn = findViewById<View>(R.id.btn) as Button
        txt = findViewById<View>(R.id.txt) as TextView
        img = findViewById<View>(R.id.img) as ImageView


        btn!!.setOnClickListener(View.OnClickListener {
            FirstClass(activity, context)
        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        FirstClass.ConnectivityUtils.onRequestPermissionsResult(requestCode, permissions, grantResults,activity,context)

    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        FirstClass.ConnectivityUtils.onActivityResult(requestCode, resultCode, data, activity, this)

    }



    override fun geturi(uri: Uri?) {
        txt!!.setText(uri.toString())
        img!!.setImageURI(uri)
    }
}
