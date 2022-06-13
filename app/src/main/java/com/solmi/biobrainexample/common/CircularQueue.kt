package com.solmi.biobrainexample.common

import android.util.Log

class CircularQueue {
    private val list = FloatArray(QUEUE_SIZE)
    private var head = 0
    private var tail = -1
    private var queueSize = 0
    private val falseScore = -99999999f

    fun insert(value: Float) {
//        list[(++tail % QUEUE_SIZE)] = value
        ++queueSize
    }

    fun pop(): Float {
        if(head >= QUEUE_SIZE) {
            println("queue'size is overflow")
            return falseScore
        }
        if(list[head] == falseScore) {
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

    fun getFront() : Float {
//        println("Queue Front ${list[head % QUEUE_SIZE]}")
        Log.d("Queue","Queue Front ${list[head % QUEUE_SIZE]}")
        return list[head % QUEUE_SIZE]
    }

    fun getTail(): Float{
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