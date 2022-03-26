package com.example.training.fragments.startFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.training.R
import com.example.training.database.TrainingDatabase
import com.example.training.database.dataClasses.blockData.Block
import com.example.training.databinding.StartFragmentBinding


/**
 * fragment for the Start activity. the entry screen of the app that navigates to all other fragments
 */
class StartFragment : Fragment() {
    private lateinit var viewModel: StartViewModel
    private lateinit var binding: StartFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.start_fragment, container, false)
        setUp()
        return binding.root
    }

    /**
     * calls all the setup functions
     */
    private fun setUp() {
        setUpViewModel()
        setUpNavigation()
    }

    /**
     * sets up the ViewModel
     */
    private fun setUpViewModel() {
        val application = requireNotNull(this.activity).application
        val dataBase = TrainingDatabase.getInstance(application).databaseDao
        val viewModelFactory = StartViewModelFactory(dataBase, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StartViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * sets up the navigation so all buttons navigate onClick
     */
    private fun setUpNavigation() {
        binding.bodyWeightNavBtn.setOnClickListener { findNavController().navigate(R.id.action_startFragment_to_bodyWeightFragment) }
        binding.exerciseNavBtn.setOnClickListener { findNavController().navigate(R.id.action_startFragment_to_exerciseFragment) }
    }
}

