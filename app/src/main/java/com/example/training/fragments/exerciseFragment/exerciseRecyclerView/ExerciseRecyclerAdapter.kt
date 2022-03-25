package com.example.training.fragments.exerciseFragment.exerciseRecyclerView


import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.training.database.dataClasses.Exercise

/**
 * RecyclerAdapter for the ExerciseFragment
 * @property touchListener the function that is executed when views from this adapter are clicked
 */
internal class ExerciseRecyclerAdapter(private val touchListener: ExerciseListener) : ListAdapter<Exercise, ExerciseViewHolder>(ExerciseDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(getItem(position), touchListener)
    }
}

/**
 * DiffcallBack for the ExerciseRecyclerAdapter
 */
internal class ExerciseDiffCallBack : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return (oldItem.id == newItem.id) && (oldItem.isMainLift == newItem.isMainLift)
    }

}