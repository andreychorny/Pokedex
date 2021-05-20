package com.example.pokedex.domain

import androidx.lifecycle.LiveData
import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.database.entity.DbPokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemonList(
        filter: PokemonApiFilter,
        generationId: Long = 1, typeId: Long = 1
    ): List<PokemonEntity>

    suspend fun getGenerationsList(): List<GenerationEntity>
    suspend fun getTypesList(): List<TypeEntity>

    suspend fun getPokemonById(id: Long): Flow<PokemonDetailEntity?>

    suspend fun updatePokemonInDatabase(dbPokemonDetail: DbPokemonDetail)

    suspend fun downloadPokemonDetail(id: Long)
}