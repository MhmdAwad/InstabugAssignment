package com.mhmdawad.instabugassignment.data.remote

import com.mhmdawad.instabugassignment.common.Resource
import com.mhmdawad.instabugassignment.domain.model.MapModel
import com.mhmdawad.instabugassignment.domain.model.ResponseModel

interface NetworkingHandler {

    fun makePostApiRequest(
        request: String,
        requestBody: String,
        headers: List<MapModel>
    ): Resource<ResponseModel>

    fun makeGetApiRequest(request: String, headers: List<MapModel>): Resource<ResponseModel>
}