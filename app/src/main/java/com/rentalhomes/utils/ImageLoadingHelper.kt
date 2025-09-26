package com.rentalhomes.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.rentalhomes.R

object ImageLoadingHelper {

    fun loadProfileCircularImage(
        context: Context,
        ivProfile: ImageView,
        profilePic: String?,
        profileColor: String?
    ) {
//        if(profileColor.isNullOrEmpty()){
//            Glide.with(context).load(chatRoomList.get(absoluteAdapterPosition).profilePic).circleCrop().dontAnimate()
//                .placeholder(R.drawable.ic_user).error(R.drawable.ic_user).into(ivProfile)
//        }

        var placeHolder = getProfilePlaceholder(profileColor)
        Glide.with(context).load(profilePic).circleCrop().dontAnimate()
            .placeholder(placeHolder).error(placeHolder).into(ivProfile)
    }

    private fun getProfilePlaceholder(profileColor: String?): Int {
        var placeHolder = R.drawable.ic_user
        if (profileColor != null) {
            if (profileColor.equals("red")) {
                placeHolder = R.drawable.ic_user_red
            } else if (profileColor.equals("green")) {
                placeHolder = R.drawable.ic_user_green
            } else if (profileColor.equals("yellow")) {
                placeHolder = R.drawable.ic_user_yellow
            } else if (profileColor.equals("purple")) {
                placeHolder = R.drawable.ic_user_purple
            }
        }
        return placeHolder
    }

    fun loadProfilePictureFull(
        context: Context,
        ivProfile: ImageView,
        profilePic: String?,
        profileColor: String?
    ) {
        var placeHolder = getProfilePlaceholder(profileColor)
        Glide.with(context)
            .load(profilePic)
            .placeholder(placeHolder)
            .error(placeHolder).fitCenter().into(ivProfile)
    }

    fun loadMessageThumbnailImage(context: Context, ivMessageImage: ImageView, thumbnail: String?) {
        Glide.with(context)
            .load(thumbnail)
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .transform(
                CenterCrop(),
//                RoundedCorners(context.resources.getDimensionPixelSize(R.dimen._8sdp))
            )
            .dontAnimate()
            .into(ivMessageImage)
    }

    fun loadMessageImageFull(
        context: Context,
        ivMessageImage: ImageView,
        originalMedia: String?,
        thumbnailMedia: String?
    ) {
        Glide.with(context).load(originalMedia)
            .thumbnail(Glide.with(context).load(thumbnailMedia))
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .fitCenter()
            .into(ivMessageImage)
    }
}