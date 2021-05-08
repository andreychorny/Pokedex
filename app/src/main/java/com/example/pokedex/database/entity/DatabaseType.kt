package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseType (
    @PrimaryKey
    val name: String
)