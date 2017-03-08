package com.lsw.demo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MessengerService extends Service {

    /*public static final int MSG_SAY_HELLO = 1;

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }*/

    private Messenger messenger = new Messenger(new IncomingHandler());
    private Messenger mActivityMessenger;

    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder = messenger.getBinder();
        return binder;
    }

    //1.定义一个 Handler 对象，该 Handler 处理 Activity 发送过来的消息
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.d("tag", msg.toString());
                    if (mActivityMessenger != null) {
                        Message message = new Message();
                        message.what = 2;
                        message.obj = "地瓜地瓜我是土豆";
                        try {
                            mActivityMessenger.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    mActivityMessenger = (Messenger) msg.obj;
                    Log.d("tag", "已经获取到 Activity 发送了的 Messenger 对象");
                    break;
                default:
                    break;
            }
        }
    }

}
