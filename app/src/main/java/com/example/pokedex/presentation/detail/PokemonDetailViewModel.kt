package com.example.pokedex.presentation.detail

import androidx.lifecycle.*
import com.example.pokedex.domain.PokemonDetailEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.domain.asDatabaseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class PokemonDetailViewModel(private val repository: PokemonRepository): ViewModel() {

    private val viewStateLiveData = MutableLiveData<PokemonDetailViewState>()
    fun viewState(): LiveData<PokemonDetailViewState> = viewStateLiveData

    @InternalCoroutinesApi
    fun loadDetail(id: Long) {
        loadDetailFromDatabase(id)
        updatePokemonDetailFromNet(id)
    }

    private fun loadDetailFromDatabase(id: Long) {
        viewModelScope.launch {
            viewStateLiveData.value = PokemonDetailViewState.Loading
            repository.getPokemonById(id).collect { detail ->
                detail?.let {
                    viewStateLiveData.value = PokemonDetailViewState.Data(it)
                }
            }
        }
    }

    private fun updatePokemonDetailFromNet(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.downloadPokemonDetail(id)
                } catch (e: Exception) {
                    viewStateLiveData.postValue(PokemonDetailViewState.Error("Network error"))
                }
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