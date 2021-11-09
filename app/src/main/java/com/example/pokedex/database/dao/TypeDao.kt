package com.example.pokedex.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pokedex.database.entity.DbGeneration
import com.example.pokedex.database.entity.DbType
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(types: List<DbType>)

    @Transaction
    @Query("SELECT * FROM dbtype")
    fun getTypeList(): LiveData<List<DbType>>

}