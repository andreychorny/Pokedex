package com.example.pokedex.domain

import androidx.lifecycle.LiveData
import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.database.entity.DatabasePokemonDetail
import com.example.pokedex.database.entity.FullDatabasePokemonDetail

interface PokemonRepository {

    suspend fun getPokemonList(
        filter: PokemonApiFilter,
        generationId: Long = 1, typeId: Long = 1
    ): List<PokemonEntity>

    suspend fun getGenerationsList(): List<GenerationEntity>

    suspend fun getPokemonById(id: Long): LiveData<PokemonDetailEntity>

    suspend fun updatePokemonInDatabase(databasePokemonDetail: DatabasePokemonDetail)

    suspend fun insertPokemonDetail(id: Long)
}