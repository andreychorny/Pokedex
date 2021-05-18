package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbGeneration (
    @PrimaryKey
    val generationId: Long,
    val name: String
)