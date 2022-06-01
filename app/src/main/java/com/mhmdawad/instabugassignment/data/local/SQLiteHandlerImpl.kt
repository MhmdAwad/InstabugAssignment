package com.mhmdawad.instabugassignment.data.local

import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log
import com.mhmdawad.instabugassignment.common.Constants.PARAMETER_QUERY
import com.mhmdawad.instabugassignment.common.Constants.REQUEST_BODY
import com.mhmdawad.instabugassignment.common.Constants.REQUEST_HEADER
import com.mhmdawad.instabugassignment.common.Constants.REQUEST_URL
import com.mhmdawad.instabugassignment.common.Constants.RESPONSE_BODY
import com.mhmdawad.instabugassignment.common.Constants.RESPONSE_CODE
import com.mhmdawad.instabugassignment.common.Constants.RESPONSE_EXECUTION_TIME
import com.mhmdawad.instabugassignment.common.Constants.RESPONSE_HEADER
import com.mhmdawad.instabugassignment.common.Constants.RESPONSE_TYPE
import com.mhmdawad.instabugassignment.common.Constants.TABLE_NAME
import com.mhmdawad.instabugassignment.data.local.ResponseDatabase.Companion.SQL_CREATE_ENTRIES
import com.mhmdawad.instabugassignment.domain.model.ResponseModel
import com.mhmdawad.instabugassignment.domain.model.toContentValues

class SQLiteHandlerImpl(
    private val responseDatabase: ResponseDatabase
) : SQLiteHandler {

    private val db by lazy { responseDatabase.writableDatabase }

    override fun insertResponse(responseModel: ResponseModel) {
        try {
            db.insert(TABLE_NAME, null, responseModel.toContentValues())
        } catch (e: Exception) {
            Log.d(this::class.java.name, "error: ${e.localizedMessage}")
        }
    }

    override fun deleteResponse(executionTime: String) {
        try {
            val selection = "$RESPONSE_EXECUTION_TIME LIKE ?"
            db.delete(TABLE_NAME, selection, arrayOf(executionTime))
        } catch (e: Exception) {
            Log.d(this::class.java.name, "error: ${e.localizedMessage}")
        }
    }

    override fun getAllResponses(): List<ResponseModel> {
        var cursor: Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
            getResponses(cursor)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            emptyList()
        }finally {
            cursor?.close()
        }
    }

    override fun getResponseByRequestType(filterType: String): List<ResponseModel> {
        var cursor: Cursor? = null
        return try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $RESPONSE_TYPE = ?", arrayOf(filterType))
            getResponses(cursor)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            emptyList()
        }finally {
            cursor?.close()
        }
    }

    private fun getResponses(cursor: Cursor): List<ResponseModel>{
        val responseList = mutableListOf<ResponseModel>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                responseList.add(getResponseFromCursor(cursor))
                cursor.moveToNext()
            }
            cursor.close()
        }
        return responseList
    }

    private fun getResponseFromCursor(cursor: Cursor): ResponseModel {
        return ResponseModel(
            cursor.getString(cursor.getColumnIndexOrThrow(RESPONSE_EXECUTION_TIME)),
            cursor.getString(cursor.getColumnIndexOrThrow(RESPONSE_CODE)),
            cursor.getString(cursor.getColumnIndexOrThrow(REQUEST_BODY)),
            cursor.getString(cursor.getColumnIndexOrThrow(RESPONSE_BODY)),
            cursor.getString(cursor.getColumnIndexOrThrow(REQUEST_HEADER)),
            cursor.getString(cursor.getColumnIndexOrThrow(RESPONSE_HEADER)),
            cursor.getString(cursor.getColumnIndexOrThrow(PARAMETER_QUERY)),
            cursor.getString(cursor.getColumnIndexOrThrow(RESPONSE_TYPE)),
            cursor.getString(cursor.getColumnIndexOrThrow(REQUEST_URL)),
        )
    }

}