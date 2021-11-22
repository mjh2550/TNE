package com.solmi.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.solmi.bluetoothlibrary.common.BTDataDefine;
import com.solmi.uxprotocol.UxParserEvent;
import com.solmi.uxprotocol.UxProtocol;

import java.util.ArrayList;
import java.util.List;

/**
 * BioBrainExample
 * Class: BLECommManager
 * Created by solmitech on 2021-09-27.
 * Description: BLE 통신 클래스
 */
public class BLECommManager {

	/**
	 * 로그 출력을 위한 TAG
	 */
	private final String TAG = BLECommManager.class.getSimpleName();
	/**
	 * BLE 검색 시간
	 */
	private final int SCAN_PERIOD = 10000;

	/**
	 * API 21 이전에서 사용하는 BLE 검색 이벤트 핸들러
	 */
	private BluetoothAdapter.LeScanCallback mLeScanCallback = null;
	/**
	 * API 21 또는 이후에서 사용하는 BLE 검색 이벤트 핸들러
	 */
	private ScanCallback mBleScanCallback = null;
	/**
	 * BLE 상태 변화 이벤트 핸들러
	 */
	private BTStateEvent mBTStateEventHandler = null;
	/**
	 * BLE 검색 이벤트 핸들러
	 */
	private BTScanEvent mBTScanEventHandler = null;
	/**
	 * 데이터 파싱 이벤트 핸들러
	 */
	private UxParserEvent mUxParserEventHandler = null;

	/**
	 * 안드로이드에서 제공하는 API 를 호출 할 수 있는 클래스
	 */
	private Context mContext = null;
	/**
	 * Bluetooth device adapter
	 */
	private BluetoothAdapter mBluetoothAdapter = null;
	/**
	 * BLE 서비스 클래스
	 */
	private BLEService mBLEService = null;
	/**
	 * BLE 검색 플래그 변수
	 */
	private boolean isBleScanning = false;
	/**
	 * 이벤트 핸들러
	 */
	private Handler mBleHandler = new Handler();
	/**
	 * 재연결 종료 이벤트
	 */
	private Runnable mScanStopRunnable = null;
	/**
	 * BLE service 등록 이벤트 핸들러
	 */
	private ServiceConnection mServiceConnection = null;
	/**
	 * API 21 또는 이후에서 사용하는 BLE scanner
	 */
	private BluetoothLeScanner mBleScanner = null;
	/**
	 * 연결할 디바이스 종류
	 */
	private BTDataDefine.DeviceType mDeviceType = BTDataDefine.DeviceType.SHC_U7D;

	public BLECommManager(Context context) {
		mContext = context;
		handlerInit();
	}

	/**
	 * 이벤트 핸들러 초기화하는 함수
	 */
	private void handlerInit() {
		// TODO Auto-generated method stub
		initScanEventHandler();
		initServiceConnection();
		initScanStopRunnable();
	}

	/**
	 * 재연결 종료 이벤트 초기화하는 함수
	 */
	private void initScanStopRunnable() {
		// TODO Auto-generated method stub
		mScanStopRunnable = new Runnable() {
			@Override
			public void run() {
				stopScanDevice();
			}
		};
	}

	/**
	 * BLE 서비스 등록 이벤트 초기화하는 함수
	 */
	private void initServiceConnection() {
		// TODO Auto-generated method stub
		mServiceConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
				mBLEService = ((BLEService.LocalBinder) iBinder).getService();
				if (mBTStateEventHandler != null) {
					mBLEService.registerBTStateEventHandler(mBTStateEventHandler);
					mBLEService.registerParserEventHandler(mUxParserEventHandler);
					mBLEService.setDeviceType(mDeviceType);
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				mBLEService = null;
			}
		};
	}

	/**
	 * BLE 검색 이벤트 핸들러 초기화하는 함수
	 */
	private void initScanEventHandler() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mBleScanCallback = new ScanCallback() {
				@Override
				public void onScanResult(int callbackType, ScanResult result) {
					super.onScanResult(callbackType, result);
					if (mBTScanEventHandler != null) {
						mBTScanEventHandler.onScanDevice(result.getDevice());
					}
				}

				@Override
				public void onBatchScanResults(List<ScanResult> results) {
					super.onBatchScanResults(results);
					List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
					for (ScanResult result :
							results) {
						bluetoothDeviceList.add(result.getDevice());
					}

					if (mBTScanEventHandler != null) {
						mBTScanEventHandler.onScanDeviceList(bluetoothDeviceList);
					}
				}

				@Override
				public void onScanFailed(int errorCode) {
					super.onScanFailed(errorCode);
				}
			};
		} else {
			mLeScanCallback = new BluetoothAdapter.LeScanCallback(){
				@Override
				public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
					if (mBTScanEventHandler != null) {
						mBTScanEventHandler.onScanDevice(bluetoothDevice);
					}
				}
			};
		}
	}

	/**
	 * BLE 기능 지원하는지 확인하는 함수
	 * @return BLE 기능 지원 여부
	 */
	public boolean checkIsBLESupport() {
		// TODO Auto-generated method stub
		PackageManager packageManager = mContext.getPackageManager();
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) == false) {
			return false;
		}

		final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			return false;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();
		}

		return true;
	}

	/**
	 * BLE 서비스 시작하는 함수
	 * @return BLE 서비스 시작 성공 여부
	 */
	public boolean startBLEService() {
		// TODO Auto-generated method stub
		if (mBLEService == null) {
			//ble connect service start
			Intent gattServiceIntent = new Intent(mContext, BLEService.class);
			boolean result = mContext.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
			return result;
		} else {
			return true;
		}
	}

	/**
	 * BLE 서비스 종료하는 함수
	 */
	public void stopBLEService() {
		// TODO Auto-generated method stub
		if (mServiceConnection != null) {
			mContext.unbindService(mServiceConnection);
			mBLEService = null;
		}
	}

	/**
	 * BLE 상태 변화 이벤트 핸들러 등록하는 함수
	 * @param handler BLE 상태 변화 이벤트 핸들러
	 */
	public void registerBTStateEventHandler(BTStateEvent handler) {
		// TODO Auto-generated method stub
		mBTStateEventHandler = handler;
	}

	/**
	 * BLE 검색 이벤트 핸들러 등록하는 함수
	 * @param handler BLE 검색 이벤트 핸들러
	 */
	public void registerBTScanEventHandler(BTScanEvent handler) {
		mBTScanEventHandler = handler;
	}

	/**
	 * 데이터 파싱 이벤트 핸들러 등록하는 함수
	 * @param handler 데이터 파싱 이벤트 핸들러
	 */
	public void registerParserEventHandler(UxParserEvent handler) {
		mUxParserEventHandler = handler;
	}

	/**
	 * 연결할 BLE 디바이스 종류를 설정하는 함수
	 * @param device 연결할 BLE 디바이스 종류
	 */
	public void setDeviceType(BTDataDefine.DeviceType device) {
		mDeviceType = device;
		if (mBLEService != null) {
			mBLEService.setDeviceType(device);
		}
	}

	/**
	 * BLE 검색 시작하는 함수
	 * 검색 시간: 10초
	 * @param isAutoStop 10초 경과 후 검색 종료하는지 여부
	 */
	public void startScanDevice(boolean isAutoStop) {
		// TODO Auto-generated method stub
		if (isBleScanning == false) {
			if (isAutoStop) {
				mBleHandler.postDelayed(mScanStopRunnable, SCAN_PERIOD);
			}

			startScanDevice(SCAN_PERIOD);
		}
	}

	/**
	 * BLE 검색 시작하는 함수
	 * @param millisec 검색 시간(ms)
	 */
	public void startScanDevice(final int millisec) {
		// TODO Auto-generated method stub
		if (isBleScanning == false) {
			isBleScanning = true;
			mBleHandler.postDelayed(mScanStopRunnable, millisec);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				ArrayList<ScanFilter> filters = new ArrayList<>();
				ScanSettings.Builder scanSettingBuilder = new ScanSettings.Builder();
				scanSettingBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
				ScanSettings settings = scanSettingBuilder.build();
				mBleScanner.startScan(filters, settings, mBleScanCallback);
			} else {
				mBluetoothAdapter.startLeScan(mLeScanCallback);
			}
		}
	}

	/**
	 * BLE 검색 정지하는 함수
	 */
	public void stopScanDevice() {
		// TODO Auto-generated method stub
		if (isBleScanning) {
			isBleScanning = false;
			mBleHandler.removeCallbacks(mScanStopRunnable);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				mBleScanner.stopScan(mBleScanCallback);
			} else {
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
			}

			if (mBTScanEventHandler != null) {
				mBTScanEventHandler.onScanFinished();
			}
		}
	}

	/**
	 * BLE 연결하는 함수
	 * @param device 연결할 BLE 디바이스
	 */
	public void connect(BluetoothDevice device) {
		// TODO Auto-generated method stub
		if (mBLEService != null) {
			boolean result = mBLEService.connect(device);
			if (result == false) {
				if (mBTStateEventHandler != null) {
					mBTStateEventHandler.onStateChanged(BLEDefine.BluetoothState.STATE_CONNECT_FAIL);
				}
			}
		}
	}

	/**
	 * BLE 연결 종료하는 함수
	 */
	public void disconnect() {
		// TODO Auto-generated method stub
		if (mBLEService != null) {
			mBLEService.reconnectStop();
			mBLEService.disconnect(true);
		}
	}

	/**
	 * 데이터 전달하는 함수
	 * @param command 데이터
	 */
	private void sendCommand(byte[] command) {
		// TODO Auto-generated method stub
		if (mBLEService != null) {
			mBLEService.writeCharacteristic(command);
		}
	}

	/**
	 * 연결 상태 반환하는 함수
	 * @return 연결 상태
	 */
	public BLEDefine.BluetoothState getBluetoothState() {
		// TODO Auto-generated method stub
		if (mBLEService == null) {
			return BLEDefine.BluetoothState.STATE_DISCONNECTED;
		} else {
			return mBLEService.getBluetoothState();
		}
	}

	/**
	 * 연결 끊기면 재연결 시도할 횟수 설정하는 함수
	 * @param count 재연결 시도할 횟수
	 *              -1: 무제한 재연결 시도
	 */
	public void setReconnectCount(int count) {
		if (mBLEService != null) {
			mBLEService.setReconnectCount(count);
		}
	}

	/**
	 * 연결 끊긴 후 재연결 시도 정지하는 함수
	 */
	public void reconnectStop() {
		if (mBLEService != null) {
			mBLEService.reconnectStop();
		}
	}

	/**
	 * 연결된 디바이스의 신호 세기 요청하는 함수
	 * @return 신호 세기 요청 성공 여부
	 */
	public boolean readRemoteRssi() {
		if (mBLEService == null) {
			return false;
		} else {
			return mBLEService.readRemoteRssi();
		}
	}

	/**
	 * 동작 시작하는 함수
	 * @param daq 동작할 명령어
	 */
	public void start(byte daq) {
		byte[] command = {UxProtocol.STX1, UxProtocol.STX2, UxProtocol.STX3, UxProtocol.REQ_DAQ, daq, (byte) 0x01, UxProtocol.SAMPLINGRATE_250, UxProtocol.EOC};
		sendCommand(command);
	}

	/**
	 * 동작 시작하는 함수
	 * @param daq      동작할 명령어
	 * @param sampling 데이터 샘플링
	 */
	public void start(byte daq, byte sampling) {
		byte[] command = {UxProtocol.STX1, UxProtocol.STX2, UxProtocol.STX3, UxProtocol.REQ_DAQ, daq, (byte) 0x01, sampling, UxProtocol.EOC};
		sendCommand(command);
	}

	/**
	 * 동작 정지하는 함수
	 */
	public void stop() {
		byte[] command = {UxProtocol.STX1, UxProtocol.STX2, UxProtocol.STX3, UxProtocol.REQ_DAQ_STOP, (byte) 0x00, (byte) 0x00, (byte) 0x00, UxProtocol.EOC};
		sendCommand(command);
	}

	/**
	 * 배터리 정보 요청하는 함수
	 */
	public void getBatteryInfo() {
		byte[] command = {UxProtocol.STX1, UxProtocol.STX2, UxProtocol.STX3, UxProtocol.REQ_BATT_INFO, (byte) 0x00, (byte) 0x00, (byte) 0x00, UxProtocol.EOC};
		sendCommand(command);
	}

	/**
	 * ECG 신호 크기 설정하는 함수
	 * @param scale ECG 신호 크기
	 */
	public void setECGSignalScale(byte scale) {
		byte[] command = {UxProtocol.STX1, UxProtocol.STX2, UxProtocol.STX3, UxProtocol.REQ_SCALE_SET, scale, (byte) 0x00, (byte) 0x00, UxProtocol.EOC};
		sendCommand(command);
	}

	/**
	 * 펌웨어 버전 요청하는 함수
	 */
	public void getFirmwareVersion() {
		byte[] command = {UxProtocol.STX1, UxProtocol.STX2, UxProtocol.STX3, UxProtocol.REQ_FIRM_INFO, (byte) 0x00, (byte) 0x00, (byte) 0x00, UxProtocol.EOC};
		sendCommand(command);
	}
}
