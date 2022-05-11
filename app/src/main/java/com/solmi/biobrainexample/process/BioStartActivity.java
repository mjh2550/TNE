package com.solmi.biobrainexample.process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.solmi.biobrainexample.DeviceListAdapter;
import com.solmi.biobrainexample.R;
import com.solmi.biobrainexample.Simple1ChannelGraph;
import com.solmi.biobrainexample.Simple3ChannelGraph;
import com.solmi.ble.BLECommManager;
import com.solmi.ble.BLEDefine;
import com.solmi.ble.BTScanEvent;
import com.solmi.ble.BTStateEvent;
import com.solmi.bluetoothlibrary.common.BTDataDefine;
import com.solmi.uxprotocol.BatteryInfo;
import com.solmi.uxprotocol.DeviceInfo;
import com.solmi.uxprotocol.HeaderPacket;
import com.solmi.uxprotocol.UxParserEvent;
import com.solmi.uxprotocol.UxProtocol;

import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BioStartActivity extends AppCompatActivity implements BioStart,Animation{


    /**
     * 로그 출력을 위한 태그
     */
    private final String TAG = BioStartActivity.class.getSimpleName();

    /**
     * CONTEXT를 전역변수화
     * 모든 화면에서 해당 액티비티 사용 가능하게끔
     */
    public static Context mContext;

    /**
     * 필요한 권한 요청 상수
     */
    private final int PERMISSION_REQUEST_CODE = 100;
//    @BindView(R.id.tv_mainLogTextView)
    protected TextView mTVLogTextView;
//    @BindView(R.id.btn_Scan)
    protected Button mBtnScan;
//    @BindView(R.id.btn_Disconnect)
    protected Button mBtnDisconnect;
//    @BindView(R.id.lv_DeviceList)
    protected ListView mLVDeviceList;
//    @BindView(R.id.sg_mainEMGGraph)
    protected Simple1ChannelGraph mSGEMGGraph;
//    @BindView(R.id.sg_mainAccGraph)
    protected Simple3ChannelGraph mSGAccGraph;
//    @BindView(R.id.sg_mainGyroGraph)
    protected Simple3ChannelGraph mSGGyroGraph;
//    @BindView(R.id.sg_mainMagnetoGraph)
    protected Simple3ChannelGraph mSGMagnetoGraph;
//    @BindView(R.id.rg_mainSamplingRate)
    protected RadioGroup mRGSamplingRate;
//    @BindView(R.id.tv_connect_device)
    protected TextView tv_connect_device;
//    @BindView(R.id.tv_battery)
    protected TextView tv_battery;
//    @BindView(R.id.btn_alram)
    protected Button btn_alram;
//    @BindView(R.id.btn_battery)
    protected Button btn_battery;
//    @BindView(R.id.btn_tray)
    protected Button btn_tray;
//    @BindView(R.id.btn_dataCnt)
    protected Button btn_dataCnt;
    /**
     * 블루투스 검색 이벤트 핸들러
     */
    private BTScanEvent mBTScanEventHandler = null;
    /**
     * 블루투스 상태 변화 이벤트 핸들러
     */
    private BTStateEvent mBTStateEventHandler = null;
    /**
     * 데이터 파싱 이벤트 핸들러
     */
    private UxParserEvent mUxParserEventHandler = null;
    /**
     * 디바이스 리스트 뷰 아이템 클릭 이벤트 핸들러
     */
    private AdapterView.OnItemClickListener mItemClickListener = null;

    /**
     * 블루투스 통신 클래스
     */
    private BLECommManager mBLEManager = null;
    /**
     * 검색된 디바이스 리스트 뷰 어댑터
     */
    private DeviceListAdapter mDeviceListAdapter = null;
    /**
     * EMG 데이터 버퍼
     */
    private Queue<int[]> mEMGBuffer = null;
    /**
     * Acc 데이터 버퍼
     */
    private Queue<int[]> mAccBuffer = null;
    /**
     * Gyro 데이터 버퍼
     */
    private Queue<int[]> mGyroBuffer = null;
    /**
     * Magneto 데이터 버퍼
     */
    private Queue<int[]> mMagnetoBuffer = null;
    /**
     * 데이터 업데이트 타이머
     */
    private Timer mDataUpdateTimer = null;
    /**
     * 측정 시작 시간
     */
    private long mStartTime = 0;
    /**
     * 수신한 EMG 데이터 수
     */
    private int mEMGCount = 0;
    /**
     * 수신한 Acc 데이터 수
     */
    private int mAccCount = 0;
    /**
     * 수신한 Gyro 데이터 수
     */
    private int mGyroCount = 0;
    /**
     * 수신한 Magneto 데이터 수
     */
    private int mMagnetoCount = 0;

    /**
     * Intent request code
     */
    //private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BroadcastReceiver mReceiver;

    View frameContentView;
    View linearContentView;


//    @BindView(R.id.tv_log_01)
    protected TextView tv_01;
//    @BindView(R.id.tv_log_02)
    protected TextView tv_02;
//    @BindView(R.id.tv_log_03)
    protected TextView tv_03;
//    @BindView(R.id.tv_log_04)
    protected TextView tv_04;

//    @BindView(R.id.tv_log_05)
    protected TextView tv_05;
//    @BindView(R.id.tv_log_06)
    protected TextView tv_06;
//    @BindView(R.id.tv_log_07)
    protected TextView tv_07;
//    @BindView(R.id.tv_log_08)
    protected TextView tv_08;

    //protected Button btn_start;
    //protected Button btn_stop;

//    @BindView(R.id.btn_1frag)
    protected Button btn_1frag;

//    @BindView(R.id.btn_2frag)
    protected Button btn_2frag;

    BioStartFragment bioStartFragment;
    BioStart1Fragment bioStart1Fragment;
    BioStart2Fragment bioStart2Fragment;
    FragmentManager fm;
    FragmentTransaction ft;
  //  TextView tv_mainTitle;
    TextView tv_a_log_5;
    TextView tv_a_log_6;
    TextView tv_a_log_7;
    TextView tv_a_log_8;
    TextView tv_data_conn;


    /*@Override
    protected void onPause() {
        super.onPause();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(MyBroadcastReceiver.Myaction);
        registerReceiver(mReceiver,intentFilter);
    }*/

    public void sendMyBroadcast(String action){

        Intent intent = new Intent();
        //intent.setAction(MyBroadcastReceiver.Myaction);
        intent.setAction(action);
        sendBroadcast(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_start);
        ButterKnife.bind(this);
        mContext = this;

//        frameContentView = findViewById(R.id.bio_content_layout);
//        linearContentView = findViewById(R.id.bio_content_l_layout);

        //TODO:프래그먼트 변수 액티비티에 가져오기
        //fragment bind

        fm = getSupportFragmentManager();
        //매니져 객체에게 findFragmentById()를 요청하면서 id 전달

        //프래그먼트 싱글톤으로 생성
        bioStartFragment = BioStartFragment.getInstance();
        bioStart1Fragment = BioStart1Fragment.getInstance();
        bioStart2Fragment = BioStart2Fragment.getInstance();
//        setFrag(0);


       // bioStart1Fragment = (BioStart1Fragment)fm.findFragmentById(R.id.b1f);
       // frameContentView.findViewById(R.id.tv_f_log_05);


        initHandler();
        initComponent();

        mReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(MyBroadcastReceiver.Myaction);
        registerReceiver(mReceiver,intentFilter);

    }

    /**
     * 프래그먼트 바인드
     * @param fragment
     */
    public void setFragmentBind(Fragment fragment){
      //  fm = getSupportFragmentManager();
      //  fragment = (Fragment) fm.findFragmentById(R.id.b1f);
        tv_a_log_5 = fragment.getView().findViewById(R.id.tv_f_log_05);
        tv_a_log_6 = fragment.getView().findViewById(R.id.tv_f_log_06);
        tv_a_log_7 = fragment.getView().findViewById(R.id.tv_f_log_07);
        tv_a_log_8 = fragment.getView().findViewById(R.id.tv_f_log_08);
        tv_data_conn = fragment.getView().findViewById(R.id.tv_data_conn);
      /*  btn_start = fragment.getView().findViewById(R.id.btn_Start);
        btn_stop = fragment.getView().findViewById(R.id.btn_Stop);*/
    }

    /**
     * 구성 요소들 초기화하는 함수
     */
    private void initComponent() {
        mBLEManager = new BLECommManager(this);
        mBLEManager.registerBTScanEventHandler(mBTScanEventHandler);
        mBLEManager.registerBTStateEventHandler(mBTStateEventHandler);
        mBLEManager.registerParserEventHandler(mUxParserEventHandler);
        boolean isBLESupport = mBLEManager.checkIsBLESupport();
        if (isBLESupport) {
            if (mBLEManager.startBLEService() == false) {
                Toast.makeText(getApplicationContext(), R.string.error_ble_start_service, Toast.LENGTH_SHORT).show();
                mBLEManager = null;
                return;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_not_support_ble, Toast.LENGTH_SHORT).show();
        }
        boolean isBLEOnOff = mBLEManager.checkBLEOnOff();//블루투스가 켜져있는지 확인
        if(isBLEOnOff){
            Toast.makeText(getApplicationContext(), R.string.main_info_ble_on, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), R.string.main_info_ble_off, Toast.LENGTH_SHORT).show();

            // 블루투스를 활성화 하기 위한 다이얼로그 출력
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 선택한 값이 onActivityResult 함수에서 콜백된다.
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }
        mLVDeviceList.setOnItemClickListener(mItemClickListener);
        mDeviceListAdapter = new DeviceListAdapter(this);
        mLVDeviceList.setAdapter(mDeviceListAdapter);
        mTVLogTextView.setMovementMethod(new ScrollingMovementMethod());
        mEMGBuffer = new LinkedBlockingQueue<>();
        mAccBuffer = new LinkedBlockingQueue<>();
        mGyroBuffer = new LinkedBlockingQueue<>();
        mMagnetoBuffer = new LinkedBlockingQueue<>();
    }

    /**
     * 이벤트 핸들러들 초기화하는 함수
     */
    private void initHandler() {
        initBTScanEventHandler();
        initBTStateEventHandler();
        initUxParserEventHandler();
        initItemClickListener();
    }

    /**
     * 블루투스 설정 콜백 함수
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK) {
                    // 블루투스가 활성 상태로 변경됨

                    finish();//인텐트 종료
                    overridePendingTransition(0, 0);//인텐트 효과 없애기
                    Intent intent = getIntent(); //인텐트
                    startActivity(intent); //액티비티 열기
                    overridePendingTransition(0, 0);//인텐트 효과 없애기
                }

                else if(resultCode == RESULT_CANCELED) {
                    // 블루투스가 비활성 상태임
                    finish();  //  어플리케이션 종료
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 디바이스 리스트 뷰 아이템 클릭 이벤트 핸들러 초기화하는 함수
     */
    private void initItemClickListener() {
        mItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                if (mBLEManager.getBluetoothState() == BLEDefine.BluetoothState.STATE_CONNECTED) {
                    Toast.makeText(getApplicationContext(), R.string.error_device_connected, Toast.LENGTH_LONG).show();
                    return;
                }

                mBLEManager.stopScanDevice();
                //BluetoothDevice device = mDeviceListAdapter.getItem(index);

                mBLEManager.setDeviceType(BTDataDefine.DeviceType.SHC_U4);
                mBLEManager.setReconnectCount(3);
                BluetoothDevice device = mBLEManager.connect(mDeviceListAdapter.getItem(index));
                tv_connect_device.setText(device.getName()==null ? "" : "DEVICE_NAME: "+device.getName());
                mTVLogTextView.setText("device name: " + device.getName());
                //showNoti(device);


                /*
                BluetoothDevice connDeviceInfo = mBLEManager.connect(device);
                tv_connect_device.setText(connDeviceInfo.getName()==null ? "" : connDeviceInfo.getName());
                showNoti(connDeviceInfo);

                 */
            }
        };
    }

    /**
     * 데이터 파싱 이벤트 핸들러 초기화하는 함수
     */
    private void initUxParserEventHandler() {
        mUxParserEventHandler = new UxParserEvent() {
            @Override
            public void onParserHeaderPacket(HeaderPacket headerPacket) {
                switch (headerPacket.getPacketType()) {
                    case UxProtocol.RES_DAQ_STOP:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTVLogTextView.append("\nRES_DAQ_STOP");
                                stopDataUpdateTimer();
                            }
                        });
                        break;
                    case UxProtocol.RES_DAQ:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTVLogTextView.append("\nRES_DAQ");
                                startDataUpdateTimer();
                            }
                        });
                        break;
                    case UxProtocol.RES_SCALE_SET:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String log = "";
                                switch (headerPacket.getECGSignalScale()) {
                                    case UxProtocol.SCALE_1X:
                                        log = String.format("\nRES_SCALE_SET: Scale: 1X");
                                        break;
                                    case UxProtocol.SCALE_2X:
                                        log = String.format("\nRES_SCALE_SET: Scale: 2X");
                                        break;
                                    case UxProtocol.SCALE_4X:
                                        log = String.format("\nRES_SCALE_SET: Scale: 4X");
                                        break;
                                }

                                mTVLogTextView.append(log);
                            }
                        });
                        break;
                    case UxProtocol.RES_BATT_INFO:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BatteryInfo batteryInfo = headerPacket.getBatteryInfo();
                                //String log = String.format("\nRES_BATT_INFO: Max: %.1f Cur: %.1f", batteryInfo.getMaximumVoltage(), batteryInfo.getCurrentVoltage());
                                float batMax = batteryInfo.getMaximumVoltage();
                                float batCur = batteryInfo.getCurrentVoltage();
                                float batMax100 =  (batMax/batMax)*100;
                                float batCur100 =  (batCur/batMax)*100;
                                /*String log = String.format("RES_BATT_INFO: Max: %.1f Cur: %.1f"
                                            + "\nRES_BATT_INFO100: Max: %.0f Cur: %.2f"
                                            , batMax
                                            , batCur
                                            , batMax100
                                            , batCur100
                                            );*/
                                String log = String.format("%.2f",batMax);
                                        log += String.format("/ %.2f",batCur);


                                mTVLogTextView.append(log);
                                tv_battery.setText(log);
                            }
                        });
                        break;
                    case UxProtocol.RES_FIRM_INFO:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DeviceInfo deviceInfo = headerPacket.getDeviceInfo();
                                String log = String.format("\nRES_FIRM_INFO: Major: %d Minor: %d Build: %d", deviceInfo.getMajorVersion(), deviceInfo.getMinorVersion(), deviceInfo.getBuildVersion());
                                mTVLogTextView.append(log);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onParserSpecialPacket(byte type, byte value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String log = String.format("SpecialPacket %02X %02X", type, value);
                        mTVLogTextView.append(log);
                    }
                });
            }

            @Override
            public void onParserECG(int[] channels) {
                mEMGCount++;
                mEMGBuffer.offer(channels.clone());
            }

            @Override
            public void onParserACC(int[] channels) {
                mAccCount++;
                mAccBuffer.offer(channels.clone());
            }

            @Override
            public void onParserGYRO(int[] channels) {
                mGyroCount++;
                mGyroBuffer.offer(channels.clone());
            }

            @Override
            public void onParserMAGNETO(int[] channels) {
                mMagnetoCount++;
                mMagnetoBuffer.offer(channels.clone());
            }

            @Override
            public void onParserTEMP(int temperature) {
                //Not support
            }

            @Override
            public void onParserPPG(int ppg) {
                //Not support
            }

            @Override
            public void onParserStep(int step) {
                //Not support
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, error);
            }
        };
    }

    /**
     * 데이터 업데이트 타이머 시작하는 함수
     */
    private void startDataUpdateTimer() {
        if (mDataUpdateTimer == null) {
            mStartTime = System.currentTimeMillis();
            mDataUpdateTimer = new Timer("Data update Timer");
            mDataUpdateTimer.schedule(getDataUpdateTimerTask(), 0, 25);
        }
    }

    public Float getRunTime(long mStartTime){
        return (System.currentTimeMillis() - mStartTime) / 1000f;
    }
    /**
     * 데이터 업데이트 타이머 태스크 반환하는 함수
     * @return 타이머 태스크
     */
    private TimerTask getDataUpdateTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("시간초 로그 출력 >>>>>>>>>>", String.format("\nRun time: %.3f(s)", getRunTime(mStartTime)));
                        int emgSize = mEMGBuffer.size();
                        for (int count = 0; count < emgSize; count++) {
                            int[] channels = mEMGBuffer.poll();
                            if (channels != null) {
                                float value = (channels[0] / 2047f) * 7.4f;
                                mSGEMGGraph.putValue(value);
                                tv_01.setText(Float.toString(value));
                                tv_05.setText(Integer.toString(mEMGCount));
                               tv_a_log_5.setText(Integer.toString(mEMGCount));
                               if(tv_data_conn.getText().equals("수신한 데이터 없음") && Integer.parseInt((String) tv_05.getText())>0){
                                   tv_data_conn.setText("데이터 수신 중....");
                               }

                            }
                        }

                        int accSize = mAccBuffer.size();
                        for (int count = 0; count < accSize; count++) {
                            int[] channels = mAccBuffer.poll();
                            if (channels != null) {
                                float[] valueArray = new float[3];
                                for (int index = 0; index < 3; index++) {
                                    valueArray[index] = (channels[index] / 1023f) * 3f;
                                    tv_02.setText(Float.toString(valueArray[index]));
                                    tv_06.setText(Integer.toString(mAccCount));
                                    tv_a_log_6.setText(Integer.toString(mAccCount));
                                }

                                mSGAccGraph.putValueArray(valueArray);
                            }
                        }

                        int gyroSize = mGyroBuffer.size();
                        for (int count = 0; count < gyroSize; count++) {
                            int[] channels = mGyroBuffer.poll();
                            if (channels != null) {
                                float[] valueArray = new float[3];
                                for (int index = 0; index < 3; index++) {
                                    valueArray[index] = (channels[index] / 1023f) * 3f;
                                    tv_03.setText(Float.toString(valueArray[index]));
                                    tv_07.setText(Integer.toString(mGyroCount));
                                    tv_a_log_7.setText(Integer.toString(mGyroCount));
                                }

                                mSGGyroGraph.putValueArray(valueArray);
                            }
                        }

                        int magnetoSize = mMagnetoBuffer.size();
                        for (int count = 0; count < magnetoSize; count++) {
                            int[] channels = mMagnetoBuffer.poll();
                            if (channels != null) {
                                float[] valueArray = new float[3];
                                for (int index = 0; index < 3; index++) {
                                    valueArray[index] = (channels[index] / 1023f) * 3f;
                                    tv_04.setText(Float.toString(valueArray[index]));
                                    tv_08.setText(Integer.toString(mMagnetoCount));
                                    tv_a_log_8.setText(Integer.toString(mMagnetoCount));
                                 //   Log.i("<<<TESTMSG>>>", "run: ongoing....");
                                }

                                mSGMagnetoGraph.putValueArray(valueArray);
                            }
                        }
                    }
                });
            }
        };
    }

    /**
     * 데이터 업데이트 타이머 종료하는 함수
     */
    private void stopDataUpdateTimer() {
        if (mDataUpdateTimer == null) {
            return;
        }

        mTVLogTextView.append(String.format("\nRun time: %.3f(s)", (System.currentTimeMillis() - mStartTime) / 1000f));
        mTVLogTextView.append(String.format("\nEMG: %d Acc: %d Gyro: %d Magneto: %d", mEMGCount, mAccCount, mGyroCount, mMagnetoCount));
        mDataUpdateTimer.cancel();
        mDataUpdateTimer = null;
    }

    /**
     * 블루투스 상태 변화 이벤트 핸들러 초기화하는 함수
     */
    private void initBTStateEventHandler() {
        mBTStateEventHandler = new BTStateEvent() {
            @Override
            public void onStateChanged(BLEDefine.BluetoothState bluetoothState) {
                //BluetoothDevice
                switch (bluetoothState) {
                    case STATE_DISCONNECTED:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_DISCONNECTED", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_DISCONNECTED");
                                mBtnDisconnect.setEnabled(false);
                                stopDataUpdateTimer();
                                delNoti();
                            }
                        });
                        break;
                    case STATE_CONNECTING:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_CONNECTING", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_CONNECTING");
                            }
                        });
                        break;
                    case STATE_CONNECTED:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_CONNECTED", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_CONNECTED");
                                mBtnDisconnect.setEnabled(true);
                                showNoti();

                            }
                        });
                        break;
                    case STATE_CONNECT_FAIL:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_CONNECT_FAIL", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_CONNECT_FAIL");
                                delNoti();
                            }
                        });
                        break;
                    case STATE_CONNECTION_LOST:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_CONNECTION_LOST", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_CONNECTION_LOST");
                                delNoti();
                            }
                        });
                        break;
                }
            }

            @Override
            public void onReconnect(int reconnectCount) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), String.format("onReconnect: %d", reconnectCount), Toast.LENGTH_SHORT).show();
                        mTVLogTextView.append(String.format("\nonReconnect: %d", reconnectCount));
                    }
                });
            }

            @Override
            public void onReconnectStop() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "onReconnectStop: ", Toast.LENGTH_SHORT).show();
                        mTVLogTextView.append("\nonReconnectStop: ");
                        mBtnDisconnect.setEnabled(false);
                        stopDataUpdateTimer();
                    }
                });
            }

            @Override
            public void onUpdateRSSI(int rssi) {

            }
        };
    }

    /**
     * 블루투스 검색 이벤트 핸들러 초기화하는 함수
     */
    private void initBTScanEventHandler() {
        mBTScanEventHandler = new BTScanEvent() {
            @Override
            public void onScanDevice(BluetoothDevice bluetoothDevice) {
                if (bluetoothDevice == null) {
                    return;
                }

                String name = bluetoothDevice.getName();
                if (name == null) {
                    return;
                }

                if (name.contains("SHC") || name.contains("i8")) {
                    mDeviceListAdapter.addItem(bluetoothDevice);
                }
            }

            @Override
            public void onScanDeviceList(List<BluetoothDevice> bluetoothDeviceList) {
                for (BluetoothDevice bluetoothDevice :
                        bluetoothDeviceList) {
                    if (bluetoothDevice == null) {
                        continue;
                    }

                    String name = bluetoothDevice.getName();
                    if (name == null) {
                        continue;
                    }

                    if (name.contains("SHC")) {
                        mDeviceListAdapter.addItem(bluetoothDevice);
                    }
                }
            }

            @Override
            public void onScanFinished() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnScan.setEnabled(true);
                        mBtnScan.setText(R.string.button_scan);
                        Toast.makeText(getApplicationContext(), R.string.scan_complete, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    @Override
    protected void onDestroy() {
         super.onDestroy();
        if (mBLEManager != null) {
            mBLEManager.stopScanDevice();
            mBLEManager.stop();
            mBLEManager.disconnect();
            mBLEManager.stopBLEService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length == 1) {
                    boolean isPermissionGranted = false;
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = true;
                    }

                    if (isPermissionGranted == false) {
                        requestPermission();
                    }
                } else {
                    requestPermission();
                }
                break;
        }
    }

    /**
     * 필요한 권한 요청하는 함수
     */
    private void requestPermission() {
        String[] needPermissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                //@@추가
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        ActivityCompat.requestPermissions(this, needPermissions, PERMISSION_REQUEST_CODE);
    }

//    @OnClick(R.id.btn_Scan)
    public void onClickScan() {
        mDeviceListAdapter.reset();
        /*
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
         */
        mBtnScan.setText(R.string.button_scanning);
        mBtnScan.setEnabled(false);
        mBLEManager.startScanDevice(20 * 1000);
    }

    //@OnClick(R.id.btn_Start)
    public void onClickStart() {
        resetComponent();
        if(tv_a_log_5==null){
            setFragmentBind(bioStart1Fragment);
        }
        /*if (mRGSamplingRate.getCheckedRadioButtonId() == R.id.rb_mainSamplingRate125) {
            mSGEMGGraph.setSamplingRate(125);
            mSGAccGraph.setSamplingRate(15.625f);
            mSGGyroGraph.setSamplingRate(15.625f);
            mSGMagnetoGraph.setSamplingRate(15.625f);
            mTVLogTextView.append("\nonClickStart: Send start command 125 SPS");
            mBLEManager.start(UxProtocol.DAQ_ECG_ACC_GYRO_MAGNETO_SET, UxProtocol.SAMPLINGRATE_125);
        }else if (mRGSamplingRate.getCheckedRadioButtonId() == R.id.rb_mainSamplingRate250) {
            mSGEMGGraph.setSamplingRate(250);
            mSGAccGraph.setSamplingRate(31.25f);
            mSGGyroGraph.setSamplingRate(31.25f);
            mSGMagnetoGraph.setSamplingRate(31.25f);
            mTVLogTextView.append("\nonClickStart: Send start command 250 SPS");
            mBLEManager.start(UxProtocol.DAQ_ECG_ACC_GYRO_MAGNETO_SET, UxProtocol.SAMPLINGRATE_250);
        } else if (mRGSamplingRate.getCheckedRadioButtonId() == R.id.rb_mainSamplingRate500) {
            mSGEMGGraph.setSamplingRate(500);
            mSGAccGraph.setSamplingRate(62.5f);
            mSGGyroGraph.setSamplingRate(62.5f);
            mSGMagnetoGraph.setSamplingRate(62.5f);
            mTVLogTextView.append("\nonClickStart: Send start command 500 SPS");
            mBLEManager.start(UxProtocol.DAQ_ECG_ACC_GYRO_MAGNETO_SET, UxProtocol.SAMPLINGRATE_500);
        }
*/

    }
    
//    @OnClick(R.id.btn_tray)
    public void onClickTray(){
        //홈버튼 효과
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(homeIntent);
    }

//    @OnClick(R.id.btn_dataCnt)
    public void onClickBtnDataCnt(){
        tv_05.setText(Integer.toString(mEMGCount));
        tv_06.setText(Integer.toString(mAccCount));
        tv_07.setText(Integer.toString(mGyroCount));
        tv_08.setText(Integer.toString(mMagnetoCount));
    }

    /**
     * 구성요소 초기화하는 함수
     */
    private void resetComponent() {
        mEMGBuffer.clear();
        mAccBuffer.clear();
        mGyroBuffer.clear();
        mMagnetoBuffer.clear();
        mStartTime = 0;
        mEMGCount = 0;
        mAccCount = 0;
        mGyroCount = 0;
        mMagnetoCount = 0;
    }

   // @OnClick(R.id.btn_Stop)
    public void onClickStop() {
        mTVLogTextView.append("\nonClickStop: Send stop command");
        mBLEManager.stop();
    }

//    @OnClick(R.id.btn_Disconnect)
    public void onClickDisconnect() {
        if (mBLEManager.getBluetoothState() == BLEDefine.BluetoothState.STATE_CONNECTED) {
            mBLEManager.stop();
            mBLEManager.disconnect();
        } else {
            mBLEManager.stop();
            mBLEManager.disconnect();
            mBtnDisconnect.setEnabled(false);
            stopDataUpdateTimer();
        }


    }
//    @OnClick(R.id.btn_alram)
    public void onClickAlram(){
        //showNoti();
        sendMyBroadcast(MyBroadcastReceiver.Myaction);

    }

    private final String CHANNEL_ID = "channel1";
    private final int CHANNEL_ID_NUM = 1;
    private final String CHANEL_NAME = "Channel1";

    @Override
    public void showNoti(){

        NotificationManager manager;
        NotificationCompat.Builder builder;

         /* Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
          snoozeIntent.setAction(ACTION_SNOOZE);
          snoozeIntent.putExtra(CHANNEL_ID, 0);
          PendingIntent snoozePendingIntent =
                    PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);*/

        Intent bleIntent = new Intent(this, BioStartActivity.class);
        //bleIntent.setAction(getClass().);
         PendingIntent  blePendingIntent = PendingIntent.getActivity(this,0,bleIntent,0);


          Intent intent = new Intent(this, BioStartActivity.class);

          PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
          //builder = null;
          manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
              if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//버전 오레오 이상일 경우
                  manager.createNotificationChannel( new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT) );
          builder = new NotificationCompat.Builder(this,CHANNEL_ID);
          //하위 버전일 경우
          }else{
          builder = new NotificationCompat.Builder(this);
          }
          //알림창 제목
          builder.setContentTitle("알림");
          //알림창 메시지
          //builder.setContentText("블루투스 기기가 연결되었어요. \n디바이스 이름 : "+device.getName());
          builder.setContentText("블루투스 기기가 연결되었어요.");
          //알림창 아이콘
          builder.setSmallIcon(R.drawable.bg_tutle);
          //알림창 유지
          builder.setOngoing(true);
          //탭 클릭 시 화면이동
          builder.setContentIntent(pendingIntent);
          //탭 클릭 후에도 알림은 사라지지 않음 (연결 종료시에만 사라짐)
          builder.setAutoCancel(false);

          builder.addAction(R.drawable.arrow,"측정페이지로 이동",blePendingIntent);

          Notification notification = builder.build();

          //알림창 실행
          manager.notify(CHANNEL_ID_NUM,notification);

          //배터리 정보
          mBLEManager.getBatteryInfo();

          }

        //  @RequiresApi(api = Build.VERSION_CODES.O)

         @Override
        public  void delNoti(){

              NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
              manager.cancel(CHANNEL_ID_NUM);
              setTvClear();
              //manager.cancelAll();//이전 알림 전부다 삭제

        }
        private void setTvClear(){
          //  setFragmentBind(bioStart1Fragment);
            tv_connect_device.setText("");
            tv_battery.setText("");
            tv_01.setText("");
            tv_02.setText("");
            tv_03.setText("");
            tv_04.setText("");
            tv_05.setText("");
            tv_06.setText("");
            tv_07.setText("");
            tv_08.setText("");
            if(tv_a_log_5!=null) {
                tv_a_log_5.setText("");
                tv_a_log_6.setText("");
                tv_a_log_7.setText("");
                tv_a_log_8.setText("");
                tv_data_conn.setText("수신한 데이터 없음");
            }
        }

//        @OnClick(R.id.btn_battery)
        public void onClickBtnBattery(){
            mBLEManager.getBatteryInfo();
        }
/*

    */
/**
     *프래그먼트 교체 함수
     * *//*

    public void setFrag(int n)
    {
      //  fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n)
        {
            case 0:
                frameContentView.setVisibility(View.GONE);
                ft.setCustomAnimations(R.anim.horizon_exit,R.anim.none);
                ft.replace(R.id.bio_content_layout,bioStartFragment);
                ft.commit();
                linearContentView.setVisibility(View.VISIBLE);
                break;
            case 1:
                linearContentView.setVisibility(View.GONE);
                ft.setCustomAnimations(R.anim.horizon_exit,R.anim.none);
                ft.replace(R.id.bio_content_layout,bioStart1Fragment);
                ft.commit();
                frameContentView.setVisibility(View.VISIBLE);
                break;
            case 2:
                linearContentView.setVisibility(View.GONE);
                ft.setCustomAnimations(R.anim.horizon_exit,R.anim.none);
                ft.replace(R.id.bio_content_layout,bioStart2Fragment);
                ft.commit();
                frameContentView.setVisibility(View.VISIBLE);
                break;
        }
    }
        @OnClick(R.id.btn_0frag)
        public void onClickBtn0Frag(){
            //onNextAnim();
            setFrag(0);
        }
        @OnClick(R.id.btn_1frag)
        public void onClickBtn1Frag(){
            //onNextAnim();
            setFrag(1);
        }
        @OnClick(R.id.btn_2frag)
        public void onClickBtn2Frag(){
            setFrag(2);
        }
*/

    @Override
    public void onNextAnim() {
        //새로 나타나는 화면, 이전화면 에니메이션 설정
        overridePendingTransition(R.anim.horizon_exit,R.anim.none);
    }

    @Override
    public void onBackAnim() {

    }

    @Override
    public void onUpAnim() {

    }

    @Override
    public void onDownAnim() {

    }
}