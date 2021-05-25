package com.example.pokedex

fun generateOfficialArtworkUrlFromId(id: Long): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

fun generateSpritePicUrlFromId(id: Long): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

val RETRIEVE_ID_REGEX = "(\\d+)(?!.*\\d)".toRegex()
