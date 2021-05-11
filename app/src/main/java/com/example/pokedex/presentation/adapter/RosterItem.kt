package com.example.pokedex.presentation.adapter


sealed class RosterItem

class PokemonItem(
        val id: Long,
        val name: String,
        val frontImgUrl: String,
) : RosterItem()

class GenerationListItem(
        val generationList: List<Long>
) : RosterItem()