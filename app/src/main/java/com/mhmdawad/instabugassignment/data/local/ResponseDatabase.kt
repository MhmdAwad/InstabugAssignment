package com.mhmdawad.instabugassignment.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mhmdawad.instabugassignment.common.Constants

class ResponseDatabase(context: Context) :
    SQLiteOpenHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION)  {

    companion object {

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Constants.TABLE_NAME + " (" +
                    Constants.RESPONSE_EXECUTION_TIME + " TEXT PRIMARY KEY," +
                    Constants.RESPONSE_CODE + " TEXT," +
                    Constants.RESPONSE_BODY + " TEXT," +
                    Constants.REQUEST_BODY + " TEXT," +
                    Constants.REQUEST_HEADER + " TEXT," +
                    Constants.RESPONSE_HEADER + " TEXT," +
                    Constants.PARAMETER_QUERY + " TEXT," +
                    Constants.RESPONSE_TYPE + " TEXT," +
                    Constants.REQUEST_URL + " TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${Constants.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}