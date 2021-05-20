package com.example.pokedex

fun generateOfficialArtworkUrlFromId(id: Long): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

fun generateDreamWorldPicUrlFromId(id: Long): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/$id.svg"

val RETRIEVE_ID_REGEX = "(\\d+)(?!.*\\d)".toRegex()
