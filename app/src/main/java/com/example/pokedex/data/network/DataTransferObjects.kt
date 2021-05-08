package com.example.pokedex.data.network

import com.example.pokedex.database.entity.DatabasePokemonDetail
import com.example.pokedex.database.entity.DatabaseStat
import com.example.pokedex.database.entity.DatabaseType
import com.example.pokedex.domain.PokemonDetailEntity
import com.example.pokedex.generateDreamWorldPicUrlFromId
import com.example.pokedex.generateOfficialArtworkUrlFromId
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

fun PokemonDetailsResponse.asDomainEntity(): PokemonDetailEntity {
    return PokemonDetailEntity(
        id = id.toLong(),
        name = name,
        weight = weight,
        height = height,
        stats = stats.map { it.stat.name to it.base_stat }.toMap(),
        types = types.map { it.type.name },
        officialArtworkUrl = generateOfficialArtworkUrlFromId(id.toLong()),
        dreamWorldUrlPic = generateDreamWorldPicUrlFromId(id.toLong()),
        isLiked = false
    )
}

fun PokemonDetailsResponse.asDatabaseEntity(
    isLiked: Boolean = false
): DatabasePokemonDetail {
    return DatabasePokemonDetail(
        pokemonId = id.toLong(),
        name = name,
        weight = weight,
        height = height,
        officialArtworkUrl = generateOfficialArtworkUrlFromId(id.toLong()),
        dreamWorldUrlPic = generateDreamWorldPicUrlFromId(id.toLong()),
        isLiked = isLiked
    )
}

data class PokemonStatsData(
    val stat: StatData,
    val base_stat: Int
)
fun PokemonStatsData.asDatabaseStat(pokemonId: Long): DatabaseStat{
    return DatabaseStat(parentPokemonId = pokemonId, name = stat.name, value = base_stat)
}
fun List<PokemonStatsData>.asDatabaseStat(pokemonId: Long): List<DatabaseStat>{
    return map { it.asDatabaseStat(pokemonId) }
}

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
fun PokemonTypesData.asDatabaseType(): DatabaseType{
    return DatabaseType(type.name)
}
fun List<PokemonTypesData>.asDatabaseType(): List<DatabaseType>{
    return map { it.asDatabaseType() }
}
