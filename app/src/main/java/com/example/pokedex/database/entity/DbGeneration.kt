package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex.domain.GenerationEntity

@Entity
data class DbGeneration (
    @PrimaryKey
    val generationId: Long,
    val name: String
)

fun DbGeneration.asDomainEntity(): GenerationEntity {
    return GenerationEntity(
        id = generationId,
        name = name
    )
}
