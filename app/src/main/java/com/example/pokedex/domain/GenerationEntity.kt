package com.example.pokedex.domain

import com.example.pokedex.database.entity.DbGeneration

data class GenerationEntity(
    val id: Long,
    val name: String
)
fun GenerationEntity.asDatabaseEntity(isLiked: Boolean = false): DbGeneration {
    return DbGeneration(
        generationId = id,
        name = name
    )
}