package com.mhmdawad.instabugassignment.domain.repository

import com.mhmdawad.instabugassignment.common.Resource
import com.mhmdawad.instabugassignment.domain.model.MapModel
import com.mhmdawad.instabugassignment.domain.model.ResponseModel

interface ResponseRepository {

    fun makePostApiRequest(
        request: String,
        requestBody: String,
        headers: List<MapModel>
    ): Resource<ResponseModel>

    fun makeGetApiRequest(
        request: String,
        headers: List<MapModel>
    ): Resource<ResponseModel>

    fun insertResponseToDb(responseModel: ResponseModel)
    fun deleteResponseFromDb(executionTime: String)
    fun getAllResponsesFromDb(): Resource<List<ResponseModel>>
    fun getFilterResponse(filterType: String): Resource<List<ResponseModel>>
}