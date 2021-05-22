package com.example.pokedex.domain

import com.example.pokedex.database.entity.DbType

data class TypeEntity (
    val id: Long,
    val name: String
)
fun TypeEntity.asDatabaseEntity(): DbType {
    return DbType(
        typeId = id,
        name = name
    )
}