package com.mhmdawad.instabugassignment.presentation.local_request

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mhmdawad.instabugassignment.R
import com.mhmdawad.instabugassignment.common.extention.toFormattedDate
import com.mhmdawad.instabugassignment.databinding.LocalRequestRvItemBinding
import com.mhmdawad.instabugassignment.domain.model.ResponseModel

class LocalRequestAdapter: RecyclerView.Adapter<LocalRequestAdapter.LocalRequestViewHolder>() {

    private val _differCallback = object : DiffUtil.ItemCallback<ResponseModel>() {
        override fun areItemsTheSame(oldItem: ResponseModel, newItem: ResponseModel): Boolean {
            return oldItem.executionTime == newItem.executionTime
        }

        override fun areContentsTheSame(oldItem: ResponseModel, newItem: ResponseModel): Boolean {
            return oldItem == newItem
        }
    }


    private val _differ = AsyncListDiffer(this, _differCallback)
    var responseList: List<ResponseModel>
        get() = _differ.currentList
        set(value) = _differ.submitList(value){
            onNewItemInserted?.let {
                it()
            }
        }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocalRequestViewHolder =
        LocalRequestViewHolder(
            LocalRequestRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(
        holder: LocalRequestViewHolder,
        position: Int
    ) {
        holder.bind(responseList[position])
    }

    override fun getItemCount() = responseList.size
    inner class LocalRequestViewHolder(private val binding: LocalRequestRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(responseModel: ResponseModel) = with(binding) {
            requestURL.text = responseModel.requestURL
            responseType.text = root.context.getString(
                R.string.responseType,
                responseModel.responseType,
                responseModel.code
            )
            responseDate.text = responseModel.executionTime.toFormattedDate()
            root.setOnClickListener {
                onItemClick?.let {
                    it(responseModel)
                }
            }
        }
    }

    private var onNewItemInserted: (() -> Unit)? = null
    private var onItemClick: ((ResponseModel) -> Unit)? = null
    fun scrollRecyclerViewListener(listener: ()-> Unit){
        onNewItemInserted = listener
    }
    fun openResponseDetails(listener: (ResponseModel)-> Unit){
        onItemClick = listener
    }
}