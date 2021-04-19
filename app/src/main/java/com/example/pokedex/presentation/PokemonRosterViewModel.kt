package com.example.pokedex.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.data.network.PokemonRosterApi
import com.example.pokedex.domain.GenerationEntity
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.presentation.adapter.*
import kotlinx.coroutines.launch

class PokemonRosterViewModel: ViewModel() {

    private val _pokemonList = MutableLiveData<List<RosterItem>>()
    private val repository: PokemonRepository = NetworkPokemonRepository(
        api = PokemonRosterApi.retrofitService
    )
    private var adapter: GenerationListAdapter
    //TODO: retrieve currentGenerationId minimal generation id from query of all generations
    private var currentGenerationId: Long = 1
    private var currentTypeId: Long = 1

    fun getPokemonList(): LiveData<List<RosterItem>> = _pokemonList

    init{
        loadData(PokemonApiFilter.SHOW_ALL)
        adapter = GenerationListAdapter(GenerationListAdapter.OnClickListener{
            currentGenerationId = it.id
            loadData(PokemonApiFilter.SHOW_GENERATION)
        })
    }

    private fun loadData(filter: PokemonApiFilter){
        viewModelScope.launch {
            val resultList = mutableListOf<RosterItem>()
            if(filter == PokemonApiFilter.SHOW_GENERATION) {
                resultList.add(GenerationListItem(adapter))
                val generationList = repository.getGenerationsList()
                adapter.data = generationList
            }
            val pokemons = repository.getPokemonList(filter, currentGenerationId, currentTypeId)
            resultList.addAll(pokemons.map { it.toItem() })
            _pokemonList.value = resultList
        }
    }

    fun updateFilter(filter: PokemonApiFilter) {
        loadData(filter)
    }

    private fun PokemonEntity.toItem(): PokemonItem = PokemonItem(id, name, frontImgUrl, generation)
}