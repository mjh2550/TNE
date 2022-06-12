package com.solmi.biobrainexample.bio.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.solmi.biobrainexample.common.CircularQueue
import org.json.JSONArray
import org.json.JSONObject

class Converters {

    /**
     * 저장
     * CirCularQueue -> JsonString
     */
    @TypeConverter
    fun toJsonString(circularQueue: CircularQueue) : String{
        val jsonObj = JSONObject()
        val jsonArray = JSONArray()
        while(true){
            val jsonObject = JSONObject()
            var data = circularQueue.pop()
            if(data==-99999999f){
                break
            }
            jsonObject.put("bioData",circularQueue.pop())
            jsonObject.put("Time",System.currentTimeMillis().toString())
            jsonArray.put(jsonObject)
        }
        jsonObj.put("item",jsonArray)

//        val gson = Gson()
//        gson.toJson(jsonObj)

        return ""
    }


    /**
     * 로드
     * JsonString -> CircularQueue
     */
    @TypeConverter
    fun toCircularQueue(jsonString: String) : CircularQueue{

        return CircularQueue()
    }

}