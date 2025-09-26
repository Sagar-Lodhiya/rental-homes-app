package com.rentalhomes.utils

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rentalhomes.R
import com.yalantis.ucrop.UCrop
//import com.yalantis.ucrop.UCrop
import java.io.File

/**
 * Created by Dharmesh
 * */
class ImagePickerActivity : AppCompatActivity() {
    private val TAG = "ImagePickerActivity"

    companion object {
        const val INTENT_IMAGE_PICKER_OPTION: String = "image_picker_option"
        const val INTENT_ASPECT_RATIO_X: String = "aspect_ratio_x"
        const val INTENT_ASPECT_RATIO_Y: String = "aspect_ratio_Y"
        const val INTENT_LOCK_ASPECT_RATIO: String = "lock_aspect_ratio"
        const val INTENT_IMAGE_COMPRESSION_QUALITY: String = "compression_quality"
        const val INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT: String = "set_bitmap_max_width_height"
        const val INTENT_BITMAP_MAX_WIDTH: String = "max_width"
        const val INTENT_BITMAP_MAX_HEIGHT: String = "max_height"
        const val INTENT_IS_CIRCULAR: String = "isCircular"


        const val REQUEST_IMAGE_CAPTURE: Int = 0
        const val REQUEST_GALLERY_IMAGE: Int = 1

        private fun queryName(resolver: ContentResolver, uri: Uri): String {
            val returnCursor = resolver.query(uri, null, null, null, null)
            val nameIndex: Int = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            return (returnCursor.getString(nameIndex)
                ?: returnCursor.close()) as String
        }
    }

    private var lockAspectRatio: Boolean = false
    private var isCircular: Boolean = false
    private var setBitmapMaxWidthHeight: Boolean = false
    private var ASPECT_RATIO_X: Int = 16
    private var ASPECT_RATIO_Y: Int = 9
    private var bitmapMaxWidth: Int = 1000
    private var bitmapMaxHeight: Int = 1000
    private var IMAGE_COMPRESSION = 80
    var fileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = intent

        ASPECT_RATIO_X = intent.getIntExtra(INTENT_ASPECT_RATIO_X, ASPECT_RATIO_X)
        ASPECT_RATIO_Y = intent.getIntExtra(INTENT_ASPECT_RATIO_Y, ASPECT_RATIO_Y)
        IMAGE_COMPRESSION = intent.getIntExtra(INTENT_IMAGE_COMPRESSION_QUALITY, IMAGE_COMPRESSION)
        lockAspectRatio = intent.getBooleanExtra(INTENT_LOCK_ASPECT_RATIO, false)
        setBitmapMaxWidthHeight = intent.getBooleanExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false)
        bitmapMaxWidth = intent.getIntExtra(INTENT_BITMAP_MAX_WIDTH, bitmapMaxWidth)
        bitmapMaxHeight = intent.getIntExtra(INTENT_BITMAP_MAX_HEIGHT, bitmapMaxHeight)
        isCircular = intent.getBooleanExtra(INTENT_IS_CIRCULAR,isCircular)
        val requestCode: Int = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            takeCameraImage()
        } else {
            chooseImageFromGallery()
        }
    }

    private fun takeCameraImage() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null) {
                        if (report.areAllPermissionsGranted()) {
                            fileName = fileName + System.currentTimeMillis() + ".jpg"
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            takePictureIntent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                getCacheImagePath(fileName)
                            )
                            if (takePictureIntent.resolveActivity(packageManager) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                            }
                        }else {
                            finish()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .check()
    }

    private fun chooseImageFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null) {
                        if (report.areAllPermissionsGranted()) {
                            val pickPhoto = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE)
                        } else {
                            finish()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .check()

    }

    private fun getCacheImagePath(fileName: String): Uri {
        val path = File(externalCacheDir, "camera")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        return getUriForFile(this, "$packageName.provider", image)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK) {
                    cropImage(getCacheImagePath(fileName))
                } else {
                    setResultCancelled()
                }
            }
            REQUEST_GALLERY_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    val imageUri = data!!.data
                    if (imageUri != null) {
                        cropImage(imageUri)
                    }
                } else {
                    setResultCancelled()
                }
            }

            UCrop.REQUEST_CROP -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        handleUCropResult(data)
                    }
                } else {
                    setResultCancelled()
                }
            }

            UCrop.RESULT_ERROR -> {
                var cropError: Throwable? = UCrop.getError(data!!)
                Log.e(TAG, "Crop error: " + cropError)
                setResultCancelled()
            }
            else -> {
                setResultCancelled()
            }
        }
    }

    private fun cropImage(sourceUri: Uri) {
        val destinationUri =
            Uri.fromFile(File(cacheDir, queryName(contentResolver, sourceUri)))
        val options: UCrop.Options = UCrop.Options()
        options.setCircleDimmedLayer(isCircular)
        options.setCompressionQuality(IMAGE_COMPRESSION)

        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(this, R.color.color_green))
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.color_green))
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.color_green))

        if (lockAspectRatio)
            options.withAspectRatio(ASPECT_RATIO_X.toFloat(), ASPECT_RATIO_Y.toFloat());

        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);

        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(this)
    }

    private fun setResultCancelled() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        this.finish()
    }

    private fun setResultOk(imagePath: Uri) {
        val intent = Intent()
        intent.putExtra("path", imagePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun handleUCropResult(data: Intent) {
                var resultUri: Uri = UCrop.getOutput(data)!!;
        setResultOk(resultUri);
    }

}

