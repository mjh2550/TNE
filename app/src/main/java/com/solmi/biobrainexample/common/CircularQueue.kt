package com.solmi.biobrainexample.common

import android.util.Log

class CircularQueue {
    private val list = FloatArray(QUEUE_SIZE)
    private var head = 0
    private var tail = -1
    private var queueSize = 0

    fun insert(value: Float) {
        list[(++tail % QUEUE_SIZE)] = value
        ++queueSize
    }

    fun pop(): Float {
        if(head >= 25000) {
            println("queue'size is overflow")
            return -1f
        }
        if(list[head] == -1f) {
            println("position $head is empty")
            return -1f
        }
        println("front pop ${list[head]}")
        val result = list[head]
        list[head] = -1f
        head = (head + 1) %  QUEUE_SIZE
        return result
    }

    fun printFront() {
//        println("Queue Front ${list[head % QUEUE_SIZE]}")
        Log.d("Queue","Queue Front ${list[head % QUEUE_SIZE]}")
    }

    fun printTail() {
//        println("Queue Tail ${list[tail % QUEUE_SIZE]}")
        Log.d("Queue","Queue Tail ${list[tail % QUEUE_SIZE]}")
    }

    fun printAllElement() {
        for (index in 0..9) {
            println(list[index])
        }
    }

    companion object {
        const val QUEUE_SIZE = 25000
    }
}