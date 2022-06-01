package com.mhmdawad.instabugassignment.data.remote

import com.mhmdawad.instabugassignment.common.Constants
import com.mhmdawad.instabugassignment.common.Resource
import com.mhmdawad.instabugassignment.common.enums.RequestType
import com.mhmdawad.instabugassignment.domain.model.MapModel
import com.mhmdawad.instabugassignment.domain.model.ResponseModel
import java.io.DataOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NetworkingHandlerImpl: NetworkingHandler {

    private lateinit var httpURLConnection: HttpURLConnection
    private val networkingHelper by lazy { NetworkingHelper() }

    override fun makePostApiRequest(
        request: String,
        requestBody: String,
        headers: List<MapModel>
    ): Resource<ResponseModel> {

        try {
            val requestData: ByteArray = requestBody.encodeToByteArray()
            createNewConnection(request)

            return httpURLConnection.let {
                it.doOutput = true
                it.requestMethod = RequestType.POST.name

                headers.forEach { header ->
                    if (header.key.isNotBlank() && header.value.isNotBlank())
                        it.setRequestProperty(header.key, header.value)
                }

                DataOutputStream(it.outputStream).use { wr -> wr.write(requestData) }
                Resource.Success(
                    networkingHelper.createResponseModel(
                        http = it,
                        request = request,
                        headers = headers,
                        requestBody = requestBody
                    )
                )
            }
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage ?: Constants.ERROR_MESSAGE)
        } finally {
            closeConnection()
        }
    }


    override fun makeGetApiRequest(request: String, headers: List<MapModel>): Resource<ResponseModel> {
        return try {
            createNewConnection(request)
            Resource.Success(
                networkingHelper.createResponseModel(
                    http = httpURLConnection,
                    request = request,
                    headers = headers,
                    requestBody = ""
                )
            )
        } catch (ioexception: IOException) {
            return Resource.Error(ioexception.localizedMessage ?: Constants.ERROR_MESSAGE)
        } finally {
            closeConnection()
        }
    }


    private fun createNewConnection(request: String) {
        val url = URL(request)
        httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.connectTimeout = 5000
        httpURLConnection.readTimeout = 5000
    }

    private fun closeConnection() {
        if (this::httpURLConnection.isInitialized)
            httpURLConnection.disconnect()
    }
}