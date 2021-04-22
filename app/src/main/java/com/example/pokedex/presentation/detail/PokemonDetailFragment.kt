package com.example.pokedex.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pokedex.databinding.FragmentPokemonDetailBinding
import com.example.pokedex.domain.PokemonDetailEntity

class PokemonDetailFragment: Fragment() {

    private var pokemonDetailViewModel: PokemonDetailViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        pokemonDetailViewModel = PokemonDetailViewModel()
        val binding = FragmentPokemonDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val id = arguments?.getLong("id")
        pokemonDetailViewModel = PokemonDetailViewModel()
        pokemonDetailViewModel!!.getPokemonDetail().observe(viewLifecycleOwner, { pokemonDetail: PokemonDetailEntity ->
            binding.pokemonDetailName.text = pokemonDetail.name
            binding.pokemonHeight.text = pokemonDetail.height.toString()
            binding.pokemonWeight.text = pokemonDetail.weight.toString()
            Glide.with(binding.pokemonDetailImg.context)
                .load(pokemonDetail.officialArtworkUrl)
                .into(binding.pokemonDetailImg)

        })
        pokemonDetailViewModel!!.loadDetail(id!!)
        return binding.root
    }

}