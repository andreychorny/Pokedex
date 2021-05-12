package com.example.pokedex.presentation.roster

import android.os.Bundle
import android.view.*
import android.widget.ScrollView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.data.network.PokemonApiFilter
import com.example.pokedex.databinding.FragmentPokemonRosterBinding
import com.example.pokedex.presentation.adapter.PokemonRosterAdapter

class PokemonRosterFragment : Fragment() {

    private val pokemonRosterViewModel = PokemonRosterViewModel()
    private var adapter = PokemonRosterAdapter(
        onPokemonItemClicked = { id: Long ->
            this.findNavController().navigate(PokemonRosterFragmentDirections
                .actionPokemonRosterFragmentToPokemonDetailFragment(id))
        },
        onGenerationItemClicked = { id: Long, isChecked: Boolean ->
            pokemonRosterViewModel.updateGenerationId(id)
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPokemonRosterBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.pokemonRoster.adapter = adapter
        pokemonRosterViewModel.getPokemonList().observe(viewLifecycleOwner, {pokemonList ->
            adapter.submitList(pokemonList)
        })

        //scrolling screen to top after updates
        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
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
        setHasOptionsMenu(true)
        return binding.root
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



}