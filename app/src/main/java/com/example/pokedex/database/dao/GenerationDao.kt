package com.example.pokedex.database.dao

import androidx.room.*
import com.example.pokedex.database.entity.DbGeneration
import com.example.pokedex.database.entity.FullDbGeneration

@Dao
interface GenerationDao {


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(generations: List<DbGeneration>)

}