package com.example.pokedex.presentation.adapter

import android.R.attr
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokedex.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


private const val ITEM_TYPE_POKEMON = 1
private const val ITEM_TYPE_GENERATION_LIST = 2
private const val ITEM_TYPE_TYPE_LIST = 3

class PokemonRosterAdapter(
    private val onPokemonItemClicked: (id: Long) -> Unit,
    private val onGenerationItemClicked: (id: Long, isChecked: Boolean) -> Unit,
    private val onTypeItemClicked: (id: Long, isChecked: Boolean) -> Unit
) : ListAdapter<RosterItem, RecyclerView.ViewHolder>(PokemonRosterDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_POKEMON -> PokemonViewHolder.from(parent, onPokemonItemClicked)
            ITEM_TYPE_GENERATION_LIST -> GenerationListViewHolder.from(
                parent,
                onGenerationItemClicked
            )
            ITEM_TYPE_TYPE_LIST -> TypeListViewHolder.from(parent, onTypeItemClicked)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is PokemonItem -> {
                (holder as PokemonViewHolder).bind(item)
            }
            is GenerationListItem -> {
                (holder as GenerationListViewHolder).bind(item)
            }
            is TypeListItem -> {
                (holder as TypeListViewHolder).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PokemonItem -> ITEM_TYPE_POKEMON
            is GenerationListItem -> ITEM_TYPE_GENERATION_LIST
            is TypeListItem -> ITEM_TYPE_TYPE_LIST
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = getSpanSizeLookup()
    }

    private fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (getItem(position)) {
                    is PokemonItem -> 1
                    is GenerationListItem -> 2
                    is TypeListItem -> 2
                }
            }
        }
    }

    class PokemonViewHolder(
        view: View, val onItemClicked: (id: Long) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val nameView = itemView.findViewById<TextView>(R.id.pokemonName)
        private val imageView = itemView.findViewById<ImageView>(R.id.pokemonImage)
        private val cardView = itemView.findViewById<MaterialCardView>(R.id.pokemonCard)

        fun bind(item: PokemonItem) {
            nameView.text = item.name
            Glide.with(imageView.context)
                .asBitmap()
                .load(item.artImgUrl)
//                    in case of error, we try download another sprite image, as some pokemons don't have
//                    official art pic
                .error(
                    Glide
                        .with(imageView.context)
                        .asBitmap()
                        .load(item.spriteImgUrl)
                        .listener(setBackgroundColor())
                ).listener(setBackgroundColor())
                .into(imageView)
            itemView.setOnClickListener {
                onItemClicked(item.id)
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
                    cardView.setBackgroundColor(palette.getLightMutedColor(Color.CYAN))
                    nameView.setTextColor(palette.getDarkVibrantColor(Color.BLACK))
                }
                return false
            }

        }

        companion object {
            fun from(parent: ViewGroup, onItemClicked: (id: Long) -> Unit): PokemonViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.pokemon_item, parent, false)

                return PokemonViewHolder(view, onItemClicked)
            }
        }
    }

    class GenerationListViewHolder(
        view: View,
        val onItemClicked: (id: Long, isChecked: Boolean) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val chipGroup =
            itemView.findViewById<ChipGroup>(R.id.generationList)

        private var isFirstChip = true

        fun bind(item: GenerationListItem) {
            val inflator = LayoutInflater.from(chipGroup.context)
            val children = item.generationList.map { generationId ->
                val chip = inflator.inflate(R.layout.generation_item, chipGroup, false) as Chip
                chip.text = "Generation ${generationId.toString()}"
                chip.tag = generationId
                if(isFirstChip){
                    chip.isChecked = true
                    isFirstChip = false
                }
                chip.setOnCheckedChangeListener { button, isChecked ->
                    if(isChecked){
                        onItemClicked(button.tag as Long, isChecked)
                    }
                }
                chip
            }
            chipGroup.removeAllViews()
            for (chip in children) {
                chipGroup.addView(chip)
            }
            isFirstChip = true
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onItemClicked: (id: Long, isChecked: Boolean) -> Unit
            ): GenerationListViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.generation_list, parent, false)
                return GenerationListViewHolder(view, onItemClicked)
            }
        }
    }

    class TypeListViewHolder(
        view: View,
        val onItemClicked: (id: Long, isChecked: Boolean) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val chipGroup =
            itemView.findViewById<ChipGroup>(R.id.typeList)

        private var isFirstChip = true

        fun bind(item: TypeListItem) {
            val inflator = LayoutInflater.from(chipGroup.context)
            val children = item.typeMap.map { (typeId: Long, typeName: String) ->
                val chip = inflator.inflate(R.layout.type_item, chipGroup, false) as Chip
                chip.text = typeName
                chip.tag = typeId
                if(isFirstChip){
                    chip.isChecked = true
                    isFirstChip = false
                }
                chip.setOnCheckedChangeListener { button, isChecked ->
                    if(isChecked){
                        onItemClicked(button.tag as Long, isChecked)
                    }
                }
                chip
            }
            chipGroup.removeAllViews()
            for (chip in children) {
                chipGroup.addView(chip)
            }
            isFirstChip = true
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onItemClicked: (id: Long, isChecked: Boolean) -> Unit
            ): TypeListViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.type_list, parent, false)
                return TypeListViewHolder(view, onItemClicked)
            }
        }
    }
}

class PokemonRosterDiffUtil: DiffUtil.ItemCallback<RosterItem>() {

    override fun areItemsTheSame(oldItem: RosterItem, newItem: RosterItem): Boolean {
        if(oldItem is PokemonItem && newItem is PokemonItem){
            return oldItem.id == newItem.id
        }
        if(oldItem is GenerationListItem && newItem is GenerationListItem){
            return oldItem.generationList == newItem.generationList
        }
        if(oldItem is TypeListItem && newItem is TypeListItem) {
            return oldItem.typeMap == newItem.typeMap
        }
        return false
    }

    override fun areContentsTheSame(oldItem: RosterItem, newItem: RosterItem): Boolean {
        if(oldItem is PokemonItem && newItem is PokemonItem){
            return oldItem == newItem
        }
        if(oldItem is GenerationListItem && newItem is GenerationListItem){
            return oldItem.generationList == newItem.generationList
        }
        if(oldItem is TypeListItem && newItem is TypeListItem){
            return oldItem.typeMap == newItem.typeMap
        }
        return false
    }
}
