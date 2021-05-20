package com.example.pokedex.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["pokemonId", "typeId"])
data class PokemonTypeCrossRef(
    val pokemonId: Long,
    val typeId: Long
)
