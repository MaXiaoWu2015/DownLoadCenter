package com.konka.downloadcenter;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Ernest on 2016-5-5.
 */
public class DownloadService extends Service {
    private static final HashMap<Integer, RemoteCallbackList<IDownloadListener>> listeners = new HashMap<>();

    private static final String TAG = DownloadService.class.getSimpleName();

    public static HashMap<Integer, RemoteCallbackList<IDownloadListener>> getListeners() {
        return listeners;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new IDownloadInterface.Stub() {


        @Override
        public void registerCallback(int downloadId,IDownloadListener listener) throws RemoteException {
            Log.d(TAG, "registerCallback() called with: " + "listener = [" + listener + "]");
            synchronized (listeners) {
            if (!listeners.containsKey(downloadId)) {
                listeners.put(downloadId, new RemoteCallbackList<IDownloadListener>());
            }
            listeners.get(downloadId).register(listener);
            }
        }

        @Override
        public void unregisterCallback(int downloadId,IDownloadListener listener) throws RemoteException {
            Log.d(TAG, "unregisterCallback() called with: " + "listener = [" + listener + "]");
            synchronized (listeners) {
                if (listeners.containsKey(downloadId)) {
                    listeners.get(downloadId).unregister(listener);
                }
            }

        }

        @Override
        public boolean checkDownloading(String url, String path) throws RemoteException {
            return false;
        }
        @Override
        public int start(FileDownloadTask task) throws RemoteException {
            return task.start();
        }

        @Override
        public boolean pause(int downloadId) throws RemoteException {
            FileDownloader.getImpl().pause(downloadId);
            return false;
        }

        @Override
        public boolean cancel(FileDownloadTask task) throws RemoteException {
            if (TextUtils.isEmpty(task.getPath())||TextUtils.isEmpty(task.getUrl())){
                return false;
            }
            if (task.isShowInDownloadCenter()) {
                //TODO 删除下载列表的记录
            }
            FileDownloader.getImpl().pause(task.getDownloadId());
            return new File(task.getPath()).delete();
        }



        @Override
        public void pauseAllTasks() throws RemoteException {
            FileDownloader.getImpl().pauseAll();
        }

        @Override
        public long getSofar(int downloadId) throws RemoteException {
            return FileDownloader.getImpl().getSoFar(downloadId);
        }

        @Override
        public long getTotal(int downloadId) throws RemoteException {
            return FileDownloader.getImpl().getTotal(downloadId);
        }

        @Override
        public int getStatus(int downloadId) throws RemoteException {
            return FileDownloader.getImpl().getStatus(downloadId);
        }

        @Override
        public boolean isIdle() throws RemoteException {
            return false;
        }

        @Override
        public void startForeground(int id, Notification notification) throws RemoteException {

        }

        @Override
        public void stopForeground(boolean removeNotification) throws RemoteException {

        }
    };
}
