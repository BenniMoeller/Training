package com.example.training.fragments.trainingFragment.trainingViews


import android.R.attr.value
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.training.R
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.trainingData.TrainingSet
import com.example.training.databinding.TrainingexerciseViewBinding


/**
 * class that manages a whole TrainingExercise
 * @property binding TrainingexerciseViewBinding
 */
class TrainingExerciseView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, defStyleRes: Int = 0) :
        LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val binding: TrainingexerciseViewBinding
    var setsChangedListener: () -> Unit = { } //is called whenever the information in this view is changed

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.trainingexercise_view, this, true)
    }

    /**
     * adds Sets to this view.
     * @param blockSetList List<BlockSet> the BlockSets that are templates for this TrainingExercise
     * @param areSetsNew Boolean true if the sets are new or false if they have already been saved once
     * @param trainingSetList List<TrainingSet> the TrainingSets from last training. optional value if there hasn't been a last training
     */
    fun addSetList(blockSetList: List<BlockSet>, areSetsNew: Boolean, trainingSetList: List<TrainingSet> = mutableListOf()) {
      if (trainingSetList.isNotEmpty() && blockSetList.size != trainingSetList.size) {
            throw IllegalArgumentException("the trainingSetList and blockSetList mustn't have different sizes")
        }
        binding.setContainer.removeAllViews()

        for (counter in blockSetList.indices) {
            val setView = TrainingSetView(context, setsChangedListener)
            setView.addInformation(blockSetList[counter], areSetsNew, trainingSetList.getOrNull(counter))
            binding.setContainer.addView(setView)
        }
    }

    /**
     * returns the list of TrainingSets currently displayed in this view. this Sets only get changed if the view is changed in the ui
     */
    fun getTrainingSetList(): List<TrainingSet> {
        val setList = mutableListOf<TrainingSet>()
        for (view in binding.setContainer.children) {
            val trainingSet = (view as TrainingSetView).getTrainingSet()
            setList.add(trainingSet)
        }
        return setList
    }

    /**
     * checks whether a TrainingList equals the TrainingSets in this view
     * @param setList List<TrainingSet>
     */
    fun equalTrainingSetList(setList: List<TrainingSet>): Boolean {
        val currentSets = getTrainingSetList()
        if (setList.size != currentSets.size) return false
        else {
            for (index in currentSets.indices) {
                if (!setList[index].equalsTrainingSet(currentSets[index])) return false
            }
            return true
        }
    }
}

class TrainingExerciseViewBindingAdapter() {
    companion object {

        @BindingAdapter(value = ["blockSets", "trainingSets", "areSetsNew"])
        @JvmStatic
        fun setSets(view: TrainingExerciseView, blockSetList: List<BlockSet>, trainingSetList: List<TrainingSet>, areSetsNew: Boolean) {
            if (!view.equalTrainingSetList(trainingSetList) || view.getTrainingSetList().isEmpty()) {
                view.addSetList(blockSetList, areSetsNew, trainingSetList)
            }
        }

        @InverseBindingAdapter(attribute = "trainingSets")
        @JvmStatic
        fun getSets(view: TrainingExerciseView): List<TrainingSet> {
            val test = view.getTrainingSetList()
            return view.getTrainingSetList()
        }

        @BindingAdapter("app:trainingSetsAttrChanged")
        @JvmStatic
        fun setListeners(view: TrainingExerciseView, attrChange: InverseBindingListener) {
            view.setsChangedListener = { attrChange.onChange() }
        }
    }

}