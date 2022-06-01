package com.mhmdawad.instabugassignment.data.repository

import com.mhmdawad.instabugassignment.common.Resource
import com.mhmdawad.instabugassignment.common.enums.RequestType
import com.mhmdawad.instabugassignment.data.local.ResponseDatabase
import com.mhmdawad.instabugassignment.data.local.SQLiteHandlerImpl
import com.mhmdawad.instabugassignment.data.remote.NetworkingHandlerImpl
import com.mhmdawad.instabugassignment.domain.model.MapModel
import com.mhmdawad.instabugassignment.domain.model.ResponseModel
import com.mhmdawad.instabugassignment.domain.repository.ResponseRepository

class ResponseRepositoryImpl(
    responseDatabase: ResponseDatabase
) : ResponseRepository {
    private val _networkHandler = NetworkingHandlerImpl()
    private val _sqliteHandler = SQLiteHandlerImpl(responseDatabase)

    override fun makePostApiRequest(
        request: String,
        requestBody: String,
        headers: List<MapModel>
    ): Resource<ResponseModel> {
        val result = _networkHandler.makePostApiRequest(request, requestBody, headers)
        if (result.data != null)
            insertResponseToDb(result.data)
        return result
    }

    override fun makeGetApiRequest(
        request: String,
        headers: List<MapModel>
    ): Resource<ResponseModel> {
        val result = _networkHandler.makeGetApiRequest(request, headers)
        if (result.data != null)
            insertResponseToDb(result.data)
        return result
    }

    override fun insertResponseToDb(responseModel: ResponseModel) =
        _sqliteHandler.insertResponse(responseModel)

    override fun deleteResponseFromDb(executionTime: String) =
            _sqliteHandler.deleteResponse(executionTime)

    override fun getAllResponsesFromDb(): Resource<List<ResponseModel>> {
        val result = _sqliteHandler.getAllResponses()
        return if(result.isNotEmpty()){
            Resource.Success(result.sortedByDescending { it.executionTime.toLong() })
        }else{
            Resource.Error("No Requests!")
        }
    }

    override fun getFilterResponse(filterType: String): Resource<List<ResponseModel>> {
        if(filterType == RequestType.ALL.name){
            return getAllResponsesFromDb()
        }
        val result = _sqliteHandler.getResponseByRequestType(filterType)
        return if(result.isNotEmpty()){
            Resource.Success(result.sortedByDescending { it.executionTime.toLong() })
        }else{
            Resource.Error("No Requests!")
        }
    }
}