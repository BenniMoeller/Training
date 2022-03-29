package com.example.training.fragments.exerciseFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.training.R
import com.example.training.database.TrainingDatabase
import com.example.training.database.repositories.ExerciseRepository
import com.example.training.databinding.ExerciseFragmentBinding
import com.example.training.fragments.exerciseFragment.exerciseRecyclerView.ExerciseListener
import com.example.training.fragments.exerciseFragment.exerciseRecyclerView.ExerciseRecyclerAdapter


/**
 * fragment for the Exercise activity
 */
class ExerciseFragment : Fragment() {
    private lateinit var viewModel: ExerciseViewModel
    private lateinit var binding: ExerciseFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.exercise_fragment, container, false)
        setUp()
        return binding.root
    }

    /**
     * calls all the setup functions
     */
    private fun setUp() {
        setUpViewModel()
        setUpRecyclerView()
        setUpExerciseCheck()
    }

    /**
     * sets up the viewmodel
     */
    private fun setUpViewModel() {
        val application = requireNotNull(this.activity).application
        val dataBase = TrainingDatabase.getInstance(application).databaseDao
        val repository = ExerciseRepository(dataBase)
        val viewModelFactory = ExerciseViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ExerciseViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * sets up the recyclerView so it displays the exercises correctly
     */
    private fun setUpRecyclerView() {
        val adapter = ExerciseRecyclerAdapter(ExerciseListener({ viewModel.deleteExercise(it) }, { viewModel.updateMainLift(it) }))
        binding.exerciseRecyclerView.adapter = adapter
        viewModel.exercises.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        } //todo the isMainLift image in the viewholder can only be changed every few seconds. also on changing it the view briefly disappears
    }

    /**
     * displayes a toast when an exercise already exists in the database according to the viewmodel
     */
    private fun setUpExerciseCheck() {
        viewModel.doesExerciseAlreadyExist.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(requireContext(), "An exercise with this name already exists", Toast.LENGTH_SHORT).show()
                viewModel.resetDoesExerciseAlreadyExist()
            }
        }
    }


}

