package com.example.training.fragments.bodyWeightFragment.bodyWeightRecyclerView


import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.training.database.dataClasses.BodyWeight

/**
 * RecyclerAdapter for the BodyWeightFragment
 * @property touchListener the function that is executed when views from this adapter are clicked
 */
internal class BodyWeightRecyclerAdapter(private val touchListener: BodyWeightListener) : ListAdapter<BodyWeight, BodyWeightViewHolder>(
        BodyWeightDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyWeightViewHolder {
        return BodyWeightViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BodyWeightViewHolder, position: Int) {
        holder.bind(getItem(position), touchListener)
    }
}

/**
 * DiffcallBack for the BodyWeightRecyclerAdapter
 */
internal class BodyWeightDiffCallBack : DiffUtil.ItemCallback<BodyWeight>() {
    override fun areItemsTheSame(oldItem: BodyWeight, newItem: BodyWeight): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: BodyWeight, newItem: BodyWeight): Boolean {
        return (oldItem.date == newItem.date) && (oldItem.weight == newItem.weight)
    }

}