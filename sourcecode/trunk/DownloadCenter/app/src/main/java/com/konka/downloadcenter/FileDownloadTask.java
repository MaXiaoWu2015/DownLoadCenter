package com.konka.downloadcenter;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadModel;

/**
 * Created by Ernest on 2016-5-9.
 */
public class FileDownloadTask  implements Parcelable {
    private static final String TAG = FileDownloadTask.class.getSimpleName();
    private int downloadId;
    private String url;
    private String path;
//    private Object tag;

    private int autoRetryTimes = 0;
    // Number of times to try again
    private int retryingTimes = 0;
    // The min interval millisecond for updating the download speed.
    private int minIntervalUpdateSpeed = 5;

    private boolean isForceReDownload = false;
    private int callbackProgressTimes = FileDownloadModel.DEFAULT_CALLBACK_PROGRESS_TIMES;

    // KB/s
    private int speed;
    private boolean isShowInDownloadCenter = false;
    private int status;
    private String taskName;

    public FileDownloadTask() {
    }
    public FileDownloadTask(String url) {
        this.url = url;
    }

    protected FileDownloadTask(BaseDownloadTask task) {
        this.downloadId = task.getId();
        this.url = task.getUrl();
        this.path = task.getPath();
        this.isForceReDownload = task.isForceReDownload();
        this.callbackProgressTimes = task.getCallbackProgressTimes();
        this.speed = task.getSpeed();
        this.status = task.getStatus();
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
    public static final Parcelable.Creator<FileDownloadTask> CREATOR = new Parcelable.Creator<FileDownloadTask>() {
        @Override
        public FileDownloadTask createFromParcel(Parcel source) {
            return new FileDownloadTask(source);
        }

        @Override
        public FileDownloadTask[] newArray(int size) {
            return new FileDownloadTask[size];
        }
    };

    public FileDownloadTask setPath(String path) {
        this.path = path;
        return this;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }
    public int getSpeed() {
        return this.speed;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public int getDownloadId(){
        return downloadId;
    }


    public void setStatus(int status) {
        this.status =status;
    }

    public int getStatus() {
        return status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setShowInDownloadCenter(boolean isShowInDownloadCenter) {
        this.isShowInDownloadCenter =isShowInDownloadCenter;
    }

    public boolean isShowInDownloadCenter() {
        return isShowInDownloadCenter;
    }

    public int start() throws RemoteException {
        if (isShowInDownloadCenter) {
         //   DBManager.getInstance(APP.CONTEXT).updateDownloadTask(this);
            //// TODO: 添加记录到数据库  如果要全部任务都记录任务名，那这里所有的记录都应该存到数据库

        }

        return FileDownloader.getImpl()
                .create(url)
                .setPath(path)
                .setCallbackProgressTimes(callbackProgressTimes)
                .setAutoRetryTimes(autoRetryTimes)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "pending() called with: " + "task = [" + task + "], soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "]");
                        RemoteCallbackList<IDownloadListener> taskDownloadListeners = DownloadService.getListeners().get(task.getId());
                        if (taskDownloadListeners!=null){
                            try {
                                int count = taskDownloadListeners.beginBroadcast();
                                FileDownloadTask fileDownloadTask = new FileDownloadTask(task);
                                fileDownloadTask.setShowInDownloadCenter(isShowInDownloadCenter);
                                fileDownloadTask.setTaskName(taskName);
                                for (int i = 0 ;i< count ;i++) {
                                    taskDownloadListeners.getBroadcastItem(i).pending(fileDownloadTask,soFarBytes, totalBytes);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }finally {
                                taskDownloadListeners.finishBroadcast();
                            }
                        }
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        Log.d(TAG, "connected() called with: " + "task = [" + task + "], soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "]");
                        RemoteCallbackList<IDownloadListener> taskDownloadListeners = DownloadService.getListeners().get(task.getId());
                        if (taskDownloadListeners!=null){
                            try {
                                int count = taskDownloadListeners.beginBroadcast();
                                FileDownloadTask fileDownloadTask = new FileDownloadTask(task);
                                fileDownloadTask.setShowInDownloadCenter(isShowInDownloadCenter);
                                fileDownloadTask.setTaskName(taskName);
                                for (int i = 0 ;i< count ;i++) {
                                    taskDownloadListeners.getBroadcastItem(i).connected(fileDownloadTask,isContinue,soFarBytes, totalBytes);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }finally {
                                taskDownloadListeners.finishBroadcast();
                            }
                        }
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "progress() called with: " + "task = [" + task + "], soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "]");
                        System.out.println(toString());
                        RemoteCallbackList<IDownloadListener> taskDownloadListeners = DownloadService.getListeners().get(task.getId());
                        if (taskDownloadListeners!=null){
                            try {
                                int count = taskDownloadListeners.beginBroadcast();
                                FileDownloadTask fileDownloadTask = new FileDownloadTask(task);
                                fileDownloadTask.setShowInDownloadCenter(isShowInDownloadCenter);
                                fileDownloadTask.setTaskName(taskName);
                                for (int i = 0 ;i< count ;i++) {
                                    taskDownloadListeners.getBroadcastItem(i).progress(fileDownloadTask,soFarBytes, totalBytes);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }finally {
                                taskDownloadListeners.finishBroadcast();
                            }
                        }
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d(TAG, "completed() called with: " + "task = [" + task + "]");
                        RemoteCallbackList<IDownloadListener> taskDownloadListeners = DownloadService.getListeners().get(task.getId());
                        if (taskDownloadListeners!=null){
                            try {
                                int count = taskDownloadListeners.beginBroadcast();
                                FileDownloadTask fileDownloadTask = new FileDownloadTask(task);
                                fileDownloadTask.setShowInDownloadCenter(isShowInDownloadCenter);
                                fileDownloadTask.setTaskName(taskName);
                                for (int i = 0 ;i< count ;i++) {
                                    taskDownloadListeners.getBroadcastItem(i).completed(fileDownloadTask);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }finally {
                                taskDownloadListeners.finishBroadcast();
                            }
                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "paused() called with: " + "task = [" + task + "], soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "]");
                        RemoteCallbackList<IDownloadListener> taskDownloadListeners = DownloadService.getListeners().get(task.getId());
                        if (taskDownloadListeners!=null){
                            try {
                                int count = taskDownloadListeners.beginBroadcast();
                                FileDownloadTask fileDownloadTask = new FileDownloadTask(task);
                                fileDownloadTask.setShowInDownloadCenter(isShowInDownloadCenter);
                                fileDownloadTask.setTaskName(taskName);
                                for (int i = 0 ;i< count ;i++) {
                                    taskDownloadListeners.getBroadcastItem(i).paused(fileDownloadTask,soFarBytes, totalBytes);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }finally {
                                taskDownloadListeners.finishBroadcast();
                            }
                        }
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.d(TAG, "error() called with: " + "task = [" + task + "], e = [" + e + "]");
                        RemoteCallbackList<IDownloadListener> taskDownloadListeners = DownloadService.getListeners().get(task.getId());
                        if (taskDownloadListeners!=null){
                            try {
                                int count = taskDownloadListeners.beginBroadcast();
                                FileDownloadTask fileDownloadTask = new FileDownloadTask(task);
                                fileDownloadTask.setShowInDownloadCenter(isShowInDownloadCenter);
                                fileDownloadTask.setTaskName(taskName);
                                for (int i = 0 ;i< count ;i++) {
                                    taskDownloadListeners.getBroadcastItem(i).error(fileDownloadTask,e.getMessage());
                                }
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }finally {
                                taskDownloadListeners.finishBroadcast();
                            }
                        }
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.d(TAG, "warn() called with: " + "task = [" + task + "]");
                        RemoteCallbackList<IDownloadListener> taskDownloadListeners = DownloadService.getListeners().get(task.getId());
                        if (taskDownloadListeners!=null){
                            try {
                                int count = taskDownloadListeners.beginBroadcast();
                                FileDownloadTask fileDownloadTask = new FileDownloadTask(task);
                                fileDownloadTask.setShowInDownloadCenter(isShowInDownloadCenter);
                                fileDownloadTask.setTaskName(taskName);
                                for (int i = 0 ;i< count ;i++) {
                                    taskDownloadListeners.getBroadcastItem(i).warn(fileDownloadTask);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }finally {
                                taskDownloadListeners.finishBroadcast();
                            }
                        }
                    }
                })
                .addFinishListener(new BaseDownloadTask.FinishListener() {
                    @Override
                    public void over(BaseDownloadTask task) {
                        RemoteCallbackList<IDownloadListener> taskDownloadListeners = DownloadService.getListeners().get(task.getId());
                        if (taskDownloadListeners!=null){
                            try {
                                int count = taskDownloadListeners.beginBroadcast();
                                FileDownloadTask fileDownloadTask = new FileDownloadTask(task);
                                fileDownloadTask.setShowInDownloadCenter(isShowInDownloadCenter);
                                fileDownloadTask.setTaskName(taskName);
                                for (int i = 0 ;i< count ;i++) {
                                    taskDownloadListeners.getBroadcastItem(i).over(fileDownloadTask);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }finally {
                                taskDownloadListeners.finishBroadcast();
                            }
                        }
                    }
                })
                .start();
    }

    @Override
    public String toString() {
        return "FileDownloadTask{" +
                "downloadId=" + downloadId +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", autoRetryTimes=" + autoRetryTimes +
                ", retryingTimes=" + retryingTimes +
                ", minIntervalUpdateSpeed=" + minIntervalUpdateSpeed +
                ", isForceReDownload=" + isForceReDownload +
                ", callbackProgressTimes=" + callbackProgressTimes +
                ", speed=" + speed +
                ", isShowInDownloadCenter=" + isShowInDownloadCenter +
                ", status=" + status +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
