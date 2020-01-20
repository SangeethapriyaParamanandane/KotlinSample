package com.example.mylibrary

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


class FirstClass(ac: Activity?, ctx: Context?, wantVideo: Boolean?) {

    var activity: Activity? = ac
    var wantVideo: Boolean? = wantVideo


    init {
        checkpermission(ctx, ac)
    }


    fun checkpermission(ctx: Context?, ac: Activity?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    ctx!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
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
                showPictureDialog(wantVideo)
            }

        } else {
            showPictureDialog(wantVideo)
        }

    }


    fun showPictureDialog(wantVideo: Boolean?) {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")

        if (wantVideo!!) {
            val pictureDialogItems = arrayOf(
                "Select photo from gallery photo", "Capture photo from camera",
                "Select video from gallery video", "Capture video from camera"
            )




            pictureDialog.setItems(
                pictureDialogItems
            ) { dialog, which ->
                when (which) {

                    0 -> pickImageFromGallery()
                    1 -> takePhotoFromCamera()
                    2 -> pickVideofromGallery()
                    3 -> takeVideofromCamera()

                }
            }


        } else {
            pictureDialog.setItems(
                pictureDialogItems
            ) { dialog, which ->
                when (which) {

                    0 -> pickImageFromGallery()
                    1 -> takePhotoFromCamera()

                }
            }
        }


        pictureDialog.show()
    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity!!.startActivityForResult(intent, 1)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity!!.startActivityForResult(intent, 2)
    }

    private fun pickVideofromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        activity!!.startActivityForResult(galleryIntent, 3)
    }

    private fun takeVideofromCamera() {

        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        activity!!.startActivityForResult(intent, 4)
    }


    object ConnectivityUtils {
        fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
            ac: Activity?,
            sample: Sample
        ) {

            if (data != null) {
                val bitmap = getThumbnailPath(data?.data, ac)



                if (resultCode == Activity.RESULT_OK && requestCode == 1) {

                    sample.geturi(data?.data, false, bitmap)

                } else if (requestCode == 2) {
                    val thumbnail = data!!.extras!!.get("data") as Bitmap
                    var gh: String = savebitmap(thumbnail)
                    sample.geturi(Uri.fromFile(File(gh)), false, bitmap)

                    Toast.makeText(ac, "Image Saved!", Toast.LENGTH_SHORT).show()
                } else if (requestCode == 3) {


                    sample.geturi(data?.data, true, bitmap)


                } else if (requestCode == 4) {
                    sample.geturi(data?.data, true, bitmap)

                }
            }
        }


        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
            activity: Activity?,
            context: Context?,
            wantVideo: Boolean?
        ) {
            when (requestCode) {
                1001 -> {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        FirstClass(activity, context, wantVideo)
                    } else {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        fun getThumbnailPath(uri: Uri?, ac: Activity?): Bitmap? {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = ac!!.contentResolver.query(uri!!, filePathColumn, null, null, null)
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor!!.close()

            val bitmap = ThumbnailUtils.createVideoThumbnail(
                picturePath,
                MediaStore.Video.Thumbnails.MICRO_KIND
            )
            return bitmap

        }


        private fun savebitmap(bmp: Bitmap): String {
            val extStorageDirectory =
                Environment.getExternalStorageDirectory().toString()
            var outStream: OutputStream? = null
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