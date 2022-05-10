package com.example.training.fragments.blockFragment.blockViews


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
import com.example.training.databinding.BlockexerciseViewBinding

/**
 * view that represents a blockExercise
 */
class BlockExerciseView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, defStyleRes: Int = 0) :
        LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val binding: BlockexerciseViewBinding
    var setsChangedListener: (() -> Unit)? = null //the function that is executed when the contents of this view are changed

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.blockexercise_view, this, true)
        setUp()
    }

    /**
     * calls all the relevant setUp Function so the view functions properly
     */
    private fun setUp() {
        setUpAddSetBtn()
    }

    /**
     * returns all the blockSets currently in this view
     * @return List<BlockSet> a list containing all the BlockSets
     */
    fun getBlockSets(): List<BlockSet> {
        val setList = mutableListOf<BlockSet>()
        for (blockSetView in binding.setContainer.children) {
            val blockSet = (blockSetView as BlockSetView).getBlockSet()
            setList.add(blockSet)
        }
        return setList
    }

    /**
     * adds a BlockSet to the container in the xml
     * @param blockSet BlockSet the BlockSet which data should be added. if null then just an empty BlockSet will be added
     * @param alertChange Boolean whether the view should alert it's observers that it changed. standard value is false
     *
     */
    private fun addBlockSetView(blockSet: BlockSet? = null, alertChange: Boolean = false) {
        val blockSetView = BlockSetView(context, { setView -> binding.setContainer.removeView(setView) }).apply {
            this.editedListener = setsChangedListener
            if (blockSet != null) this.addBlockSet(blockSet)
        }
        binding.setContainer.addView(blockSetView)

        if (alertChange) setsChangedListener?.let { it() }
    }

    /**
     * adds a list of BlockSets to this view
     * @param blockSetList List<BlockSet> the list containing all the BlockSets that should be added
     */
    fun addBlockSets(blockSetList: List<BlockSet>) {
        binding.setContainer.removeAllViews()
        for (blockSet in blockSetList) {
            addBlockSetView(blockSet)
        }
        setsChangedListener?.let { it() } //we only want to alert that the view changed after the last set is added
    }

    /**
     * compare whether a list of BlockSets equals the information of this view
     * @param blockSetList List<BlockSet> the list of BlockSets to be checked
     * @return Boolean true if the information matches
     */
    fun equalsBlockSets(blockSetList: List<BlockSet>): Boolean {
        val blockSetViewList = binding.setContainer.children.toList()
        if (blockSetList.size != blockSetViewList.size) { //if the list are of different sizes then they already don't match
            return false
        } else {
            for (index in blockSetViewList.indices) { //we can just use this since both lists are now of equal length
                val blockSet = blockSetList[index]
                val blockSetView = blockSetViewList[index] as BlockSetView
                if (!blockSetView.equalsBlockSet(blockSet)) return false
            }
        }

        return true //if no blockSets didn't match then they are the same
    }

    /**
     * sets up the addSetBtn so BlockSetViews are added onClick
     */
    private fun setUpAddSetBtn() = binding.addSetBtn.setOnClickListener { addBlockSetView(alertChange = true) }


}

/**
 * class that contains BindingAdapters for the BlockExerciseView class, thus enabling two-way databinding
 */
class BlockExerciseViewBindingAdapters() {

    companion object {

        @BindingAdapter("blockSets")
        @JvmStatic
        fun setBlockSets(view: BlockExerciseView, newValue: List<BlockSet>) {
            if (!view.equalsBlockSets(newValue)) { // Important to break potential infinite loops.
                view.addBlockSets(newValue)
            }
        }

        @InverseBindingAdapter(attribute = "blockSets")
        @JvmStatic
        fun getBlockSets(view: BlockExerciseView): List<BlockSet> = view.getBlockSets()

        @BindingAdapter("app:blockSetsAttrChanged")
        @JvmStatic
        fun setListeners(view: BlockExerciseView, attrChange: InverseBindingListener) {
            view.setsChangedListener = { attrChange.onChange() }
        }
    }

}