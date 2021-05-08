package com.example.pokedex.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.example.pokedex.database.entity.DatabaseStat
import com.example.pokedex.database.entity.DatabaseType

@Dao
interface TypeDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(types: List<DatabaseType>)

}