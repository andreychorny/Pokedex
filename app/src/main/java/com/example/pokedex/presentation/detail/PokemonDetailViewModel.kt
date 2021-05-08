package com.example.pokedex.presentation.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.network.PokemonRosterApi
import com.example.pokedex.database.PokedexDatabase
import com.example.pokedex.domain.PokemonDetailEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.domain.asDatabaseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class PokemonDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _pokemonDetail = MediatorLiveData<PokemonDetailEntity>()

    private val repository: PokemonRepository = NetworkPokemonRepository(
        api = PokemonRosterApi.retrofitService,
        database = PokedexDatabase.getInstance(application)
    )

    fun loadDetail(id: Long): MediatorLiveData<PokemonDetailEntity> {
        Log.w("BEFORE", "BEFORE")
        viewModelScope.launch {
            //TODO fix error of app shutdown if no pokemon in cache + no internet connection
            _pokemonDetail.addSource(repository.getPokemonById(id), Observer {
                if (it != null) {
                    _pokemonDetail.value = it
                }
            })
            withContext(Dispatchers.IO) {
                launch {
                    try {
                        repository.insertPokemonDetail(id)
                    } catch (networkError: IOException) {
                        //TODO ErrorState indication
                    }
                }
            }
        }

        return _pokemonDetail
    }

    fun updateLiked() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _pokemonDetail.value?.let {
                    val newIsLiked = it.isLiked.not()
                    repository.updatePokemonInDatabase(
                        it.asDatabaseEntity(newIsLiked)
                    )
                }
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PokemonDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PokemonDetailViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}