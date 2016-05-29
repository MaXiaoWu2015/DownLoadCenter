package com.konka.downloadcenter;

import android.os.RemoteException;

/**
 * Created by Ernest on 2016-5-9.
 */
public class FileDownloadListener extends IDownloadListener.Stub{


    @Override
    public void pending(FileDownloadTask task, int soFarBytes, int totalBytes) throws RemoteException {

    }
    @Override
    public void connected(FileDownloadTask task, boolean isContinue,int soFarBytes, int totalBytes) throws RemoteException {

    }

    @Override
    public void progress(FileDownloadTask task,  int soFarBytes, int totalBytes) throws RemoteException {

    }

    @Override
    public void blockComplete(FileDownloadTask task) throws RemoteException {

    }

    @Override
    public void completed(FileDownloadTask task) throws RemoteException {

    }

    @Override
    public void paused(FileDownloadTask task, int soFarBytes, int totalBytes) throws RemoteException {

    }

    @Override
    public void error(FileDownloadTask task, String message) throws RemoteException {

    }

    @Override
    public void warn(FileDownloadTask task) throws RemoteException {

    }


    @Override
    public void over(FileDownloadTask task) throws RemoteException {

    }
}
