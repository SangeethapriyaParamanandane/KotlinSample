package com.example.mylibrary

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.content.Context

import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission

import java.io.*
import java.util.*


class FirstClass(ac: Activity?, ctx: Context?) {

    var activity: Activity? = ac


    init {
        checkpermission(ctx, ac)
    }


    fun checkpermission(ctx: Context?, ac: Activity?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    ctx!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
                && checkSelfPermission(
                    ctx!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
                && checkSelfPermission(
                    ctx!!,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                );
                requestPermissions(ac!!, permissions, 1001);
            } else {
                showPictureDialog()
            }

        } else {
            showPictureDialog()
        }

    }


    fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {

                0 -> pickImageFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity!!.startActivityForResult(intent, 2)
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity!!.startActivityForResult(intent, 1)
    }

    object ConnectivityUtils {
        fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
            ac: Activity?,
            sample: Sample
        ) {
            if (resultCode == Activity.RESULT_OK && requestCode == 1) {

                sample.geturi(data?.data)
            } else if (requestCode == 2) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                var gh: String = savebitmap(thumbnail)
                sample.geturi(Uri.fromFile(File(gh)))

                Toast.makeText(ac, "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        }


        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
            activity: Activity?,
            context: Context?
        ) {
            when (requestCode) {
                1001 -> {
                    if (grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        FirstClass(activity, context)
                    }
                }
            }
        }


        private fun savebitmap(bmp: Bitmap): String {
            val extStorageDirectory =
                Environment.getExternalStorageDirectory().toString()
            var outStream: OutputStream? = null
            // String temp = null;
            var file = File(
                extStorageDirectory,
                Calendar.getInstance().getTimeInMillis().toString() + ".png"
            )
            if (file.exists()) {
                file.delete()
                file = File(
                    extStorageDirectory,
                    Calendar.getInstance().getTimeInMillis().toString() + ".png"
                )
            }
            try {
                outStream = FileOutputStream(file)
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                outStream.flush()
                outStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
            return file.getAbsolutePath()
        }


    }


}