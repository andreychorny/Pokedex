package com.example.pokedex.database.dao

import androidx.room.*
import com.example.pokedex.database.entity.DbGeneration
import com.example.pokedex.database.entity.DbPokemonBaseInfo

@Dao
interface GenerationDao {

    @Transaction
    @Query("SELECT * FROM dbgeneration")
    fun getGenerationList(): List<DbGeneration>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(generations: List<DbGeneration>)

}