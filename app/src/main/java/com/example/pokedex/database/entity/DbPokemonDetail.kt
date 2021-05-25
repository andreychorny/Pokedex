package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbPokemonDetail(
    @PrimaryKey
    val pokemonId: Long,
    val name: String,
    val weight: Int,
    val height: Int,
    val officialArtworkUrl: String,
    val spriteUrlPic: String,
    val isLiked: Boolean
)


