package com.example.pokedex.domain.repository

import androidx.lifecycle.LiveData
import com.example.pokedex.database.entity.DbPokemonDetail
import com.example.pokedex.database.entity.FullDbPokemonDetail
import kotlinx.coroutines.CoroutineScope

interface PokemonDetailsRepository {

    fun getPokemonDetailsLiveData(): LiveData<FullDbPokemonDetail?>

    suspend fun updatePokemonInDatabase(dbPokemonDetail: DbPokemonDetail)

    suspend fun loadPokemonById(id: Long, scope: CoroutineScope)

}