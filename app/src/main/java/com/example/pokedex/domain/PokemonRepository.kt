package com.example.pokedex.domain

interface PokemonRepository {

    suspend fun getPokemonList(): List<PokemonEntity>

}