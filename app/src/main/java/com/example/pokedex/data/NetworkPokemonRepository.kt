package com.example.pokedex.data

import com.example.pokedex.data.network.PokemonRosterService
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.generatePicUrlFromId

private val  RETRIEVE_ID_REGEX = "(\\d+)(?!.*\\d)".toRegex()

class NetworkPokemonRepository(val api: PokemonRosterService): PokemonRepository {

    override suspend fun getPokemonList(): List<PokemonEntity> {
        return api.getPokemonRoster().results
            .filter { RETRIEVE_ID_REGEX.containsMatchIn(it.url) }
            .map {
                val id = RETRIEVE_ID_REGEX.find(it.url)!!.value.toLong()
                PokemonEntity(id, it.name, generatePicUrlFromId(id), 1) }
    }


}