package com.example.pokedex.presentation.roster.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R

class EmptyStateViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {

    fun bind(item: EmptyStateItem) {
        //Nothing to bind, EmptyState view contains all information
    }

    companion object {
        fun from(
            parent: ViewGroup,
        ): EmptyStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.empty_state_item, parent, false)
            return EmptyStateViewHolder(view)
        }
    }
}
