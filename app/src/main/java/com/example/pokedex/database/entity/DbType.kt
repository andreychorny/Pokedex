package com.example.pokedex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex.domain.TypeEntity

@Entity
data class DbType(
    @PrimaryKey
    val typeId: Long,
    val name: String
)
fun DbType.asDomainEntity(): TypeEntity {
    return TypeEntity(
        id = typeId,
        name = name
    )
}
