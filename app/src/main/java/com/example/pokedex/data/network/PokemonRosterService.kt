package com.example.pokedex.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL =
    "https://pokeapi.co/api/v2/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface PokemonRosterService {

    @GET("pokemon")
    suspend fun getAllPokemonRoster(): PokemonListResponse

    @GET("generation")
    suspend fun getAllGenerations(): GenerationListResponse

    @GET("generation/{id}")
    suspend fun getPokemonRosterByGeneration(@Path("id") id: Long): GenerationOfPokemonsResponse

    //TODO
//    @GET("type/{id}")
//    suspend fun getPokemonRosterByType(@Path("id") name: Long): PokemonListResponse

}

enum class PokemonApiFilter {
    SHOW_GENERATION,
    SHOW_TYPE,
    SHOW_ALL
}

object PokemonRosterApi {
    val retrofitService : PokemonRosterService by lazy {
        retrofit.create(PokemonRosterService::class.java) }
}
