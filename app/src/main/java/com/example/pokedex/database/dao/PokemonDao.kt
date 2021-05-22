package com.example.pokedex.database.dao

import androidx.room.*
import com.example.pokedex.database.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Transaction
    @Query("SELECT * FROM dbpokemonbaseinfo LIMIT 50")
    fun getPokemonList(): List<DbPokemonBaseInfo>

    @Transaction
    @Query("SELECT * FROM dbpokemonbaseinfo WHERE pokemonId IN" +
            " (SELECT p.pokemonId FROM dbgeneration g LEFT JOIN pokemontogeneration p" +
            " ON g.generationId = p.generationId WHERE g.generationId = :generationId)")
    fun getPokemonListByGeneration(generationId: Long): List<DbPokemonBaseInfo>

    @Transaction
    @Query("SELECT * FROM dbpokemonbaseinfo WHERE pokemonId IN (SELECT p.pokemonId " +
            "FROM dbtype t LEFT JOIN pokemontypecrossref p ON t.typeId = p.typeId WHERE t.typeId = :typeId)")
    fun getPokemonListByType(typeId: Long): List<DbPokemonBaseInfo>

    @Transaction
    @Query("SELECT isLiked FROM dbpokemondetail WHERE pokemonId = :pokemonId")
    fun isPokemonLiked(pokemonId: Long): Boolean?

    @Transaction
    @Query("SELECT * FROM dbpokemondetail WHERE pokemonId = :pokemonId")
    fun getPokemonDetail(pokemonId: Long): Flow<FullDbPokemonDetail?>


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail( pokemon: DbPokemonDetail)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonToGeneration(pokemonToGenerationList: List<PokemonToGeneration>)

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