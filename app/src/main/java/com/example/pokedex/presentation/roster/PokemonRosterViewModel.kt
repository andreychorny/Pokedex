package com.example.pokedex.presentation.roster

import androidx.lifecycle.*
import com.example.pokedex.database.entity.asDomainEntity
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.repository.PokemonRosterRepository
import com.example.pokedex.presentation.roster.adapter.*
import kotlinx.coroutines.launch
import java.lang.Exception

class PokemonRosterViewModel(private val repository: PokemonRosterRepository) : ViewModel() {

    private lateinit var rosterLiveData: LiveData<List<PokemonEntity>>
    fun rosterLiveData(): LiveData<List<PokemonEntity>> = rosterLiveData

    private lateinit var generationLiveData: LiveData<GenerationListItem>
    fun generationLiveData(): LiveData<GenerationListItem> = generationLiveData

    private lateinit var typeLiveData: LiveData<TypeListItem>
    fun typeLiveData(): LiveData<TypeListItem> = typeLiveData

    private var currentGenerationId: Long = 1
    private var currentTypeId: Long = 1
    var filter: PokemonApiFilter

    init {
        filter = PokemonApiFilter.SHOW_ALL
        rosterLiveData =
            Transformations.map(
                repository.getPokemonListMediatorLD()
            ) { list -> list.map { it.asDomainEntity() } }
        loadData()
        viewModelScope.launch {
            retrieveGenerationsFromDB()
            retrieveTypesFromDB()
        }
    }

    private suspend fun retrieveTypesFromDB() {
        typeLiveData = Transformations.map(
            repository.loadTypesList(viewModelScope)
        ) { list ->
            TypeListItem(list
                //backend contains empty dummy types with no pokemons. Those types are secluded
                //by having much higher id (currently it's 10001 and 10002)
                //so here we need to remove them
                .filter { it.typeId < 1000 }
                .map { it.typeId to it.name }.toMap(),
                currentTypeId)
        }
    }

    private suspend fun retrieveGenerationsFromDB() {
        generationLiveData = Transformations.map(
            repository.loadGenerationsList(viewModelScope)
        ) { list ->
            GenerationListItem(
                list.map { it.generationId to it.name }.toMap(),
                currentGenerationId
            )
        }
    }

    fun loadData() {
        viewModelScope.launch {
            loadFromDatabase()
        }
    }

    private suspend fun loadFromDatabase() {
        when (filter) {
            PokemonApiFilter.SHOW_GENERATION -> {
                repository.loadPokemonByGeneration(currentGenerationId, viewModelScope)
            }
            PokemonApiFilter.SHOW_TYPE -> {
                repository.loadPokemonByTypeFromDB(currentTypeId, viewModelScope)
            }
            PokemonApiFilter.SHOW_ALL -> {
                repository.loadAllPokemon(viewModelScope)
            }
            PokemonApiFilter.SHOW_LIKED -> {
                repository.loadLikedPokemon(viewModelScope)
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
}