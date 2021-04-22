package com.example.pokedex.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pokedex.databinding.FragmentPokemonDetailBinding
import com.example.pokedex.domain.PokemonDetailEntity

class PokemonDetailFragment: Fragment() {

    private var pokemonDetailViewModel: PokemonDetailViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val args = PokemonDetailFragmentArgs.fromBundle(requireArguments())
        pokemonDetailViewModel = PokemonDetailViewModel()
        val binding = FragmentPokemonDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        pokemonDetailViewModel = PokemonDetailViewModel()
        pokemonDetailViewModel!!.getPokemonDetail().observe(viewLifecycleOwner, { pokemonDetail: PokemonDetailEntity ->
            bind(binding, pokemonDetail)

        })
        pokemonDetailViewModel!!.loadDetail(args.pokemonId)
        return binding.root
    }

    private fun bind(
        binding: FragmentPokemonDetailBinding,
        pokemonDetail: PokemonDetailEntity
    ) {
        binding.pokemonDetailName.text = pokemonDetail.name
        binding.pokemonHeight.text = pokemonDetail.height.toString()
        binding.pokemonWeight.text = pokemonDetail.weight.toString()
        Glide.with(binding.pokemonDetailImg.context)
            .load(pokemonDetail.officialArtworkUrl)
            .into(binding.pokemonDetailImg)
    }

}