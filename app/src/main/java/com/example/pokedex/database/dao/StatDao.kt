package com.example.pokedex.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.example.pokedex.database.entity.DatabaseStat

@Dao
interface StatDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stats: List<DatabaseStat>)

}