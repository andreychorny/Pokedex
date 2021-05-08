package com.example.pokedex.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.pokedex.data.network.*
import com.example.pokedex.database.PokedexDatabase
import com.example.pokedex.database.entity.DatabasePokemonDetail
import com.example.pokedex.database.entity.asDomainEntity
import com.example.pokedex.domain.*
import com.example.pokedex.generateOfficialArtworkUrlFromId

private val  RETRIEVE_ID_REGEX = "(\\d+)(?!.*\\d)".toRegex()

class NetworkPokemonRepository(
    private val api: PokemonRosterService,
    private val database: PokedexDatabase): PokemonRepository {


    override suspend fun getPokemonList(filter: PokemonApiFilter,
                                        generationId: Long, typeId: Long): List<PokemonEntity> {

        return when(filter){
            PokemonApiFilter.SHOW_ALL -> retrieveAllPokemon()
            PokemonApiFilter.SHOW_GENERATION -> retrievePokemonByGeneration(generationId)
            PokemonApiFilter.SHOW_TYPE -> retrievePokemonByType(typeId)
        }
    }

    override suspend fun getGenerationsList(): List<GenerationEntity> {
        return api.getAllGenerations().results.map {
            val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
            GenerationEntity(id, it.name)
        }
    }

    override suspend fun getPokemonById(id: Long): LiveData<PokemonDetailEntity> {

        return Transformations.map(database.pokemonDao.getPokemonDetail(id)) {
            it.asDomainEntity()
        }

    }

    override suspend fun insertPokemonDetail(id: Long) {

        val oldIsLiked = database.pokemonDao.isPokemonLiked(id)
        val jsonPokemon = api.getPokemonDetails(id)
        val stats = jsonPokemon.stats.asDatabaseStat(jsonPokemon.id.toLong())
        val types = jsonPokemon.types.asDatabaseType()
        //if pokemon is in database - we need to enter old "IsLiked" state
        val newPokemonDetail = jsonPokemon.asDatabaseEntity(
            oldIsLiked ?: false
        )
        database.statDao.insert(stats)
        database.typeDao.insert(types)
        database.pokemonDao.insert(newPokemonDetail)

    }

    override suspend fun updatePokemonInDatabase(databasePokemonDetail: DatabasePokemonDetail) {
        database.pokemonDao.update(databasePokemonDetail)
    }


    private suspend fun retrieveAllPokemon(): List<PokemonEntity>{
        return api.getAllPokemonRoster().results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generateOfficialArtworkUrlFromId(id)) }
    }

    private suspend fun retrievePokemonByGeneration(generationId: Long): List<PokemonEntity>{
        return api.getPokemonRosterByGeneration(generationId).results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generateOfficialArtworkUrlFromId(id)) }
    }

    private suspend fun retrievePokemonByType(typeId: Long): List<PokemonEntity>{
        //TODO
        return api.getAllPokemonRoster().results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generateOfficialArtworkUrlFromId(id)) }
    }
}