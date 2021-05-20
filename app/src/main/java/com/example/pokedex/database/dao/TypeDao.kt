package com.example.pokedex.database.dao

import androidx.room.*
import com.example.pokedex.database.entity.DbGeneration
import com.example.pokedex.database.entity.DbType

@Dao
interface TypeDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(types: List<DbType>)

    @Transaction
    @Query("SELECT * FROM dbtype")
    fun getTypeList(): List<DbType>

}