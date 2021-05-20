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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class PokemonDetailViewModel(private val repository: PokemonRepository): ViewModel() {

    private val viewStateLiveData = MutableLiveData<PokemonDetailViewState>()
    fun viewState(): LiveData<PokemonDetailViewState> = viewStateLiveData


    @InternalCoroutinesApi
    fun loadDetail(id: Long) {
        viewModelScope.launch {
            //TODO fix error of app shutdown if no pokemon in cache + no internet connection
            repository.getPokemonById(id).collect { detail ->
                viewStateLiveData.value = detail?.let { PokemonDetailViewState.Data(it) }

            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.downloadPokemonDetail(id)
            }
        }
    }

    fun updateLiked(pokemonDetail: PokemonDetailEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pokemonDetail.let {
                    val newIsLiked = it.isLiked.not()
                    repository.updatePokemonInDatabase(
                        it.asDatabaseEntity(newIsLiked)
                    )
                }
            }
        }
    }

}