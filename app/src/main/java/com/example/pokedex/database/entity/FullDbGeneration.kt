package com.example.pokedex.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class FullDbGeneration(
    @Embedded val generation: DbGeneration,
    @Relation(
        parentColumn = "generationId",
        entityColumn = "generationId"
    )
    val pokemons: List<PokemonToGeneration>

)