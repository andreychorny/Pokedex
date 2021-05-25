package com.example.pokedex.domain

import com.example.pokedex.database.entity.DbPokemonBaseInfo

data class PokemonEntity(
        val id: Long,
        val name: String,
        val artImgUrl: String,
        val spriteImgUrl: String
)
fun PokemonEntity.asDatabaseEntity(): DbPokemonBaseInfo {
        return DbPokemonBaseInfo(
                pokemonId = id,
                name = name,
                artImgUrl = artImgUrl,
                spriteImgUrl = spriteImgUrl
        )
}