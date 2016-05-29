package com.konka.downloadcenter.manager;

/**
 * Created by xiaowu on 2016-5-28.
 */

import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.List;

public class DownLoaderManager {

    private static final String TAG = "DownLoaderManager";

    private final static class HolderClass{
        private final static DownLoaderManager INSTANCE = new DownLoaderManager();
    }

    public static DownLoaderManager getImpl(){
        return HolderClass.INSTANCE;
    }

    // 可以考虑与url绑定?与id绑定?
    private ArrayList<DownloadUpdater> updaterList = new ArrayList<>();

    public void getDownloadTask(final String url, final String path, final int callbackProgressTimes, DownLoadAdpater.DownLoadViewHolder holder){
       BaseDownloadTask task=FileDownloader.getImpl().create(url);

            task.setPath(path)
                    .setCallbackProgressTimes(callbackProgressTimes)
                    .setListener(fileDownloadListener)
                    .setTag(holder)
                    .start();
//        return task;
    }

    public void addUpdater(final DownloadUpdater updater) {
//        if (!updaterList.contains(updater)) {
            updaterList.add(updater);
//        }
    }

    public boolean removeUpdater(final DownloadUpdater updater) {
        return updaterList.remove(updater);
    }


    public FileDownloadListener fileDownloadListener = new FileDownloadSampleListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//           if (mDownloadUpdater!=null)
//                mDownloadUpdater.pending(task,soFarBytes,totalBytes);
            update(task);
        }


        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//            if (mDownloadUpdater!=null)
//                mDownloadUpdater.progress(task,soFarBytes,totalBytes);
            update(task);
        }


        @Override
        protected void completed(BaseDownloadTask task) {
//            if (mDownloadUpdater!=null)
//                mDownloadUpdater.completed(task);
            update(task);
        }


        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//            if (mDownloadUpdater!=null)
//                mDownloadUpdater.paused(task,soFarBytes,totalBytes);
            update(task);
        }
        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
//            if (mDownloadUpdater!=null)
//                mDownloadUpdater.error(task,e);
//        }
            update(task);
        }

    };

    private void update(final BaseDownloadTask task){
        final List<DownloadUpdater> updaterListCopy = (List<DownloadUpdater>) updaterList.clone();
        Log.d(TAG, "update: updaterlist"+updaterListCopy.size()+"---"+updaterListCopy);
        for (DownloadUpdater downloadStatusUpdater : updaterListCopy) {
            downloadStatusUpdater.update(task);
        }
    }
    public interface DownloadUpdater {

            void update(BaseDownloadTask task);
    }

}
