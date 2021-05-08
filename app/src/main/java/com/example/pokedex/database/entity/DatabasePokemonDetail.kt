package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.pokedex.domain.PokemonDetailEntity

@Entity
data class DatabasePokemonDetail(
    @PrimaryKey
    val pokemonId: Long,
    val name: String,
    val weight: Int,
    val height: Int,
    val officialArtworkUrl: String,
    val dreamWorldUrlPic: String,
    val isLiked: Boolean
)


