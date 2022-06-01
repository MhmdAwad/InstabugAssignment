package com.mhmdawad.instabugassignment.presentation.new_request

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.mhmdawad.instabugassignment.R
import com.mhmdawad.instabugassignment.common.Constants.BLANK
import com.mhmdawad.instabugassignment.common.extention.hide
import com.mhmdawad.instabugassignment.common.extention.show
import com.mhmdawad.instabugassignment.databinding.HeadersRvItemBinding
import com.mhmdawad.instabugassignment.domain.model.MapModel

class NewRequestHeadersAdapter :
    RecyclerView.Adapter<NewRequestHeadersAdapter.NewRequestViewHolder>() {

    private val _headersList = mutableListOf(MapModel())
    val headersList get() = _headersList.filter { it.value.isNotBlank() }

    fun addNewHeader() {
        _headersList.add(MapModel())
        notifyItemInserted(_headersList.size - 1)
    }
    private fun removeSpecificHeader(mapModel: MapModel, position: Int) {
        _headersList.remove(mapModel)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewRequestHeadersAdapter.NewRequestViewHolder =
        NewRequestViewHolder(
            HeadersRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(
        holder: NewRequestHeadersAdapter.NewRequestViewHolder,
        position: Int
    ) {
        holder.bind(_headersList[position])
    }

    override fun getItemCount(): Int = _headersList.size

    inner class NewRequestViewHolder(private val binding: HeadersRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mapModel: MapModel) = with(binding) {

            fillHeadersEditText(mapModel)
            if (adapterPosition == 0)
                removeHeader.hide()
            else {
                removeHeader.show()
                removeHeader.setOnClickListener {
                    removeSpecificHeader(mapModel, adapterPosition)
                }
            }
            editTextListeners(this)
        }

        private fun fillHeadersEditText(mapModel: MapModel) = with(binding){
            headerKey.nonRequiredInputEditText.let {
                if (mapModel.key.isNotBlank())
                    it.setText(mapModel.key)
                else
                    it.setText(BLANK)
            }
            headerValue.nonRequiredInputEditText.let{
                if (mapModel.value.isNotBlank())
                    it.setText(mapModel.value)
                else
                    it.setText(BLANK)
            }
            headerValue.nonRequiredTextInput.hint = root.context.getString(R.string.value)
            headerKey.nonRequiredTextInput.hint = root.context.getString(R.string.key)
        }

        private fun editTextListeners(binding: HeadersRvItemBinding) = with(binding) {
            headerKey.nonRequiredInputEditText.let {
                it.doAfterTextChanged { _ ->
                    _headersList[adapterPosition].key = it.text.toString()
                }
            }
            headerValue.nonRequiredInputEditText.let {
                it.doAfterTextChanged { _ ->
                    _headersList[adapterPosition].value = it.text.toString()
                }
            }
        }
    }
}