package com.example.pokedex.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.domain.PokemonEntity

class PokemonRosterAdapter: RecyclerView.Adapter<PokemonRosterAdapter.PokemonViewHolder>() {

    var data = listOf<PokemonEntity>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    class PokemonViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val nameView = itemView.findViewById<TextView>(R.id.pokemonName)
        private val imageView = itemView.findViewById<ImageView>(R.id.pokemonImage)

        fun bind(item: PokemonEntity){
            nameView.text = item.name
            Glide.with(imageView.context)
                    .load(item.frontImgUrl)
                    .into(imageView);
        }

        companion object {
            fun from(parent: ViewGroup): PokemonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                        .inflate(R.layout.pokemon_item, parent, false)

                return PokemonViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder.from(parent)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(data[position])
    }


}