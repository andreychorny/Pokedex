package com.example.pokedex.presentation.adapter

sealed class RosterItem

class PokemonItem(
        val id: Long,
        val name: String,
        val frontImgUrl: String,
        val generation: Int
) : RosterItem()

class GenerationItem(
        val generationText: String
) : RosterItem()