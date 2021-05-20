package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonToType (
    @PrimaryKey val pokemonId: Long,
    val typeId: Long
)