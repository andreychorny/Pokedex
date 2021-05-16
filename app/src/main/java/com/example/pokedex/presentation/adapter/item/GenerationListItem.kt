package com.example.pokedex.presentation.adapter.item

import android.view.LayoutInflater
import com.example.pokedex.R
import com.google.android.material.chip.Chip
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.generation_list.view.*

class GenerationListItem(
    private val generationList: List<Long>
): Item<GroupieViewHolder>(), RosterItem {

    private var isFirstChip = true

    var onItemClicked: ((Long, Boolean) -> Unit)? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val chipGroup = viewHolder.itemView.generationList
        val inflator = LayoutInflater.from(chipGroup.context)
        val children = generationList.map { generationId ->
            val chip = inflator.inflate(R.layout.generation_item, chipGroup, false) as Chip
            chip.text = "Generation ${generationId.toString()}"
            chip.tag = generationId
            if(isFirstChip){
                chip.isChecked = true
                isFirstChip = false
            }
            chip.setOnCheckedChangeListener { button, isChecked ->
                if(isChecked){
                    onItemClicked?.invoke(button.tag as Long, isChecked)
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

    override fun getLayout(): Int = R.layout.generation_list

    override fun getSpanSize(spanCount: Int, position: Int): Int = 2
}