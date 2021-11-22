package com.solmi.ble;

/**
 * BioBrainExample
 * Class: BLEDefine
 * Created by solmitech on 2021-09-27.
 * Description: BLE 통신 관련 정의 클래스
 */
public class BLEDefine {

    /**
     * 블루투스 상태 정의
     */
    public enum BluetoothState {
        STATE_DISCONNECTED,
        STATE_CONNECTING,
        STATE_CONNECTED,
        STATE_CONNECT_FAIL,
        STATE_CONNECTION_LOST
    }
}
