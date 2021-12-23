package com.solmi.ble;

import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import com.solmi.bluetoothlibrary.common.BTDataDefine;
import com.solmi.bluetoothlibrary.common.UxParserThread;
import com.solmi.uxprotocol.UxParserEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * BioBrainExample
 * Class: BLEService
 * Created by solmitech on 2021-09-27.
 * Description: BLE 통신 서비스
 */
public class BLEService extends Service {

	/**
	 * LOG 출력을 위한 TAG
	 */
	private final String TAG = BLEService.class.getSimpleName();

	/**
	 * 처음 최대 연결 시도 횟수
	 */
	private final int MAX_CHECK_GATT_CONNECTION = 3;
	/**
	 * BLE scan Time
	 */
	private final int SCAN_PERIOD = 10000;
	/**
	 * 데이터 수신할 수 있는 최대 Credit 수
	 */
	private final int MAX_RX_CREDITS_COUNT = 250;//64;
	/**
	 * 데이터 수신할 수 있는 최소 Credit 수
	 */
	private final int MIN_RX_CREDITS_COUNT = 10;//16;
	/**
	 * 연결 시도 후 상태 확인하는 이벤트
	 */
	private final int CHECK_GATT_CONNECTION = 100;

	/**
	 * 이벤트 처리 핸들러
	 */
	private Handler mEventHandler = null;
	/**
	 * GATT 이벤트 처리 핸들러
	 */
	private BluetoothGattCallback mGattCallback = null;
	/**
	 * 블루투스 연결 후 이벤트 처리 핸들러
	 */
	private BTStateEvent mBTStateEventHandler = null;
	/**
	 * 데이터 파싱 이벤트 처리 핸들러
	 */
	private UxParserEvent mParserEventHandler = null;
	/**
	 * BLE device discovery event handler
	 */
	private BluetoothAdapter.LeScanCallback mLeScanCallback = null;
	/**
	 * API 21 and later BLE device discovery event handler
	 */
	private ScanCallback mBleScanCallback = null;
	/**
	 * BLE scan end runnable
	 */
	private Runnable mScanStopRunnable = null;

	/**
	 * 서비스 바인더
	 */
	private final IBinder mBinder = new LocalBinder();
	/**
	 * 연결할 블루투스 디바이스
	 */
	private BluetoothDevice mBluetoothDevice = null;
	/**
	 * 연결된 디바이스 Gatt
	 */
	private BluetoothGatt mBluetoothGatt = null;
	/**
	 * Write gatt characteristic
	 */
	private BluetoothGattCharacteristic mTxCharacteristic = null;
	/**
	 * Receive gatt characteristic
	 */
	private BluetoothGattCharacteristic mRxCharacteristic = null;
	/**
	 * Credits write gatt characteristic
	 */
	private BluetoothGattCharacteristic mTxCreditsCharacteristic = null;
	/**
	 * Credits receive gatt characteristic
	 */
	private BluetoothGattCharacteristic mRxCreditsCharacteristic = null;
	/**
	 * 데이터 파싱 스레드
	 */
	private UxParserThread mUxParserThread = null;
	/**
	 * 연결 상태
	 */
	private BLEDefine.BluetoothState mState = BLEDefine.BluetoothState.STATE_DISCONNECTED;

	/**
	 * BLE UUID
	 */
	private UUID UUID_SERVICE = UUID.fromString(GattAttributes.UUID_TELIT_CUSTOM_SERVICE_V1);
	/**
	 * 데이터 송신 UUID
	 */
	private UUID UUID_TX_CHARACTERISTIC = UUID.fromString(GattAttributes.UUID_TELIT_UART_DATA_RX_V1);
	/**
	 * 데이터 수신 UUI
	 */
	private UUID UUID_RX_CHARACTERISTIC = UUID.fromString(GattAttributes.UUID_TELIT_UART_DATA_TX_V1);
	/**
	 * 크레딧 송신 UUID
	 */
	private UUID UUID_TX_CREDITS_CHARACTERISTIC = null;
	/**
	 * 크레딧 수신 UUI
	 */
	private UUID UUID_RX_CREDITS_CHARACTERISTIC = null;
	private UUID UUID_CCC_BITS = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");
	/**
	 * 연결할 디바이스 종류
	 */
	protected BTDataDefine.DeviceType mDeviceType = BTDataDefine.DeviceType.SHC_U7D;
	/**
	 * 사용자가 연결 종료 한 경우
	 */
	private boolean mIsUserDisconnect = false;
	/**
	 * 재연결 시도 여부
	 */
	private boolean mIsTryReconnect = false;
	/**
	 * 연결 시도 횟수
	 */
	private int mReconnectCount = 0;
	/**
	 * 최대 재연결 시도 횟수
	 */
	private int MAX_RECONNECT_COUNT = 0;
	/**
	 * 연결 시도 후 상태 확인한 수
	 */
	private int mCheckGattConnectionCount = 0;
	/**
	 * 데이터 수신을 위해 Credit 처리가 필요한지 여부
	 */
	private boolean mIsRequestCredits = true;
	/**
	 * Bluetooth device adapter
	 */
	private BluetoothAdapter mBluetoothAdapter = null;
	/**
	 * API 21 and later BLE scanner
	 */
	private BluetoothLeScanner mBleScanner = null;
	/**
	 * 재연결할 디바이스 블루투스 주소
	 */
	private String mReconnectDeviceAddress = null;
	/**
	 * 데이터 수신할 수 있는 Credit 수
	 */
	private int mLocalCredits = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		initHandler();
		initComponent();
	}

	/**
	 * 멤버 변수들 초기화하는 함수
	 */
	private void initComponent() {
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();
		}

		/*
		if(mBluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)

			selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출

		}

		else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)

			// 블루투스를 활성화 하기 위한 다이얼로그 출력

			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

			// 선택한 값이 onActivityResult 함수에서 콜백된다.

			startActivityForResult(intent, REQUEST_ENABLE_BT);

		}

		 */

	}
/*
	public void selectBluetoothDevice() {

		// 이미 페어링 되어있는 블루투스 기기를 찾습니다.

		devices = bluetoothAdapter.getBondedDevices();

		// 페어링 된 디바이스의 크기를 저장

		pariedDeviceCount = devices.size();

		// 페어링 되어있는 장치가 없는 경우

		if(pariedDeviceCount == 0) {

			// 페어링을 하기위한 함수 호출

		}

		// 페어링 되어있는 장치가 있는 경우

		else {

			// 디바이스를 선택하기 위한 다이얼로그 생성

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");

			// 페어링 된 각각의 디바이스의 이름과 주소를 저장

			List<String> list = new ArrayList<>();

			// 모든 디바이스의 이름을 리스트에 추가

			for(BluetoothDevice bluetoothDevice : devices) {

				list.add(bluetoothDevice.getName());

			}

			list.add("취소");



			// List를 CharSequence 배열로 변경

			final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);

			list.toArray(new CharSequence[list.size()]);



			// 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너

			builder.setItems(charSequences, new DialogInterface.OnClickListener() {

				@Override

				public void onClick(DialogInterface dialog, int which) {

					// 해당 디바이스와 연결하는 함수 호출

					connectDevice(charSequences[which].toString());

				}

			});



			// 뒤로가기 버튼 누를 때 창이 안닫히도록 설정

			builder.setCancelable(false);

			// 다이얼로그 생성

			AlertDialog alertDialog = builder.create();

			alertDialog.show();

		}

	}

 */

	/**
	 * 각 종 핸들러 초기화하는 함수
	 */
	private void initHandler() {
		initEventHandler();
		initGattCallback();
		initScanEventHandler();
		initScanStopRunnable();
	}

	/**
	 * The runnable initialization function stops the BLE scan.
	 */
	private void initScanStopRunnable() {
		// TODO Auto-generated method stub
		mScanStopRunnable = new Runnable() {
			@Override
			public void run() {
				stopScanDevice();
				if (mBluetoothDevice == null) {
					startScanDevice(SCAN_PERIOD);
				}
			}
		};
	}

	/**
	 * Function to stop BLE scan.
	 */
	public void stopScanDevice() {
		// TODO Auto-generated method stub
		mEventHandler.removeCallbacks(mScanStopRunnable);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mBleScanner.stopScan(mBleScanCallback);
		} else {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

	/**
	 * Functions that initialize the Bluetooth discovery event handler
	 */
	private void initScanEventHandler() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mBleScanCallback = new ScanCallback() {
				@Override
				public void onScanResult(int callbackType, ScanResult result) {
					super.onScanResult(callbackType, result);
					if (mIsTryReconnect == false) {
						stopScanDevice();
						return;
					}

					BluetoothDevice bluetoothDevice = result.getDevice();
					if (mReconnectDeviceAddress.equals(bluetoothDevice.getAddress())) {
						connect(bluetoothDevice);
						stopScanDevice();
					}
				}

				@Override
				public void onBatchScanResults(List<ScanResult> results) {
					super.onBatchScanResults(results);
					if (mIsTryReconnect == false) {
						stopScanDevice();
						return;
					}

					for (ScanResult result :
							results) {
						BluetoothDevice bluetoothDevice = result.getDevice();
						if (mReconnectDeviceAddress.equals(bluetoothDevice.getAddress())) {
							connect(bluetoothDevice);
							stopScanDevice();
							break;
						}
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
					if (mIsTryReconnect == false) {
						stopScanDevice();
						return;
					}

					if (mReconnectDeviceAddress.equals(bluetoothDevice.getAddress())) {
						connect(bluetoothDevice);
						stopScanDevice();
					}
				}
			};
		}
	}

	/**
	 * GATT callback 초기화하는 함수
	 */
	private void initGattCallback() {
		mGattCallback = new BluetoothGattCallback() {
			@Override
			public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
				// TODO Auto-generated method stub
				if (characteristic.getUuid().equals(UUID_RX_CHARACTERISTIC)) {
					onDataReceived(characteristic.getValue());
				} else if (characteristic.getUuid().equals(UUID_RX_CREDITS_CHARACTERISTIC)) {
					onRemoteCreditNotification(characteristic.getValue());
				}
			}

			@Override
			public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
				// TODO Auto-generated method stub
				try {
					if (newState == BluetoothProfile.STATE_CONNECTED) {
						if (status == BluetoothGatt.GATT_SUCCESS) {
							onConnected();
						} else {
							onConnectFail();
						}
					} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
						if (status == 133) {
							onConnectFail();
						} else if (status == BluetoothGatt.GATT_FAILURE) {
							onConnectFail();
						} else {
							if (mIsUserDisconnect) {
								mIsUserDisconnect = false;
								onDisconnected();
							} else {
								onConnectionLost();
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e(TAG, "onConnectionStateChange: ", e);
				}
			}

			@Override
			public void onServicesDiscovered(BluetoothGatt gatt, int status) {
				// TODO Auto-generated method stub
				if (status == BluetoothGatt.GATT_SUCCESS) {
					if (setServiceCharacteristic()) {
						if (mIsRequestCredits) {
							if (subscribeRxCreditsCharacteristic() == false) {
								onConnectFail();
							}
						} else {
							if (subscribeRxCharacteristic() == false) {
								onConnectFail();
							}
						}
					} else {
						onConnectFail();
					}
				} else {
					onConnectFail();
				}
			}

			@Override
			public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
				BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
				if (status != BluetoothGatt.GATT_SUCCESS) {
					onConnectFail();
				} else if (characteristic.getUuid().equals(UUID_RX_CHARACTERISTIC)) {
					if (mIsRequestCredits) {
						grantLocalCredits();
					}

					if (mState == BLEDefine.BluetoothState.STATE_CONNECTING) {
						mState = BLEDefine.BluetoothState.STATE_CONNECTED;
						createParserThread();
						if (mBTStateEventHandler != null) {
							mBTStateEventHandler.onStateChanged(mState);
						}
					}
				} else if (characteristic.getUuid().equals(UUID_RX_CREDITS_CHARACTERISTIC)) {
					if (subscribeRxCharacteristic() == false) {
						onConnectFail();
					}
				}
			}

			@Override
			public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					if (mBTStateEventHandler != null) {
						mBTStateEventHandler.onUpdateRSSI(rssi);
					}
				}
			}
		};
	}

	/**
	 * The remote device has granted additional credits, extract credits count from characteristic value
	 * @param dataArray received data array
	 */
	void onRemoteCreditNotification(byte[] dataArray) {

	}

	/**
	 * Data received from remote process them
	 * @param dataArray received data array
	 */
	private void onDataReceived(byte[] dataArray) {
		if (mIsRequestCredits) {
			mLocalCredits--;
			if (mLocalCredits < MIN_RX_CREDITS_COUNT)  {
				grantLocalCredits();
			}
		}

		try {
			mUxParserThread.inputDataArray(dataArray);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "onCharacteristicChanged: ", e);
		}
	}

	/**
	 * Send the local RX credit information to remote
	 */
	private void grantLocalCredits() {
		int credits = MAX_RX_CREDITS_COUNT - mLocalCredits;
		mLocalCredits += credits;
		byte[] value = new byte[] { (byte) (credits & 0xff) };
		mTxCreditsCharacteristic.setValue(value);
		mBluetoothGatt.writeCharacteristic(mTxCreditsCharacteristic);
	}

	/**
	 * 수신된 데이터 파싱하는 쓰레드 생성하는 함수
	 */
	private void createParserThread() {
		// TODO Auto-generated method stub
		if (mUxParserThread == null) {
			mUxParserThread = new UxParserThread(mDeviceType);
			mUxParserThread.registerParserEventHandler(mParserEventHandler);
			mUxParserThread.start();
		}
	}

	/**
	 * 연결 끊겼을때 처리하는 함수
	 */
	private void onConnectionLost() {
		mState = BLEDefine.BluetoothState.STATE_CONNECTION_LOST;
		mIsTryReconnect = true;
		if (mBTStateEventHandler != null) {
			mBTStateEventHandler.onStateChanged(mState);
		}

		stopParsingThread();
		if (mBluetoothGatt == null) {
			return;
		}

		mBluetoothGatt.disconnect();
		mBluetoothGatt.close();
		refreshDeviceCache(mBluetoothGatt);
		mBluetoothGatt = null;
		mTxCharacteristic = null;
		mRxCharacteristic = null;
		mTxCreditsCharacteristic = null;
		mRxCreditsCharacteristic = null;
		mReconnectDeviceAddress = mBluetoothDevice.getAddress();
		mBluetoothDevice = null;
		startScanDevice(SCAN_PERIOD);
	}

	/**
	 * Function to start BLE scan.
	 * @param millisec Time to scan(ms)
	 */
	public void startScanDevice(final int millisec) {
		// TODO Auto-generated method stub
		mEventHandler.removeCallbacks(mScanStopRunnable);
		mReconnectCount++;
		if (MAX_RECONNECT_COUNT != -1) {
			if (mReconnectCount == MAX_RECONNECT_COUNT) {
				reconnectStop();
				if (mBTStateEventHandler != null) {
					mBTStateEventHandler.onReconnectStop();
				}

				return;
			}
		}

		if (mBTStateEventHandler != null) {
			mBTStateEventHandler.onReconnect(mReconnectCount);
		}

		mEventHandler.postDelayed(mScanStopRunnable, millisec);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			ArrayList<ScanFilter> filters = new ArrayList<>();
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
				ScanFilter telit1ScanFilter = new ScanFilter.Builder()
						.setServiceUuid(ParcelUuid.fromString(GattAttributes.UUID_TELIT_CUSTOM_SERVICE_V1))
						.build();
				filters.add(telit1ScanFilter);
				ScanFilter telit2ScanFilter = new ScanFilter.Builder()
						.setServiceUuid(ParcelUuid.fromString(GattAttributes.UUID_TELIT_CUSTOM_SERVICE_V2))
						.build();
				filters.add(telit2ScanFilter);
			}

			ScanSettings.Builder scanSettingBuilder = new ScanSettings.Builder();
			scanSettingBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
			ScanSettings settings = scanSettingBuilder.build();
			mBleScanner.startScan(filters, settings, mBleScanCallback);
		} else {
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		}
	}

	/**
	 * 재연결처리하는 함수
	 */
	private void reconnect() {
		if (mIsTryReconnect) {
			coercionDisconnect();
			if (MAX_RECONNECT_COUNT == -1) {
				mReconnectCount++;
				connect(mBluetoothDevice);
				if (mBTStateEventHandler != null) {
					mBTStateEventHandler.onReconnect(mReconnectCount);
				}
			} else {
				if (mReconnectCount < MAX_RECONNECT_COUNT) {
					mReconnectCount++;
					connect(mBluetoothDevice);
					if (mBTStateEventHandler != null) {
						mBTStateEventHandler.onReconnect(mReconnectCount);
					}
				} else {
					reconnectStop();
					if (mBTStateEventHandler != null) {
						mBTStateEventHandler.onReconnectStop();
					}
				}
			}
		}
	}

	/**
	 * 연결 종료 처리하는 함수
	 */
	private void onDisconnected() {
		mState = BLEDefine.BluetoothState.STATE_DISCONNECTED;
		mBluetoothGatt.close();
		refreshDeviceCache(mBluetoothGatt);
		mBluetoothGatt = null;
		mTxCharacteristic = null;
		mRxCharacteristic = null;
		mTxCreditsCharacteristic = null;
		mRxCreditsCharacteristic = null;
		if (mBTStateEventHandler != null) {
			mBTStateEventHandler.onStateChanged(mState);
		}
	}

	/**
	 * 연결 실패 처리하는 함수
	 */
	private void onConnectFail() {
		if (mIsTryReconnect) {
			mEventHandler.removeMessages(CHECK_GATT_CONNECTION);
			reconnect();
		} else {
			coercionDisconnect();
			mState = BLEDefine.BluetoothState.STATE_CONNECT_FAIL;
			if (mEventHandler.hasMessages(CHECK_GATT_CONNECTION)) {
				mEventHandler.removeMessages(CHECK_GATT_CONNECTION);
				mCheckGattConnectionCount++;
				if (mCheckGattConnectionCount >= MAX_CHECK_GATT_CONNECTION) {
					reconnectStop();
					if (mBTStateEventHandler != null) {
						mBTStateEventHandler.onStateChanged(mState);
					}

					mState = BLEDefine.BluetoothState.STATE_DISCONNECTED;
				} else {
					connect(mBluetoothDevice);
				}
			} else {
				reconnectStop();
				if (mBTStateEventHandler != null) {
					mBTStateEventHandler.onStateChanged(mState);
				}

				mState = BLEDefine.BluetoothState.STATE_DISCONNECTED;
			}
		}
	}

	/**
	 * 연결 성공 처리하는 함수
	 */
	private void onConnected() {
		mIsUserDisconnect = false;
		reconnectStop();
		mLocalCredits = 0;
		mBluetoothGatt.discoverServices();
	}

	/**
	 * 연결 중 처리하는 함수
	 */
	private void onConnecting() {
		mState = BLEDefine.BluetoothState.STATE_CONNECTING;
		if (mBTStateEventHandler != null) {
			mBTStateEventHandler.onStateChanged(mState);
		}
	}

	/**
	 * 이벤트 처리 핸들러 초기화하는 함수.
	 */
	private void initEventHandler() {
		mEventHandler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case CHECK_GATT_CONNECTION:
						if (mIsTryReconnect) {
							onConnectFail();
						} else {
							if (mState == BLEDefine.BluetoothState.STATE_CONNECTING) {
								mCheckGattConnectionCount++;
								if (mCheckGattConnectionCount >= MAX_CHECK_GATT_CONNECTION) {
									onConnectFail();
								} else {
									coercionDisconnect();
									connect(mBluetoothDevice);
								}
							}
						}
						break;
				}

				return false;
			}
		});
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		mCheckGattConnectionCount = 0;
		mEventHandler.removeMessages(CHECK_GATT_CONNECTION);
		coercionDisconnect();
		return super.onUnbind(intent);
	}

	/**
	 * 블루투스 이벤트 처리 핸들러를 등록하는 함수
	 * @param handler 블루투스 이벤트 처리 핸들러
	 */
	public void registerBTStateEventHandler(BTStateEvent handler) {
		// TODO Auto-generated method stub
		mBTStateEventHandler = handler;
	}

	/**
	 * Ux 데이터 파싱 이벤트 핸들러 등록하는 함수
	 * @param handler 파싱 이벤트 핸들러
	 */
	public void registerParserEventHandler(UxParserEvent handler) {
		mParserEventHandler = handler;
	}

	/**
	 * 연결할 디바이스 종류를 설정하는 함수
	 * @param deviceType 연결할 디바이스 종류
	 */
	public void setDeviceType(BTDataDefine.DeviceType deviceType) {
		mDeviceType = deviceType;
		if (mUxParserThread != null) {
			mUxParserThread.setDeviceType(mDeviceType);
		}
	}

	/**
	 * 블루투스 디바이스와 연결하는 함수
	 * @param device 연결할 블루투스 디바이스
	 * @return 성공 여부
	 */
	public boolean connect(BluetoothDevice device) {
		// TODO Auto-generated method stub
		if (device == null) {
			return false;
		}

		int connectDelay = 0;
		if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
			unpairDevice(device);
			connectDelay = 400;
		} else {
			connectDelay = 300;
		}

		mBluetoothDevice = device;
		mEventHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
//				// We want to directly connect to the device, so we are setting the autoConnect
//				// parameter to false.
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					mBluetoothGatt = mBluetoothDevice.connectGatt(getApplicationContext(), false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
				} else {
					mBluetoothGatt = mBluetoothDevice.connectGatt(getApplicationContext(), false, mGattCallback);
				}

				onConnecting();
				mEventHandler.sendEmptyMessageDelayed(CHECK_GATT_CONNECTION, 10000);
			}
		}, connectDelay);

		return true;
	}

	/**
	 * 기존에 페어링된 디바이스를 페어링 취소하는 함수
	 */
	private boolean unpairDevice(BluetoothDevice device) {
		try {
			Method method = device.getClass().getMethod("removeBond", (Class[]) null);
			if (method != null) {
				final boolean success = (Boolean) method.invoke(device, (Object[]) null);
				return success;
			}
		} catch (Exception e) {
			Log.e(TAG, "unpairDevice: ", e);
		}
		return false;
	}

	/**
	 * 연결된 디바이스와 블루투스 연결을 종료하는 함수
	 * @param isUserDisconnect 사용자가 종료하는 여부
	 */
	public void disconnect(boolean isUserDisconnect) {
		// TODO Auto-generated method stub
		mIsUserDisconnect = isUserDisconnect;
		stopParsingThread();
		if (mBluetoothGatt == null) {
			return;
		}

		mBluetoothGatt.disconnect();
	}

	/**
	 * 연결된 디바이스와 강제적으로 블루투스 연결을 종료하는 함수
	 */
	private void coercionDisconnect() {
		mState = BLEDefine.BluetoothState.STATE_DISCONNECTED;
		mIsUserDisconnect = true;
		stopParsingThread();
		if (mBluetoothGatt == null) {
			return;
		}

		mBluetoothGatt.close();
		refreshDeviceCache(mBluetoothGatt);
		mBluetoothGatt = null;
	}

	/**
	 * 해당 Gatt를 초기화하는 함수
	 * @param bluetoothGatt 초기화할 Gatt
	 * @return 성공 여부
	 */
	private boolean refreshDeviceCache(BluetoothGatt bluetoothGatt) {
		try {
			final Method method = BluetoothGatt.class.getMethod("refresh");
			if (method != null) {
				final boolean success = (Boolean) method.invoke(bluetoothGatt);
				return success;
			}
		} catch (Exception e) {
			Log.e(TAG, "refreshDeviceCache: ", e);
		}
		return false;
	}

	/**
	 * 수신된 데이터 파싱하는 쓰레드 종료하는 함수
	 */
	private void stopParsingThread() {
		// TODO Auto-generated method stub
		if (mUxParserThread != null) {
			mUxParserThread.stopThread();
			mUxParserThread = null;
		}
	}

	/**
	 * 재연결 관련 변수 초기화하는 함수
	 */
	public void reconnectStop() {
		mEventHandler.removeCallbacks(mScanStopRunnable);
		mEventHandler.removeMessages(CHECK_GATT_CONNECTION);
		mIsTryReconnect = false;
		mReconnectCount = 0;
		mCheckGattConnectionCount = 0;
	}

	/**
	 * 연결된 디바이스로 데이터 전달하는 함수
	 * @param writeBuffer 전송할 데이터
	 * @return 성공 여부
	 */
	public boolean writeCharacteristic(byte[] writeBuffer) {
		// TODO Auto-generated method stub
		if (mBluetoothGatt == null) {
			return false;
		}

		if (mTxCharacteristic == null) {
			return false;
		}

		mTxCharacteristic.setValue(writeBuffer);
		return mBluetoothGatt.writeCharacteristic(mTxCharacteristic);
	}

	/**
	 * Subscribe to RX CREDITS characteristic
	 * @return Subscribe result
	 */
	private boolean subscribeRxCreditsCharacteristic() {
		mBluetoothGatt.setCharacteristicNotification(mRxCreditsCharacteristic, true);
		BluetoothGattDescriptor descriptor = mRxCreditsCharacteristic.getDescriptor(UUID_CCC_BITS);
		if (descriptor == null) {
			return false;
		} else {
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
			return mBluetoothGatt.writeDescriptor(descriptor);
		}
	}

	/**
	 * Subscribe to RX characteristic
	 * @return Subscribe result
	 */
	private boolean subscribeRxCharacteristic() {
		mBluetoothGatt.setCharacteristicNotification(mRxCharacteristic, true);
		BluetoothGattDescriptor descriptor = mRxCharacteristic.getDescriptor(UUID_CCC_BITS);
		if (descriptor == null) {
			return false;
		} else {
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			return mBluetoothGatt.writeDescriptor(descriptor);
		}
	}

	/**
	 * Gatt 서비스의 characteristic 설정하는 함수
	 * @return 설정 완료 여부
	 */
	private boolean setServiceCharacteristic() {
		// TODO Auto-generated method stub
		boolean result = false;
		boolean resultOfUUID = setUUIDAccordingToBLEType();
		if (resultOfUUID) {
			BluetoothGattService service = mBluetoothGatt.getService(UUID_SERVICE);
			if (service == null) {
				result = false;
			} else {
				mTxCharacteristic = service.getCharacteristic(UUID_TX_CHARACTERISTIC);
				mRxCharacteristic = service.getCharacteristic(UUID_RX_CHARACTERISTIC);
				if (mIsRequestCredits) {
					mTxCreditsCharacteristic = service.getCharacteristic(UUID_TX_CREDITS_CHARACTERISTIC);
					mRxCreditsCharacteristic = service.getCharacteristic(UUID_RX_CREDITS_CHARACTERISTIC);
					if (mTxCharacteristic == null || mRxCharacteristic == null || mTxCreditsCharacteristic == null || mRxCreditsCharacteristic == null) {
						result = false;
					} else {
						mTxCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
						mTxCreditsCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
						result = true;
					}
				} else {
					if (mTxCharacteristic == null || mRxCharacteristic == null) {
						result = false;
					} else {
						mTxCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
						result = true;
					}
				}
			}
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * BLEType에 따른 UUID 설정하는 함수
	 * @return 설정 여부
	 */
	private boolean setUUIDAccordingToBLEType() {
		boolean result = false;
		List<BluetoothGattService> serviceList = mBluetoothGatt.getServices();
		Collections.reverse(serviceList);
		for (BluetoothGattService gattService :
				serviceList) {
			UUID uuid = gattService.getUuid();
			String strUUID = uuid.toString();
			if (strUUID.equalsIgnoreCase(GattAttributes.UUID_TELIT_CUSTOM_SERVICE_V1)) {
				result = true;
				setTelitUUIDV1();
                mIsRequestCredits = false;
				break;
			} else if (strUUID.equalsIgnoreCase(GattAttributes.UUID_TELIT_CUSTOM_SERVICE_V2)) {
				result = true;
				setTelitUUIDV2();
                mIsRequestCredits = true;
			}
		}

		return result;
	}

	/**
	 * telit uuid version 2 설정하는 함수
	 */
	private void setTelitUUIDV2() {
		UUID_SERVICE = UUID.fromString(GattAttributes.UUID_TELIT_CUSTOM_SERVICE_V2);
		UUID_TX_CHARACTERISTIC = UUID.fromString(GattAttributes.UUID_TELIT_UART_DATA_TX_V2);
		UUID_RX_CHARACTERISTIC = UUID.fromString(GattAttributes.UUID_TELIT_UART_DATA_RX_V2);
		UUID_TX_CREDITS_CHARACTERISTIC = UUID.fromString(GattAttributes.UUID_TELIT_UART_CREDITS_TX_V2);
		UUID_RX_CREDITS_CHARACTERISTIC = UUID.fromString(GattAttributes.UUID_TELIT_UART_CREDITS_RX_V2);
	}

	/**
	 * telit uuid version 1 설정하는 함수
	 */
	private void setTelitUUIDV1() {
		UUID_SERVICE = UUID.fromString(GattAttributes.UUID_TELIT_CUSTOM_SERVICE_V1);
		UUID_TX_CHARACTERISTIC = UUID.fromString(GattAttributes.UUID_TELIT_UART_DATA_TX_V1);
		UUID_RX_CHARACTERISTIC = UUID.fromString(GattAttributes.UUID_TELIT_UART_DATA_RX_V1);
	}

	/**
	 * 기기의 상태를 반환하는 함수
	 * @return 현재 상태
	 */
	public BLEDefine.BluetoothState getBluetoothState() {
		// TODO Auto-generated method stub
		return mState;
	}

	/**
	 * 파싱에 사용되는 보안 코드 가져오는 함수
	 * @return 보안 코드
	 */
	public byte getSecurityCode() {
		byte securityCode = 0x00;
		securityCode = mUxParserThread.getSecurityCode();
		return securityCode;
	}

	/**
	 * 최대 재연결 시도 횟수 지정하는 함수
	 * @param count 지정할 최대 횟수
	 *              -1 : 재연결 횟수 무한
	 */
	public void setReconnectCount(int count) {
		MAX_RECONNECT_COUNT = count;
	}

	/**
	 * 신호 세기 읽는 함수
	 * @return 성공 여부
	 */
	public boolean readRemoteRssi() {
		if (mBluetoothGatt == null) {
			return false;
		}

		return mBluetoothGatt.readRemoteRssi();
	}

	public class LocalBinder extends Binder {
		public BLEService getService() {
			return BLEService.this;
		}
	}



}
