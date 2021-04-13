package com.example.pokedex.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.presentation.adapter.PokemonRosterAdapter

class MainActivity : AppCompatActivity() {

    val pokemonRosterViewModel = PokemonRosterViewModel()
    val adapter = PokemonRosterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.pokemonRoster)
        recyclerView.adapter = adapter

        pokemonRosterViewModel.getPokemonList().observe(this, Observer(){pokemonList ->
            adapter.data = pokemonList
            Log.d("","!!!!!!! $pokemonList")
        })
        pokemonRosterViewModel.loadData()
    }
}