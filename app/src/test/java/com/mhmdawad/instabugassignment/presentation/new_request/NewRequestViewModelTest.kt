package com.mhmdawad.instabugassignment.presentation.new_request


import com.mhmdawad.instabugassignment.repository.FakeResponseRepository
import org.junit.Assert.*
import org.junit.Test

class NewRequestViewModelTest {

    private val repository = FakeResponseRepository()

    @Test
    fun `Successful GET request with query parameters, return 200 successful code`() {
        val result = repository.makeGetApiRequest(
            request = "https://httpbin.org/image/png?page=1",
            headers = emptyList()
        )
        assertEquals("200", result.data?.code)
    }

    @Test
    fun `Successful GET request without query parameters, return 500 error code`() {
        val result = repository.makeGetApiRequest(
            request = "https://httpbin.org/image/png",
            headers = emptyList()
        )
        assertEquals("500", result.data?.code)
    }

    @Test
    fun `Successful POST request with valid URL, return 200 successful code`() {
        val result = repository.makePostApiRequest(
            request = "https://httpbin.org/image/png",
            headers = emptyList(),
            requestBody = ""
        )
        assertEquals("200", result.data?.code)
    }

    @Test
    fun `Successful POST request with not valid URL, return 404 successful code`() {
        val result = repository.makePostApiRequest(
            request = "org/image/png",
            headers = emptyList(),
            requestBody = ""
        )
        assertEquals("404", result.data?.code)
    }
}