package com.example.pokedex.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.PokemonRepository
import kotlinx.coroutines.launch

class PokemonDetailViewModel(private val repository: PokemonRepository): ViewModel() {

    private val viewStateLiveData = MutableLiveData<PokemonDetailViewState>()
    fun viewState(): LiveData<PokemonDetailViewState> = viewStateLiveData


    fun loadDetail(id: Long){
        viewStateLiveData.value = PokemonDetailViewState.Loading
        viewModelScope.launch {
            try {
                viewStateLiveData.value = PokemonDetailViewState.Data(repository.getPokemonById(id))
            }catch (e: Exception){
                viewStateLiveData.value = PokemonDetailViewState.Error("Loading failed, no internet connection")
            }
        }
    }

}