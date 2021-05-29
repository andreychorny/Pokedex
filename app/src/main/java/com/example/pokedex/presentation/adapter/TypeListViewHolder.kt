package com.example.pokedex.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class TypeListViewHolder(
    view: View,
    val onItemClicked: (id: Long, isChecked: Boolean) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val chipGroup =
        itemView.findViewById<ChipGroup>(R.id.typeList)

    fun bind(item: TypeListItem) {
        val inflator = LayoutInflater.from(chipGroup.context)
        val children = item.typeMap.map { (typeId: Long, typeName: String) ->
            val chip = inflator.inflate(R.layout.type_item, chipGroup, false) as Chip
            chip.text = typeName
            chip.tag = typeId
            if (item.checkedId == typeId) {
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
        ): TypeListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.type_list, parent, false)
            return TypeListViewHolder(view, onItemClicked)
        }
    }
}
