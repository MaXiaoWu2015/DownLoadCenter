package com.konka.downloadcenter;

/**
 * Created by Ernest on 2016-5-10.
 */
public interface FileDownloadConnectListener {
    /**
     * connected file download service
     */
     void connected();

    /**
     * disconnected file download service
     */
      void disconnected();
}
