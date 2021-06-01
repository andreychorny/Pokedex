package com.example.pokedex.presentation.detail

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.InternalCoroutinesApi

class PokemonDetailFragment : Fragment() {

    private val pokemonDetailViewModel: PokemonDetailViewModel by viewModel()
    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO FIX CHANGING BACKGROUND COLOR OF CARD AND FLICKERING
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.pokemonHostFragment
            duration = 500L
            scrimColor = Color.TRANSPARENT
        }
//        OPTION №2 WITHOUT FIX:
//        sharedElementEnterTransition = MaterialElevationScale(true).apply {
//            duration = 500L
//        }

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
                    showProgress()
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
        binding.pokemonDetailName.isVisible = true
        binding.likeImage.isVisible = true
        binding.pokemonWeight.isVisible = true
        binding.pokemonHeight.isVisible = true
        binding.detailTypeList.isVisible = true

        binding.pokemonDetailName.text = pokemonDetail.name
        binding.pokemonHeight.text = pokemonDetail.height.toString()
        binding.pokemonWeight.text = pokemonDetail.weight.toString()
        setImage(pokemonDetail)
        setTypeChipGroup(pokemonDetail)
        binding.hpIndicator.progress = pokemonDetail.stats["hp"] ?: 0
        binding.attackIndicator.progress = pokemonDetail.stats["attack"] ?: 0
        binding.defenseIndicator.progress = pokemonDetail.stats["defense"] ?: 0
        binding.specialAttackIndicator.progress = pokemonDetail.stats["special-attack"] ?: 0
        binding.specialDefenseIndicator.progress = pokemonDetail.stats["special-defense"] ?: 0
        binding.speedIndicator.progress = pokemonDetail.stats["speed"] ?: 0

    }

    private fun setImage(pokemonDetail: PokemonDetailEntity) {
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

    private fun setTypeChipGroup(pokemonDetail: PokemonDetailEntity) {
        val colors = resources.getIntArray(R.array.typesColors)
        val typeNames = resources.getStringArray(R.array.typeNames)
        val chipGroup = binding.detailTypeList
        val inflator = LayoutInflater.from(chipGroup.context)
        val children = pokemonDetail.types.map { typeName ->
            val chip = inflator.inflate(R.layout.detail_type_chip, chipGroup, false) as Chip
            chip.text = typeName
            chip.tag = typeName
            var backgroundColor = R.color.otherType
            val index = typeNames.indexOf(typeName)
            if( index > -1 ){
                backgroundColor = colors[index]
            }
            chip.chipBackgroundColor = ColorStateList.valueOf(backgroundColor)
            chip
        }
        chipGroup.removeAllViews()

        for (chip in children) {
            chipGroup.addView(chip)
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

//                SETING APPBAR COLOR THE SAME AS BACKGROUND FOR PICTURE
//                (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
//                    ColorDrawable(palette.getLightMutedColor(Color.WHITE))
//                )
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
        //I hide views selectively as hiding pokemon image breaks transition animation
        binding.detailProgressBar.isVisible = true
        binding.pokemonDetailName.isVisible = false
        binding.likeImage.isVisible = false
        binding.pokemonWeight.isVisible = false
        binding.pokemonHeight.isVisible = false
        binding.detailTypeList.isVisible = false

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