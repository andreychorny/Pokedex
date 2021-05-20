package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex.domain.PokemonDetailEntity
import com.example.pokedex.domain.PokemonEntity

@Entity
data class DbPokemonBaseInfo (
    @PrimaryKey
    val pokemonId: Long,
    val name: String,
    val frontImgUrl: String
    )

fun DbPokemonBaseInfo.asDomainEntity(): PokemonEntity {
    return PokemonEntity(
        id = pokemonId,
        name = name,
        frontImgUrl = frontImgUrl
    )
}
