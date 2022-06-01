package com.mhmdawad.instabugassignment.domain.model

import android.os.Parcelable
import com.mhmdawad.instabugassignment.common.extention.listToString
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapModel(
    var key: String="",
    var value: String=""
): Parcelable

fun convertMapToHeaders(map: Map<String, List<String>>): String {
    val headers = mutableListOf<MapModel>()
    for (data in map) {
        if (data.value.isNotEmpty() && data.value[0].isNotBlank()){
            val model = MapModel()
            model.key = data.key ?: ""
            model.value = buildString {
                for(index in data.value.indices){
                    append(data.value[index])
                    if(index != data.value.size-1){
                        append(", ")
                    }
                }
            }
            headers.add(model)
        }
    }
    return listToString(headers)
}