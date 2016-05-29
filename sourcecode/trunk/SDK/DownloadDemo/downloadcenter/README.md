# 下载中心SDK笔记
- downloadcenter这个module是作为SDK提供给第三方应用使用。正式发布时候使用aar打包，应用只需要加入这个aar便拥有了连接下载中心的能力。
- app这个module作为SDK的使用例子。

## 使用步骤
1. 在AndroidManifest.xml中声明如下必要权限
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission
           android:name="android.permission.READ_EXTERNAL_STORAGE"
           android:maxSdkVersion="18" />
        <uses-permission
           android:name="android.permission.WRITE_EXTERNAL_STORAGE"
           android:maxSdkVersion="18" />
2. 在Application的onCreate中调用`FileDownloader.init(Context)`初始化下载引擎;
3. 在合适的地方开始调用下载引擎,如：
    `FileDownloader.getInstance().create(url).setPath(path).setListener(listener).start();`
    `FileDownloader.getInstance().pauseDownload(downloadId);`
     可以动态根据下载id添加监听器  
     `FileDownloader.getInstance().addListener(downloadId,FileDownloadListener);`
## Tips
 - 通过`FileDownloader的addServiceConnectListener(FileDownloadConnectListener)` 和`removeServiceConnectListener(removeServiceConnectListener)`监听SDK和下载服务的连接情况

--------------
## TODO
- 根据需求修改API，设置是否显示在下载中心列表
