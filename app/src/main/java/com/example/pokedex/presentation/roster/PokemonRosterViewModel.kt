package com.example.pokedex.presentation.roster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.data.network.PokemonRosterApi
import com.example.pokedex.database.PokedexDatabase
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.presentation.adapter.*
import com.example.pokedex.presentation.detail.PokemonDetailViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class PokemonRosterViewModel (private val repository: PokemonRepository) : ViewModel() {

    private val viewStateLiveData = MutableLiveData<PokemonRosterViewState>()
    fun viewState(): LiveData<PokemonRosterViewState> = viewStateLiveData

    //TODO: retrieve currentGenerationId as minimal generation id from query of all generations
    private var currentGenerationId: Long = 1
    private var currentTypeId: Long = 1
    private var filter: PokemonApiFilter

    init {
        filter = PokemonApiFilter.SHOW_ALL
        loadData()
    }

    fun loadData() {

        viewStateLiveData.value = PokemonRosterViewState.Loading

        viewModelScope.launch {
            try {
                val resultList = mutableListOf<RosterItem>()
                if (filter == PokemonApiFilter.SHOW_GENERATION) {
                    val generationList = repository.getGenerationsList()
                    resultList.add(GenerationListItem(generationList.map { it.id }))
                }
                if (filter == PokemonApiFilter.SHOW_TYPE) {
                    val typeList = repository.getTypesList()
                    resultList.add(TypeListItem(typeList
                        //backend contains empty dummy types with no pokemons. Those types are secluded
                        //by having much higher id than normal (currently it's 10001 and 10002)
                        //so here we need to remove them
                        .filter { it.id < 1000 }
                        .map { it.id to it.name }.toMap()
                    )
                    )
                }
                val pokemons = repository.getPokemonList(filter, currentGenerationId, currentTypeId)
                resultList.addAll(pokemons.map { it.toItem() })
                viewStateLiveData.value = PokemonRosterViewState.Data(resultList)
            } catch (e: Exception) {
                viewStateLiveData.value = PokemonRosterViewState.Error("Loading failed, no internet connection")
            }
        }
    }

    fun updateGenerationId(id: Long) {
        currentGenerationId = id
        filter = PokemonApiFilter.SHOW_GENERATION
        loadData()
    }


    fun updateTypeId(id: Long) {
        currentTypeId = id
        filter = PokemonApiFilter.SHOW_TYPE
        loadData()
    }

    fun updateFilter(filter: PokemonApiFilter) {
        currentGenerationId = 1
        currentTypeId = 1
        this.filter = filter
        loadData()
    }

    private fun PokemonEntity.toItem(): PokemonItem = PokemonItem(id, name, frontImgUrl)
}