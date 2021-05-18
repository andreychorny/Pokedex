package com.example.pokedex.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pokedex.database.entity.*

@Dao
interface PokemonDao {

    @Transaction
    @Query("SELECT * FROM dbpokemonbaseinfo")
    fun getPokemonList(): List<DbPokemonBaseInfo>

    @Transaction
    @Query("SELECT isLiked FROM dbpokemondetail WHERE pokemonId = :pokemonId")
    fun isPokemonLiked(pokemonId: Long): Boolean?

    @Transaction
    @Query("SELECT * FROM dbpokemondetail WHERE pokemonId = :pokemonId")
    fun getPokemonDetail(pokemonId: Long): LiveData<FullDbPokemonDetail>


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail( pokemon: DbPokemonDetail)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonToTypes( pokemonToTypeList: List<PokemonTypeCrossRef>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBaseInfoList( pokemons: List<DbPokemonBaseInfo>)

    @Transaction
    @Update
    fun update(pokemon: DbPokemonDetail)
}