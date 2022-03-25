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
import com.example.training.fragments.bodyWeightFragment.bodyWeightRecyclerView.BodyWeightListener
import com.example.training.fragments.bodyWeightFragment.bodyWeightRecyclerView.BodyWeightRecyclerAdapter


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
        setUpRecyclerView()
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

    /**
     * sets up the recyclerView so the bodyWeights are displayed
     */
    private fun setUpRecyclerView() {
        val adapter = BodyWeightRecyclerAdapter(BodyWeightListener { viewModel.deleteBodyWeight(it) })
        binding.bodyWeightRecyclerView.adapter = adapter
        viewModel.bodyWeights.observe(viewLifecycleOwner) { adapter.submitList(it) }

        //todo add a graph that shows the bodyWeight Progress, either here or in the startFragment
    }

}

