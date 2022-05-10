package com.example.training.fragments.trainingFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.training.R
import com.example.training.database.TrainingDatabase
import com.example.training.databinding.TrainingFragmentBinding


/**
 * fragment for the Training activity
 */
class TrainingFragment : Fragment() {
    private lateinit var viewModel: TrainingViewModel
    private lateinit var binding: TrainingFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.training_fragment, container, false)
        setUp()
        return binding.root
    }

    /**
     * calls all the setup functions
     */
    private fun setUp() {
        setUpViewModel()
        setUpTrainingDayFinished()
    }

    /**
     * sets up the viewmodel
     */
    private fun setUpViewModel() {
        val application = requireNotNull(this.activity).application
        val database = TrainingDatabase.getInstance(application).databaseDao
        val viewModelFactory = TrainingViewModelFactory(database, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrainingViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * sets up the navigation back to the start fragment once the training is finished
     */
    private fun setUpTrainingDayFinished() {
        viewModel.trainingFinished.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.resetTrainingFinished()
                findNavController().navigate(R.id.action_trainingFragment_to_startFragment)
                Toast.makeText(context, "The Training was succesfully saved", Toast.LENGTH_SHORT).show()
            }
        } //todo add possibility to alter already recorded training.
        //todo write tests
    }
}

