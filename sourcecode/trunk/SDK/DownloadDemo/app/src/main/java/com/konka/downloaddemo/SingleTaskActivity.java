package com.konka.downloaddemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.konka.downloadcenter.FileDownloadConnectListener;
import com.konka.downloadcenter.FileDownloadListener;
import com.konka.downloadcenter.FileDownloadTask;
import com.konka.downloadcenter.FileDownloader;

import java.io.File;

/**
 * Created by Ernest on 2016-5-9.
 */
public class SingleTaskActivity extends Activity  implements View.OnClickListener,FileDownloadConnectListener{
    private static final String TAG = SingleTaskActivity.class.getSimpleName();
    private int id;
    private EditText etUrl;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private TextView tvLog;
    private TextView tvStatus;
    private Button btnStart;
    private Button btnPause;
    private Button btnDelete;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressBar.setProgress((int)( msg.arg1*100.0f/msg.arg2));
            tvStatus.setText("已下载："+msg.arg1+" 总大小："+msg.arg2+" 下载速度："+msg.obj+"KB/s");
        }
    };

    private FileDownloadListener listener = new FileDownloadListener(){
        @Override
        public void pending(final FileDownloadTask task, final int soFarBytes, final int totalBytes) {
            try {
                super.pending(task, soFarBytes, totalBytes);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLog.append("pending,"+task.getTaskName()+" soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "]\n");
                }
            });


        }

        @Override
        public void connected(final FileDownloadTask task,  boolean isContinue,final int soFarBytes, final int totalBytes) {
            try {
                super.connected(task,isContinue, soFarBytes,totalBytes);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Message msg = handler.obtainMessage();
            msg.arg1 =soFarBytes;
            msg.arg2 = totalBytes;
            msg.obj =    task.getSpeed();
            handler.sendMessage(msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLog.append("connected,"+task.getTaskName()+" soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "],speed = "+task.getSpeed()+"\n");
                    scrollView.post(scroll2Bottom);
                }
            });

        }

        @Override
        public void progress(final FileDownloadTask task, final int soFarBytes, final int totalBytes) {
            try {
                super.progress(task, soFarBytes, totalBytes);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Message msg = handler.obtainMessage();
            msg.arg1 =soFarBytes;
            msg.arg2 = totalBytes;
            msg.obj =    task.getSpeed();
            handler.sendMessage(msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLog.append("progress,"+task.getTaskName()+" soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "],speed = "+task.getSpeed()+"\n");
                    scrollView.post(scroll2Bottom);
                }
            });

        }

        @Override
        public void blockComplete(final FileDownloadTask task) {
            try {
                super.blockComplete(task);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLog.append("blockComplete,+"+task.getTaskName()+"\n");
                    scrollView.post(scroll2Bottom);
                }
            });

        }

        @Override
        public void completed(final FileDownloadTask task) {
            try {
                super.completed(task);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLog.append("completed,+"+task.getTaskName()+"\n");
                    scrollView.post(scroll2Bottom);
                }
            });

        }

        @Override
        public void paused(final FileDownloadTask task, final int soFarBytes, final int totalBytes) {
            try {
                super.paused(task, soFarBytes, totalBytes);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLog.append("paused,"+task.getTaskName()+ " soFarBytes = [" + soFarBytes + "], totalBytes = [" + totalBytes + "],speed = "+task.getSpeed()+"\n");
                    scrollView.post(scroll2Bottom);

                }
            });

        }

        @Override
        public void error(FileDownloadTask task, String message) {
            try {
                super.error(task, message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLog.append("error\n");
                    scrollView.post(scroll2Bottom);
                }
            });

        }

        @Override
        public void warn(FileDownloadTask task) {
            try {
                super.warn(task);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLog.append("warn\n");
                    scrollView.post(scroll2Bottom);
                }
            });

        }

        @Override
        public void over(FileDownloadTask task) {
            try {
                super.over(task);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLog.append("over\n");
                    scrollView.post(scroll2Bottom);
                }
            });

        }
    };
    private FileDownloadTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);
        etUrl = (EditText) findViewById(R.id.etUrl);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tvLog = (TextView) findViewById(R.id.tvLog);
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        FileDownloader.getInstance().addServiceConnectListener(this);
    }
    private Runnable scroll2Bottom = new Runnable() {
        @Override
        public void run() {
            if (scrollView != null) {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                id = startDownload(etUrl.getText().toString(),new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "mobileqq_android.apk").getAbsolutePath(),listener);
//                FileDownloader.getInstance().addListener(id,listenerByAdd);
                break;
            case R.id.btnPause:
                pauseDownload(id);
                break;
            case R.id.btnDelete:
                deleteDownload();
                break;
        }

    }

    private boolean first = true;
    private int startDownload(String url, String path, FileDownloadListener listener) {
        if (first) {
            task = FileDownloader.getInstance().create(url);
            task.setPath(path).setListener(listener).setTaskName("任务名");
            first = false;
        }

        return task.start();

    }


    private void pauseDownload(int id) {
//        FileDownloader.getInstance().pauseDownload(id);
            task.pause();
    }


    private void deleteDownload() {
        FileDownloader.getInstance().cancelDownload(task);

    }

    @Override
    public void connected() {
        Log.d(TAG, "connected: ");
    }

    @Override
    public void disconnected() {
        Log.d(TAG, "disconnected: ");
    }
}
