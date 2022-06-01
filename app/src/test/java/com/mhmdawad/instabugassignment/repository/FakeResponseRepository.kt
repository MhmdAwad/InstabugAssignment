package com.mhmdawad.instabugassignment.repository

import android.webkit.URLUtil
import com.mhmdawad.instabugassignment.common.Resource
import com.mhmdawad.instabugassignment.domain.model.MapModel
import com.mhmdawad.instabugassignment.domain.model.ResponseModel
import com.mhmdawad.instabugassignment.domain.repository.ResponseRepository

class FakeResponseRepository : ResponseRepository {

    override fun makePostApiRequest(
        request: String,
        requestBody: String,
        headers: List<MapModel>
    ): Resource<ResponseModel> {
        return if (request.startsWith("http"))
            Resource.Success(ResponseModel(code = "200"))
        else
            Resource.Success(ResponseModel(code = "404"))
    }

    override fun makeGetApiRequest(
        request: String,
        headers: List<MapModel>
    ): Resource<ResponseModel> {
        return if (request.contains("?")) {
            Resource.Success(ResponseModel(code = "200"))
        } else
            Resource.Success(ResponseModel(code = "500"))
    }

    override fun insertResponseToDb(responseModel: ResponseModel) {}

    override fun deleteResponseFromDb(executionTime: String) {}

    override fun getAllResponsesFromDb(): Resource<List<ResponseModel>> {
        TODO("Not yet implemented")
    }

    override fun getFilterResponse(filterType: String): Resource<List<ResponseModel>> {
        TODO("Not yet implemented")
    }
}