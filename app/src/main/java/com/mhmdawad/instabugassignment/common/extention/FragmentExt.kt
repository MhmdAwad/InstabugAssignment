package com.mhmdawad.instabugassignment.common.extention

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mhmdawad.instabugassignment.MainActivity
import com.mhmdawad.instabugassignment.R


fun Fragment.openFragment(fragment: Fragment) {
    requireActivity().supportFragmentManager.beginTransaction().apply {
        setCustomAnimations(
            R.anim.slide_in_bottom, R.anim.slide_out_top,
            R.anim.slide_in_top, R.anim.slide_out_bottom
        )
        replace(R.id.fragmentContainer, fragment)
        addToBackStack(fragment.tag)
        commit()
    }
}

fun Fragment.getRepositoryClass() =
    (requireActivity() as MainActivity).getRepositoryClass()

fun Fragment.isInternetAvailable(): Boolean {
    val result: Boolean
    val connectivityManager =
        requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val network =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        network.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

fun Fragment.changeActionbar(enable: Boolean = true, title: String) {
    (requireActivity() as AppCompatActivity).supportActionBar?.let {
        it.setDisplayHomeAsUpEnabled(enable)
        it.setHomeButtonEnabled(enable)
        it.title = title
    }

}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}
