package com.mhmdawad.instabugassignment.presentation.local_request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhmdawad.instabugassignment.common.Event
import com.mhmdawad.instabugassignment.common.Resource
import com.mhmdawad.instabugassignment.common.enums.DialogType
import com.mhmdawad.instabugassignment.common.enums.RequestType
import com.mhmdawad.instabugassignment.domain.model.ResponseModel
import com.mhmdawad.instabugassignment.domain.repository.ResponseRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalRequestViewModel(
    private val repository: ResponseRepository
): ViewModel() {

    private var _lastFilter = RequestType.ALL
    val lastFilter get() = _lastFilter

    private val _executor: ExecutorService = Executors.newSingleThreadExecutor()

    private val _filterDialog = MutableLiveData(DialogType.DISMISS)
    val filterDialog: LiveData<DialogType> = _filterDialog
    fun dismissFilterDialog(){ _filterDialog.value = DialogType.DISMISS }
    fun showFilterDialog(){ _filterDialog.value = DialogType.SHOW }

    private val _responseData = MutableLiveData<Event<Resource<List<ResponseModel>>>>()
    val responseData: LiveData<Event<Resource<List<ResponseModel>>>> = _responseData

    private fun showLoading(){
        _responseData.postValue(Event(Resource.Loading()))
    }

    fun getFilterResponse(filterType: RequestType = _lastFilter) {
        _lastFilter = filterType

        _executor.execute {
            showLoading()
            _responseData.postValue(
                Event(
                    repository.getFilterResponse(filterType.name)
                )
            )
        }
    }

    fun deleteLocalRequest(executionTime: String){
        _executor.execute {
            try{
                repository.deleteResponseFromDb(executionTime)
                getFilterResponse()
            }catch (e: Exception){}
        }
    }
}