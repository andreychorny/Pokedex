package com.example.pokedex.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    suspend fun getAllPokemonRoster(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

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
