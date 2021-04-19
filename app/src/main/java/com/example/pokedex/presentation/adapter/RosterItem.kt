package com.example.pokedex.presentation.adapter

import com.example.pokedex.domain.GenerationEntity

sealed class RosterItem

class PokemonItem(
        val id: Long,
        val name: String,
        val frontImgUrl: String,
        val generation: Int
) : RosterItem()

class GenerationListItem(
        val adapter: GenerationListAdapter
) : RosterItem()