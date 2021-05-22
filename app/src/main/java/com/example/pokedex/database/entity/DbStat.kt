package com.example.pokedex.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["parentPokemonId", "name"])
data class DbStat (
    val parentPokemonId: Long,
    val name: String,
    val value: Int
        )