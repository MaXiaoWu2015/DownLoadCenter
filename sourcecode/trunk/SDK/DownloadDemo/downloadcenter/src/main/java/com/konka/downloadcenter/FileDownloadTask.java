package com.konka.downloadcenter;

import android.app.ActivityManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;

/**
 * Created by Ernest on 2016-5-9.
 */
public class FileDownloadTask  implements Parcelable {
    private static final String TAG = FileDownloadTask.class.getSimpleName();
    private IDownloadInterface downloadService;
    private int downloadId;

    private String url;
    private String path;

    private FileDownloadListener listener;

    private Object tag;

    private int autoRetryTimes = 0;
    // Number of times to try again
    private int retryingTimes = 0;
    // The min interval millisecond for updating the download speed.
    private int minIntervalUpdateSpeed = 5;
    private int callbackProgressTimes = 100;
    private boolean isForceReDownload = false;
    // KB/s
    private int speed;

    private boolean isShowInDownloadCenter = false;
    private int status;
    private String taskName;
    private IDownloadListener callback;

    public FileDownloadTask() {

    }

    protected FileDownloadTask(Parcel in) {
      readFromParcel(in);
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.downloadId);
        dest.writeString(this.url);
        dest.writeString(this.path);
        dest.writeByte(((byte) (isForceReDownload ? 1 : 0)));
        dest.writeInt(callbackProgressTimes);
        dest.writeInt(speed);
        dest.writeByte(((byte) (isShowInDownloadCenter ? 1 : 0)));
        dest.writeInt(status);
        dest.writeString(taskName);
    }

    public void readFromParcel(Parcel in)
    {
        this.downloadId = in.readInt();
        this.url = in.readString();
        this.path = in.readString();
        this.isForceReDownload = in.readByte()==1;
        this.callbackProgressTimes = in.readInt();
        this.speed = in.readInt();
        this.isShowInDownloadCenter = in.readByte()==1;
        this.status = in.readInt();
        this.taskName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<FileDownloadTask> CREATOR = new Creator<FileDownloadTask>() {
        @Override
        public FileDownloadTask createFromParcel(Parcel source) {
            return new FileDownloadTask(source);
        }

        @Override
        public FileDownloadTask[] newArray(int size) {
            return new FileDownloadTask[size];
        }
    };
    public FileDownloadTask(String url) {
        this.url = url;
    }

    public FileDownloadTask setPath(String path) {
        this.path = path;
        return this;
    }

    public int getSpeed() {
        return this.speed;
    }


    public int getDownloadId(){
        return downloadId;
    }

    public int getStatus() {
        return status;
    }

    public String getTaskName() {
        return taskName;
    }

    public FileDownloadTask setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public FileDownloadTask setShowInDownloadCenter(boolean isShowInDownloadCenter) {
        this.isShowInDownloadCenter =isShowInDownloadCenter;
        return this;
    }

    public boolean isShowInDownloadCenter() {
        return isShowInDownloadCenter;
    }

    /**
     * start the task.
     *
     * @return Download id
     */
    public int start() {
        if (callback == null) {
            callback = new IDownloadListener.Stub() {
                @Override
                public void pending(FileDownloadTask task, int soFarBytes, int totalBytes) throws RemoteException {
                    listener.pending(task, soFarBytes, totalBytes);
                }

                @Override
                public void connected(FileDownloadTask task, boolean isContinue, int soFarBytes, int totalBytes) throws RemoteException {
                    System.out.println(task.toString());
                    listener.connected(task, isContinue, soFarBytes, totalBytes);
                }

                @Override
                public void progress(FileDownloadTask task, int soFarBytes, int totalBytes) throws RemoteException {
                    System.out.println(task.toString());
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
                    listener.error(task, message);
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

        }

        try {
            downloadId = downloadService.start(this);
            downloadService.registerCallback(downloadId, callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return downloadId;
    }


    /**
     * @param listener For callback download status(pending,connected,progress,
     *                 blockComplete,retry,error,paused,completed,warn)
     */
    public FileDownloadTask setListener(final FileDownloadListener listener) {
        this.listener = listener;
        return this;
    }


    /*package*/ void setDownloadService(IDownloadInterface downloadService) {
        this.downloadService = downloadService;
    }

    /**
     * Force re download whether already downloaded completed
     *
     * @param isForceReDownload If set to true, will not check whether the file is downloaded
     *                          by past, default false
     */
    public FileDownloadTask setForceReDownload(final boolean isForceReDownload) {
        this.isForceReDownload = isForceReDownload;
        return this;
    }


    public boolean pause(){

        if (downloadService != null) {
            try {
                downloadService.pause(downloadId);
                downloadService.unregisterCallback(downloadId,callback);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "FileDownloadTask{" +
                "downloadService=" + downloadService +
                ", downloadId=" + downloadId +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", listener=" + listener +
                ", tag=" + tag +
                ", autoRetryTimes=" + autoRetryTimes +
                ", retryingTimes=" + retryingTimes +
                ", minIntervalUpdateSpeed=" + minIntervalUpdateSpeed +
                ", callbackProgressTimes=" + callbackProgressTimes +
                ", isForceReDownload=" + isForceReDownload +
                ", speed=" + speed +
                ", isShowInDownloadCenter=" + isShowInDownloadCenter +
                ", status=" + status +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
