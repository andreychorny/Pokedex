package com.example.pokedex.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.domain.GenerationEntity

class GenerationListAdapter(private val onClickListener: OnClickListener):
    RecyclerView.Adapter<GenerationListAdapter.GenerationViewHolder>() {

    var data = listOf<GenerationEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenerationViewHolder {
        return GenerationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GenerationViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, onClickListener)
    }

    override fun getItemCount(): Int = data.size


    class GenerationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val generationButton = itemView.findViewById<Button>(R.id.changeGenerationButton)

        fun bind(item: GenerationEntity, onClickListener: OnClickListener){
            generationButton.text = item.id.toString()
            generationButton.setOnClickListener{
                onClickListener.onClick(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): GenerationViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.generation_item, parent, false)

                return GenerationViewHolder(view)
            }
        }
    }

    class OnClickListener(val clickListener: (generation: GenerationEntity) -> Unit) {
        fun onClick(generation: GenerationEntity) = clickListener(generation)
    }

}
