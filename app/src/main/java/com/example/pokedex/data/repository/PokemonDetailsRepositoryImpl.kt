package com.example.pokedex.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.pokedex.data.network.PokemonService
import com.example.pokedex.data.network.asDatabaseEntity
import com.example.pokedex.data.network.asDatabaseStat
import com.example.pokedex.data.network.asDatabaseType
import com.example.pokedex.database.PokedexDatabase
import com.example.pokedex.database.entity.DbPokemonDetail
import com.example.pokedex.database.entity.FullDbPokemonDetail
import com.example.pokedex.database.entity.PokemonTypeCrossRef
import com.example.pokedex.domain.repository.PokemonDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonDetailsRepositoryImpl(
    private val api: PokemonService,
    private val database: PokedexDatabase,
) : PokemonDetailsRepository {

    private val detailsLiveData: MediatorLiveData<FullDbPokemonDetail?> =
        MediatorLiveData()

    override fun getPokemonDetailsLiveData(): LiveData<FullDbPokemonDetail?> = detailsLiveData

    private var liveData: LiveData<FullDbPokemonDetail?>? = null

    override suspend fun loadPokemonById(id: Long, scope: CoroutineScope) {

        liveData?.let {
            detailsLiveData.removeSource(it)
        }
        liveData = database.pokemonDao.getPokemonDetail(id)
        detailsLiveData.addSource(liveData!!) {
            detailsLiveData.value = it
        }
        scope.launch(Dispatchers.IO) {
            downloadPokemonDetail(id)
        }
    }

    private suspend fun downloadPokemonDetail(
        id: Long,
    ) {
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
        for (type in types) {
            pokemonToTypeList.add(PokemonTypeCrossRef(id, type.typeId))
        }
        database.pokemonDao.insertPokemonToTypes(pokemonToTypeList)
        database.pokemonDao.insertDetail(newPokemonDetail)

    }

    override suspend fun updatePokemonInDatabase(dbPokemonDetail: DbPokemonDetail) {
        database.pokemonDao.update(dbPokemonDetail)
    }

}