package com.example.pokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokedex.database.dao.GenerationDao
import com.example.pokedex.database.dao.PokemonDao
import com.example.pokedex.database.dao.StatDao
import com.example.pokedex.database.dao.TypeDao
import com.example.pokedex.database.entity.*

@Database(entities = [DbPokemonDetail::class, DbStat::class, DbType::class, PokemonTypeCrossRef::class,
    DbPokemonBaseInfo::class, DbGeneration::class, PokemonToGeneration::class],
    version = 8)
abstract class PokedexDatabase: RoomDatabase() {
    abstract val pokemonDao: PokemonDao
    abstract val statDao: StatDao
    abstract val typeDao: TypeDao
    abstract val generationDao: GenerationDao
}

