package com.example.pokedex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokedex.database.dao.PokemonDao
import com.example.pokedex.database.dao.StatDao
import com.example.pokedex.database.dao.TypeDao
import com.example.pokedex.database.entity.DatabasePokemonDetail
import com.example.pokedex.database.entity.DatabaseStat
import com.example.pokedex.database.entity.DatabaseType
import com.example.pokedex.database.entity.PokemonTypeCrossRef

@Database(entities = [DatabasePokemonDetail::class, DatabaseStat::class,
    DatabaseType::class, PokemonTypeCrossRef::class], version = 1)
abstract class PokedexDatabase: RoomDatabase() {
    abstract val pokemonDao: PokemonDao
    abstract val statDao: StatDao
    abstract val typeDao: TypeDao

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

