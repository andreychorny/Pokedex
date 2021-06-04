package com.example.pokedex.domain


interface PokemonRepository {

    suspend fun downloadPokemonDetail(id: Long): PokemonDetailEntity

    suspend fun downloadGenerationList(): List<GenerationEntity>

    suspend fun downloadAllPokemon(): List<PokemonEntity>

    suspend fun downloadPokemonByGeneration(generationId: Long): List<PokemonEntity>

    suspend fun downloadPokemonByType(typeId: Long): List<PokemonEntity>

    suspend fun downloadTypeList(): List<TypeEntity>

}