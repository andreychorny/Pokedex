package com.example.pokedex.data

import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.generatePicUrlFromId

//class MockPokemonRepository : PokemonRepository {
//
//    val list = mutableListOf<PokemonEntity>(
//            PokemonEntity(1, "bulbasaur", generatePicUrlFromId(1), 1),
//            PokemonEntity(2, "ivizavr", generatePicUrlFromId(2), 1),
//            PokemonEntity(3, "venusaur", generatePicUrlFromId(3), 1),
//            PokemonEntity(4, "charmander", generatePicUrlFromId(4), 1),
//            PokemonEntity(5, "charmeleon", generatePicUrlFromId(5), 1),
//            PokemonEntity(6, "charizard", generatePicUrlFromId(6), 1),
//            PokemonEntity(158, "totodile", generatePicUrlFromId(158), 2),
//            PokemonEntity(159, "croconaw", generatePicUrlFromId(159), 2),
//            PokemonEntity(160, "feraligatr", generatePicUrlFromId(160), 2),
//            PokemonEntity(161, "sentret", generatePicUrlFromId(161), 2),
//            PokemonEntity(162, "furret", generatePicUrlFromId(162), 2),
//            PokemonEntity(163, "hoothoot", generatePicUrlFromId(163), 2),
//            PokemonEntity(251, "celebi", generatePicUrlFromId(251), 3),
//            PokemonEntity(252, "treecko", generatePicUrlFromId(252), 3),
//            PokemonEntity(253, "grovyle", generatePicUrlFromId(253), 3)
//
//            )
//
//    override suspend fun getPokemonList(): List<PokemonEntity> {
//        return list
//    }
//
//}