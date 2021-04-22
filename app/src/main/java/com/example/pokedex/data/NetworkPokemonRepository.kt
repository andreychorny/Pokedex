package com.example.pokedex.data

import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.data.network.PokemonRosterService
import com.example.pokedex.domain.GenerationEntity
import com.example.pokedex.domain.PokemonDetailEntity
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.generateDreamWorldPicUrlFromId
import com.example.pokedex.generateOfficialArtworkUrlFromId

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

    override suspend fun getPokemonById(id: Long): PokemonDetailEntity {
        val jsonPokemon = api.getPokemonDetails(id)
        return PokemonDetailEntity(
            jsonPokemon.id.toLong(),
            jsonPokemon.name,
            jsonPokemon.weight,
            jsonPokemon.height,
            jsonPokemon.stats.map{it.stat.name to it.base_stat}.toMap(),
            jsonPokemon.types.map { it.type.name },
            generateOfficialArtworkUrlFromId(jsonPokemon.id.toLong()),
            generateDreamWorldPicUrlFromId(jsonPokemon.id.toLong())
        )
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