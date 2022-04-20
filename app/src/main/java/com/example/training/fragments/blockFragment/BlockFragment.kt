package com.example.training.fragments.blockFragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.GestureDetectorCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.training.R
import com.example.training.database.DataConverter
import com.example.training.database.TrainingDatabase
import com.example.training.databinding.BlockFragmentBinding
import com.google.android.material.tabs.TabLayout
import java.lang.Math.abs
import java.time.DayOfWeek
import java.util.*
import kotlin.math.absoluteValue


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
        setUpTabLayout()
        setUpExerciseSwiping()
        setUpBlockSaveButton()
        observeBlockSaved()
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
            this.setTitle("Choose a StartDate for the Block") //change this to a string stored in the strings.xml
            this.setButton(DatePickerDialog.BUTTON_NEGATIVE, "", { a, b -> }) //to hide the dismiss button
            this.setCanceledOnTouchOutside(false)
            this.setOnDateSetListener { datePicker, year, month, day ->
                val selectedDate = Calendar.getInstance().apply { set(year, month, day) }.time
                val serializedDate = DataConverter.serializeDate(selectedDate)
                viewModel.startDate = serializedDate
                showDayChooser()
            }
        }
        datePickerDialog.show()
    }

    /**
     * creates a alertdialog from which the trainingDays are supposed to be chosen
     */
    private fun showDayChooser() {
        var dialog: AlertDialog? = null
        val selectedDays = mutableListOf<Int>()

        /**
         * toggles the ok button in the alertdialog depending on whether items are selected or not
         */
        fun togglePositiveButton() {
            dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = selectedDays.size >= 1
        }

        /**
         * deals with the logic that should happen when an item in the AlertDialog is clicked
         * @param isChecked Boolean whether the item is checked or unchecked
         * @param which Int the index of the item that was clicked
         */
        fun onItemClicked(isChecked: Boolean, which: Int) {
            if (isChecked) {
                selectedDays.add(which)
                togglePositiveButton()
            } else if (selectedDays.contains(which)) {
                selectedDays.remove(which)
                togglePositiveButton()
            }
        }

        val dialogBuilder = AlertDialog.Builder(context).apply {
            this.setCancelable(false)
            setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, index ->
                viewModel.addWeekDays(selectedDays.sorted())
                addTabsToTabLayout(selectedDays)
            })
            this.setMultiChoiceItems(R.array.weekdays, null, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                onItemClicked(isChecked, which)
            })
        }
        dialog = dialogBuilder.create()
        dialog.show()
        togglePositiveButton()
    }

    /**
     * adds the days from the selectedDays to the TabLayout
     */
    private fun addTabsToTabLayout(selectedDays: MutableList<Int>) {
        selectedDays.sort()
        val tabLayout = binding.weekDayTabLayout
        for (weekDayIndex in selectedDays) {
            val tab = tabLayout.newTab().setText(DayOfWeek.of(weekDayIndex + 1).toString()) //the +1 since DayOfWeek starts at 1
            tabLayout.addTab(tab)
        }
    }

    /**
     * sets up the TabLayout in the ui so it works properly
     */
    private fun setUpTabLayout() {
        binding.weekDayTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { viewModel.setNewWeekDay(it.position) } //doing this in the fragment since there is no tabSelected dataBinding adapter
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    /**
     * sets up the BlockExerciseView so the BlockExercise gets changed on Swiping
     */
    private fun setUpExerciseSwiping() {
        val gestureListener = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                if (velocityX.absoluteValue > 2 * velocityY.absoluteValue) { //the swipe should only register if it is more horizontal than vertical
                    if (e1!!.x < e2!!.x) { //swipe to the right
                        viewModel.previousBlockExercise()
                    } else {
                        viewModel.nextBlockExercise()
                    }
                    return false
                }
                return true
            }
        })

        binding.blockExerciseView.setOnTouchListener { view, motionEvent -> gestureListener.onTouchEvent(motionEvent) }
    }

    /**
     * sets up the blockSaveBtn so the block in this fragment can be saved to the database
     */
    private fun setUpBlockSaveButton() {
        //add a name for the block and decide whether it is a development Block or not
        val layout = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
        val developmentButton = CheckBox(context).apply { this.setText("Is a Development Block") }
        layout.addView(developmentButton)
        val blockNameEdt = EditText(context)
        layout.addView(blockNameEdt)

        val alertDialog = AlertDialog.Builder(context).apply {
            setPositiveButton("OK", ({ dialog, index ->
                val blockName = blockNameEdt.text.toString()
                val isDevelopment = developmentButton.isChecked
                viewModel.saveBlock(blockName, isDevelopment)
            }))
        }
        alertDialog.setView(layout)

        binding.blockSaveBtn.setOnClickListener { alertDialog.show() }
    }

    /**
     * observes the ViewModel so if a block is saved we can navigate back to the StartFragment
     */
    private fun observeBlockSaved() {
        viewModel.blockSaved.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(R.id.action_blockFragment_to_startFragment)
                viewModel.resetBlockSaved()
                Toast.makeText(context, "The Block was succesfully saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

