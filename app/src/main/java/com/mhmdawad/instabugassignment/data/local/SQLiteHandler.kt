package com.mhmdawad.instabugassignment.data.local

import com.mhmdawad.instabugassignment.domain.model.ResponseModel

interface SQLiteHandler {

    fun insertResponse(responseModel: ResponseModel)
    fun deleteResponse(executionTime: String)
    fun getAllResponses(): List<ResponseModel>
    fun getResponseByRequestType(filterType: String): List<ResponseModel>
}