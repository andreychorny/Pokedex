package com.example.pokedex.presentation.roster

import com.example.pokedex.database.entity.DbPokemonBaseInfo
import com.example.pokedex.presentation.roster.adapter.RosterItem

sealed class PokemonRosterViewState {

    object Loading: PokemonRosterViewState()
    data class Error(val message: String): PokemonRosterViewState()
    data class Data(val items: List<DbPokemonBaseInfo>): PokemonRosterViewState()
}