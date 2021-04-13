package com.example.pokedex.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.data.PokemonRepositoryImpl
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository

class PokemonRosterViewModel {

    private val _pokemonList = MutableLiveData<List<PokemonEntity>>()
    private val repository: PokemonRepository = PokemonRepositoryImpl()

    fun getPokemonList(): LiveData<List<PokemonEntity>> = _pokemonList

    fun loadData(){
        _pokemonList.value = repository.getPokemonList()
    }
}