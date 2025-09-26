package com.rentalhomes.ui.common.crop

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivityCropBinding
import com.rentalhomes.utils.Constant
import com.rentalhomes.utils.Constant.DELETE_FILE
import com.rentalhomes.utils.Constant.FILE_TYPE
import com.rentalhomes.utils.Constant.FILE_UPLOAD_RESULT
import com.rentalhomes.utils.Constant.IMAGE
import com.rentalhomes.utils.GlobalMethods
import com.yalantis.ucrop.UCrop
import com.yanzhenjie.album.AlbumFile
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*


class CropActivity : AppCompatActivity(), View.OnClickListener, VideoPathPass {
    private lateinit var binding: ActivityCropBinding
    private val profilePicUri: Uri? = null
    private var cropFileUri: Uri? = null
    var uriArrayList: ArrayList<AlbumFile>? = null
    var mCarsPagerAdapter: ImagePagerAdapter? = null

    //    private PageIndicatorView pageIndicatorView;
    var videoPathPass: VideoPathPass? = null
    var videoPath: String? = null
    var comprssedUri: Uri? = null
    var intCropPos = 0
    private var alDeleteFile: ArrayList<String>? = null

    //Compression
    private val destPath: String? = null
    private val outputDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
    private val startTime: Long = 0
    private val endTime: Long = 0
    private val fdelete: File? = null
    private val progressDialog: ProgressDialog? = null
    private val videoView: VideoView? = null
    private val mediacontroller: MediaController? = null
    private val mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crop)
        alDeleteFile = ArrayList()
        uriArrayList = this.intent.getParcelableArrayListExtra("URI")
        for (i in uriArrayList!!.indices) {
            Log.e("I", " " + uriArrayList!![i])
        }
        Log.e("URI SIZE", "" + uriArrayList!!.size)
        videoPathPass = this
        mCarsPagerAdapter = ImagePagerAdapter(this@CropActivity, uriArrayList!!)
        binding.pagerAdapter = mCarsPagerAdapter
        //        binding.pageIndicatorView.setViewPager(binding.vpCarsImage);
        setCropTrimText()
        setAccordingToSize(uriArrayList!!.size)
        setListner()
//        binding.tvTrim.setOnClickListener(View.OnClickListener { doTrimming(binding.vpCarsImage.currentItem) })
        binding.tvCrop.setOnClickListener(View.OnClickListener { doTrimming(binding.vpCarsImage.currentItem) })
        binding.tvFinal.setOnClickListener(View.OnClickListener { //                Log.e("uriArrayList", "uriArrayList : " + uriArrayList.size());
//
//                LogM.e("uriArrayList : " + uriArrayList.get(0).getThumbPath());
//                LogM.e("uriArrayList : " + uriArrayList.get(0).getPath());
//                LogM.e("uriArrayList MediaType: " + uriArrayList.get(0).getMediaType());
//                LogM.e("uriArrayList Uri : " + Uri.fromFile(new File(uriArrayList.get(0).getPath())));
//                LogM.e("uriArrayList Duration: " + uriArrayList.get(0).getDuration());
            var durationOneMin = false
            for (i in uriArrayList!!.indices) {
                Log.d("Size", "Video file size: ${uriArrayList!![i].size}")
                var fileSize = uriArrayList!![i].size.toDouble() / (1024*1024)
                Log.d("Size", "Video file size: ${fileSize}")
                if (fileSize>50) {
                    durationOneMin = false
                    Toast.makeText(
                        this@CropActivity,
                        "Please select video with size less than 20 MB.",
                        Toast.LENGTH_SHORT
                    ).show()
                    break
                } else {
                    durationOneMin = true
                }

//                if (uriArrayList!![i].duration > 60000) {
//                    durationOneMin = false
//                    Toast.makeText(
//                        this@CropActivity,
//                        "Please trim videos to 1 minute.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    break
//                } else {
//                    durationOneMin = true
//                }
            }
            if (durationOneMin) {
                val gson = Gson()
                val deleteFileList: String = gson.toJson(alDeleteFile)
                val returnIntent = Intent()
                returnIntent.putExtra(
                    FILE_UPLOAD_RESULT,
                    uriArrayList as ArrayList<out Parcelable?>?
                )
                returnIntent.putExtra(DELETE_FILE, deleteFileList)
                returnIntent.putExtra(FILE_TYPE, IMAGE)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        })
        binding.vpCarsImage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(pagerPosition: Int) {
                when (pagerPosition) {
                    0 -> binding.ivOne.performClick()
                    1 -> binding.ivTwo.performClick()
                    2 -> binding.ivThree.performClick()
                    3 -> binding.ivFour.performClick()
                    4 -> binding.ivFive.performClick()
                }
                setVisibilityCropTrim(pagerPosition)
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    private fun setListner() {
        binding.ivOne.setOnClickListener(this)
        binding.ivTwo.setOnClickListener(this)
        binding.ivThree.setOnClickListener(this)
        binding.ivFour.setOnClickListener(this)
        binding.ivFive.setOnClickListener(this)
    }

    private fun doTrimming(itemPosition: Int) {

            binding.tvTrim.visibility = View.GONE
            binding.tvCrop.visibility = View.VISIBLE
            try {
//                val currentBitmap: Bitmap? = GlobalMethods.handleSamplingAndRotationBitmap(
//                    this@CropActivity,
//                    Uri.fromFile(File(uriArrayList!![itemPosition].getPath()))
//                )
//                cropFileUri = GlobalMethods.getImageUri(this@CropActivity, currentBitmap!!)
                intCropPos = itemPosition
//                performCrop(cropFileUri, itemPosition)
                cropImage(Uri.fromFile(File(uriArrayList!![itemPosition].path)))
            } catch (e: IOException) {
                e.printStackTrace()
            }
    }

    private fun cropImage(sourceUri: Uri) {
        var file = File(cacheDir, "${System.currentTimeMillis()}.jpg")
        if (!file.exists()) {
            file.createNewFile()
        }
        var destinationUri = Uri.fromFile(file)
        var options: UCrop.Options = UCrop.Options()
        options.setCompressionQuality(100)

        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(this, R.color.status_bar_color))
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color))
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.colorBlueTheme))


        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(this)
    }

    private fun setAccordingToSize(size: Int) {
        when (size) {
            1 -> if (uriArrayList!![0].path.length > 0) {
                if (uriArrayList!![0].mediaType == AlbumFile.TYPE_VIDEO) {
                    binding.ivOneVideo.visibility = View.VISIBLE
                    val interval = (1 * 1000).toLong()
                    val options: RequestOptions = RequestOptions().frame(interval)
                    Glide.with(this).asBitmap()
                        .load(uriArrayList!![0].path)
                        .apply(options)
                        .into(binding.ivOne)
                } else {
                    Glide.with(this).load(uriArrayList!![0].path)
                        .into(binding.ivOne)
                }
            }
            2 -> {
                if (uriArrayList!![0].path.length > 0) {
                    if (uriArrayList!![0].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivOneVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![0].path)
                            .apply(options)
                            .into(binding.ivOne)
                    } else {
                        Glide.with(this).load(uriArrayList!![0].path)
                            .into(binding.ivOne)
                    }
                }
                if (uriArrayList!![1].path.length > 0) {
                    if (uriArrayList!![1].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivTwoVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![1].path)
                            .apply(options)
                            .into(binding.ivTwo)
                    } else {
                        Glide.with(this).load(uriArrayList!![1].path)
                            .into(binding.ivTwo)
                    }
                }
            }
            3 -> {
                if (uriArrayList!![0].path.length > 0) {
                    if (uriArrayList!![0].mediaType == AlbumFile.TYPE_VIDEO) {
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![0].path)
                            .apply(options)
                            .into(binding.ivOne)
                    } else {
                        Glide.with(this).load(uriArrayList!![0].path)
                            .into(binding.ivOne)
                    }
                }
                if (uriArrayList!![1].path.length > 0) {
                    if (uriArrayList!![1].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivTwoVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![1].path)
                            .apply(options)
                            .into(binding.ivTwo)
                    } else {
                        Glide.with(this).load(uriArrayList!![1].path)
                            .into(binding.ivTwo)
                    }
                }
                if (uriArrayList!![2].path != null) {
                    if (uriArrayList!![2].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivThreeVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![2].path)
                            .apply(options)
                            .into(binding.ivThree)
                    } else {
                        Glide.with(this).load(uriArrayList!![2].path)
                            .into(binding.ivThree)
                    }
                }
            }
            4 -> {
                if (uriArrayList!![0].path.length > 0) {
                    if (uriArrayList!![0].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivOneVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![0].path)
                            .apply(options)
                            .into(binding.ivOne)
                    } else {
                        Glide.with(this).load(uriArrayList!![0].path)
                            .into(binding.ivOne)
                    }
                }
                if (uriArrayList!![1].path.length > 0) {
                    if (uriArrayList!![1].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivTwoVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![1].path)
                            .apply(options)
                            .into(binding.ivTwo)
                    } else {
                        Glide.with(this).load(uriArrayList!![1].path)
                            .into(binding.ivTwo)
                    }
                }
                if (uriArrayList!![2].path != null) {
                    if (uriArrayList!![2].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivThreeVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![2].path)
                            .apply(options)
                            .into(binding.ivThree)
                    } else {
                        Glide.with(this).load(uriArrayList!![2].path)
                            .into(binding.ivThree)
                    }
                }
                if (uriArrayList!![3].path.length > 0) {
                    if (uriArrayList!![3].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivFourVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![3].path)
                            .apply(options)
                            .into(binding.ivFour)
                    } else {
                        Glide.with(this).load(uriArrayList!![3].path)
                            .into(binding.ivFour)
                    }
                }
            }
            5 -> {
                if (uriArrayList!![0].path.length > 0) {
                    if (uriArrayList!![0].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivOneVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![0].path)
                            .apply(options)
                            .into(binding.ivOne)
                    } else {
                        Glide.with(this).load(uriArrayList!![0].path)
                            .into(binding.ivOne)
                    }
                }
                if (uriArrayList!![1].path.length > 0) {
                    if (uriArrayList!![1].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivTwoVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![1].path)
                            .apply(options)
                            .into(binding.ivTwo)
                    } else {
                        Glide.with(this).load(uriArrayList!![1].path)
                            .into(binding.ivTwo)
                    }
                }
                if (uriArrayList!![2].path != null) {
                    if (uriArrayList!![2].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivThreeVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![2].path)
                            .apply(options)
                            .into(binding.ivThree)
                    } else {
                        Glide.with(this).load(uriArrayList!![2].path)
                            .into(binding.ivThree)
                    }
                }
                if (uriArrayList!![3].path.length > 0) {
                    if (uriArrayList!![3].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivFourVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![3].path)
                            .apply(options)
                            .into(binding.ivFour)
                    } else {
                        Glide.with(this).load(uriArrayList!![3].path)
                            .into(binding.ivFour)
                    }
                }
                if (uriArrayList!![4].path.length > 0) {
                    if (uriArrayList!![4].mediaType == AlbumFile.TYPE_VIDEO) {
                        binding.ivFiveVideo.visibility = View.VISIBLE
                        val interval = (1 * 1000).toLong()
                        val options: RequestOptions = RequestOptions().frame(interval)
                        Glide.with(this).asBitmap()
                            .load(uriArrayList!![4].path)
                            .apply(options)
                            .into(binding.ivFive)
                    } else {
                        Glide.with(this).load(uriArrayList!![4].path)
                            .into(binding.ivFive)
                    }
                }
            }
        }
    }

    private fun setCropTrimText() {
        when (binding.vpCarsImage.currentItem) {
            0 -> setVisibilityCropTrim(0)
            1 -> setVisibilityCropTrim(1)
            2 -> setVisibilityCropTrim(2)
            3 -> setVisibilityCropTrim(3)
            4 -> setVisibilityCropTrim(4)
        }
    }

    private fun setVisibilityCropTrim(position: Int) {
        if (uriArrayList!![position].mediaType == AlbumFile.TYPE_VIDEO) {
            binding.tvTrim.visibility = View.VISIBLE
            binding.tvCrop.visibility = View.GONE
        } else if (uriArrayList!![position].mediaType == AlbumFile.TYPE_IMAGE) {
            binding.tvTrim.visibility = View.GONE
            binding.tvCrop.visibility = View.VISIBLE
        }
    }


    var trimVideoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val requiredValue: String = result.data?.getStringExtra("trimmedPath")!!
                if (result.data!!.hasExtra(Constant.MediaPosition)) {
                    intCropPos = result?.data!!.getIntExtra(Constant.MediaPosition, 0)
                }
                binding.vpCarsImage.currentItem = intCropPos
                if (requiredValue != null) {
                    uriArrayList!![intCropPos].path = requiredValue
                    alDeleteFile!!.add(requiredValue)
                    val retriever = MediaMetadataRetriever()
                    //use one of overloaded setDataSource() functions to set your data source
                    val file = File(requiredValue)
                    retriever.setDataSource(this@CropActivity, Uri.fromFile(file))
                    val time: String =
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
                    val timeInMillisec = time.toLong()
                    retriever.release()
                    uriArrayList!![intCropPos].duration = timeInMillisec
                    uriArrayList!![intCropPos].size = file.length()
                    setAccordingToSize(uriArrayList!!.size)
                    mCarsPagerAdapter!!.notifyDataSetChanged()
                }
            }
        }

//    var cropLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            var currentBitmap: Bitmap? = null
//            var latestUri: Uri? = null
//            binding.vpCarsImage.currentItem = intCropPos
//            if (Build.VERSION.SDK_INT >= 29) {
//                val source: ImageDecoder.Source =
//                    ImageDecoder.createSource(
//                        applicationContext.contentResolver,
//                        result.data!!.data!!
//                    )
//                try {
//                    currentBitmap = ImageDecoder.decodeBitmap(source)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            } else {
//                currentBitmap =
//                    MediaStore.Images.Media.getBitmap(contentResolver, result.data?.data)
//            }
//
//            GlobalScope.launch(Dispatchers.IO) {
//                var latestUri: Uri? = null
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    latestUri = GlobalMethods.getImageFromURIQ(this@CropActivity, currentBitmap!!)
//                } else {
//                    latestUri =
//                        GlobalMethods.saveTheImageLegacyStyle(this@CropActivity, currentBitmap!!)
//                }
////            latestUri = GlobalMethods.getImageUri(this@CropActivity, currentBitmap!!)
//                val finalFile: File =
//                    File(GlobalMethods.getRealPathFromURI(this@CropActivity, latestUri))
//                uriArrayList!![intCropPos].path = finalFile.toString()
//                alDeleteFile!!.add(finalFile.toString())
//                setAccordingToSize(uriArrayList!!.size)
//                withContext(Dispatchers.Main) {
//                    mCarsPagerAdapter!!.notifyDataSetChanged()
//                }
//            }
//
//
//        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            var currentBitmap: Bitmap? = null
            var latestUri: Uri? = null
            var resultUri: Uri = UCrop.getOutput(data!!)!!
            binding.vpCarsImage.currentItem = intCropPos
            if (Build.VERSION.SDK_INT >= 29) {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(this.contentResolver, resultUri)
                try {
                    currentBitmap = ImageDecoder.decodeBitmap(source)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                currentBitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
            }

            GlobalScope.launch(Dispatchers.IO) {
                var latestUri: Uri? = null
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    latestUri = async {  GlobalMethods.getImageFromURIQ(this@CropActivity, currentBitmap!!) }.await()
//                } else {
//                    latestUri = async { GlobalMethods.saveTheImageLegacyStyle(this@CropActivity, currentBitmap!!) }.await()
//
//                }

                latestUri = async { GlobalMethods.saveCroppedImage(this@CropActivity, currentBitmap!!) }.await()
//            latestUri = GlobalMethods.getImageUri(this@CropActivity, currentBitmap!!)
//                val finalFile: File =
//                    File(GlobalMethods.getRealPathFromURI(this@CropActivity, latestUri))
                val finalFile: File =
                    File(latestUri!!.path)
                uriArrayList!![intCropPos].path = finalFile.toString()
                alDeleteFile!!.add(finalFile.toString())

                withContext(Dispatchers.Main) {
                    setAccordingToSize(uriArrayList!!.size)
                    mCarsPagerAdapter!!.notifyDataSetChanged()
                }
            }
        }

    }

//    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        try {
//            var currentBitmap: Bitmap? = null
//            var latestUri: Uri? = null
//            if (requestCode == Constant.VIDEO_ONE && resultCode == Activity.RESULT_OK) {
//                val requiredValue: String = data?.getStringExtra("trimmedPath")!!
//                if (data.hasExtra(Constant.MediaPosition)) {
//                    intCropPos = data.getIntExtra(Constant.MediaPosition, 0)
//                }
//                binding.vpCarsImage.setCurrentItem(intCropPos)
//                if (requiredValue != null) {
//                    uriArrayList!![intCropPos].setPath(requiredValue)
//                    alDeleteFile!!.add(requiredValue)
//                    val retriever = MediaMetadataRetriever()
//                    //use one of overloaded setDataSource() functions to set your data source
//                    val file = File(requiredValue)
//                    retriever.setDataSource(this@CropActivity, Uri.fromFile(file))
//                    val time: String =
//                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
//                    val timeInMillisec = time.toLong()
//                    retriever.release()
//                    uriArrayList!![intCropPos].setDuration(timeInMillisec)
//                    setAccordingToSize(uriArrayList!!.size)
//                }
//            } else if (uriArrayList!![intCropPos].getMediaType() == AlbumFile.TYPE_IMAGE) {
//                try {
//                    binding.vpCarsImage.setCurrentItem(intCropPos)
//                    currentBitmap =
//                        MediaStore.Images.Media.getBitmap(getContentResolver(), data?.getData())
//                    latestUri = GlobalMethods.getImageUri(this@CropActivity, currentBitmap)
//                    val finalFile: File =
//                        File(GlobalMethods.getRealPathFromURI(this@CropActivity, latestUri))
//                    uriArrayList!![intCropPos].setPath(finalFile.toString())
//                    alDeleteFile!!.add(finalFile.toString())
//                    setAccordingToSize(uriArrayList!!.size)
//
//                    // For Delete cropped uri
////                        File fdelete = new File(getFilePath(latestUri));
////
////                        if (fdelete.exists()) {
////                            if (fdelete.delete()) {
////                                System.out.println("file Deleted :");
////                            } else {
////                                System.out.println("file not Deleted :");
////                            }
////                        }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//            mCarsPagerAdapter!!.notifyDataSetChanged()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    private fun performCrop(picUri: Uri?, position: Int) {
        try {
            val cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            cropIntent.setDataAndType(picUri, "image/*")
            // set crop properties here
            cropIntent.putExtra("crop", true)
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            // indicate output X and Y
            cropIntent.putExtra("outputX", 500)
            cropIntent.putExtra("outputY", 500)
            // retrieve data on return
            cropIntent.putExtra("return-data", true)
            //            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(profilePicFile));
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, position)
        } catch (e: Exception) {
            val errorMessage = "Whoops - your device doesn't support the crop action!"
            //            AlertDialogUtility.showSnakeBar(errorMessage, snackBar, this);
        }
    }

    //getting real path from uri
    private fun getFilePath(uri: Uri): String? {
        val projection = arrayOf<String>(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            val picturePath = cursor.getString(columnIndex) // returns null
            cursor.close()
            return picturePath
        }
        return null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivOne -> {
                binding.ivOne.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_blue_border
                )
                binding.ivTwo.background = null
                binding.ivThree.background = null
                binding.ivFour.background = null
                binding.ivFive.background = null
                binding.vpCarsImage.currentItem = 0
            }
            R.id.ivTwo -> {
                binding.ivOne.background = null
                binding.ivTwo.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_blue_border
                )
                binding.ivThree.background = null
                binding.ivFour.background = null
                binding.ivFive.background = null
                binding.vpCarsImage.currentItem = 1
            }
            R.id.ivThree -> {
                binding.ivOne.background = null
                binding.ivTwo.background = null
                binding.ivThree.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_blue_border
                )
                binding.ivFour.background = null
                binding.ivFive.background = null
                binding.vpCarsImage.currentItem = 2
            }
            R.id.ivFour -> {
                binding.ivOne.background = null
                binding.ivTwo.background = null
                binding.ivThree.background = null
                binding.ivFour.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_blue_border
                )
                binding.ivFive.background = null
                binding.vpCarsImage.currentItem = 3
            }
            R.id.ivFive -> {
                binding.ivOne.background = null
                binding.ivTwo.background = null
                binding.ivThree.background = null
                binding.ivFour.background = null
                binding.ivFive.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_blue_border
                )
                binding.vpCarsImage.currentItem = 4
            }
        }
    }

    override fun videoPath(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
        Log.e("Activity TrimmedPath", "" + str)
        videoPath = str
        comprssedUri = Uri.fromFile(File(str))
        Log.e("Activity Compress URI", "" + comprssedUri)
    }
}