package com.solmi.ble;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.List;

/**
 * BioBrainExample
 * Class: BTScanEvent
 * Created by solmitech on 2021-09-27.
 * Description: BLE 검색 이벤트
 */
public interface BTScanEvent {

    /**
     * BLE 디바이스 검색 이벤트
     * @param device 검색된 디바이스
     */
    void onScanDevice(BluetoothDevice device);

    /**
     * BLE 디바이스 목록 이벤트
     * @param deviceList 검색된 디바이스 목록
     */
    void onScanDeviceList(List<BluetoothDevice> deviceList);

    /**
     * BLE 검색 종료 이벤트
     */
    void onScanFinished();


}
