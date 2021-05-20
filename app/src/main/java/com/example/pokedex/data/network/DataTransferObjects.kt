package com.example.pokedex.data.network

import com.example.pokedex.database.entity.DbPokemonDetail
import com.example.pokedex.database.entity.DbStat
import com.example.pokedex.database.entity.DbType
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
): DbPokemonDetail {
    return DbPokemonDetail(
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
fun PokemonStatsData.asDatabaseStat(pokemonId: Long): DbStat{
    return DbStat(parentPokemonId = pokemonId, name = stat.name, value = base_stat)
}
fun List<PokemonStatsData>.asDatabaseStat(pokemonId: Long): List<DbStat>{
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

fun PokemonTypesData.asDatabaseType(): DbType{
    return DbType(type.name)
}
fun List<PokemonTypesData>.asDatabaseType(): List<DbType>{
    return map { it.asDatabaseType() }
}
