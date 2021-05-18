package com.example.pokedex.domain

import com.example.pokedex.database.entity.DbPokemonDetail
import com.example.pokedex.database.entity.DbStat
import com.example.pokedex.database.entity.DbType

data class PokemonDetailEntity(
    val id: Long,
    val name: String,
    val weight: Int,
    val height: Int,
    val stats: Map<String, Int>,
    val types: List<String>,
    val officialArtworkUrl: String,
    val dreamWorldUrlPic: String,
    val isLiked: Boolean
)

fun PokemonDetailEntity.asDatabaseEntity(isLiked: Boolean = false): DbPokemonDetail {
    return DbPokemonDetail(
        pokemonId = id,
        name = name,
        weight = weight,
        height = height,
        officialArtworkUrl = officialArtworkUrl,
        dreamWorldUrlPic = dreamWorldUrlPic,
        isLiked = isLiked
    )
}

fun Map<String, Int>.asDatabaseStat(pokemonId: Long): List<DbStat> {
    return map { DbStat(pokemonId, it.key, it.value) }
}

fun List<String>.asDatabaseType(): List<DbType> {
    return map { DbType(name = it) }
}