package com.example.pokedex.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonDetailBinding
import com.example.pokedex.domain.PokemonDetailEntity

class PokemonDetailFragment: Fragment() {

    private val pokemonDetailViewModel: PokemonDetailViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, PokemonDetailViewModel.Factory(activity.application))
            .get(PokemonDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val args = PokemonDetailFragmentArgs.fromBundle(requireArguments())
        val binding = FragmentPokemonDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        pokemonDetailViewModel.loadDetail(args.pokemonId).observe(viewLifecycleOwner, { pokemonDetail: PokemonDetailEntity ->
            bind(binding, pokemonDetail)

        })

        return binding.root
    }

    private fun bind(
        binding: FragmentPokemonDetailBinding,
        pokemonDetail: PokemonDetailEntity
    ) {
        binding.pokemonDetailName.text = pokemonDetail.name
        binding.pokemonHeight.text = pokemonDetail.height.toString()
        binding.pokemonWeight.text = pokemonDetail.weight.toString()
        Glide.with(binding.pokemonImage.context)
            .load(pokemonDetail.officialArtworkUrl)
            .into(binding.pokemonImage)
        updateLikeImg(binding, pokemonDetail.isLiked)
        binding.likeImage.setOnClickListener {
            pokemonDetailViewModel.updateLiked()
            updateLikeImg(binding, pokemonDetail.isLiked.not())
        }
        Log.e("!!!!", pokemonDetail.types.size.toString())
    }

    private fun updateLikeImg(binding: FragmentPokemonDetailBinding, isLiked: Boolean){
        when(isLiked){
            false -> binding.likeImage.setImageResource(R.drawable.heart_outline)
            true -> binding.likeImage.setImageResource(R.drawable.heart)
        }
    }

}