package com.example.pokedex.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["pokemonId", "name"])
data class PokemonTypeCrossRef(
    val pokemonId: Long,
    val name: String
)
