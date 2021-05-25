package com.example.pokedex.domain

import com.example.pokedex.database.entity.DbPokemonDetail

data class PokemonDetailEntity(
    val id: Long,
    val name: String,
    val weight: Int,
    val height: Int,
    val stats: Map<String, Int>,
    val types: List<String>,
    val officialArtworkUrl: String,
    val spriteUrlPic: String,
    val isLiked: Boolean
)

fun PokemonDetailEntity.asDatabaseEntity(isLiked: Boolean = false): DbPokemonDetail {
    return DbPokemonDetail(
        pokemonId = id,
        name = name,
        weight = weight,
        height = height,
        officialArtworkUrl = officialArtworkUrl,
        spriteUrlPic = spriteUrlPic,
        isLiked = isLiked
    )
}
