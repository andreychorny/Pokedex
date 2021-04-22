package com.example.pokedex.domain

import com.example.pokedex.data.network.PokemonApiFilter

interface PokemonRepository {

    suspend fun getPokemonList(
        filter: PokemonApiFilter,
        generationId: Long = 1, typeId: Long = 1
    ): List<PokemonEntity>

    suspend fun getGenerationsList(): List<GenerationEntity>

    suspend fun getPokemonById(id: Long): PokemonDetailEntity
}