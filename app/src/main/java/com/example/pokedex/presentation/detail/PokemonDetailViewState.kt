package com.example.pokedex.presentation.detail

import com.example.pokedex.domain.PokemonDetailEntity

sealed class PokemonDetailViewState {

    object Loading: PokemonDetailViewState()
    data class Error(val message: String): PokemonDetailViewState()
    data class Data(val detail: PokemonDetailEntity): PokemonDetailViewState()

}