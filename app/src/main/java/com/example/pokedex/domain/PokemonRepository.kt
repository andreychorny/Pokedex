package com.example.pokedex.domain

interface PokemonRepository {

    fun getPokemonList(): List<PokemonEntity>

    fun addPokemon(pokemonEntity: PokemonEntity): Boolean
}