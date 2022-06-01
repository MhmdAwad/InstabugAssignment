package com.mhmdawad.instabugassignment

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mhmdawad.instabugassignment.common.extention.dismissAlertDialog
import com.mhmdawad.instabugassignment.common.extention.dismissFilterDialog
import com.mhmdawad.instabugassignment.common.extention.dismissLoadingDialog
import com.mhmdawad.instabugassignment.data.local.ResponseDatabase
import com.mhmdawad.instabugassignment.data.repository.ResponseRepositoryImpl
import com.mhmdawad.instabugassignment.presentation.local_request.LocalRequestFragment

class MainActivity : AppCompatActivity() {

    private val _repositoryImpl = ResponseRepositoryImpl(
        ResponseDatabase(this)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LocalRequestFragment()).commit()
        }
    }

    fun getRepositoryClass() = _repositoryImpl

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStackImmediate()
        }else{
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == android.R.id.home){
            onBackPressed()
            true
        }else
            super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissAlertDialog()
        dismissLoadingDialog()
        dismissFilterDialog()
    }
}