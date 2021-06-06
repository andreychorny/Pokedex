package com.example.pokedex.domain

import kotlinx.coroutines.CoroutineScope


interface PokemonRepository {

    suspend fun downloadPokemonDetail(id: Long, scope: CoroutineScope): PokemonDetailEntity

    suspend fun downloadGenerationList(scope: CoroutineScope): List<GenerationEntity>

    suspend fun downloadAllPokemon(scope: CoroutineScope): List<PokemonEntity>

    suspend fun downloadPokemonByGeneration(generationId: Long, scope: CoroutineScope): List<PokemonEntity>

    suspend fun downloadPokemonByType(typeId: Long, scope: CoroutineScope): List<PokemonEntity>

    suspend fun downloadTypeList(scope: CoroutineScope): List<TypeEntity>

}