package com.mhmdawad.instabugassignment.domain.model

import android.content.ContentValues
import android.os.Parcelable
import com.mhmdawad.instabugassignment.common.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseModel(
    val executionTime: String = "",
    val code: String = "",
    val requestBody: String = "",
    val responseBody: String = "",
    val requestHeader: String = "",
    val responseHeader: String = "",
    val parameterQuery: String = "",
    val responseType: String = "",
    val requestURL: String = ""
): Parcelable

fun ResponseModel.toContentValues(): ContentValues {
    val values = ContentValues()
    values.put(Constants.RESPONSE_EXECUTION_TIME, executionTime)
    values.put(Constants.RESPONSE_CODE, code)
    values.put(Constants.RESPONSE_BODY, responseBody)
    values.put(Constants.REQUEST_BODY, requestBody)
    values.put(Constants.REQUEST_HEADER, requestHeader)
    values.put(Constants.RESPONSE_HEADER, responseHeader)
    values.put(Constants.PARAMETER_QUERY, parameterQuery)
    values.put(Constants.RESPONSE_TYPE, responseType)
    values.put(Constants.REQUEST_URL, requestURL)
    return values
}

