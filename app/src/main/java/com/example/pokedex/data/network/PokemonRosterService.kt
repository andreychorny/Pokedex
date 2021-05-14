package com.example.pokedex.data.network

import retrofit2.http.GET
import retrofit2.http.Path


interface PokemonRosterService {

    @GET("pokemon")
    suspend fun getAllPokemonRoster(): PokemonListResponse

    @GET("generation")
    suspend fun getAllGenerations(): GenerationListResponse

    @GET("generation/{id}")
    suspend fun getPokemonRosterByGeneration(@Path("id") id: Long): GenerationOfPokemonsResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Long): PokemonDetailsResponse

    @GET("type")
    suspend fun getAllTypes(): TypeListResponse

    @GET("type/{id}")
    suspend fun getPokemonRosterByType(@Path("id") name: Long): TypeOfPokemonsResponse

}

enum class PokemonApiFilter {
    SHOW_GENERATION,
    SHOW_TYPE,
    SHOW_ALL
}

