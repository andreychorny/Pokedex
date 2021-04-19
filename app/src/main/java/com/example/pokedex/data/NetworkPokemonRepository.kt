package com.example.pokedex.data

import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.data.network.PokemonRosterService
import com.example.pokedex.domain.GenerationEntity
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.generatePicUrlFromId

private val  RETRIEVE_ID_REGEX = "(\\d+)(?!.*\\d)".toRegex()

class NetworkPokemonRepository(val api: PokemonRosterService): PokemonRepository {

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

    private suspend fun retrieveAllPokemon(): List<PokemonEntity>{
        return api.getAllPokemonRoster().results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generatePicUrlFromId(id), 1) }
    }

    private suspend fun retrievePokemonByGeneration(generationId: Long): List<PokemonEntity>{
        return api.getPokemonRosterByGeneration(generationId).results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generatePicUrlFromId(id), 1) }
    }

    private suspend fun retrievePokemonByType(typeId: Long): List<PokemonEntity>{
        //TODO
        return api.getAllPokemonRoster().results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generatePicUrlFromId(id), 1) }
    }
}