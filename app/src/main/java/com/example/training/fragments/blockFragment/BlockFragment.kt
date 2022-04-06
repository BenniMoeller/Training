package com.example.training.fragments.blockFragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.training.R
import com.example.training.database.DataConverter
import com.example.training.database.TrainingDatabase
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.Range
import com.example.training.databinding.BlockFragmentBinding
import java.util.*


/**
 * fragment for the Block activity. made to create training templates in form of a block
 */
class BlockFragment : Fragment() {
    private lateinit var viewModel: BlockViewModel
    private lateinit var binding: BlockFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.block_fragment, container, false)
        setUp()
        return binding.root
    }

    /**
     * calls all the setup functions
     */
    private fun setUp() {
        setUpViewModel()
        showDatePicker()
        binding.test.setOnClickListener { //todo this is just for testing. delete this
            val a = binding.blockExerciseView.getBlockSets()
            val b = viewModel.currentBlockSets.value
            val c = mutableListOf<BlockSet>(BlockSet(Range(1, 2), Range(3, 3)))
            viewModel.currentBlockSets.postValue(c)
        }
    }

    /**
     * sets up the viewmodel
     */
    private fun setUpViewModel() {
        val application = requireNotNull(this.activity).application
        val database = TrainingDatabase.getInstance(application).databaseDao
        val viewModelFactory = BlockViewModelFactory(database, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BlockViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * creates a DatePickerDialog to get the startDate of the Block
     */
    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(requireContext()).apply {
            this.datePicker.minDate = Calendar.getInstance().time.time //we don't want to create a block that starts before today }
            setTitle("Choose a StartDate for the Block")
            this.setOnDateSetListener(DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                val selectedDate = Calendar.getInstance().apply { set(year, month, day) }.time
                val serializedDate = DataConverter.serializeDate(selectedDate)
                viewModel.startDate = serializedDate
            })
        }
        datePickerDialog.show()
    }

}

