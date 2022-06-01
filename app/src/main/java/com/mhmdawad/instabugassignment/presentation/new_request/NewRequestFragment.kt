package com.mhmdawad.instabugassignment.presentation.new_request

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mhmdawad.instabugassignment.R
import com.mhmdawad.instabugassignment.common.Constants
import com.mhmdawad.instabugassignment.common.Resource
import com.mhmdawad.instabugassignment.common.ViewModelFactory
import com.mhmdawad.instabugassignment.common.enums.DialogType
import com.mhmdawad.instabugassignment.common.enums.RequestType
import com.mhmdawad.instabugassignment.common.extention.*
import com.mhmdawad.instabugassignment.databinding.FragmentNewRequestBinding
import com.mhmdawad.instabugassignment.domain.model.ResponseModel
import com.mhmdawad.instabugassignment.presentation.request_detail.RequestDetailFragment


class NewRequestFragment : Fragment(R.layout.fragment_new_request),
    CompoundButton.OnCheckedChangeListener {

    private lateinit var _binding: FragmentNewRequestBinding
    private val _headersAdapter by lazy { NewRequestHeadersAdapter() }
    private var _requestType: RequestType = RequestType.GET
    private lateinit var _viewModel: NewRequestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewRequestBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(
            this,
            ViewModelFactory(getRepositoryClass())
        ).get(NewRequestViewModel::class.java)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeActionbar(title = Constants.NEW_REQUEST)
        initViews()
        observeListener()
    }

    private fun observeListener() {
        _viewModel.requestStatus.observe(viewLifecycleOwner) { event ->
            when (event.peekContent()) {
                is Resource.Success -> {
                    if(!event.hasBeenHandled) {
                        dismissLoadingDialog()
                        event.getContentIfNotHandled()?.data?.let { data ->
                            showResponseData(data)
                        }
                    }
                }
                is Resource.Error -> {
                    dismissLoadingDialog()
                    event.peekContent().msg?.let { msg ->
                        showToast(msg)
                    }
                }
                is Resource.Loading-> requireActivity().showLoadingDialog()
                is Resource.Idle -> dismissLoadingDialog()
            }
        }

        _viewModel.requestPostHeader.observe(viewLifecycleOwner) { status ->
            if (status == DialogType.SHOW) {
                requireActivity().showAlertDialog({
                    makeNewRequest(false)
                },{
                    _viewModel.dismissHeaderDialog()
                })
            }
        }
    }

    private fun showResponseData(data: ResponseModel) {
        RequestDetailFragment().let {
            Bundle().let { bundle ->
                bundle.putParcelable(Constants.RESPONSE_ARGS, data)
                it.arguments = bundle
            }
            openFragment(it)
        }
    }

    private fun initViews() {
        _binding.apply {
            postRequest.setOnCheckedChangeListener(this@NewRequestFragment)
            getRequest.setOnCheckedChangeListener(this@NewRequestFragment)
            requestBodyEditText.nonRequiredTextInput.hint = getString(R.string.request_body)
            requestBodyEditText.nonRequiredInputEditText.hint =
                getString(R.string.request_body_example)
            requestBodyEditText.nonRequiredInputEditText.apply {
                gravity = Gravity.START or Gravity.TOP
                minLines = 5
            }
            headersRV.adapter = _headersAdapter
            addNewHeader.setOnClickListener {
                _headersAdapter.addNewHeader()
            }
            makeRequest.setOnClickListener {
                makeNewRequest()
            }
        }
    }

    private fun makeNewRequest(checkHeaders: Boolean = true) = with(_binding) {
        val request = requestUrlLayout.requiredInputEditText.text.toString().trim()
        val requestBody =
            requestBodyEditText.nonRequiredInputEditText.text.toString().trim()
        _viewModel.makeNewRequest(
            request,
            _requestType,
            requestBody,
            _headersAdapter.headersList,
            isInternetAvailable(),
            checkHeaders = checkHeaders
        )
    }

    override fun onCheckedChanged(radioButton: CompoundButton?, isChecked: Boolean) {
        when (radioButton?.id) {
            R.id.getRequest -> {
                if (radioButton.isChecked) {
                    _binding.requestBodyLayout.hide()
                    _requestType = RequestType.GET
                }
            }
            R.id.postRequest -> {
                if (radioButton.isChecked) {
                    _binding.requestBodyLayout.show()
                    _requestType = RequestType.POST
                }
            }
        }
    }
}