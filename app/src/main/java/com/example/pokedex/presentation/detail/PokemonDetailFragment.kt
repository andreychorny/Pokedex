package com.example.pokedex.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonDetailBinding
import com.example.pokedex.domain.PokemonDetailEntity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.InternalCoroutinesApi

class PokemonDetailFragment: Fragment() {

    private val pokemonDetailViewModel: PokemonDetailViewModel by viewModel()

    @InternalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val args = PokemonDetailFragmentArgs.fromBundle(requireArguments())
        val binding = FragmentPokemonDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        pokemonDetailViewModel.viewState().observe(viewLifecycleOwner, {state ->
            when (state) {
                is PokemonDetailViewState.Loading -> {
                    showProgress(binding)
                }
                is PokemonDetailViewState.Data -> {
                    showData(binding, state.detail)
                }
                is PokemonDetailViewState.Error -> {
                    showError(binding, args.pokemonId, state.message)
                }
            }
        })

        pokemonDetailViewModel.loadDetail(args.pokemonId)
        return binding.root
    }

    private fun showData(
        binding: FragmentPokemonDetailBinding,
        pokemonDetail: PokemonDetailEntity
    ) {
        binding.detailProgressBar.isVisible = false
        binding.detailViewGroup.isVisible = true

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


    private fun showProgress(binding: FragmentPokemonDetailBinding) {
        binding.detailProgressBar.isVisible = true
        binding.detailViewGroup.isVisible = false
    }

    @InternalCoroutinesApi
    private fun showError(
        binding: FragmentPokemonDetailBinding,
        id: Long,
        errorMessage: String) {
        binding.detailProgressBar.isVisible = false
        binding.detailViewGroup.isVisible = false

        Snackbar.make(binding.detailCoordinator, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                pokemonDetailViewModel.loadDetail(id)
            }
            .show()
    }
}