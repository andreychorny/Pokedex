package com.example.pokedex.presentation.roster.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


private const val ITEM_TYPE_POKEMON = 1
private const val ITEM_TYPE_GENERATION_LIST = 2
private const val ITEM_TYPE_TYPE_LIST = 3
private const val ITEM_TYPE_EMPTY_STATE = 4

class PokemonRosterAdapter(
    private val onPokemonItemClicked: (id: Long, cardView: View) -> Unit,
    private val onGenerationItemClicked: (id: Long, isChecked: Boolean) -> Unit,
    private val onTypeItemClicked: (id: Long, isChecked: Boolean) -> Unit
) : ListAdapter<RosterItem, RecyclerView.ViewHolder>(PokemonRosterDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_POKEMON -> PokemonViewHolder.from(parent, onPokemonItemClicked)
            ITEM_TYPE_GENERATION_LIST -> GenerationListViewHolder.from(
                parent,
                onGenerationItemClicked
            )
            ITEM_TYPE_TYPE_LIST -> TypeListViewHolder.from(parent, onTypeItemClicked)
            ITEM_TYPE_EMPTY_STATE -> EmptyStateViewHolder.from(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is PokemonItem -> {
                (holder as PokemonViewHolder).bind(item)
            }
            is GenerationListItem -> {
                (holder as GenerationListViewHolder).bind(item)
            }
            is TypeListItem -> {
                (holder as TypeListViewHolder).bind(item)
            }
            is EmptyStateItem -> {
                (holder as EmptyStateViewHolder).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PokemonItem -> ITEM_TYPE_POKEMON
            is GenerationListItem -> ITEM_TYPE_GENERATION_LIST
            is TypeListItem -> ITEM_TYPE_TYPE_LIST
            is EmptyStateItem -> ITEM_TYPE_EMPTY_STATE
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = getSpanSizeLookup()
    }

    private fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (getItem(position)) {
                    is PokemonItem -> 1
                    is GenerationListItem -> 2
                    is TypeListItem -> 2
                    is EmptyStateItem -> 2
                }
            }
        }
    }
}


class PokemonRosterDiffUtil : DiffUtil.ItemCallback<RosterItem>() {

    override fun areItemsTheSame(oldItem: RosterItem, newItem: RosterItem): Boolean {
        if (oldItem is PokemonItem && newItem is PokemonItem) {
            return oldItem.id == newItem.id
        }
        if (oldItem is GenerationListItem && newItem is GenerationListItem) {
            return oldItem.generationList == newItem.generationList
        }
        if (oldItem is TypeListItem && newItem is TypeListItem) {
            return oldItem.typeMap == newItem.typeMap
        }
        return false
    }

    override fun areContentsTheSame(oldItem: RosterItem, newItem: RosterItem): Boolean {
        if (oldItem is PokemonItem && newItem is PokemonItem) {
            return oldItem == newItem
        }
        if (oldItem is GenerationListItem && newItem is GenerationListItem) {
            return oldItem.generationList == newItem.generationList
        }
        if (oldItem is TypeListItem && newItem is TypeListItem) {
            return oldItem.typeMap == newItem.typeMap
        }
        return false
    }
}
