package com.mhmdawad.instabugassignment.common.extention

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.Window
import com.mhmdawad.instabugassignment.R
import com.mhmdawad.instabugassignment.common.Constants
import com.mhmdawad.instabugassignment.common.enums.RequestType
import com.mhmdawad.instabugassignment.databinding.FilterDialogLayoutBinding


private var loadingDialog: Dialog? = null
private var filterDialog: Dialog? = null
private var alertDialog: AlertDialog? = null



fun Activity.showLoadingDialog() {
    if (loadingDialog == null) {
        loadingDialog = Dialog(this)
        loadingDialog?.let {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            it.setCancelable(false)
            it.setContentView(R.layout.loading_dialog_layout)
        }
    }

    dismissLoadingDialog()
    loadingDialog?.show()
}

fun dismissLoadingDialog() {
    if (loadingDialog != null && loadingDialog!!.isShowing) {
        loadingDialog!!.dismiss()
    }
}


fun Activity.showAlertDialog(
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    alertDialog = AlertDialog.Builder(this)
        .setTitle("No Headers")
        .setMessage("Are you sure you want to continue without headers?")
        .setCancelable(false)
        .setPositiveButton(Constants.CONTINUE) { _, _ ->
            onSubmit()
            onDismiss()
        }.setNegativeButton(android.R.string.cancel) { _, _ ->
            onDismiss()
        }.show()
}

fun dismissAlertDialog() {
    alertDialog?.dismiss()
}

fun Activity.showFilterDialog(
    lastFilter: RequestType,
    filter: (RequestType) -> Unit,
    dismiss: () -> Unit,
) {
    val binding = FilterDialogLayoutBinding.inflate(LayoutInflater.from(this))
    filterDialog = Dialog(this)
    filterDialog?.let {
        it.requestWindowFeature(Window.FEATURE_NO_TITLE)
        it.setCancelable(true)
        it.setContentView(binding.root)

        when (lastFilter) {
            RequestType.ALL -> binding.filterAll.isChecked = true
            RequestType.GET -> binding.filterGet.isChecked = true
            RequestType.POST -> binding.filterPost.isChecked = true
        }

        it.setOnCancelListener {
            dismiss()
        }

        binding.filterAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filter(RequestType.ALL)
                dismiss()
            }
        }
        binding.filterGet.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filter(RequestType.GET)
                dismiss()
            }
        }
        binding.filterPost.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filter(RequestType.POST)
                dismiss()
            }
        }
        it.show()
    }
}

fun dismissFilterDialog(){
    filterDialog?.dismiss()
}
