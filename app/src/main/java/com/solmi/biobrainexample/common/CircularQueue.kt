package com.solmi.biobrainexample.common

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CircularQueue {
    private val list = Array(QUEUE_SIZE){""}
    private var head = 0
    private var tail = -1
    private var queueSize = 0
    private val falseScore = "fs"

    fun insert(value: Float) {
        val quotes = '"'
        list[(++tail % QUEUE_SIZE)] = "$value${quotes},${quotes}insertTime${quotes}:${quotes}${getTime()}"
        ++queueSize
    }

    /**
     * 날짜 , 시간 구하는 함수
     */
    private fun getTime() : String{
        //Current Time
        val dateFormat = SimpleDateFormat("HH-mm-ss.SSS")
        val calendar = Calendar.getInstance()

        return dateFormat.format(calendar.time).toString()
    }

    fun pop(): String {
        if(head >= QUEUE_SIZE) {
            println("queue'size is overflow")
            return falseScore
        }
        if(list[head] == falseScore || list[head] == "") {
            println("position $head is empty")
            return falseScore
        }
        println("front pop ${list[head]}")
        val result = list[head]
        list[head] = falseScore
        head = (head + 1) %  QUEUE_SIZE
        queueSize--
        return result
    }

    fun getFront() : String {
//        println("Queue Front ${list[head % QUEUE_SIZE]}")
        Log.d("Queue","Queue Front ${list[head % QUEUE_SIZE]}")
        return list[head % QUEUE_SIZE]
    }

    fun getTail(): String {
//        println("Queue Tail ${list[tail % QUEUE_SIZE]}")
        Log.d("Queue","Queue Tail ${list[tail % QUEUE_SIZE]}")
        return list[tail % QUEUE_SIZE]
    }

    fun printAllElement() {
        for (index in 0 until QUEUE_SIZE) {
            println(list[index])
        }
    }
    fun getQueueSize() : Int{
        return queueSize
    }

    companion object {
        const val QUEUE_SIZE = 25000
    }
}