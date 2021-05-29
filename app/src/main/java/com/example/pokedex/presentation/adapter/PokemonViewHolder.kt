package com.example.pokedex.presentation.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokedex.R
import com.example.pokedex.databinding.PokemonItemBinding

class PokemonViewHolder(
    view: View, val onItemClicked: (id: Long, cardView: View) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = PokemonItemBinding.bind(itemView)

    fun bind(item: PokemonItem) = with(binding) {
        pokemonCard.transitionName = "transition${item.id}"
        Glide.with(pokemonImage.context)
            .asBitmap()
            .load(item.artImgUrl)
//                    in case of error, we try download another sprite image, as some pokemons don't have
//                    official art pic
            .error(
                Glide
                    .with(pokemonImage.context)
                    .asBitmap()
                    .load(item.spriteImgUrl)
                    .listener(setBackgroundColor())
            ).listener(setBackgroundColor())
            .into(pokemonImage)
        itemView.setOnClickListener {
            onItemClicked(item.id, pokemonCard)
        }
        pokemonName.text = item.name
    }

    private fun setBackgroundColor() = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            resource?.let {
                val palette = Palette.from(it).generate()
                binding.pokemonCard.setBackgroundColor(palette.getLightMutedColor(Color.WHITE))
                binding.pokemonName.setTextColor(palette.getDarkVibrantColor(Color.BLACK))
            }
            return false
        }
    }

    companion object {
        fun from(parent: ViewGroup, onItemClicked: (id: Long, cardView: View) -> Unit): PokemonViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemon_item, parent, false)

            return PokemonViewHolder(view, onItemClicked)
        }
    }
}