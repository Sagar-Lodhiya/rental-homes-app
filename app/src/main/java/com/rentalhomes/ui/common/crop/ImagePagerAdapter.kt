package com.rentalhomes.ui.common.crop

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.rentalhomes.R
import com.rentalhomes.databinding.ItemImagePagerBinding
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.AlbumLoader
import java.io.File
import java.util.*

class ImagePagerAdapter(private val context: Context, private val alCars: ArrayList<AlbumFile>) :
    PagerAdapter(), AlbumLoader {
    private val inflater: LayoutInflater
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return alCars.size
    }

    private lateinit var binding: ItemImagePagerBinding
    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_image_pager, view, false)
        //        binding.setVariable(BR.carsImage, alCars.get(position));
//        binding.executePendingBindings();

//        Glide.with(context).load(alCars.get(position)).into(binding.ivItems);
        var mediaController: MediaController? = null
        if (alCars[position].mediaType == AlbumFile.TYPE_VIDEO) {
            binding.ivItems.visibility = View.GONE
            binding.videoView.visibility = View.VISIBLE
            val videoUri = Uri.fromFile(File(alCars[position].path))
            binding.videoView.setVideoURI(videoUri)
            //            binding.videoView.seekTo(1);

//            binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    mediaPlayer.setLooping(true);
//                    binding.videoView.start();
//                }
//            });

//            binding.ivPlay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    binding.ivPlay.setVisibility(View.GONE);
//                    binding.videoView.start();
//                }
//            });

            //Creating MediaController
            mediaController = MediaController(context)
            mediaController.setAnchorView(binding.videoView)

//            Uri videoUri = Uri.fromFile(new File(alCars.get(position).getPath()));
//            binding.videoView.setVideoURI(videoUri);

            //Setting MediaController and URI, then starting the videoView
            binding.videoView.setMediaController(mediaController)
            binding.videoView.setVideoURI(videoUri)
            binding.videoView.seekTo(1)
            binding.videoView.requestFocus()
            //            binding.videoView.start();

//            binding.videoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    binding.videoView.start();
//                }
//            });

//            binding.ivPlay.setOnClickListener(new View.OnClickListener() {
//
//                public void onClick(View v) {
//                    Log.e("setOnClickListener b", "setOnClickListener" + binding.videoView.isPlaying());
//                    binding.ivPlay.setVisibility(View.GONE);
//                    binding.videoView.start();
//                    Log.e("setOnClickListener", "setOnClickListener" + binding.videoView.isPlaying());
//                }
//            });
        } else if (alCars[position].mediaType == AlbumFile.TYPE_IMAGE) {
            if (mediaController != null) {
                if (mediaController.isShowing) {
                    mediaController.hide()
                }
            }
            binding.ivPlay.visibility = View.GONE
            binding.ivItems.visibility = View.VISIBLE
            binding.videoView.visibility = View.GONE
            Glide.with(binding.ivItems.context)
                .load(alCars[position].path) //                    .error(R.drawable.place_holder_modifications)
                //                    .placeholder(R.drawable.place_holder_modifications)
                .into(binding.ivItems)
        }
        view.addView(binding.getRoot())
        return binding.getRoot()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}
    override fun saveState(): Parcelable? {
        return null
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun load(imageView: ImageView, albumFile: AlbumFile) {}
    override fun load(imageView: ImageView, url: String) {} //    @Override

    //    public void load(ImageView imageView, AlbumFile albumFile) {
    //        load(binding.ivItems, albumFile.getPath());
    //    }
    //
    //    @Override
    //    public void load(ImageView imageView, String url) {
    //        Glide.with(imageView.getContext())
    //                .load(url)
    //                .error(R.drawable.placeholder)
    //                .placeholder(R.drawable.placeholder)
    //                .crossFade()
    //                .into(imageView);
    //    }
    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}