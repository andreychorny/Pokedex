package com.example.pokedex.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.pokedex.domain.PokemonDetailEntity

data class FullDatabasePokemonDetail(
    @Embedded val pokemonDetail: DatabasePokemonDetail,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "parentPokemonId"
    )
    val stats: List<DatabaseStat>,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "name",
        associateBy = Junction(PokemonTypeCrossRef::class)
    )
    val types: List<DatabaseType>
)

fun FullDatabasePokemonDetail.asDomainEntity(): PokemonDetailEntity {
    return PokemonDetailEntity(
        id = pokemonDetail.pokemonId,
        name = pokemonDetail.name,
        weight = pokemonDetail.weight,
        height = pokemonDetail.height,
        stats = stats.map { it.name to it.value }.toMap(),
        types = types.map { it.name },
        officialArtworkUrl = pokemonDetail.officialArtworkUrl,
        dreamWorldUrlPic = pokemonDetail.dreamWorldUrlPic,
        isLiked = pokemonDetail.isLiked
    )
}
