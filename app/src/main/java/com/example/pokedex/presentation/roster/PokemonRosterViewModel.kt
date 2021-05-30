package com.example.pokedex.presentation.roster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.CacheablePokemonRepository
import com.example.pokedex.presentation.roster.adapter.*
import kotlinx.coroutines.launch
import java.lang.Exception

class PokemonRosterViewModel(private val repositoryCacheable: CacheablePokemonRepository) : ViewModel() {

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
                loadRosterFromNet()
            } catch (e: Exception) {
                loadFromDatabase()
            }
        }
    }

    private suspend fun loadFromDatabase() {
        val resultList = mutableListOf<RosterItem>()
        if (filter == PokemonApiFilter.SHOW_GENERATION) {
            val generationList = repositoryCacheable.getGenerationsList()
            resultList.add(GenerationListItem(generationList.map { it.id to it.name }.toMap(), currentGenerationId))
        }
        if (filter == PokemonApiFilter.SHOW_TYPE) {
            val typeList = repositoryCacheable.getTypesList()
            resultList.add(TypeListItem(typeList
                //backend contains empty dummy types with no pokemons. Those types are secluded
                //by having much higher id than normal (currently it's 10001 and 10002)
                //so here we need to remove them
                .filter { it.id < 1000 }
                .map { it.id to it.name }.toMap(),
            currentTypeId)
            )
        }
        val pokemons = repositoryCacheable.getPokemonList(filter, currentGenerationId, currentTypeId)
        if(pokemons.isNotEmpty()){
            resultList.addAll(pokemons.map { it.toItem() })
            viewStateLiveData.value = PokemonRosterViewState.Data(resultList)
        }else if(filter == PokemonApiFilter.SHOW_LIKED){
            resultList.add(EmptyStateItem)
            viewStateLiveData.value = PokemonRosterViewState.Data(resultList)
        }else{
            viewStateLiveData.value = PokemonRosterViewState.Error("Loading failed, no internet connection")
        }
    }

    private suspend fun loadRosterFromNet() {
        val resultList = mutableListOf<RosterItem>()
        when (filter) {
            PokemonApiFilter.SHOW_GENERATION -> {
                val generationList = repositoryCacheable.downloadGenerationList()
                resultList.add(GenerationListItem(generationList.map { it.id to it.name }.toMap(),
                currentGenerationId))
                val pokemons = repositoryCacheable.downloadPokemonByGeneration(currentGenerationId)
                resultList.addAll(pokemons.map { it.toItem() })
                viewStateLiveData.value = PokemonRosterViewState.Data(resultList)
            }
            PokemonApiFilter.SHOW_TYPE -> {
                val typeList = repositoryCacheable.downloadTypeList()
                resultList.add(TypeListItem(typeList
                    //backend contains empty dummy types with no pokemons. Those types are secluded
                    //by having much higher id than normal (currently it's 10001 and 10002)
                    //so here we need to remove them
                    .filter { it.id < 1000 }
                    .map { it.id to it.name }.toMap(),
                currentTypeId)
                )
                val pokemons = repositoryCacheable.downloadPokemonByType(currentTypeId)
                resultList.addAll(pokemons.map { it.toItem() })
                viewStateLiveData.value = PokemonRosterViewState.Data(resultList)
            }
            PokemonApiFilter.SHOW_ALL -> {
                val pokemons = repositoryCacheable.downloadAllPokemon()
                resultList.addAll(pokemons.map { it.toItem() })
                viewStateLiveData.value = PokemonRosterViewState.Data(resultList)
            }
            PokemonApiFilter.SHOW_LIKED -> {
                loadFromDatabase()
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

    private fun PokemonEntity.toItem(): PokemonItem = PokemonItem(id, name, artImgUrl, spriteImgUrl)
}