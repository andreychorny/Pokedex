package com.example.pokedex.database.entity

import androidx.room.Embedded

data class PokemonToGeneration(
    @Embedded val pokemonBaseInfo: DbPokemonBaseInfo,
    val generationId: Long
)