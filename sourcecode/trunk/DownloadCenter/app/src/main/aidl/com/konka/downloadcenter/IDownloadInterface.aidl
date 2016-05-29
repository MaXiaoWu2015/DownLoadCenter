
package com.konka.downloadcenter;
import com.konka.downloadcenter.FileDownloadTask;
import com.konka.downloadcenter.IDownloadListener;
import android.app.Notification;

interface IDownloadInterface {

    oneway void registerCallback(int downloadId,in IDownloadListener listener);
    oneway void unregisterCallback(int downloadId,in IDownloadListener listener);

    int start(in FileDownloadTask task);
    boolean checkDownloading(String url, String path);
    boolean pause(int downloadId);

    boolean cancel(inout FileDownloadTask task);
    void pauseAllTasks();

    long getSofar(int downloadId);
    long getTotal(int downloadId);
    int getStatus(int downloadId);
    boolean isIdle();

    oneway void startForeground(int id, in Notification notification);
    oneway void stopForeground(boolean removeNotification);

}
