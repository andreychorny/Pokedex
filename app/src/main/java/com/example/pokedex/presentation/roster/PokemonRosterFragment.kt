package com.example.pokedex.presentation.roster

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonRosterBinding
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.presentation.roster.adapter.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonRosterFragment : Fragment() {

    private val pokemonRosterViewModel: PokemonRosterViewModel by viewModel()
    private var adapter: PokemonRosterAdapter? = null
    private var _binding: FragmentPokemonRosterBinding? = null
    private val binding get() = _binding!!
    private var generationsList: GenerationListItem? = null
    private var typeList: TypeListItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonRosterBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.toolbarRoster.inflateMenu(R.menu.overflow_menu)
        binding.toolbarRoster.setOnMenuItemClickListener(onMenuItemClick())
        initRecyclerView()
        binding.pokemonRoster.adapter = adapter
        showProgress()
        pokemonRosterViewModel.rosterLiveData().observe(viewLifecycleOwner) { list ->
            showData(list.map { it.toItem() })
        }
        pokemonRosterViewModel.generationLiveData().observe(viewLifecycleOwner){ list ->
            generationsList = list
        }
        pokemonRosterViewModel.typeLiveData().observe(viewLifecycleOwner){ list ->
            typeList = list
        }

        setScrollingToTop()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun onMenuItemClick() = object : MenuItem.OnMenuItemClickListener,
        Toolbar.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem): Boolean {
            pokemonRosterViewModel.updateFilter(
                when (item.itemId) {
                    R.id.show_generation_menu -> PokemonApiFilter.SHOW_GENERATION
                    R.id.show_type_menu -> PokemonApiFilter.SHOW_TYPE
                    R.id.show_liked -> PokemonApiFilter.SHOW_LIKED
                    else -> PokemonApiFilter.SHOW_ALL
                }
            )
            return true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //scrolling screen to top after updates
    private fun setScrollingToTop() {
        adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                binding.pokemonRoster.smoothScrollToPosition(0)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                binding.pokemonRoster.smoothScrollToPosition(0)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initRecyclerView() {
        adapter = PokemonRosterAdapter(
            onPokemonItemClicked = { id: Long, view: View ->
                exitTransition = MaterialElevationScale(false).apply {
                    duration = 500L
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = 500L
                }

                val detailCardTransitionName = getString(R.string.string_transition)
                val extras = FragmentNavigatorExtras(view to detailCardTransitionName)
                this.findNavController().navigate(
                    PokemonRosterFragmentDirections
                        .actionPokemonRosterFragmentToPokemonDetailFragment(id), extras
                )
            },
            onGenerationItemClicked = { id: Long, isChecked: Boolean ->
                showProgress()
                pokemonRosterViewModel.updateGenerationId(id)
            },
            onTypeItemClicked = { id: Long, isChecked: Boolean ->
                showProgress()
                pokemonRosterViewModel.updateTypeId(id)
            }
        )
    }

    private fun showProgress() {
        binding.rosterProgressBar.isVisible = true
        binding.rosterViewGroup.isVisible = false
    }

    private fun showData(items: List<RosterItem>) {
        binding.rosterProgressBar.isVisible = false
        binding.rosterViewGroup.isVisible = true
        val resultsList: MutableList<RosterItem> = mutableListOf()
        if(pokemonRosterViewModel.filter == PokemonApiFilter.SHOW_GENERATION){
            generationsList?.let { resultsList.add(it) }
        }
        if(pokemonRosterViewModel.filter == PokemonApiFilter.SHOW_TYPE){
            typeList?.let { resultsList.add(it) }
        }
        resultsList.addAll(items)
        adapter?.submitList(resultsList)
    }

    private fun showError(
        state: PokemonRosterViewState.Error
    ) {
        binding.rosterProgressBar.isVisible = false
        Snackbar.make(binding.rosterCoordinator, state.message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                pokemonRosterViewModel.loadData()
            }
            .show()
    }

}
fun PokemonEntity.toItem(): PokemonItem = PokemonItem(id, name, artImgUrl, spriteImgUrl)
