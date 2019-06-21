package com.weegcn.weegdtu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weegcn.mydragview.MyLinearLayout;
import com.weegcn.mydragview.myDragviewlayout;
import com.weegcn.weegdtu.bluetooth.BluetoothService;
import com.weegcn.weegdtu.bluetooth.BluetoothState;
import com.weegcn.weegdtu.bluetooth.DeviceListActivity;
import com.weegcn.weegdtu.dtugc.GCSettingFragment;
import com.weegcn.weegdtu.myviews.CustomDialog;
import com.weegcn.weegdtu.utils.Constants;
import com.weegcn.weegdtu.utils.DigitalTrans;
import com.weegcn.weegdtu.utils.ToastUtils;

import static com.weegcn.weegdtu.bluetooth.BluetoothState.REQUEST_CONNECT_DEVICE;
import static com.weegcn.weegdtu.bluetooth.BluetoothState.REQUEST_ENABLE_BT;

public class MainActivity extends AppCompatActivity {
    //蓝牙状态显示
    private TextView mTxtStatus;
    //蓝牙状态保存
    public Boolean mIsconnect = false;
    // Name of the connected device
    public String mConnectedDeviceName = null;
    //蓝牙连接Logo按钮
    ImageView bluetoothbtn;
    //蓝牙超时线程
    private BlueToothTimeOutMornitor mThreedTimeout;
    //接口
    Ondataparse mydataparse=null;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    private BluetoothService mBTService = null;

    //拖拽视图
    //Weeg Logo open menu
    ImageView WeegLogo;
    public myDragviewlayout dragviewlayout;
    ListView mLeftList;
    int mCurSelectedMenuItem;
    ArrayAdapter leftmenuad;
    static MainActivity mainActivity;
    BaseFragment mCurrentpage;
    RootViewFragment rootViewFragment;

    //设备信息
    TextView deviceinfo;
    public CustomDialog mDialog;
    public static MainActivity getcurinstance() {

        return mainActivity;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            //打开蓝牙
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mBTService == null) {
                // Initialize the BluetoothService to perform bluetooth
                // connections
                mBTService = new BluetoothService(this, mHandler);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity
        // returns.
        if (mBTService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't
            // started already
            if (mBTService.getState() == BluetoothState.STATE_NONE) {
                // Start the Bluetooth services
                mBTService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (mBTService != null)
            mBTService.stop();
    }

    private void InitBlueTooth() {
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            ToastUtils.showToast(this, "该设备不支持蓝牙，强制退出");
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        dragviewlayout = findViewById(R.id.dragview);
        MyLinearLayout mLinearLayout = findViewById(R.id.mll);
        mLinearLayout.setDraglayout(dragviewlayout);
        dragviewlayout.setDragStatusListener(new myDragviewlayout.OnDragStatusChangeListener() {
            @Override
            public void onClose() {

            }

            @Override
            public void onOpen() {

            }

            @Override
            public void onDraging(float percent) {

            }

            @Override
            public void onSizeChanged() {
                dragviewlayout.open(true);
//                Log.d("zl","size changed");
            }
        });


        mLeftList = findViewById(R.id.lv_left);
        initLeftContent();
        //  mMainList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.NAMES));
        dragviewlayout.mMainContent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // Log.d("zl","onLayoutChange");
                dragviewlayout.open(false);
            }
        });
        InitBlueTooth();
        mainActivity = this;
        initviews();
    }

    private void initviews() {
        mDialog = CustomDialog.createProgressDialog(this, Constants.TimeOutSecond, new CustomDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(CustomDialog dialog) {
                dialog.dismiss();
                ToastUtils.showToast(getBaseContext(), "超时啦!");
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("zl","dialog has been cancelde");
                if(mCurrentpage!=null)
                {
                    mCurrentpage.Ondlgcancled();
                }
            }
        });
        mTxtStatus = findViewById(R.id.bluetoothid);
        bluetoothbtn = findViewById(R.id.bluetoothbutid);
        bluetoothbtn.setOnClickListener( new OnclickListererImp() );

        WeegLogo =findViewById(R.id.iv_header);
        WeegLogo.setOnClickListener( new OnclickListererImp() );
        deviceinfo = findViewById(R.id.deviceinfo);
    }

    private void initLeftContent() {
        mCurSelectedMenuItem = 0;
        mLeftList = (ListView) findViewById(R.id.lv_left);
        leftmenuad = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Constants.MenuItems) {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView mText = (TextView) view.findViewById(android.R.id.text1);
                // mText.setTextColor(R.drawable.menu_text_selector);
                //  Log.d("zl","in get view " + mCurSelectedMenuItem );
                if (mCurSelectedMenuItem == position) {
                    mText.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    mText.setTextColor(getResources().getColor(R.color.white));
                }
                return view;
            }
        };
        mLeftList.setAdapter(leftmenuad);

        mLeftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurSelectedMenuItem = position;
                leftmenuad.notifyDataSetChanged();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction(); //开启事务
                switch (position) {
                    case 0:
                        rootViewFragment = new GCSettingFragment();
                        transaction.replace(R.id.lv_main, rootViewFragment,
                                "DTU_GC");
                        deviceinfo.setText("DTU GC2018");
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
                transaction.commit();// 提交事务
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // Attempt to connect to the device
                    mBTService.connect(device);
                }
                break;
            case BluetoothState.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled
                    // Initialize the BluetoothService to perform bluetooth
                    // connections

                    mBTService = new BluetoothService(this, mHandler);

                    Intent serverIntent = new Intent(this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);


                } else {
                    // User did not enable Bluetooth or an error occured
                    // Log.d(TAG, "BT not enabled");
                    ToastUtils.showToast(this, "蓝牙没有打开，程序退出");

                    finish();
                }

                break;
        }
    }

    public void sendData(String data, String strOwner) {


        // Check that we're actually connected before trying anything
        if (mBTService.getState() != BluetoothState.STATE_CONNECTED) {

            ToastUtils.showToast(this,  R.string.not_connected);
            return;
        }

        // Check that there's actually something to send
        if (data.length() > 0) {
//            gOwner = strOwner;
            Log.v("ttt", "Send: " + data);
            String hexString = data;
            byte[] buff = DigitalTrans.hex2byte(hexString);

            mBTService.write(buff);
            if(mThreedTimeout==null)
            {
                mThreedTimeout=new BlueToothTimeOutMornitor();
                mThreedTimeout.start();
            }
        }
    }

    //当timeout设置为0时 ，不会做超时计算
    public void sendData(String data, String strOwner,int timeout) {

//        Log.d("zl","MainActivity:"+data);
        // Check that we're actually connected before trying anything
        if (mBTService.getState() != BluetoothState.STATE_CONNECTED) {

            ToastUtils.showToast(this,  R.string.not_connected);
            return;
        }

        // Check that there's actually something to send
        if (data.length() > 0) {
//            gOwner = strOwner;
            Log.v("ttt", "Send: " + data);
            String hexString = data;
            byte[] buff = DigitalTrans.hex2byte(hexString);

            mBTService.write(buff);
            if(timeout>0)
            {
                if(mThreedTimeout==null)
                {
                    mThreedTimeout=new BlueToothTimeOutMornitor(timeout);
                    mThreedTimeout.start();
                }
            }
        }
    }

    public void sendData(byte[] databuf,int timeout)
    {
        mBTService.write(databuf);
        if(timeout>0)
        {
            if(mThreedTimeout==null)
            {
                mThreedTimeout=new BlueToothTimeOutMornitor(timeout);
                mThreedTimeout.start();
            }
        }
    }

    public class BlueToothTimeOutMornitor extends Thread
    {
        public int mtimeout;
        BlueToothTimeOutMornitor()
        {
            mtimeout=2000;
        }
        BlueToothTimeOutMornitor(int timeout)
        {
            mtimeout=timeout;
        }
        @Override
        public void run() {
            try {
                sleep(mtimeout);
                MainActivity.this.mHandler.obtainMessage(BluetoothState.MESSAGE_STATE_TIMEOUT)
                        .sendToTarget(); //       mHandler.obtainMessage(BluetoothState.MESSAGE_READ, bytes, -1, buffer)
                //  .sendToTarget();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothState.MESSAGE_STATE_CHANGE:
                    // if (D)
                    //    Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothState.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to,
                            //		mConnectedDeviceName));
                            mTxtStatus.setText("已连接到:" + mConnectedDeviceName);

                            // mConversationArrayAdapter.clear();
                            mIsconnect = true;
                            break;
                        case BluetoothState.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            mTxtStatus.setText(R.string.title_connecting);
                            mIsconnect = false;
                            break;
                        case BluetoothState.STATE_LISTEN:
                        case BluetoothState.STATE_NONE:
                            //   Log.d("zl","BluetoothState_state:"+"STATE_NONE/STATE_LISTEN");
                            //setStatus(R.string.title_not_connected);
                            mTxtStatus.setText(R.string.title_not_connected);
                            mIsconnect = false;
                            break;
                    }
                    break;
                case BluetoothState.MESSAGE_WRITE:
                    // byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    // String writeMessage = new String(writeBuf);
                    // mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case BluetoothState.MESSAGE_READ:
                    if (mThreedTimeout != null)
                        mThreedTimeout.interrupt();
                    mThreedTimeout = null;
                    byte[] readBuf = (byte[]) msg.obj;

                    String readMessage = "";
                    for (int i = 0; i < msg.arg1; i++) {
                        //readMessage += readBuf[i];

                        String hex = Integer.toHexString(readBuf[i] & 0xFF);
                        if (hex.length() == 1) {
                            hex = '0' + hex;
                        }

                        readMessage += hex;
                    }

                    byte[] readOutBuf = DigitalTrans.hex2byte(readMessage);
                    String readOutMsg = DigitalTrans.byte2hex(readOutBuf);

                    //获取接收的返回数据
                    Log.v("ttt", "recv:" + readOutMsg);

                    if (mydataparse != null) {
                        mydataparse.datacometoparse(readOutMsg, readOutBuf);
                    } else {
                        mCurrentpage= rootViewFragment.getcurpage();
                        if(mCurrentpage!=null)
                            mCurrentpage.OndataCometoParse(readOutMsg, readOutBuf);
                    }
                    //mDialog.dismiss();

                    break;
                case BluetoothState.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothState.DEVICE_NAME);
                    ToastUtils.showToast(getApplicationContext(), "连接到" + mConnectedDeviceName);

                    break;
                case BluetoothState.MESSAGE_TOAST:

                    ToastUtils.showToast(getApplicationContext(),
                            msg.getData().getString(BluetoothState.TOAST));
                    break;
                case BluetoothState.MESSAGE_STATE_TIMEOUT:
                    if (mIsconnect) {
                        // 关闭连接socket
                        try {
                            // 关闭蓝牙
                            mTxtStatus.setText(R.string.title_not_connected);
                            mBTService.stop();
                        } catch (Exception e) {
                        }
                    }
                    mThreedTimeout = null;
                    mDialog.dismiss();
                    // ToastUtils.showToast(getActivity(), "数据长度异常");
                    Toast.makeText(MainActivity.this, "蓝牙无回应请重连", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothState.MESSAGE_BLOCK_TIMEOUT:
//                    if(mCurrentpage==fregment3) {
////                        Log.d("zl","BluetoothState.MESSAGE_BLOCK_TIMEOUT:"+msg.arg1);
//                        if(msg.arg1==Constants.NB_FRESONDATA_KEY_BLOCK_FINISHED)
//                        {
////                            Log.d("zl","Main OnBlockdataFinished");
//                            fregment3.OnBlockdataFinished();
//                        }
//                        else if(msg.arg1==Constants.NB_FRESONDATA_KEY_TASKFINISHED_FINISHED)
//                        {
////                            Log.d("zl","Main updatelistview");
//                            fregment3.updatelistview();
//                        }
//                    }
                    break;
                case BluetoothState.MESSAGE_CONVERT_INFO:
//                    if(mCurrentpage==fragment10)
//                    {
//                        fragment10.OnFileConvertResult(msg.arg1);
//                    }
                    break;
            }
        }
    };

    public interface Ondataparse
    {
        void datacometoparse(String readOutMsg1,byte[] readOutBuf1);
    }

    public void setOndataparse(Ondataparse ondataparse)
    {
        mydataparse=ondataparse;
    }

    public String getmConnectedDeviceName()
    {
        String str="";
        if(mConnectedDeviceName==null)
        {
            return null;
        }
        if(mConnectedDeviceName.equals(getResources().getString(R.string.not_connected)))
        {
            return null;
        }
        int len=mConnectedDeviceName.length();
        for(int i=0;i<len;i++)
        {
            if(mConnectedDeviceName.charAt(i)>=48&&mConnectedDeviceName.charAt(i)<=57)
            {
                str+=mConnectedDeviceName.charAt(i);
            }

        }
        return str;
    }

    public BluetoothService getcurblueservice()
    {
        return   mBTService;
    }
    private class OnclickListererImp implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.bluetoothbutid:// 蓝牙扫描
                    if(!mIsconnect)
                    {
                        Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    }
                    else
                    {
                        // 关闭连接socket
                        try {
                            // 关闭蓝牙
                            mTxtStatus.setText(R.string.title_not_connected);
                            mBTService.stop();
                        } catch (Exception e) {
                        }
                    }
                    break;
                case R.id.iv_header:
                    if(dragviewlayout.getStatus()== myDragviewlayout.Status.Close)
                        dragviewlayout.open(true);
                    break;
                default:
                    break;
            }

        }
    }
}
