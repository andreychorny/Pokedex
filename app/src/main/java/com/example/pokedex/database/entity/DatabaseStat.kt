package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["parentPokemonId", "name"])
data class DatabaseStat (
    val parentPokemonId: Long,
    val name: String,
    val value: Int
        )