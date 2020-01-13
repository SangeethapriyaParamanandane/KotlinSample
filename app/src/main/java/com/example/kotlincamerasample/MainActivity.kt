package com.example.kotlincamerasample

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mylibrary.FirstClass
import com.example.mylibrary.Sample


class MainActivity : AppCompatActivity(), Sample {
    private var txt: TextView? = null
    private var btn: Button? = null
    private var img: ImageView? = null
    private var activity: Activity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this

        btn = findViewById<View>(R.id.btn) as Button
        txt = findViewById<View>(R.id.txt) as TextView
        img = findViewById<View>(R.id.img) as ImageView


        btn!!.setOnClickListener(View.OnClickListener {
            checkpermission()

        })
    }


    fun checkpermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                );
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                FirstClass(activity)
            }

        } else {
            FirstClass(activity)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    FirstClass(activity)
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        FirstClass.ConnectivityUtils.onActivityResult(requestCode, resultCode, data, activity,this)

    }

    companion object {
        val PERMISSION_CODE = 1001;
    }

    override fun geturi(uri: Uri?) {
        txt!!.setText(uri.toString())
        img!!.setImageURI(uri)
        //Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show()


    }
}
