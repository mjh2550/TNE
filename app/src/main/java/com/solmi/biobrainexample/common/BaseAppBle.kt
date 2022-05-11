package com.solmi.biobrainexample.common

import java.util.*

interface BaseAppBle {
    companion object{
        var bleSetData = BleSetData()
    }
    /**
     * 이벤트 핸들러들 초기화하는 함수
     */
    fun initHandler() {
        initBTScanEventHandler()
        initBTStateEventHandler()
        initUxParserEventHandler()
        initItemClickListener()
    }

    /**
     * 디바이스 리스트 뷰 아이템 클릭 이벤트 핸들러 초기화하는 함수
     */
    fun initItemClickListener()

    /**
     * 데이터 파싱 이벤트 핸들러 초기화하는 함수
     */
    fun initUxParserEventHandler()

    /**
     * 데이터 업데이트 타이머 시작하는 함수
     */
    fun startDataUpdateTimer()

    /**
     * 데이터 업데이트 타이머 태스크 반환하는 함수
     * @return 타이머 태스크
     */
    fun getDataUpdateTimerTask() : TimerTask?

    /**
     * 데이터 업데이트 타이머 종료하는 함수
     */
    fun stopDataUpdateTimer()

    /**
     * 블루투스 상태 변화 이벤트 핸들러 초기화하는 함수
     */
    fun initBTStateEventHandler()

    /**
     * 블루투스 검색 이벤트 핸들러 초기화하는 함수
     */
    fun initBTScanEventHandler()

    /**
     * 구성요소 초기화하는 함수
     */
    fun resetComponent() {
        bleSetData.mEMGBuffer!!.clear()
        bleSetData.mAccBuffer!!.clear()
        bleSetData.mGyroBuffer!!.clear()
        bleSetData.mMagnetoBuffer!!.clear()
        bleSetData.mStartTime = 0
        bleSetData.mEMGCount = 0
        bleSetData.mAccCount = 0
        bleSetData.mGyroCount = 0
        bleSetData.mMagnetoCount = 0
    }
}