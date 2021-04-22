package com.example.pokedex.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.network.PokemonRosterApi
import com.example.pokedex.domain.PokemonDetailEntity
import com.example.pokedex.domain.PokemonRepository
import kotlinx.coroutines.launch

class PokemonDetailViewModel: ViewModel() {

    private val _pokemonDetail = MutableLiveData<PokemonDetailEntity>()

    fun getPokemonDetail(): LiveData<PokemonDetailEntity> = _pokemonDetail


    private val repository: PokemonRepository = NetworkPokemonRepository(
        api = PokemonRosterApi.retrofitService
    )

    fun loadDetail(id: Long){
        viewModelScope.launch {
            _pokemonDetail.value = repository.getPokemonById(id)
        }
    }

}