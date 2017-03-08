package com.lsw.demo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /*Messenger mMessenger = null;
    boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, MessengerService.class), mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void sayHello() {
        if (!mBound) return;
        Message msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO, 0, 0);
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }*/
    private Messenger messenger;
    //将该 Handler 发送 Service
    private Messenger mOutMessenger = new Messenger(new OutgoingHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bindService = (Button) findViewById(R.id.bind_service);
        Button sendMessage = (Button) findViewById(R.id.send_message_service);
        bindService.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
    }

    // 绑定服务
    public void click1(View view) {
        Intent intent = new Intent(this, MessengerService.class);
        ServiceConnection conn = new MyServiceConnection();
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    // 发送消息
    public void click2(View view) throws RemoteException {
        if (messenger == null) {
            Toast.makeText(this, "服务不可用！", Toast.LENGTH_SHORT).show();
            return;
        }
        Message message = new Message();
        message.obj = "长江长江我是黄河";
        message.what = 0;
        messenger.send(message);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_service:
                click1(v);
                break;
            case R.id.send_message_service:
                try {
                    click2(v);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    class OutgoingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("tag", msg.toString());
        }
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "连接成功！", Toast.LENGTH_SHORT).show();
            messenger = new Messenger(service);
            Message message = new Message();
            message.what = 1;
            //Activity 绑定 Service 的时候给 Service 发送一个消息，该消息的 obj 属性是一个 Messenger 对象
            message.obj = mOutMessenger;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this, "连接已经断开！", Toast.LENGTH_SHORT).show();
        }

    }

}
