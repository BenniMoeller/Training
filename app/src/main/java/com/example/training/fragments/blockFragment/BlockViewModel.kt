package com.example.training.fragments.blockFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.blockData.BlockSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*


/**
 * viewmodel for the BlockFragment
 */
internal class BlockViewModel(private val database: DatabaseDao, application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var startDate: Date = Calendar.getInstance().time //the startDate of the Block
    val currentBlockSets = MutableLiveData<List<BlockSet>>(mutableListOf())

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}