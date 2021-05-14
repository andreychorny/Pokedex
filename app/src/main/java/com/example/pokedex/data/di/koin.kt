package com.example.pokedex.data.di

import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.network.PokemonRosterService
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.presentation.detail.PokemonDetailViewModel
import com.example.pokedex.presentation.roster.PokemonRosterViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL =
    "https://pokeapi.co/api/v2/"


val appModule = module {
    single<PokemonRosterService> { createPokedexApiService() }
    single<PokemonRepository> { NetworkPokemonRepository(get()) }

    viewModel { PokemonRosterViewModel(get()) }
    viewModel { PokemonDetailViewModel(get()) }
}

private fun createPokedexApiService(): PokemonRosterService {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()
    return retrofit.create(PokemonRosterService::class.java)
}
