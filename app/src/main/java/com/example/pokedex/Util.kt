package com.example.pokedex

fun generatePicUrlFromId(id: Long): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
