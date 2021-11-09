package com.example.pokedex.presentation.detail

import androidx.lifecycle.*
import com.example.pokedex.database.entity.asDomainEntity
import com.example.pokedex.domain.PokemonDetailEntity
import com.example.pokedex.domain.asDatabaseEntity
import com.example.pokedex.domain.repository.PokemonDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class PokemonDetailViewModel(private val detailsRepository: PokemonDetailsRepository): ViewModel() {

    private val viewStateLiveData = MutableLiveData<PokemonDetailViewState>()
    fun viewState(): LiveData<PokemonDetailViewState> = viewStateLiveData

    private lateinit var detailLiveData: LiveData<PokemonDetailEntity>
    fun detailLiveData(): LiveData<PokemonDetailEntity> = detailLiveData

    init {
        detailLiveData = Transformations.map(detailsRepository.getPokemonDetailsLiveData())
        {it?.asDomainEntity()}
    }

    @InternalCoroutinesApi
    fun loadDetail(id: Long) {
        viewModelScope.launch {
            detailsRepository.loadPokemonById(id, viewModelScope)
        }
    }


    fun updateLiked(pokemonDetail: PokemonDetailEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pokemonDetail.let {
                    val newIsLiked = it.isLiked.not()
                    detailsRepository.updatePokemonInDatabase(
                        it.asDatabaseEntity(newIsLiked)
                    )
                }
            }
        }
    }

}