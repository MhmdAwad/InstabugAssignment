package com.mhmdawad.instabugassignment.common.extention

import com.mhmdawad.instabugassignment.domain.model.MapModel
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


fun listToString(list: List<MapModel>): String {
    val stringBuilder = buildString {
        list.forEach {
            val key = if (it.key.trim().isNotEmpty()) "${it.key} : "
            else it.key
            append("$key${it.value}\n")
        }
    }
    return formatStringToJson(stringBuilder)
}

fun formatStringToJson(text: String): String {
    return try{
        JSONObject(text).toString(4).trim()
    }catch (e: Exception) {
        text.trim()
    }
}


fun String.toFormattedDate(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    return formatter.format(Date(this.toLong()))
}

