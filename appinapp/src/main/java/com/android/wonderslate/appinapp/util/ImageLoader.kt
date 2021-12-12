package com.android.wonderslate.appinapp.util

import android.app.Activity
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.util.Preconditions
import java.util.*

class ImageLoader(val context: Context) {

    companion object {

        @JvmStatic
        fun with(context: Context) = ImageLoader(context)

        @JvmStatic
        fun with(context: Fragment): ImageLoader {
            Preconditions.checkNotNull(context, "You cannot start a load on a not yet attached View or a Fragment where getActivity() "
                    + "returns null (which usually occurs when getActivity() is called before the Fragment "
                    + "is attached or after the Fragment is destroyed).")
            return ImageLoader(context.context!!)
        }

        @JvmStatic
        fun with(context: Activity) = ImageLoader(context)

    }

    fun load(url: String?) =
        Glide.with(context).load(url)

    fun load(glideUrl: GlideUrl?) =
        Glide.with(context).load(glideUrl)

    fun load(@RawRes @DrawableRes @Nullable resourceId: Int) =
        Glide.with(context).asDrawable().load(resourceId);

    fun loadAsBitmap(url: String?) =
        Glide.with(context).asBitmap().load(url)

    fun loadAsDrawable(@Nullable model: Any)  =
        Glide.with(context).asDrawable().load(model)


}