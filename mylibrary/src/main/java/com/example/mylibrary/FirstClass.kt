package com.example.mylibrary

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import java.io.*
import java.util.*


class FirstClass(ac: Activity?) {

    var activity: Activity? = ac


    init {
        showPictureDialog()
    }


    private fun showPictureDialog() {
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
                saveImage(thumbnail, ac)
                var gh: String = savebitmap(thumbnail)
                sample.geturi(Uri.fromFile(File(gh)))

                Toast.makeText(ac, "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        }


        private fun savebitmap(bmp: Bitmap): String {
            val extStorageDirectory =
                Environment.getExternalStorageDirectory().toString()
            var outStream: OutputStream? = null
            // String temp = null;
            var file = File(extStorageDirectory, "temp.png")
            if (file.exists()) {
                file.delete()
                file = File(extStorageDirectory, "temp.png")
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


        fun saveImage(myBitmap: Bitmap, ac: Activity?): String {
            val bytes = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
            val wallpaperDirectory =
                File((Environment.getExternalStorageDirectory()).toString() + "Demonut")
            Log.d("fee", wallpaperDirectory.toString())


            if (!wallpaperDirectory.exists())

                try {
                    Log.d("heel", wallpaperDirectory.toString())
                    val f = File(
                        wallpaperDirectory,
                        ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg")
                    )
                    f.getParentFile().mkdirs();
                    f.createNewFile()
                    val fo = FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    MediaScannerConnection.scanFile(
                        ac,
                        arrayOf(f.getPath()),
                        arrayOf("image/jpeg"),
                        null
                    )
                    fo.close()
                    Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

                    return f.getAbsolutePath()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

            return ""
        }
    }


}