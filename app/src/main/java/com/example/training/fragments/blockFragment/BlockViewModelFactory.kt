package com.example.training.fragments.blockFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.training.database.DatabaseDao


/**
 * viewModelFactory for the BlockViewModel
 */
internal class BlockViewModelFactory(private val dataSource: DatabaseDao, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_ cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockViewModel::class.java)) {
            return BlockViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}