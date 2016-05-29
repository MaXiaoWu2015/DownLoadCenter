package com.konka.downloadcenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ernest on 2016-5-9.
 */
public class FileDownloader {
    private static final String TAG = FileDownloader.class.getSimpleName();
    private static FileDownloader instance;
    private static Context mContext;
    private IDownloadInterface downloadService;
    private ArrayList<FileDownloadConnectListener> fileDownloadConnectListeners = new ArrayList<>();
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("onServiceConnected\n");
            downloadService = IDownloadInterface.Stub.asInterface(service);
            for (FileDownloadConnectListener listener : fileDownloadConnectListeners) {
                listener.connected();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("onServiceDisconnected\n");
            downloadService = null;
            for (FileDownloadConnectListener listener : fileDownloadConnectListeners) {
                listener.disconnected();
            }
        }
    };

    public static FileDownloader init(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new FileDownloader();
        }
        instance.startService();
        return instance;
    }

    public static FileDownloader getInstance() {
        if (mContext == null) {
            throw new RuntimeException("请先调用init(context)初始化");
        }
        if (instance == null) {
            instance = new FileDownloader();
        }
        return instance;
    }

    public FileDownloadTask create(final String url) {
        FileDownloadTask task = new FileDownloadTask(url);
        Log.d(TAG, "create: downloadService " + downloadService);
        task.setDownloadService(downloadService);
        return task;
    }

    public void addListener(int id, final FileDownloadListener listener) {
        IDownloadListener downloadListener = new IDownloadListener.Stub() {
            @Override
            public void pending(FileDownloadTask task, int soFarBytes, int totalBytes) throws RemoteException {
                listener.pending(task, soFarBytes, totalBytes);
            }

            @Override
            public void connected(FileDownloadTask task, boolean isContinue, int soFarBytes, int totalBytes) throws RemoteException {
                listener.connected(task, isContinue, soFarBytes, totalBytes);
            }

            @Override
            public void progress(FileDownloadTask task, int soFarBytes, int totalBytes) throws RemoteException {
                listener.progress(task, soFarBytes, totalBytes);
            }

            @Override
            public void blockComplete(FileDownloadTask task) throws RemoteException {
                listener.blockComplete(task);
            }

            @Override
            public void completed(FileDownloadTask task) throws RemoteException {
                listener.completed(task);
            }

            @Override
            public void paused(FileDownloadTask task, int soFarBytes, int totalBytes) throws RemoteException {
                listener.paused(task, soFarBytes, totalBytes);
            }

            @Override
            public void error(FileDownloadTask task, String message) throws RemoteException {
                listener.error(task,message);
            }

            @Override
            public void warn(FileDownloadTask task) throws RemoteException {
                listener.warn(task);
            }

            @Override
            public void over(FileDownloadTask task) throws RemoteException {
                listener.over(task);
            }

        };
        try {
        downloadService.registerCallback(id,downloadListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeListener(int downloadId,FileDownloadListener listener) {
        try {
            downloadService.unregisterCallback(downloadId,listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean pauseDownload(int downloadId) {
        try {
            downloadService.pause(downloadId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean cancelDownload(FileDownloadTask task) {
        try {
            downloadService.cancel(task);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void addServiceConnectListener(final FileDownloadConnectListener listener) {
        fileDownloadConnectListeners.add(listener);
    }

    public void removeServiceConnectListener(final FileDownloadConnectListener listener) {
        fileDownloadConnectListeners.remove(listener);
    }


    private void startService() {
        Intent intent = new Intent();
        intent.setAction("com.konka.downloadcenter.IDownloadInterface");
        intent.setPackage("com.konka.downloadcenter");
        mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }
}
