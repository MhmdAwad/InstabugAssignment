package com.mhmdawad.instabugassignment.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mhmdawad.instabugassignment.domain.repository.ResponseRepository
import com.mhmdawad.instabugassignment.presentation.local_request.LocalRequestViewModel
import com.mhmdawad.instabugassignment.presentation.new_request.NewRequestViewModel

class ViewModelFactory(private val repository: ResponseRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocalRequestViewModel::class.java)) {
            return LocalRequestViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(NewRequestViewModel::class.java)) {
            return NewRequestViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
