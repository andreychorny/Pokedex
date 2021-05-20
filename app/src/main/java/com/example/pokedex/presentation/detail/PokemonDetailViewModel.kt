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
        viewModelScope.launch {
            viewStateLiveData.value = PokemonDetailViewState.Loading
            //TODO fix error of app shutdown if no pokemon in cache + no internet connection
            repository.getPokemonById(id).collect { detail ->
                detail?.let{
                    viewStateLiveData.value = PokemonDetailViewState.Data(it)
                }
            }
        }
        //updating value of pokemon detail from net
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.downloadPokemonDetail(id)
                }catch (e: Exception){
                    //TODO add exception handling
                    //can't set PokemonViewState error because of dispatcher io
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