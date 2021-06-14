package com.example.weatherforecast

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("setImage")
fun ImageView.setIconImage(icon: String?) {  //initially, livedata is null
    val accessURL = "${WeatherViewModel.ICON_URL}${icon}@2x.png"
    val iconUri = accessURL.toUri().buildUpon().scheme("https").build()

    if (icon == null) {
        setImageDrawable(null)
    }
    else {
        //download the weather icon from the website
        Glide.with(this)
            .load(iconUri)
            .apply(
                RequestOptions().placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    }
}

//fun setIconImage(imageView: ImageView, icon: String?) {
//    val accessURL = "${WeatherViewModel.ICON_URL}${icon}@2x.png"
//    val iconUri = accessURL.toUri().buildUpon().scheme("https").build()
//
//    icon?.let {
//        //download the weather icon from the website
//        Glide.with(imageView.context)
//            .load(iconUri)
//            .apply(
//                RequestOptions().placeholder(R.drawable.loading_animation)
//                    .error(R.drawable.ic_broken_image)
//            )
//            .into(imageView)
//    }
//}
