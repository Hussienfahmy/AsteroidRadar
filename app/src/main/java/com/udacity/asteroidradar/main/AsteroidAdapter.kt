package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.LayoutItemAsteroidBinding

class AsteroidAdapter(
    private val onAsteroidClick: OnAsteroidClickListener
) : ListAdapter<Asteroid, AsteroidAdapter.AsteroidItemViewHolder>(AsteroidDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AsteroidItemViewHolder.from(parent)

    override fun onBindViewHolder(holder: AsteroidItemViewHolder, position: Int) =
        holder.bind(getItem(position), onAsteroidClick)

    class AsteroidItemViewHolder(
        private val binding: LayoutItemAsteroidBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            asteroid: Asteroid,
            onAsteroidClickListener: OnAsteroidClickListener,
        ) {
            binding.onItemClick = onAsteroidClickListener
            binding.asteroid = asteroid
        }

        companion object {
            fun from(parent: ViewGroup) = AsteroidItemViewHolder(
                LayoutItemAsteroidBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    class AsteroidDiffUtil : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid) = oldItem == newItem
    }

    class OnAsteroidClickListener(private val listener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = listener(asteroid)
    }
}