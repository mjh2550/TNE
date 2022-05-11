package com.solmi.ble;

import android.app.Activity;
import android.content.Context;

/**
 * BioBrainExample
 * Class: BTStateEvent
 * Created by solmitech on 2021-09-27.
 * Description: BLE 통신 상태 이벤트
 */
public interface BTStateEvent {

    /**
     * BLE 상태 변경 이벤트
     * @param state 블루투스 상태
     */
    void onStateChanged(BLEDefine.BluetoothState state);

    /**
     * BLE 재연결 알림 이벤트
     * @param reconnectCount 재연결 시도 수
     */
    void onReconnect(int reconnectCount);

    /**
     * BLE 재연결 종료 이벤트
     */
    void onReconnectStop();

    /**
     * 연결된 디바이스와의 신호 세기 이벤트
     * @param rssi 연결된 디바이스와의 신호 세기
     */
    void onUpdateRSSI(int rssi);
}
