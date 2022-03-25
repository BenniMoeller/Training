package com.example.training.fragments.exerciseFragment.exerciseRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.training.database.dataClasses.Exercise
import com.example.training.databinding.ExerciseViewholderBinding

/**
 * ViewHolder for the ExerciseRecyclerAdapter
 * @property binding the binding from the datBinding xml class for this view
 */
internal class ExerciseViewHolder private constructor(private val binding: ExerciseViewholderBinding) : RecyclerView.ViewHolder(binding.root) {


    companion object {
        /**
         * creates a new BodyWeightViewHolder
         * @param parent the parent to attach this view to
         * @return the viewholder that was created
         */
        fun create(parent: ViewGroup): ExerciseViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ExerciseViewholderBinding.inflate(layoutInflater, parent, false)
            return ExerciseViewHolder(binding)
        }
    }

    /**
     * binds the variables to the xml data binding
     */
    fun bind(data: Exercise, clickListener: ExerciseListener) {
        binding.data = data
        binding.clickListener = clickListener
    }
}

/**
 * ClickListener for the ExerciseViewHolder
 *  @property deleteListener the function that is executed when the viewHolder is clicked
 */
internal class ExerciseListener(private val deleteListener: (data: Exercise) -> Unit, private val mainLiftListener: (data: Exercise) -> Unit) {

    fun onDeleteClick(data: Exercise) = deleteListener(data)

    fun onMainLiftClick(data: Exercise) = mainLiftListener(data)


}
