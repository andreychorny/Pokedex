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
import com.example.pokedex.domain.GenerationEntity
import com.example.pokedex.domain.PokemonEntity
import java.lang.IllegalStateException

private const val ITEM_TYPE_POKEMON = 1
private const val ITEM_TYPE_GENERATION_LIST = 2

class PokemonRosterAdapter(
    private val onItemClicked: (id: Long) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = listOf<RosterItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_POKEMON -> PokemonViewHolder.from(parent, onItemClicked)
            ITEM_TYPE_GENERATION_LIST -> GenerationListViewHolder.from(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (item) {
            is PokemonItem -> {
                (holder as PokemonViewHolder).bind(item)
            }
            is GenerationListItem -> {
                (holder as GenerationListViewHolder).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is PokemonItem -> ITEM_TYPE_POKEMON
            is GenerationListItem -> ITEM_TYPE_GENERATION_LIST
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
                    is GenerationListItem -> 2
                }
            }
        }
    }

    class PokemonViewHolder(
        view: View, val onItemClicked: (id: Long) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val nameView = itemView.findViewById<TextView>(R.id.pokemonName)
        private val imageView = itemView.findViewById<ImageView>(R.id.pokemonImage)

        fun bind(item: PokemonItem) {
            nameView.text = item.name
            Glide.with(imageView.context)
                .load(item.frontImgUrl)
                .into(imageView)
            itemView.setOnClickListener {
                onItemClicked(item.id)
            }
        }

        companion object {
            fun from(parent: ViewGroup, onItemClicked: (id: Long) -> Unit ): PokemonViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.pokemon_item, parent, false)

                return PokemonViewHolder(view, onItemClicked)
            }
        }
    }

    class GenerationListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val generationRecyclerView =
            itemView.findViewById<RecyclerView>(R.id.generation_list)

        fun bind(item: GenerationListItem) {
            generationRecyclerView.adapter = item.adapter
        }

        companion object {
            fun from(parent: ViewGroup): GenerationListViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.generation_list_item, parent, false)

                return GenerationListViewHolder(view)
            }
        }
    }


}