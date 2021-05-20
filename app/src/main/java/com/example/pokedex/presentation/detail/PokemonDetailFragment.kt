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
import kotlinx.coroutines.InternalCoroutinesApi

class PokemonDetailFragment: Fragment() {

    private val pokemonDetailViewModel: PokemonDetailViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, PokemonDetailViewModel.Factory(activity.application))
            .get(PokemonDetailViewModel::class.java)
    }

    @InternalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val args = PokemonDetailFragmentArgs.fromBundle(requireArguments())
        val binding = FragmentPokemonDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        pokemonDetailViewModel.viewState().observe(viewLifecycleOwner, {state ->
            when (state) {
                is PokemonDetailViewState.Loading -> {
                }
                is PokemonDetailViewState.Data -> {
                    bind(binding, state.detail)
                }
                is PokemonDetailViewState.Error -> {
                }
            }
        })

        pokemonDetailViewModel.loadDetail(args.pokemonId)
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
            pokemonDetailViewModel.updateLiked(pokemonDetail)
            updateLikeImg(binding, pokemonDetail.isLiked.not())
        }
    }

    private fun updateLikeImg(binding: FragmentPokemonDetailBinding, isLiked: Boolean){
        when(isLiked){
            false -> binding.likeImage.setImageResource(R.drawable.heart_outline)
            true -> binding.likeImage.setImageResource(R.drawable.heart)
        }
    }

}