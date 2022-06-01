package com.mhmdawad.instabugassignment.data.remote

import android.net.Uri
import com.mhmdawad.instabugassignment.common.extention.listToString
import com.mhmdawad.instabugassignment.domain.model.MapModel
import com.mhmdawad.instabugassignment.domain.model.ResponseModel
import com.mhmdawad.instabugassignment.domain.model.convertMapToHeaders
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection

class NetworkingHelper {

    private fun checkIfParamExist(request: String): String {
        val uri = Uri.parse(request)
        val queryList = mutableListOf<MapModel>()
        for (key in uri.queryParameterNames) {
            if (key != null) {
                uri.getQueryParameter(key)?.let { value ->
                    if (key.isNotBlank() && value.isNotBlank())
                        queryList.add(MapModel(key, value))
                }
            }
        }
        return listToString(queryList)
    }

    fun createResponseModel(http: HttpURLConnection, request: String, headers: List<MapModel>, requestBody: String) =
        ResponseModel(
            executionTime = http.date.toString(),
            code = http.responseCode.toString(),
            requestHeader = listToString(headers),
            responseHeader = convertMapToHeaders(http.headerFields),
            requestBody = requestBody,
            responseBody = getResponseBody(http),
            parameterQuery = checkIfParamExist(request),
            responseType = http.requestMethod,
            requestURL = request
        )

    private fun getResponseBody(connection: HttpURLConnection): String {
        val bufferedReader = if (connection.responseCode in 100..399)
            BufferedReader(InputStreamReader(connection.inputStream))
        else
            BufferedReader(InputStreamReader(connection.errorStream))

        val jsonString: StringBuilder = StringBuilder()
        while (true) {
            val readLine = bufferedReader.readLine() ?: break
            jsonString.append(readLine)
        }

        return jsonString.toString()
    }

}