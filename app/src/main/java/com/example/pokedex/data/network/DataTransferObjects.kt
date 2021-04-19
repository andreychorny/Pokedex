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

data class PokemonDetailResponse(
    val id: String,
    val name: String,
    val abilities: List<PokemonAbilityData>
)

data class PokemonAbilityDetailsData(
    val name: String,
    val url: String
)

data class PokemonAbilityData(
    val ability: PokemonAbilityDetailsData,
    val is_hidden: Boolean,
    val slot: Int
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