package com.example.pokedex.presentation.adapter

sealed class RosterItem

class PokemonItem(
        val id: Long,
        val name: String,
        val artImgUrl: String,
        val spriteImgUrl: String
) : RosterItem()

class GenerationListItem(
        //map of ids to names
        val generationList: Map<Long,String>,
        val checkedId: Long
) : RosterItem()

class TypeListItem(
        //map of ids to names
        val typeMap: Map<Long,String>,
        val checkedId: Long
) : RosterItem()

object EmptyStateItem: RosterItem()