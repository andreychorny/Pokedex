package com.example.pokedex.domain.repository

import androidx.lifecycle.LiveData
import com.example.pokedex.database.entity.DbGeneration
import com.example.pokedex.database.entity.DbPokemonBaseInfo
import com.example.pokedex.database.entity.DbType
import kotlinx.coroutines.CoroutineScope

interface PokemonRosterRepository {

    fun getPokemonListMediatorLD(): LiveData<List<DbPokemonBaseInfo>>

    suspend fun loadAllPokemon(scope: CoroutineScope)


    suspend fun loadLikedPokemon(scope: CoroutineScope)

    suspend fun loadPokemonByGeneration(generationId: Long,scope: CoroutineScope)

    suspend fun loadPokemonByTypeFromDB(typeId: Long,scope: CoroutineScope)

    suspend fun loadGenerationsList(scope: CoroutineScope): LiveData<List<DbGeneration>>

    suspend fun loadTypesList(scope: CoroutineScope): LiveData<List<DbType>>

}