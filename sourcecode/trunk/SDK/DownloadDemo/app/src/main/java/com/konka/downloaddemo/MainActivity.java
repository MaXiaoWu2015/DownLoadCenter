package com.konka.downloaddemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;

import com.konka.downloadcenter.IDownloadInterface;
import com.konka.downloadcenter.IDownloadListener;

import java.io.File;

public class MainActivity extends Activity {
    private TextView tvLog;

    private static final String TAG = MainActivity.class.getSimpleName();
    private IDownloadInterface downloadService;
//    private ServiceConnection conn = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//             System.out.println("onServiceConnected\n");
//            downloadService = IDownloadInterface.Stub.asInterface(service);
//            try {
//                downloadService.registerCallback(listener);
//                 System.out.println("registerCallback+"+listener+"\n");
//                downloadService.start("https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk", new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "mobileqq_android.apk").getAbsolutePath(), 100, 3);
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//              System.out.println( e.getMessage()+"\n");
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//             System.out.println("onServiceDisconnected\n");
//            downloadService = null;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLog = (TextView) findViewById(R.id.tvLog);

    }

//    private void startService() {
//        Intent intent = new Intent();
//        intent.setAction("com.konka.downloadcenter.IDownloadInterface");
//        intent.setPackage("com.konka.downloadcenter");
//        bindService(intent, conn, Context.BIND_AUTO_CREATE);
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (downloadService == null) {
//            System.out.println("onResume: startServices\n");
//            startService();
//        }else if (listener!=null){
//            try {
//                downloadService.registerCallback(listener);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        try {
//             System.out.println("onPause() unregisterCallback\n");
//            if (downloadService != null && listener != null) {
//                downloadService.unregisterCallback(listener);
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(conn);
    }
//
//    /**
//     * service的回调方法
//     */
//    private IDownloadListener listener = new IDownloadListener.Stub() {
//        @Override
//        public void pending(int id,int soFarBytes, int totalBytes) throws RemoteException {
//            System.out.println("pending() called with: " +id+ " soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "]\n");
//        }
//
//        @Override
//        public void progress(int id,int soFarBytes, int totalBytes) throws RemoteException {
//            System.out.println("progress() called with: " + id+ "soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "]\n");
//        }
//
//        @Override
//        public void blockComplete(int id) throws RemoteException {
//            System.out.println("blockComplete() called with: " + id+ "\n");
//        }
//
//        @Override
//        public void completed(int id) throws RemoteException {
//            System.out.println("completed() called with: " + id+ "\n");
//        }
//
//        @Override
//        public void paused(int id,int soFarBytes, int totalBytes) throws RemoteException {
//            System.out.println("paused() called with: " + id+ "soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "]\n");
//        }
//
//        @Override
//        public void error(int id, String message) throws RemoteException {
//            System.out.println("error() called with: " + id+ "\n");
//        }
//
//        @Override
//        public void warn(int id) throws RemoteException {
//            System.out.println("warn() called with: " + id+ "\n");
//        }
//
//    };
}
