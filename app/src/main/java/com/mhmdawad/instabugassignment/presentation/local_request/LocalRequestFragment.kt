package com.mhmdawad.instabugassignment.presentation.local_request

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.mhmdawad.instabugassignment.R
import com.mhmdawad.instabugassignment.common.Constants
import com.mhmdawad.instabugassignment.common.Resource
import com.mhmdawad.instabugassignment.common.ViewModelFactory
import com.mhmdawad.instabugassignment.common.enums.DialogType
import com.mhmdawad.instabugassignment.common.extention.*
import com.mhmdawad.instabugassignment.databinding.FragmentLocalRequestBinding
import com.mhmdawad.instabugassignment.domain.model.ResponseModel
import com.mhmdawad.instabugassignment.presentation.new_request.NewRequestFragment
import com.mhmdawad.instabugassignment.presentation.request_detail.RequestDetailFragment


class LocalRequestFragment : Fragment(R.layout.fragment_local_request){

    private lateinit var _binding: FragmentLocalRequestBinding
    private lateinit var _viewModel: LocalRequestViewModel
    private val _localRequestAdapter by lazy { LocalRequestAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocalRequestBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this, ViewModelFactory(getRepositoryClass())).get(
            LocalRequestViewModel::class.java
        )
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        changeActionbar(false, Constants.HOME)
        _viewModel.getFilterResponse()
        initViews()
        observeListener()
    }

    private fun observeListener() {
        _viewModel.responseData.observe(viewLifecycleOwner) { result ->
            when (result.getContentIfNotHandled()) {
                is Resource.Success -> {
                    result.peekContent().data?.let {
                        _localRequestAdapter.responseList = it
                    }
                    changeLocalRequestsVisibility()
                    dismissLoadingDialog()
                }
                is Resource.Loading -> {
                    requireActivity().showLoadingDialog()
                }
                is Resource.Error -> {
                    changeLocalRequestsVisibility(false)
                    dismissLoadingDialog()
                }
                else -> dismissLoadingDialog()
            }
        }

        _viewModel.filterDialog.observe(viewLifecycleOwner) {
            if (it == DialogType.SHOW) {
                showFilterDialog()
            } else {
                dismissFilterDialog()
            }
        }
    }

    private fun showFilterDialog() {
        requireActivity().showFilterDialog(lastFilter = _viewModel.lastFilter,
            filter = {
                _viewModel.getFilterResponse(it)
            }, dismiss = {
                _viewModel.dismissFilterDialog()
            })
    }

    private fun changeLocalRequestsVisibility(showLocalRequests: Boolean = true) = with(_binding) {
        if (showLocalRequests) {
            localRV.show()
            noLocalRequestsLayout.root.hide()
        } else {
            localRV.hide()
            noLocalRequestsLayout.root.show()
        }
    }

    private fun initViews() {
        _binding.apply {
            createNewRequest.setOnClickListener {
                openFragment(NewRequestFragment())
            }
            localRV.adapter = _localRequestAdapter
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(localRV)
            _localRequestAdapter.scrollRecyclerViewListener {
                _binding.localRV.scrollToPosition(0)
            }
            _localRequestAdapter.openResponseDetails {
                openResponseDetailsFragments(it)
            }
        }
    }


    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val time = _localRequestAdapter.responseList[position].executionTime
            _viewModel.deleteLocalRequest(time)
        }
    }
    
    private fun openResponseDetailsFragments(responseModel: ResponseModel) {
        RequestDetailFragment().let {
            Bundle().let { bundle ->
                bundle.putParcelable(Constants.RESPONSE_ARGS, responseModel)
                it.arguments = bundle
            }
            openFragment(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actionbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter) {
            _viewModel.showFilterDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}


