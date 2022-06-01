package com.mhmdawad.instabugassignment.presentation.new_request

import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhmdawad.instabugassignment.common.Event
import com.mhmdawad.instabugassignment.common.Resource
import com.mhmdawad.instabugassignment.common.enums.DialogType
import com.mhmdawad.instabugassignment.common.enums.RequestType
import com.mhmdawad.instabugassignment.domain.model.MapModel
import com.mhmdawad.instabugassignment.domain.model.ResponseModel
import com.mhmdawad.instabugassignment.domain.repository.ResponseRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NewRequestViewModel(
    private val _repository: ResponseRepository
) : ViewModel() {

    private val _executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val _requestStatus = MutableLiveData<Event<Resource<ResponseModel>>>()
    val requestStatus: LiveData<Event<Resource<ResponseModel>>> = _requestStatus

    private val _requestPostHeader = MutableLiveData(DialogType.DISMISS)
    val requestPostHeader: LiveData<DialogType> = _requestPostHeader
    fun dismissHeaderDialog() {
        _requestPostHeader.value = DialogType.DISMISS
    }

    fun makeNewRequest(
        request: String,
        requestType: RequestType,
        requestBody: String,
        headersList: List<MapModel> = emptyList(),
        internetAvailable: Boolean,
        checkHeaders: Boolean = true
    ) {
        if (!internetAvailable) {
            _requestStatus.value = Event(Resource.Error("Please check your internet connection!"))
            return
        }
        if (request.isBlank() || !URLUtil.isValidUrl(request)) {
            _requestStatus.value = Event(Resource.Error("Please add a valid URL!"))
            return
        }

        if (checkHeaders && !checkHeadersNotEmpty(headersList)) {
            return
        }

        _requestStatus.value = Event(Resource.Loading())

        if (requestType == RequestType.GET) {
            makeGetApiRequest(request.trim(), headersList)
        } else
            makePostApiRequest(request.trim(), requestBody, headersList)
    }


    private fun makePostApiRequest(
        request: String,
        requestBody: String,
        headersList: List<MapModel>
    ) {
        _requestPostHeader.postValue(DialogType.DISMISS)
        _executor.execute {
            _requestStatus.postValue(
                Event(
                    _repository.makePostApiRequest(
                        request,
                        requestBody,
                        headersList
                    )
                )
            )
        }
    }


    private fun makeGetApiRequest(request: String, headersList: List<MapModel>) {
        _executor.execute {
            _requestStatus.postValue(
                Event(
                    _repository.makeGetApiRequest(
                        request,
                        headersList
                    )
                )
            )
        }
    }

    private fun checkHeadersNotEmpty(headersList: List<MapModel>): Boolean {
        if (headersList.isEmpty()) {
            _requestStatus.postValue(Event(Resource.Idle()))
            _requestPostHeader.postValue(DialogType.SHOW)
            return false
        }
        return true
    }
}