package com.example.pokedex.data.di

import android.app.Application
import androidx.room.Room
import com.example.pokedex.data.network.PokemonService
import com.example.pokedex.data.repository.PokemonDetailsRepositoryImpl
import com.example.pokedex.data.repository.PokemonRosterRepositoryImpl
import com.example.pokedex.database.PokedexDatabase
import com.example.pokedex.domain.repository.PokemonDetailsRepository
import com.example.pokedex.domain.repository.PokemonRosterRepository
import com.example.pokedex.presentation.detail.PokemonDetailViewModel
import com.example.pokedex.presentation.roster.PokemonRosterViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL =
    "https://pokeapi.co/api/v2/"


val appModule = module {
    single<PokemonService> { createPokedexApiService() }
    single<PokemonRosterRepository> { PokemonRosterRepositoryImpl(get(),get()) }
    single<PokemonDetailsRepository> { PokemonDetailsRepositoryImpl(get(),get()) }
    single{ provideDatabase(androidApplication())}
    viewModel { PokemonRosterViewModel(get()) }
    viewModel { PokemonDetailViewModel(get()) }
}

private fun provideDatabase(application: Application): PokedexDatabase {
    return Room.databaseBuilder(
        application,
        PokedexDatabase::class.java,
        "pokedex_database")
        .fallbackToDestructiveMigration()
        .build()
}

private fun createPokedexApiService(): PokemonService {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()
    return retrofit.create(PokemonService::class.java)
}
