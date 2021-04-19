package com.example.pokedex.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.presentation.adapter.PokemonRosterAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val pokemonRosterViewModel = PokemonRosterViewModel()
    private val adapter = PokemonRosterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.pokemonRoster.adapter = adapter

        pokemonRosterViewModel.getPokemonList().observe(this, {pokemonList ->
            adapter.data = pokemonList
        })
        pokemonRosterViewModel.loadData()
    }
}