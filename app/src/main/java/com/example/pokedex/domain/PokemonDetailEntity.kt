package com.example.pokedex.domain

import com.example.pokedex.database.entity.DatabasePokemonDetail
import com.example.pokedex.database.entity.DatabaseStat
import com.example.pokedex.database.entity.DatabaseType
import com.example.pokedex.database.entity.FullDatabasePokemonDetail

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

fun PokemonDetailEntity.asDatabaseEntity(isLiked: Boolean = false): DatabasePokemonDetail {
    return DatabasePokemonDetail(
        pokemonId = id,
        name = name,
        weight = weight,
        height = height,
        officialArtworkUrl = officialArtworkUrl,
        dreamWorldUrlPic = dreamWorldUrlPic,
        isLiked = isLiked
    )
}

fun Map<String, Int>.asDatabaseStat(pokemonId: Long): List<DatabaseStat> {
    return map { DatabaseStat(pokemonId, it.key, it.value) }
}

fun List<String>.asDatabaseType(): List<DatabaseType> {
    return map { DatabaseType(name = it) }
}