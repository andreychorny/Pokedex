package com.example.pokedex.presentation.roster

sealed class PokemonApiFilter {
    object SHOW_GENERATION: PokemonApiFilter(){
        var id: Long = 0
    }
    object SHOW_TYPE: PokemonApiFilter(){
        var id: Long = 0
    }
    object SHOW_LIKED: PokemonApiFilter()
    object SHOW_ALL: PokemonApiFilter()
}