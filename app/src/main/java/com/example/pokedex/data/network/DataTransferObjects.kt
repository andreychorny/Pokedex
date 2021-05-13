package com.example.pokedex.data.network

import com.squareup.moshi.Json

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonInRoster>
)

data class PokemonInRoster(
    val name: String,
    val url: String
)

data class PokemonDetailsResponse(
    val id: String,
    val name: String,
    val weight: Int,
    val height: Int,
    val stats: List<PokemonStatsData>,
    val types: List<PokemonTypesData>
)


data class PokemonStatsData(
    val stat: StatData,
    val base_stat: Int
)
data class StatData(
    val name: String
)
data class GenerationOfPokemonsResponse(
    val id: String,
    val name: String,
    @Json(name = "pokemon_species") val results: List<PokemonInRoster>
)

data class GenerationListResponse(
    val count: Int,
    val results: List<GenerationInList>

)
data class GenerationInList(
    val name: String,
    val url: String
)

data class PokemonTypesData(
    val slot: Int,
    val type: TypesData
)
data class TypesData(
    val name: String
)

data class TypeOfPokemonsResponse(
    val id: String,
    val name: String,
    @Json(name = "pokemon") val results: List<PokemonInType>
)
data class PokemonInType(
    val pokemon: PokemonInRoster,
    val slot: Int
)
data class TypeListResponse(
    val count: Int,
    val results: List<TypeInList>

)
data class TypeInList(
    val name: String,
    val url: String
)
