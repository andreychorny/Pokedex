package com.example.pokedex.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.network.PokemonRosterApi
import com.example.pokedex.domain.PokemonRepository
import kotlinx.coroutines.launch

class PokemonDetailViewModel: ViewModel() {

    private val viewStateLiveData = MutableLiveData<PokemonDetailViewState>()
    fun viewState(): LiveData<PokemonDetailViewState> = viewStateLiveData


    private val repository: PokemonRepository = NetworkPokemonRepository(
        api = PokemonRosterApi.retrofitService
    )

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