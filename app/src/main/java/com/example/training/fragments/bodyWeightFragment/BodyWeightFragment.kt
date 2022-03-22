package com.example.training.fragments.bodyWeightFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.training.R
import com.example.training.database.TrainingDatabase
import com.example.training.database.repositories.BodyWeightRepository
import com.example.training.databinding.BodyweightFragmentBinding


/**
 * fragment for the Bodyweight activity
 */
class BodyWeightFragment : Fragment() {
    private lateinit var viewModel: BodyWeightViewModel
    private lateinit var binding: BodyweightFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bodyweight_fragment, container, false)
        setUp()
        return binding.root
    }

    /**
     * calls all the setup functions
     */
    private fun setUp() {
        setUpViewModel()
    }

    /**
     * sets up the viewmodel
     */
    private fun setUpViewModel() {
        val application = requireNotNull(this.activity).application
        val dataBase = TrainingDatabase.getInstance(application).databaseDao
        val repository = BodyWeightRepository(dataBase)
        val viewModelFactory = BodyWeightViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BodyWeightViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    //todo add ability to add bodyweight
}

