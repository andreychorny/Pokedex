package com.example.pokedex.presentation.adapter.item

import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.pokemon_item.view.*

class PokemonItem(
    val pokemonId: Long,
    val name: String,
    val frontImgUrl: String,
) : Item<GroupieViewHolder>(), RosterItem {

    var onItemClicked: ((Long) -> Unit)? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.pokemonName.text = name
        Glide.with( viewHolder.itemView.pokemonImage
            .context)
            .load(frontImgUrl)
            .into(viewHolder.itemView.pokemonImage)
        viewHolder.itemView.pokemonImage.setOnClickListener {
            onItemClicked?.invoke(pokemonId)
        }
    }

    override fun getLayout(): Int = R.layout.pokemon_item

    override fun getSpanSize(spanCount: Int, position: Int): Int = 1
}