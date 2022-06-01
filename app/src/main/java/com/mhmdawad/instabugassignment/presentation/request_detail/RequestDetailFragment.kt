package com.mhmdawad.instabugassignment.presentation.request_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mhmdawad.instabugassignment.R
import com.mhmdawad.instabugassignment.common.Constants
import com.mhmdawad.instabugassignment.common.extention.*
import com.mhmdawad.instabugassignment.databinding.FragmentRequestDetailBinding
import com.mhmdawad.instabugassignment.databinding.ResponseRvItemBinding
import com.mhmdawad.instabugassignment.domain.model.ResponseModel


class RequestDetailFragment : Fragment(R.layout.fragment_request_detail) {

    private lateinit var _binding: FragmentRequestDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestDetailBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeActionbar(title = Constants.REQUEST_DETAILS)
        val responseModel: ResponseModel? = arguments?.getParcelable(Constants.RESPONSE_ARGS)
        responseModel?.let { initViews(it) }
    }

    private fun initViews(responseModel: ResponseModel) {
        _binding.run {
            responseCodeLayout.let {
                if (responseModel.code.isNotBlank()) {
                    fillView(it, getString(R.string.response_code), responseModel.code)
                }
            }
            requestBodyLayout.let {
                if (responseModel.requestBody.length > 2) {
                    fillView(it, getString(R.string.request_body), responseModel.requestBody)
                }
            }
            responseBodyLayout.let {
                if (responseModel.responseBody.length > 2) {
                    fillView(
                        it,
                        getString(R.string.response_body),
                        formatStringToJson(responseModel.responseBody)
                    )
                }
            }
            requestHeaderLayout.let {
                if (responseModel.requestHeader.isNotEmpty()) {
                    fillView(
                        it,
                        getString(R.string.request_header),
                        responseModel.requestHeader
                    )
                }
            }
            responseHeaderLayout.let {
                if (responseModel.responseHeader.isNotEmpty()) {
                    fillView(
                        it,
                        getString(R.string.response_header),
                        responseModel.responseHeader
                    )
                }
            }
            parameterQueryLayout.let {
                if (responseModel.parameterQuery.isNotEmpty()) {
                    fillView(
                        it,
                        getString(R.string.queries),
                        responseModel.parameterQuery
                    )
                }
            }
        }
    }

    private fun fillView(item: ResponseRvItemBinding, title: String, data: String) {
        item.let {
            it.root.show()
            it.responseTitle.text = title
            it.responseValue.text = data
            it.copyValue.copyToClipBoard(it.responseValue.text.toString()) {
                showToast(getString(R.string.copiedSuccessfully))
            }
        }
    }
}