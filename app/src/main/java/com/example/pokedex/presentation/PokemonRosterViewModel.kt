package com.example.pokedex.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.data.PokemonRepositoryImpl
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.presentation.adapter.GenerationItem
import com.example.pokedex.presentation.adapter.PokemonItem
import com.example.pokedex.presentation.adapter.RosterItem

class PokemonRosterViewModel {

    private val _pokemonList = MutableLiveData<List<RosterItem>>()
    private val repository: PokemonRepository = PokemonRepositoryImpl()

    fun getPokemonList(): LiveData<List<RosterItem>> = _pokemonList

    fun loadData(){
        val pokemons = repository.getPokemonList()
        val pokemonWithBiggestGeneration = pokemons.maxWith(Comparator{a,b -> a.generation - b.generation})
        val maxGeneration = pokemonWithBiggestGeneration?.generation
        val resultList = mutableListOf<RosterItem>()
        for(i in 1..maxGeneration!!){
            resultList.add(GenerationItem("Generation $i"))
            resultList.addAll(pokemons.filter { it.generation == i }.map { it.toItem() })
        }
        _pokemonList.value = resultList
    }

    private fun PokemonEntity.toItem(): PokemonItem = PokemonItem(id, name, frontImgUrl, generation)
}