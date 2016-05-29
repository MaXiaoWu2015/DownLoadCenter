package com.konka.downloadcenter.manager;

import android.util.Log;

import com.konka.downloadcenter.APP;
import com.konka.downloadcenter.database.TasksManagerDBController;
import com.konka.downloadcenter.domain.TaskManagerModel;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by xiaowu on 2016-5-20.
 */
public class TaskManager {
    private static final String TAG = "TaskManager";

    private final static class HolderClass {
        private final static TaskManager INSTANCE
                = new TaskManager();
    }

    public static TaskManager getImpl() {
        return HolderClass.INSTANCE;

    }

    private List<TaskManagerModel> mTaskList;
    private TasksManagerDBController mController;
    private FileDownloadConnectListener listener;
    public TaskManager() {
        mController=new TasksManagerDBController(APP.CONTEXT);
        initData();
        mTaskList=mController.getAllTasks();

        for (TaskManagerModel model:mTaskList)
        {
            Log.d(TAG, "TaskManager: getAllTasks "+model.getUrl()+"--"+model.getName());
        }

    }
    public int getTaskCount()
    {
        return mTaskList.size();
    }
    public  TaskManagerModel  get(int position)
    {
        if (position>=0&&position<mTaskList.size())
        {
            return mTaskList.get(position);
        }else{
            return null;
        }
    }
    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }
    public int getStatus(final  int id)
    {
        return FileDownloader.getImpl().getStatus(id);
    }
    public int getSoFar(final  int id)
    {
        return (int) FileDownloader.getImpl().getSoFar(id);
    }
    public int getTotal(final  int id)
    {
        return (int) FileDownloader.getImpl().getTotal(id);
    }

    public void addTask()
    {

    }
    public void updateTask(TaskManagerModel model)
    {
        mController.updateTask(model);
    }
    public boolean deleteTask(final int id,final int position)
    {
           boolean success=mController.deleteTask(id);
          if (success)
          {
             mTaskList.remove(position);
             FileDownloader.getImpl().pause(id);
          }
        return success;
    }

    public void onCreate(final WeakReference<MainActivity> activityWeakReference) {
        FileDownloader.getImpl().bindService();
        if (listener != null) {
            FileDownloader.getImpl().removeServiceConnectListener(listener);
        }
        listener = new FileDownloadConnectListener() {
            @Override
            public void connected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }

                activityWeakReference.get().postNotifyDataChanged();
            }
            @Override
            public void disconnected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }

                activityWeakReference.get().postNotifyDataChanged();
            }
        };

        FileDownloader.getImpl().addServiceConnectListener(listener);
    }

    public void onDestroy() {
        FileDownloader.getImpl().removeServiceConnectListener(listener);
        listener = null;
    }

    public void initData()
    {
        mController.addTask("http://download.chinaunix.net/down.php?id=10608&ResourceID=5267&site=1",APP.CONTEXT.getFilesDir()+"/test1","test1",Integer.MIN_VALUE,Integer.MIN_VALUE);
        mController.addTask("http://180.153.105.144/dd.myapp.com/16891/E2F3DEBB12A049ED921C6257C5E9FB11.apk",APP.CONTEXT.getFilesDir()+"/test2.apk","test2",Integer.MIN_VALUE,Integer.MIN_VALUE);
        mController.addTask("http://7xjww9.com1.z0.glb.clouddn.com/Hopetoun_falls.jpg",APP.CONTEXT.getFilesDir()+"/test3","test3",Integer.MIN_VALUE,Integer.MIN_VALUE);
        mController.addTask("http://download.chinaunix.net/down.php?id=10608&ResourceID=5267&site=2",APP.CONTEXT.getFilesDir()+"/test4","test4",Integer.MIN_VALUE,Integer.MIN_VALUE);

        mController.addTask("http://113.207.16.84/dd.myapp.com/16891/2E53C25B6BC55D3330AB85A1B7B57485.apk?mkey=5630b43973f537cf&f=cf87&fsname=com.htshuo.htsg_3.0.1_49.apk&asr=02f1&p=.apk",APP.CONTEXT.getFilesDir()+"/test5.apk","这是一个名字很长很长很长很长的应用名字",Integer.MIN_VALUE,Integer.MIN_VALUE);
        mController.addTask("http://dg.101.hk/1.rar",APP.CONTEXT.getFilesDir()+"/test6.rar","这是一个压缩文件",Integer.MIN_VALUE,Integer.MIN_VALUE);
        mController.addTask("http://www.pc6.com/down.asp?id=72873",APP.CONTEXT.getFilesDir()+"/test7","test7",Integer.MIN_VALUE,Integer.MIN_VALUE);
        mController.addTask("http://180.153.105.144/down.asp?id=72873",APP.CONTEXT.getFilesDir()+"/test8","test8",Integer.MIN_VALUE,Integer.MIN_VALUE);
    }
}
