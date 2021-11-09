package com.example.pokedex.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.pokedex.RETRIEVE_ID_REGEX
import com.example.pokedex.data.network.PokemonService
import com.example.pokedex.database.PokedexDatabase
import com.example.pokedex.database.entity.*
import com.example.pokedex.domain.GenerationEntity
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.TypeEntity
import com.example.pokedex.domain.asDatabaseEntity
import com.example.pokedex.domain.repository.PokemonRosterRepository
import com.example.pokedex.generateOfficialArtworkUrlFromId
import com.example.pokedex.generateSpritePicUrlFromId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonRosterRepositoryImpl(
    private val api: PokemonService,
    private val database: PokedexDatabase,
) : PokemonRosterRepository {

    private val pokemonListMediatorLD: MediatorLiveData<List<DbPokemonBaseInfo>> =
        MediatorLiveData()

    private var liveData: LiveData<List<DbPokemonBaseInfo>>? = null

    override suspend fun loadGenerationsList(scope: CoroutineScope): LiveData<List<DbGeneration>> {
        scope.launch(Dispatchers.IO) {
            downloadGenerationList()
        }
        return database.generationDao.getGenerationList()
    }

    private suspend fun downloadGenerationList() {
        val generations = api.getAllGenerations().results.map {
            val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
            GenerationEntity(id, it.name)
        }
        database.generationDao.insert(generations.map { it.asDatabaseEntity() })
    }

    override fun getPokemonListMediatorLD(): LiveData<List<DbPokemonBaseInfo>> {
        return pokemonListMediatorLD
    }

    override suspend fun loadAllPokemon(scope: CoroutineScope) {
        liveData?.let {
            pokemonListMediatorLD.removeSource(it)
        }
        liveData = database.pokemonDao.getPokemonList()
        pokemonListMediatorLD.addSource(liveData!!) {
            pokemonListMediatorLD.value = it
        }
        scope.launch(Dispatchers.IO) {
            downloadAllPokemon()
        }
    }

    override suspend fun loadLikedPokemon(scope: CoroutineScope) {
        liveData?.let {
            pokemonListMediatorLD.removeSource(it)
        }
        liveData = database.pokemonDao.getLikedPokemonList()
        pokemonListMediatorLD.addSource(liveData!!) {
            pokemonListMediatorLD.value = it
        }
    }

    private suspend fun downloadAllPokemon() {
        val pokemons = api.getAllPokemonRoster().results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(
                    id,
                    it.name,
                    generateOfficialArtworkUrlFromId(id),
                    generateSpritePicUrlFromId(id)
                )
            }
        database.pokemonDao.insertBaseInfoList(pokemons.map { it.asDatabaseEntity() })

    }

    override suspend fun loadPokemonByGeneration(generationId: Long, scope: CoroutineScope) {
        liveData?.let {
            pokemonListMediatorLD.removeSource(it)
        }
        liveData = database.pokemonDao.getPokemonListByGeneration(generationId)
        pokemonListMediatorLD.addSource(liveData!!) {
            pokemonListMediatorLD.value = it
        }
        scope.launch(Dispatchers.IO) {
            downloadPokemonByGeneration(generationId)
        }
    }

    private suspend fun downloadPokemonByGeneration(
        generationId: Long,
    ) {
        val pokemons = api.getPokemonRosterByGeneration(generationId).results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(
                    id,
                    it.name,
                    generateOfficialArtworkUrlFromId(id),
                    generateSpritePicUrlFromId(id)
                )
            }
        database.pokemonDao.insertBaseInfoList(pokemons.map { it.asDatabaseEntity() })
        database.pokemonDao.insertPokemonToGeneration(pokemons.map {
            PokemonToGeneration(
                it.id,
                generationId
            )
        })

    }

    override suspend fun loadPokemonByTypeFromDB(typeId: Long, scope: CoroutineScope) {
        liveData?.let {
            pokemonListMediatorLD.removeSource(it)
        }
        liveData = database.pokemonDao.getPokemonListByType(typeId)
        pokemonListMediatorLD.addSource(liveData!!) {
            pokemonListMediatorLD.value = it
        }
        scope.launch(Dispatchers.IO) {
            downloadPokemonByType(typeId)
        }
    }

    private suspend fun downloadPokemonByType(
        typeId: Long,
    ) {
        val pokemons = api.getPokemonRosterByType(typeId).results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.pokemon.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.pokemon.url)!!.value.toLong()
                PokemonEntity(
                    id,
                    it.pokemon.name,
                    generateOfficialArtworkUrlFromId(id),
                    generateSpritePicUrlFromId(id)
                )
            }
        database.pokemonDao.insertBaseInfoList(pokemons.map { it.asDatabaseEntity() })
        database.pokemonDao.insertPokemonToTypes(pokemons.map {
            PokemonTypeCrossRef(
                it.id,
                typeId
            )
        })

    }

    override suspend fun loadTypesList(scope: CoroutineScope): LiveData<List<DbType>> {
        scope.launch(Dispatchers.IO) {
            downloadTypeList()
        }
        return database.typeDao.getTypeList()
    }

    private suspend fun downloadTypeList() {
        val types = api.getAllTypes().results.map {
            val id = RETRIEVE_ID_REGEX.find(it.url)?.value?.toLong() ?: 0
            TypeEntity(id, it.name)
        }
        database.typeDao.insert(types.map { it.asDatabaseEntity() })
    }
}