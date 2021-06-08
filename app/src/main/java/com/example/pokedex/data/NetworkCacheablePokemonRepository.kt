package com.example.pokedex.data

import com.example.pokedex.RETRIEVE_ID_REGEX
import com.example.pokedex.data.network.*
import com.example.pokedex.database.PokedexDatabase
import com.example.pokedex.database.entity.DbPokemonDetail
import com.example.pokedex.database.entity.PokemonToGeneration
import com.example.pokedex.database.entity.PokemonTypeCrossRef
import com.example.pokedex.database.entity.asDomainEntity
import com.example.pokedex.domain.*
import com.example.pokedex.data.network.PokemonRosterService
import com.example.pokedex.generateOfficialArtworkUrlFromId
import com.example.pokedex.generateSpritePicUrlFromId
import com.example.pokedex.presentation.roster.PokemonApiFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class NetworkCacheablePokemonRepository(
    private val api: PokemonRosterService,
    private val database: PokedexDatabase
) : CacheablePokemonRepository {

    override suspend fun getPokemonList(filter: PokemonApiFilter): List<PokemonEntity> {
        var pokemons: List<PokemonEntity>
        withContext(Dispatchers.IO) {
            pokemons = when (filter) {
                is PokemonApiFilter.SHOW_ALL -> retrieveAllPokemon()
                is PokemonApiFilter.SHOW_GENERATION -> retrievePokemonByGeneration(filter.id)
                is PokemonApiFilter.SHOW_TYPE -> retrievePokemonByType(filter.id)
                is PokemonApiFilter.SHOW_LIKED -> retrieveLikedPokemon()
            }
        }
        return pokemons
    }

    override suspend fun getGenerationsList(): List<GenerationEntity> {
        var generations = listOf<GenerationEntity>()
        withContext(Dispatchers.IO) {
            generations = database.generationDao.getGenerationList().map { it.asDomainEntity() }
        }
        return generations
    }

    override suspend fun downloadGenerationList(scope: CoroutineScope): List<GenerationEntity> {
        val generations = api.getAllGenerations().results.map {
            val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
            GenerationEntity(id, it.name)
        }
        scope.launch(Dispatchers.IO) {
            database.generationDao.insert(generations.map { it.asDatabaseEntity() })
        }
        return generations
    }

    override suspend fun downloadPokemonList(
        filter: PokemonApiFilter,
        scope: CoroutineScope
    ): List<PokemonEntity> {
        var pokemons: List<PokemonEntity> = when (filter) {
            is PokemonApiFilter.SHOW_ALL -> downloadAllPokemon(scope)
            is PokemonApiFilter.SHOW_GENERATION -> downloadPokemonByGeneration(filter.id, scope)
            is PokemonApiFilter.SHOW_TYPE -> downloadPokemonByType(filter.id, scope)
            else -> throw IllegalArgumentException("No SHOW_LIKED type for downloading from net is allowed")
        }

        return pokemons
    }

    override suspend fun getPokemonById(id: Long): Flow<PokemonDetailEntity?> {
        return database.pokemonDao.getPokemonDetail(id).map { it?.asDomainEntity() }
    }

    override suspend fun downloadPokemonDetail(
        id: Long,
        scope: CoroutineScope
    ): PokemonDetailEntity {

        val oldIsLiked = database.pokemonDao.isPokemonLiked(id)
        val jsonPokemon = api.getPokemonDetails(id)
        val stats = jsonPokemon.stats.asDatabaseStat(jsonPokemon.id.toLong())
        val types = jsonPokemon.types.asDatabaseType()
        //if pokemon is in database - we need to enter old "IsLiked" state
        val newPokemonDetail = jsonPokemon.asDatabaseEntity(
            oldIsLiked ?: false
        )
        withContext(Dispatchers.IO) {
            database.statDao.insert(stats)
            database.typeDao.insert(types)
            val pokemonToTypeList = mutableListOf<PokemonTypeCrossRef>()
            for (type in types) {
                pokemonToTypeList.add(PokemonTypeCrossRef(id, type.typeId))
            }
            database.pokemonDao.insertPokemonToTypes(pokemonToTypeList)
            database.pokemonDao.insertDetail(newPokemonDetail)
        }
        return jsonPokemon.asDomainEntity()
    }

    override suspend fun updatePokemonInDatabase(dbPokemonDetail: DbPokemonDetail) {
        database.pokemonDao.update(dbPokemonDetail)
    }


    private fun retrieveAllPokemon(): List<PokemonEntity> {
        return database.pokemonDao.getPokemonList().map { it.asDomainEntity() }
    }

    private fun retrieveLikedPokemon(): List<PokemonEntity> {
        return database.pokemonDao.getLikedPokemonList().map { it.asDomainEntity() }
    }


    private suspend fun downloadAllPokemon(scope: CoroutineScope): List<PokemonEntity> {
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
        scope.launch(Dispatchers.IO) {
            database.pokemonDao.insertBaseInfoList(pokemons.map { it.asDatabaseEntity() })
        }
        return pokemons
    }

    private fun retrievePokemonByGeneration(generationId: Long): List<PokemonEntity> {
        return database.pokemonDao.getPokemonListByGeneration(generationId)
            .map { it.asDomainEntity() }
    }

    private suspend fun downloadPokemonByGeneration(
        generationId: Long,
        scope: CoroutineScope
    ): List<PokemonEntity> {
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
        scope.launch(Dispatchers.IO) {
            database.pokemonDao.insertBaseInfoList(pokemons.map { it.asDatabaseEntity() })
            database.pokemonDao.insertPokemonToGeneration(pokemons.map {
                PokemonToGeneration(
                    it.id,
                    generationId
                )
            })
        }
        return pokemons
    }

    private fun retrievePokemonByType(typeId: Long): List<PokemonEntity> {
        return database.pokemonDao.getPokemonListByType(typeId)
            .map { it.asDomainEntity() }
    }

    private suspend fun downloadPokemonByType(
        typeId: Long,
        scope: CoroutineScope
    ): List<PokemonEntity> {
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
        scope.launch(Dispatchers.IO) {
            database.pokemonDao.insertBaseInfoList(pokemons.map { it.asDatabaseEntity() })
            database.pokemonDao.insertPokemonToTypes(pokemons.map {
                PokemonTypeCrossRef(
                    it.id,
                    typeId
                )
            })
        }
        return pokemons
    }

    override suspend fun getTypesList(): List<TypeEntity> {
        var types = listOf<TypeEntity>()
        withContext(Dispatchers.IO) {
            types = database.typeDao.getTypeList().map { it.asDomainEntity() }
        }
        return types
    }

    override suspend fun downloadTypeList(scope: CoroutineScope): List<TypeEntity> {
        val types = api.getAllTypes().results.map {
            val id = RETRIEVE_ID_REGEX.find(it.url)?.value?.toLong() ?: 0
            TypeEntity(id, it.name)
        }
        scope.launch(Dispatchers.IO) {
            database.typeDao.insert(types.map { it.asDatabaseEntity() })
        }
        return types
    }
}