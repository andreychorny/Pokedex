package com.example.pokedex.domain

data class PokemonDetailEntity (
    val id: Long,
    val name: String,
    val weight: Int,
    val height: Int,
    val stats: Map<String, Int>,
    val types: List<String>,
    val officialArtworkUrl: String,
    val dreamWorldUrlPic: String,
)