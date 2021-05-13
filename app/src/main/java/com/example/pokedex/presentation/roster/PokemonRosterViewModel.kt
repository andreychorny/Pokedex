package com.example.pokedex.presentation.roster

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.data.network.PokemonRosterApi
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.presentation.adapter.*
import kotlinx.coroutines.launch

class PokemonRosterViewModel: ViewModel() {

    private val _pokemonList = MutableLiveData<List<RosterItem>>()
    private val repository: PokemonRepository = NetworkPokemonRepository(
        api = PokemonRosterApi.retrofitService
    )
    //TODO: retrieve currentGenerationId as minimal generation id from query of all generations
    private var currentGenerationId: Long = 1
    private var currentTypeId: Long = 1


    fun getPokemonList(): LiveData<List<RosterItem>> = _pokemonList

    init{
        loadData(PokemonApiFilter.SHOW_ALL)
    }

    private fun loadData(filter: PokemonApiFilter){
        viewModelScope.launch {
            val resultList = mutableListOf<RosterItem>()
            if(filter == PokemonApiFilter.SHOW_GENERATION) {
                val generationList = repository.getGenerationsList()
                resultList.add(GenerationListItem(generationList.map { it.id }))
            }
            if(filter == PokemonApiFilter.SHOW_TYPE) {
                val typeList = repository.getTypesList()
                resultList.add(TypeListItem(typeList
                    //backend contains empty dummy types with no pokemons. Those types are secluded
                    //by having much higher id than normal (currently it's 10001 and 10002)
                    //so here we need to remove them
                    .filter { it.id < 1000 }
                    .map { it.id to it.name }.toMap()))
            }
            val pokemons = repository.getPokemonList(filter, currentGenerationId, currentTypeId)
            resultList.addAll(pokemons.map { it.toItem() })
            _pokemonList.value = resultList
        }
    }

    fun updateGenerationId(id: Long){
        currentGenerationId = id
        loadData(PokemonApiFilter.SHOW_GENERATION)
    }


    fun updateTypeId(id: Long){
        currentTypeId = id
        loadData(PokemonApiFilter.SHOW_TYPE)
    }

    fun updateFilter(filter: PokemonApiFilter) {
        currentGenerationId = 1
        currentTypeId = 1
        loadData(filter)
    }

    private fun PokemonEntity.toItem(): PokemonItem = PokemonItem(id, name, frontImgUrl)
}