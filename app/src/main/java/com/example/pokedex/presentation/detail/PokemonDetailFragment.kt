package com.example.pokedex.presentation.detail

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonDetailBinding
import com.example.pokedex.domain.PokemonDetailEntity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.InternalCoroutinesApi

class PokemonDetailFragment : Fragment() {

    private val pokemonDetailViewModel: PokemonDetailViewModel by viewModel()
    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        //TODO FIX CHANGING BACKGROUND COLOR OF CARD
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.pokemonHostFragment
            duration = 500L
            scrimColor = Color.TRANSPARENT
        }
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val args = PokemonDetailFragmentArgs.fromBundle(requireArguments())
        _binding = FragmentPokemonDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        pokemonDetailViewModel.viewState().observe(viewLifecycleOwner, { state ->
            when (state) {
                is PokemonDetailViewState.Loading -> {
                }
                is PokemonDetailViewState.Data -> {
                    showData(state.detail)
                }
                is PokemonDetailViewState.Error -> {
                    showError(args.pokemonId, state.message)
                }
            }
        })

        pokemonDetailViewModel.loadDetail(args.pokemonId)
        return binding.root
    }

    private fun showData(
        pokemonDetail: PokemonDetailEntity
    ) {
        binding.detailProgressBar.isVisible = false
        binding.detailViewGroup.isVisible = true

        binding.pokemonDetailName.text = pokemonDetail.name
        binding.pokemonHeight.text = pokemonDetail.height.toString()
        binding.pokemonWeight.text = pokemonDetail.weight.toString()
        Glide.with(binding.pokemonImage.context)
            .asBitmap()
            .load(pokemonDetail.officialArtworkUrl)
            .error(
                Glide
                    .with(binding.pokemonImage.context)
                    .asBitmap()
                    .load(pokemonDetail.spriteUrlPic)
                    .listener(setBackgroundColor())
            )
            .listener(setBackgroundColor())
            .into(binding.pokemonImage)

        updateLikeImg(pokemonDetail.isLiked)
        binding.likeImage.setOnClickListener {
            pokemonDetailViewModel.updateLiked(pokemonDetail)
            updateLikeImg(pokemonDetail.isLiked.not())
        }
    }

    private fun setBackgroundColor() = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            resource?.let {
                val palette = Palette.from(it).generate()
                binding.pokemonDetailCard.setBackgroundColor(palette.getLightMutedColor(Color.WHITE))
                binding.pokemonDetailName.setTextColor(palette.getDarkVibrantColor(Color.BLACK))
            }
            return false
        }

    }


    private fun updateLikeImg(isLiked: Boolean) {
        when (isLiked) {
            false -> binding.likeImage.setImageResource(R.drawable.heart_outline)
            true -> binding.likeImage.setImageResource(R.drawable.heart)
        }
    }


    private fun showProgress() {
        binding.detailProgressBar.isVisible = true
        binding.detailViewGroup.isVisible = false
    }

    @InternalCoroutinesApi
    private fun showError(
        id: Long,
        errorMessage: String
    ) {
        binding.detailProgressBar.isVisible = false

        Snackbar.make(binding.detailCoordinator, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                pokemonDetailViewModel.loadDetail(id)
            }
            .show()
    }

}