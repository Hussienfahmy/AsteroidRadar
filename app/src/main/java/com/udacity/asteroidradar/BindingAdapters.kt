package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidAdapter

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    setImageResource(
        if (isHazardous) R.drawable.ic_status_potentially_hazardous
        else R.drawable.ic_status_normal
    )
    contentDescription =
        if (isHazardous) context.getString(R.string.asteroid_hazard)
        else context.getString(R.string.asteroid_not_hazard)
}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        setImageResource(R.drawable.asteroid_hazardous)
    } else {
        setImageResource(R.drawable.asteroid_safe)
    }

    contentDescription =
        if (isHazardous) context.getString(R.string.asteroid_hazard)
        else context.getString(R.string.asteroid_not_hazard)
}

@BindingAdapter("astronomicalUnitText")
fun TextView.bindTextViewToAstronomicalUnit(number: Double) {
    text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun TextView.bindTextViewToKmUnit(number: Double) {
    text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun TextView.bindTextViewToDisplayVelocity(number: Double) {
    text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("asteroids")
fun RecyclerView.bindAsteroidsToRecyclerView(asteroids: List<Asteroid>?) {
    val adapter = adapter
    if (adapter is AsteroidAdapter) adapter.submitList(asteroids)
}

@BindingAdapter("pictureOfDay")
fun ImageView.bindPictureOfDay(pictureOfDay: PictureOfDay?) {
    if (pictureOfDay != null && pictureOfDay.mediaType == "image") {
        contentDescription = pictureOfDay.title
        Picasso.get().load(pictureOfDay.url).into(this)
    }
}
