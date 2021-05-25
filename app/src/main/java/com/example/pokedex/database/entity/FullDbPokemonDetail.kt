package com.example.pokedex.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.pokedex.domain.PokemonDetailEntity

data class FullDbPokemonDetail(
    @Embedded val pokemonDetail: DbPokemonDetail,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "parentPokemonId"
    )
    val stats: List<DbStat>,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "typeId",
        associateBy = Junction(PokemonTypeCrossRef::class)
    )
    val types: List<DbType>
)

fun FullDbPokemonDetail.asDomainEntity(): PokemonDetailEntity {
    return PokemonDetailEntity(
        id = pokemonDetail.pokemonId,
        name = pokemonDetail.name,
        weight = pokemonDetail.weight,
        height = pokemonDetail.height,
        stats = stats.map { it.name to it.value }.toMap(),
        types = types.map { it.name },
        officialArtworkUrl = pokemonDetail.officialArtworkUrl,
        spriteUrlPic = pokemonDetail.spriteUrlPic,
        isLiked = pokemonDetail.isLiked
    )
}
