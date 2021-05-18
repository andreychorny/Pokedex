package com.example.pokedex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokedex.database.dao.GenerationDao
import com.example.pokedex.database.dao.PokemonDao
import com.example.pokedex.database.dao.StatDao
import com.example.pokedex.database.dao.TypeDao
import com.example.pokedex.database.entity.*

@Database(entities = [DbPokemonDetail::class, DbStat::class,
    DbType::class, PokemonTypeCrossRef::class, DbPokemonBaseInfo::class, DbGeneration::class],
    version = 4)
abstract class PokedexDatabase: RoomDatabase() {
    abstract val pokemonDao: PokemonDao
    abstract val statDao: StatDao
    abstract val typeDao: TypeDao
    abstract val generationDao: GenerationDao
    companion object {
        @Volatile
        private var INSTANCE: PokedexDatabase? = null

        fun getInstance(context: Context): PokedexDatabase {
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PokedexDatabase::class.java,
                        "pokedex_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}

