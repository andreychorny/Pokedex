package com.example.pokedex.presentation.roster

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.databinding.FragmentPokemonRosterBinding
import com.example.pokedex.presentation.adapter.PokemonRosterAdapter
import com.example.pokedex.presentation.adapter.RosterItem
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonRosterFragment : Fragment() {

    private val pokemonRosterViewModel: PokemonRosterViewModel by viewModel()
    private var adapter: PokemonRosterAdapter? = null
    private var _binding: FragmentPokemonRosterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonRosterBinding.inflate(inflater)
        binding.lifecycleOwner = this

        initRecyclerView()

        binding.pokemonRoster.adapter = adapter

        pokemonRosterViewModel.viewState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PokemonRosterViewState.Loading -> {
                    showProgress()
                }
                is PokemonRosterViewState.Data -> {
                    showData(state.items)
                }
                is PokemonRosterViewState.Error -> {
                    showError(state)
                }
            }
        }

        setScrollingToTop()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //scrolling screen to top after updates
    private fun setScrollingToTop() {
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

    private fun initRecyclerView() {
        adapter = PokemonRosterAdapter(
            onPokemonItemClicked = { id: Long ->
                this.findNavController().navigate(
                    PokemonRosterFragmentDirections
                        .actionPokemonRosterFragmentToPokemonDetailFragment(id)
                )
            },
            onGenerationItemClicked = { id: Long, isChecked: Boolean ->
                pokemonRosterViewModel.updateGenerationId(id)
            },
            onTypeItemClicked = { id: Long, isChecked: Boolean ->
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
        adapter?.submitList(items)
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