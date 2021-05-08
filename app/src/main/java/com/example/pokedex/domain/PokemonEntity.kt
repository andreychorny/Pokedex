package com.example.pokedex.domain

import com.example.pokedex.database.entity.DatabasePokemonDetail

data class PokemonEntity(
        val id: Long,
        val name: String,
        val frontImgUrl: String,
)
