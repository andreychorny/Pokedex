package com.example.pokedex.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.domain.PokemonEntity
import java.lang.IllegalStateException

private const val ITEM_TYPE_POKEMON = 1
private const val ITEM_TYPE_GENERATION = 2
class PokemonRosterAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = listOf<RosterItem>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_TYPE_POKEMON -> PokemonViewHolder.from(parent)
            ITEM_TYPE_GENERATION -> GenerationViewHolder.from(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when(item){
            is PokemonItem -> {
                (holder as PokemonViewHolder).bind(item)
            }
            is GenerationItem -> {
                (holder as GenerationViewHolder).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position]) {
            is PokemonItem -> ITEM_TYPE_POKEMON
            is GenerationItem -> ITEM_TYPE_GENERATION
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = getSpanSizeLookup()
    }

    private fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (data[position]) {
                    is PokemonItem -> 1
                    is GenerationItem -> 2
                }
            }
        }
    }

    class PokemonViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val nameView = itemView.findViewById<TextView>(R.id.pokemonName)
        private val imageView = itemView.findViewById<ImageView>(R.id.pokemonImage)

        fun bind(item: PokemonItem){
            nameView.text = item.name
            Glide.with(imageView.context)
                    .load(item.frontImgUrl)
                    .into(imageView);
        }

        companion object {
            fun from(parent: ViewGroup): PokemonViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.pokemon_item, parent, false)

                return PokemonViewHolder(view)
            }
        }
    }

    class GenerationViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val generationNameView = itemView.findViewById<TextView>(R.id.generationName)

        fun bind(item: GenerationItem){
            generationNameView.text = item.generationText
        }

        companion object {
            fun from(parent: ViewGroup): GenerationViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.generation_item, parent, false)

                return GenerationViewHolder(view)
            }
        }
    }


}