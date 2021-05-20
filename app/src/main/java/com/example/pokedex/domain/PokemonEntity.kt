package com.example.pokedex.domain

import com.example.pokedex.database.entity.DbPokemonBaseInfo

data class PokemonEntity(
        val id: Long,
        val name: String,
        val frontImgUrl: String,
)
fun PokemonEntity.asDatabaseEntity(isLiked: Boolean = false): DbPokemonBaseInfo {
        return DbPokemonBaseInfo(
                pokemonId = id,
                name = name,
                frontImgUrl = frontImgUrl
        )
}