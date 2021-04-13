package com.example.pokedex.data

import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository

class PokemonRepositoryImpl : PokemonRepository {

    val list = mutableListOf<PokemonEntity>(
            PokemonEntity(1, "bulbasaur", generateUrlFromId(1), 1),
            PokemonEntity(2, "ivizavr", generateUrlFromId(2), 1),
            PokemonEntity(3, "venusaur", generateUrlFromId(3), 1),
            PokemonEntity(4, "charmander", generateUrlFromId(4), 1),
            PokemonEntity(5, "charmeleon", generateUrlFromId(5), 1),
            PokemonEntity(6, "charizard", generateUrlFromId(6), 1),
            PokemonEntity(158, "totodile", generateUrlFromId(158), 2),
            PokemonEntity(159, "croconaw", generateUrlFromId(159), 2),
            PokemonEntity(160, "feraligatr", generateUrlFromId(160), 2),
            PokemonEntity(161, "sentret", generateUrlFromId(161), 2),
            PokemonEntity(162, "furret", generateUrlFromId(162), 2),
            PokemonEntity(163, "hoothoot", generateUrlFromId(163), 2),
            PokemonEntity(251, "celebi", generateUrlFromId(251), 3),
            PokemonEntity(252, "treecko", generateUrlFromId(252), 3),
            PokemonEntity(253, "grovyle", generateUrlFromId(253), 3)

            )

    override fun getPokemonList(): List<PokemonEntity> {
        return list
    }

    override fun addPokemon(pokemonEntity: PokemonEntity): Boolean {
        return list.add(pokemonEntity)
    }

    private fun generateUrlFromId(id: Int): String =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}