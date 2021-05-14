package com.example.pokedex.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pokedex.databinding.FragmentPokemonDetailBinding
import com.example.pokedex.domain.PokemonDetailEntity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonDetailFragment: Fragment() {

    private val pokemonDetailViewModel: PokemonDetailViewModel by viewModel()

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
        Glide.with(binding.pokemonDetailImg.context)
            .load(pokemonDetail.officialArtworkUrl)
            .into(binding.pokemonDetailImg)
    }


    private fun showProgress(binding: FragmentPokemonDetailBinding) {
        binding.detailProgressBar.isVisible = true
        binding.detailViewGroup.isVisible = false
    }

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