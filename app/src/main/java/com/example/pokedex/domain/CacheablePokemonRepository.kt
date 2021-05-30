package com.example.pokedex.domain

import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.database.entity.DbPokemonDetail
import kotlinx.coroutines.flow.Flow

interface CacheablePokemonRepository: PokemonRepository {

    suspend fun updatePokemonInDatabase(dbPokemonDetail: DbPokemonDetail)

    //download functions are aimed to download JSONs from backend and insert them in the local database

    suspend fun downloadPokemonDetail(id: Long): PokemonDetailEntity

    suspend fun downloadGenerationList(): List<GenerationEntity>

    suspend fun downloadAllPokemon(): List<PokemonEntity>

    suspend fun downloadPokemonByGeneration(generationId: Long): List<PokemonEntity>

    suspend fun downloadPokemonByType(typeId: Long): List<PokemonEntity>

    suspend fun downloadTypeList(): List<TypeEntity>
}