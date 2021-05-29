package com.example.pokedex.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class GenerationListViewHolder(
    view: View,
    val onItemClicked: (id: Long, isChecked: Boolean) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val chipGroup =
        itemView.findViewById<ChipGroup>(R.id.generationList)

    fun bind(item: GenerationListItem) {
        val inflator = LayoutInflater.from(chipGroup.context)
        val children = item.generationList.map { (generationId: Long, generationName: String) ->
            val chip = inflator.inflate(R.layout.generation_item, chipGroup, false) as Chip
            chip.text = "Generation ${generationId.toString()}"
            chip.tag = generationId
            if (item.checkedId == generationId) {
                chip.isChecked = true
            }
            chip.setOnCheckedChangeListener { button, isChecked ->
                if (isChecked) {
                    onItemClicked(button.tag as Long, isChecked)
                }
            }
            chip
        }
        chipGroup.removeAllViews()
        for (chip in children) {
            chipGroup.addView(chip)
        }
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
