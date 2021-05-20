package com.example.pokedex.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonToGeneration(
    @PrimaryKey val pokemonId: Long,
    val generationId: Long
)