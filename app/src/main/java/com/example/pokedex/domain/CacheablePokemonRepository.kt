package com.example.pokedex.domain

import com.example.pokedex.database.entity.DbPokemonDetail
import com.example.pokedex.presentation.roster.PokemonApiFilter
import kotlinx.coroutines.flow.Flow

interface CacheablePokemonRepository: PokemonRepository {

    suspend fun updatePokemonInDatabase(dbPokemonDetail: DbPokemonDetail)

    suspend fun getAllPokemonFromDB(): List<PokemonEntity>

    suspend fun getLikedPokemonFromDB(): List<PokemonEntity>

    suspend fun getPokemonByGenerationFromDB(generationId: Long): List<PokemonEntity>

    suspend fun getPokemonByTypeFromDB(typeId: Long): List<PokemonEntity>

    suspend fun getGenerationsList(): List<GenerationEntity>

    suspend fun getTypesList(): List<TypeEntity>

    suspend fun getPokemonById(id: Long): Flow<PokemonDetailEntity?>
}