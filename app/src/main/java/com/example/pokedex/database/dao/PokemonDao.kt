package com.example.pokedex.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pokedex.database.entity.DatabasePokemonDetail
import com.example.pokedex.database.entity.FullDatabasePokemonDetail

@Dao
interface PokemonDao {

    @Transaction
    @Query("SELECT * FROM databasepokemondetail")
    fun getPokemonList(): LiveData<List<FullDatabasePokemonDetail>>

    @Transaction
    @Query("SELECT isLiked FROM databasepokemondetail WHERE pokemonId = :pokemonId")
    fun isPokemonLiked(pokemonId: Long): Boolean?

    @Transaction
    @Query("SELECT * FROM databasepokemondetail WHERE pokemonId = :pokemonId")
    fun getPokemonDetail(pokemonId: Long): LiveData<FullDatabasePokemonDetail>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( pokemon: DatabasePokemonDetail)

    @Transaction
    @Update
    fun update(pokemon: DatabasePokemonDetail)
}