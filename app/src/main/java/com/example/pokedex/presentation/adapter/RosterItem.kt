package com.example.pokedex.presentation.adapter

sealed class RosterItem

class PokemonItem(
        val id: Long,
        val name: String,
        val artImgUrl: String,
        val spriteImgUrl: String
) : RosterItem()

class GenerationListItem(
        //list of ids
        val generationList: List<Long>
) : RosterItem()

class TypeListItem(
        //map of ids to names
        val typeMap: Map<Long,String>
) : RosterItem()
