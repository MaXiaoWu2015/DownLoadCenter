package com.konka.downloadcenter;
import com.konka.downloadcenter.FileDownloadTask;
interface IDownloadListener {
//FIXME 参数类型用out会在客户端接收到的值都是空的。暂时用inout，解决方法待定
  void pending(inout FileDownloadTask task,int soFarBytes, int totalBytes);


    void connected(inout FileDownloadTask task,boolean isContinue,int soFarBytes, int totalBytes) ;

    void progress(inout FileDownloadTask task,int soFarBytes, int totalBytes) ;

    void blockComplete(inout FileDownloadTask task) ;

    void completed(inout FileDownloadTask task) ;

    void paused(inout FileDownloadTask task,int soFarBytes, int totalBytes) ;

    void error(inout FileDownloadTask task,String message) ;

    void warn(inout FileDownloadTask task) ;

    void over(inout FileDownloadTask task) ;

}
