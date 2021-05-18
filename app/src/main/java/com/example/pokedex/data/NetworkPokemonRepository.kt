package com.example.pokedex.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.pokedex.data.network.*
import com.example.pokedex.database.PokedexDatabase
import com.example.pokedex.database.entity.DbPokemonDetail
import com.example.pokedex.database.entity.PokemonToGeneration
import com.example.pokedex.database.entity.PokemonTypeCrossRef
import com.example.pokedex.database.entity.asDomainEntity
import com.example.pokedex.domain.*
import com.example.pokedex.generateOfficialArtworkUrlFromId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val RETRIEVE_ID_REGEX = "(\\d+)(?!.*\\d)".toRegex()

class NetworkPokemonRepository(
    private val api: PokemonRosterService,
    private val database: PokedexDatabase
) : PokemonRepository {


    override suspend fun getPokemonList(
        filter: PokemonApiFilter,
        generationId: Long, typeId: Long
    ): List<PokemonEntity> {

        return when (filter) {
            PokemonApiFilter.SHOW_ALL -> retrieveAllPokemon()
            PokemonApiFilter.SHOW_GENERATION -> retrievePokemonByGeneration(generationId)
            PokemonApiFilter.SHOW_TYPE -> retrievePokemonByType(typeId)
        }
    }

    override suspend fun getGenerationsList(): List<GenerationEntity> {
        var generations = listOf<GenerationEntity>()
        withContext(Dispatchers.IO) {
            generations = database.generationDao.getGenerationList().map { it.asDomainEntity() }
            if (generations.isEmpty()) {
                generations = downloadGenerationList()
                database.generationDao.insert(generations.map { it.asDatabaseEntity() })
            }
        }
        return generations

    }

    private suspend fun downloadGenerationList(): List<GenerationEntity>{
        return api.getAllGenerations().results.map {
            val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
            GenerationEntity(id, it.name)
        }
    }

    override suspend fun getPokemonById(id: Long): LiveData<PokemonDetailEntity> {

        return Transformations.map(database.pokemonDao.getPokemonDetail(id)) {
            it?.asDomainEntity()
        }
    }

    override suspend fun downloadPokemonDetail(id: Long) {

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
        val pokemonToTypeList = mutableListOf<PokemonTypeCrossRef>()
        for(type in types) {
            pokemonToTypeList.add(PokemonTypeCrossRef(id, type.name))
        }
        database.pokemonDao.insertPokemonToTypes(pokemonToTypeList)
        database.pokemonDao.insertDetail(newPokemonDetail)

    }

    override suspend fun updatePokemonInDatabase(dbPokemonDetail: DbPokemonDetail) {
        database.pokemonDao.update(dbPokemonDetail)
    }


    private suspend fun retrieveAllPokemon(): List<PokemonEntity> {
        var pokemons = listOf<PokemonEntity>()
        withContext(Dispatchers.IO) {
            pokemons = database.pokemonDao.getPokemonList().map { it.asDomainEntity() }
            if (pokemons.isEmpty()) {
                pokemons = downloadAllPokemon()
                database.pokemonDao.insertBaseInfoList(pokemons.map { it.asDatabaseEntity() })
            }
        }
        return pokemons
    }

    private suspend fun downloadAllPokemon(): List<PokemonEntity> {
        return api.getAllPokemonRoster().results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generateOfficialArtworkUrlFromId(id))
            }
    }

    private suspend fun retrievePokemonByGeneration(generationId: Long): List<PokemonEntity> {
        var pokemons = listOf<PokemonEntity>()
        withContext(Dispatchers.IO) {
            pokemons = database.pokemonDao.getPokemonListByGeneration(generationId).map { it.asDomainEntity() }
            if (pokemons.isEmpty()) {
                pokemons = downloadPokemonByGeneration(generationId)
                database.pokemonDao.insertBaseInfoList(pokemons.map { it.asDatabaseEntity() })
                database.pokemonDao.insertPokemonToGeneration(pokemons.map{PokemonToGeneration(it.id, generationId)})
            }
        }
        return pokemons

    }

    private suspend fun downloadPokemonByGeneration(generationId: Long): List<PokemonEntity>{
        return api.getPokemonRosterByGeneration(generationId).results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generateOfficialArtworkUrlFromId(id))
            }
    }
    private suspend fun retrievePokemonByType(typeId: Long): List<PokemonEntity> {
        //TODO
        return api.getAllPokemonRoster().results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generateOfficialArtworkUrlFromId(id))
            }
    }
}