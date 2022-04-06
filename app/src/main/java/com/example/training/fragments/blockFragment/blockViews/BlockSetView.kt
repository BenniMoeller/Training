package com.example.training.fragments.blockFragment.blockViews


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.training.R
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.Range
import com.example.training.databinding.BlocksetViewBinding

/**
 * view that represents a BlockSet. Rep-targets and rir-targets can be entered and returned as a BlockSet
 * @property deleteFunction the function that is executed when the deleteButton of this view is clicked
 */
class BlockSetView @JvmOverloads constructor(context: Context,
                                             private val deleteFunction: (BlockSetView) -> Unit = {},
                                             attrs: AttributeSet? = null,
                                             defStyle: Int = 0,
                                             defStyleRes: Int = 0) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val binding: BlocksetViewBinding
    private val numberInputType = Regex("[0-9]*") //the different Input types for the edittext. either just a number
    private val rangeInputType = Regex("[0-9]+-[0-9]+") //or a Range
    private val wholeInputType = Regex("([0-9]*)|([0-9]+-[0-9]+)") // or any of the both
    var editedListener: (() -> Unit)? = null //the function that is called after the information in this view changed

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.blockset_view, this, true)
        setUp()
    }

    /**
     * calls all the relevant functions to make this view work properly
     */
    private fun setUp() {
        binding.repsEdt.setRangeListener()
        binding.rirEdt.setRangeListener()
        binding.deleteBtn.setOnClickListener { deleteFunction(this); editedListener?.let { it() } }
    }

    /**
     * returns the blockSet from this View
     * @return BlockSet the BlockSet displayed in this view
     */
    fun getBlockSet(): BlockSet {
        val repString = binding.repsEdt.text.toString()
        val rirString = binding.rirEdt.text.toString()
        val repRange = getRangeFromString(repString)
        val rirRange = getRangeFromString(rirString)
        return BlockSet(repRange, rirRange)
    }

    /**
     * adds the data from a BlockSet to this view
     * @param blockSet BlockSet the BlockSet containing the data that should be added
     */
    fun addBlockSet(blockSet: BlockSet) {
        binding.repsEdt.setText(blockSet.targetReps.asFormattedString())
        binding.rirEdt.setText(blockSet.targetRir.asFormattedString())
    }

    /**
     * checks whether the information in this view matches that of a BlockSet
     * @param blockSet BlockSet the BlockSet to compare
     * @return Boolean true if the information is equal
     */
    fun equalsBlockSet(blockSet: BlockSet) = getBlockSet() == blockSet

    /**
     * returns a Range from a String in the correct formate "number-number" or "number"
     * @param rangeString the string in the correct format
     * @return Range the range derived from the String
     */
    private fun getRangeFromString(rangeString: String): Range {
        val range: Range
        var newRangeString = rangeString //to make it possible to reassign it
        if (newRangeString.isNotEmpty() && newRangeString.last() == '-') newRangeString = newRangeString.dropLast(1)
        assert(wholeInputType.matches(newRangeString))
        if (numberInputType.matches(newRangeString)) {
            val number = newRangeString.toIntOrNull() ?: 0 // the elvis operator is for when rangeString is the empty string
            range = Range(number, number)
        } else { //if the rangeString matches the rangeInputType
            val splitString = newRangeString.split("-") //since the rangeString is in format "number-number" this returns a list of length 2
            range = Range(splitString[0].toInt(), splitString[1].toIntOrNull() ?: splitString[0].toInt())
            //elvis when the initial string was of the form "number-". then splitString[1] is "" and we just use the first number

        }
        return range
    }


    /**
     * makes it so that an edittext only accepts the input of "number-number" or number
     * @receiver EditText the EditText to accept a range
     */
    private fun EditText.setRangeListener() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && p0.filter { it == '-' }.length > 1) { //we only want to accept one '-' in the text
                    val correctedText = p0.dropLastWhile { it != '-' }.dropLast(1) //we drop until the last '-' and then that as well
                    this@setRangeListener.setText(correctedText)
                    this@setRangeListener.setSelection(correctedText.length) //todo still crashes on eg. "100-500---". but i disabled pasting
                } else if (p0 != null && p0.isNotEmpty() && p0[0] == '-') { //the '-' mustn't be the first letter
                    this@setRangeListener.setText("")
                }

                editedListener?.let { it() }
            }
        })
    }


}