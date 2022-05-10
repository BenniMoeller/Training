package com.example.training.fragments.trainingFragment.trainingViews


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.training.R
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.trainingData.TrainingSet
import com.example.training.databinding.TrainingsetViewBinding

/**
 * class that displays a TrainingSet
 * @constructor
 */
class TrainingSetView @JvmOverloads constructor(context: Context,
                                                val changedListener: () -> Unit,
                                                attrs: AttributeSet? = null,
                                                defStyle: Int = 0,
                                                defStyleRes: Int = 0) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val binding: TrainingsetViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.trainingset_view, this, true)
    }

    /**
     * sets up the changedListener so it is called everytime the content of this view is changed
     */
    private fun setUpChangedListener() {
        binding.weightEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                changedListener()
            }
        })

        binding.repsEDt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                changedListener()
            }
        })

        binding.rirEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                changedListener()
            }
        })
    }

    /**
     * adds information to this view
     * @param templateBlockSet BlockSet the BlockSet that is a template for this TrainingSet
     * @param setsAsHint Boolean true if the trainingSets should be displayed as a hint, false if they should be displayed as text
     * @param previousTrainingSet TrainingSet? the TrainingSet from the previous time. Null if there is no previous TrainingSet
     */
    fun addInformation(templateBlockSet: BlockSet, setsAsHint: Boolean, previousTrainingSet: TrainingSet? = null) {
        var repString = "Target: ${templateBlockSet.targetReps.asFormattedString()}"
        var rirString = "Target: ${templateBlockSet.targetRir.asFormattedString()}"
        previousTrainingSet?.let {
            if (setsAsHint) {
                binding.weightEdt.hint = it.weight.toString()
                repString += " Previous: ${it.reps}"
                rirString += " Previous: ${it.rir}"
            } else {
                binding.weightEdt.setText(previousTrainingSet.weight.toString())
                binding.repsEDt.setText(previousTrainingSet.reps.toString())
                binding.rirEdt.setText(previousTrainingSet.rir.toString())
            }
        }

        binding.repsEDt.hint = repString
        binding.rirEdt.hint = rirString
        setUpChangedListener() //for databinding purposes we only want this view to alert the listener after the initial information has been added
    }

    /**
     * returns the trainingSet from this view
     * @return TrainingSet the TrainingSet that is specified in this View
     */
    fun getTrainingSet(): TrainingSet {
        val weight = binding.weightEdt.text.toString().toDoubleOrNull() ?: 0.0 //the correct cast is guaranteed by the input type of the view
        val reps = binding.repsEDt.text.toString().toIntOrNull() ?: 0
        val rir = binding.rirEdt.text.toString().toIntOrNull() ?: 0
        return TrainingSet(weight, reps, rir)
    }

    /**
     * checks whether a TrainingSet matches the information of this view
     * @param trainingSet TrainingSet
     */
    fun equalsTrainingSet(trainingSet: TrainingSet): Boolean {
        val currentSet = getTrainingSet()
        return (currentSet.equalsTrainingSet(trainingSet))
    }

}                                              