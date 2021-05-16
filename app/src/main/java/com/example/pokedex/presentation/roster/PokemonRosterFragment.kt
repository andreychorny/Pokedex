package com.example.pokedex.presentation.roster

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.databinding.FragmentPokemonRosterBinding
import com.example.pokedex.presentation.adapter.item.GenerationListItem
import com.example.pokedex.presentation.adapter.item.PokemonItem
import com.example.pokedex.presentation.adapter.item.RosterItem
import com.example.pokedex.presentation.adapter.item.TypeListItem
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonRosterFragment : Fragment() {

    private val pokemonRosterViewModel: PokemonRosterViewModel by viewModel()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPokemonRosterBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.pokemonRoster.adapter = adapter

        pokemonRosterViewModel.viewState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PokemonRosterViewState.Loading -> {
                    showProgress(binding)
                }
                is PokemonRosterViewState.Data -> {
                    showData(binding, state.items)
                }
                is PokemonRosterViewState.Error -> {
                    showError(binding, state)
                }
            }
        }

        setScrollingToTop(binding)
        setHasOptionsMenu(true)
        return binding.root
    }


    //scrolling screen to top after updates
    private fun setScrollingToTop(binding: FragmentPokemonRosterBinding) {
        adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                binding.pokemonRoster.smoothScrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                binding.pokemonRoster.smoothScrollToPosition(0)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                binding.pokemonRoster.smoothScrollToPosition(0)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.pokemonRoster.smoothScrollToPosition(0)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                binding.pokemonRoster.smoothScrollToPosition(0)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                binding.pokemonRoster.smoothScrollToPosition(0)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        pokemonRosterViewModel.updateFilter(
            when (item.itemId) {
                R.id.show_generation_menu -> PokemonApiFilter.SHOW_GENERATION
                R.id.show_type_menu -> PokemonApiFilter.SHOW_TYPE
                else -> PokemonApiFilter.SHOW_ALL
            }
        )
        return true
    }

    private fun showProgress( binding: FragmentPokemonRosterBinding) {
        binding.rosterProgressBar.isVisible = true
        binding.rosterViewGroup.isVisible = false
    }

    //TODO fix span size and refreshing(diffutil) content. Maybe should use Section with header
    private fun showData( binding: FragmentPokemonRosterBinding, items: List<RosterItem>) {
        binding.rosterProgressBar.isVisible = false
        binding.rosterViewGroup.isVisible = true
        items.forEach{
            setOnClickListener(it)
        }
        adapter.clear()
        adapter.addAll(items.filterIsInstance<GenerationListItem>().map { it as GenerationListItem })
        adapter.addAll(items.filterIsInstance<TypeListItem>().map { it as TypeListItem })
        adapter.addAll(items.filterIsInstance<PokemonItem>().map{it as PokemonItem})
        adapter.spanSizeLookup


    }

    private fun showError(
        binding: FragmentPokemonRosterBinding,
        state: PokemonRosterViewState.Error
    ) {
        binding.rosterProgressBar.isVisible = false
        binding.rosterViewGroup.isVisible = false
        Snackbar.make(binding.rosterCoordinator, state.message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                pokemonRosterViewModel.loadData()
            }
            .show()
    }

    private fun setOnClickListener(item: RosterItem){
        when(item){
            is PokemonItem -> item.onItemClicked = { id: Long ->
                this.findNavController().navigate(
                    PokemonRosterFragmentDirections
                        .actionPokemonRosterFragmentToPokemonDetailFragment(id)
                )
            }
            is GenerationListItem -> item.onItemClicked = { id: Long, isChecked: Boolean ->
                pokemonRosterViewModel.updateGenerationId(id)
            }
            is TypeListItem -> item.onItemClicked =  { id: Long, isChecked: Boolean ->
                pokemonRosterViewModel.updateTypeId(id)
            }
        }

    }
}