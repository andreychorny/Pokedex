package com.example.pokedex.domain

import com.example.pokedex.presentation.roster.PokemonApiFilter
import kotlinx.coroutines.CoroutineScope


interface PokemonRepository {

    suspend fun downloadPokemonDetail(id: Long, scope: CoroutineScope): PokemonDetailEntity

    suspend fun downloadGenerationList(scope: CoroutineScope): List<GenerationEntity>

    suspend fun downloadPokemonList(filter: PokemonApiFilter, scope: CoroutineScope): List<PokemonEntity>

    suspend fun downloadTypeList(scope: CoroutineScope): List<TypeEntity>

}