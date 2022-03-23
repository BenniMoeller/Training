package com.example.training.fragments.bodyWeightFragment.bodyWeightRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.training.database.dataClasses.BodyWeight
import com.example.training.databinding.BodyweightViewholderBinding

/**
 * ViewHolder for the BodyWeightRecyclerAdapter
 * @property binding the binding from the datBinding xml class for this view
 */
internal class BodyWeightViewHolder private constructor(private val binding: BodyweightViewholderBinding) : RecyclerView.ViewHolder(binding.root) {


    companion object {
        /**
         * creates a new BodyWeightViewHolder
         * @param parent the parent to attach this view to
         * @return the viewholder that was created
         */
        fun create(parent: ViewGroup): BodyWeightViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = BodyweightViewholderBinding.inflate(layoutInflater, parent, false)
            return BodyWeightViewHolder(binding)
        }
    }

    /**
     *  binds the variables to the xml data binding
     */
    fun bind(data: BodyWeight, clickListener: BodyWeightListener) {
        binding.data = data
        binding.clickListener = clickListener
    }
}

/**
 * ClickListener for the BodyWeightViewHolder
 *  @property touchListener the function that is executed when the viewHolder is clicked
 */
internal class BodyWeightListener(val touchListener: (data: BodyWeight) -> Unit) {

    fun onClick(data: BodyWeight) = touchListener(data)
}
