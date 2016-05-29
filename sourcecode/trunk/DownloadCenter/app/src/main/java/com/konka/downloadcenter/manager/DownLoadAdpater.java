package com.konka.downloadcenter.manager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.konka.android.kkui.lib.KKButton;
import com.konka.downloadcenter.R;
import com.konka.downloadcenter.domain.TaskManagerModel;
import com.konka.downloadcenter.utils.Utils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.model.FileDownloadStatus;

/**
 * Created by xiaowu on 2016-5-16.
 */
public class DownLoadAdpater extends RecyclerView.Adapter<DownLoadAdpater.DownLoadViewHolder> {
    private static final String TAG = "DownLoadAdpater";
    private Context mContext;
    private OnItemFocusListener onItemFocusListener;
    private OnKeyEventListener onKeyEventListener;
//    public  FileDownloadSampleListener  fileDownloadListener=new FileDownloadSampleListener(){
//
//        @Override
//        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//            DownLoadViewHolder holder= (DownLoadViewHolder) task.getTag();
//            int progress=soFarBytes*100/totalBytes;
//            holder.tv_completed.setText(progress+"%");
//            holder.tv_state.setText(R.string.downloading);
//            holder.tv_speed.setText(Utils.getSpeedStr(task.getSpeed()));
//            holder.tv_size.setText(Utils.getDataSize(totalBytes));
//            holder.btn_operation.setText(Utils.getStringFromRes(mContext,R.string.pause));
//
////            notifyItemChanged(holder.getPosition());
//            if (holder.getPosition()==7)
//            {
//                Log.d(TAG, "onBindViewHolder: MIN_VALUE 7"+progress+"---"+task+"---"+holder);
////                holder.tv_speed.setText("haha");
//            }
//
//            if (holder.once)
//            {
//                updateTaskTotalSize(totalBytes,holder);
//            }
//
//        }
//        @Override
//        protected void completed(BaseDownloadTask task) {
//            DownLoadViewHolder holder= (DownLoadViewHolder) task.getTag();
//            holder.tv_state.setText(R.string.completed);
//            holder.tv_completed.setText("");
//            holder.tv_speed.setText("");
//            holder.btn_operation.setText(Utils.getStringFromRes(mContext,R.string.open_file));
//
////            notifyDataSetChanged();
//            updateTaskState(FileDownloadStatus.completed,holder.getPosition());
//
//        }
//        @Override
//        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//            DownLoadViewHolder holder= (DownLoadViewHolder) task.getTag();
//            holder.tv_state.setText(R.string.wait_download);
//            holder.tv_completed.setText("");
//            holder.btn_operation.setText(Utils.getStringFromRes(mContext,R.string.pause));
////            notifyDataSetChanged();
//            Log.d(TAG, "pending: totalbytes"+totalBytes);
//
//
//        }
//        @Override
//        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//            DownLoadViewHolder holder= (DownLoadViewHolder) task.getTag();
//            holder.tv_state.setText(R.string.pause);
//            holder.tv_completed.setText("");
//            holder.tv_speed.setText("");
//            holder.btn_operation.setText(Utils.getStringFromRes(mContext,R.string.continue_download));
//
////            notifyDataSetChanged();
//            updateTaskState(FileDownloadStatus.paused,holder.getPosition());
//        }
//        @Override
//        protected void error(BaseDownloadTask task, Throwable e) {
//            Log.d(TAG, "error: "+e.toString());
//            DownLoadViewHolder holder= (DownLoadViewHolder) task.getTag();
//            holder.tv_state.setText(R.string.failed);
//            holder.tv_completed.setText("");
//            holder.tv_speed.setText("");
//            holder.tv_size.setText(Utils.getStringFromRes(mContext,R.string.unkown_size));
//            holder.btn_operation.setText(Utils.getStringFromRes(mContext,R.string.download_again));
////            notifyDataSetChanged();
//            updateTaskState(FileDownloadStatus.error,holder.getPosition());
//        }
//
//        @Override
//        protected void warn(BaseDownloadTask task) {
//            DownLoadViewHolder holder= (DownLoadViewHolder) task.getTag();
//            holder.tv_speed.setText("warn");
//        }
//    };
public DownLoaderManager.DownloadUpdater mDownloadUpdater=new DownLoaderManager.DownloadUpdater(){

    @Override
    public void update(BaseDownloadTask task)
    {
        int status=task.getStatus();
        Log.d(TAG, "update: ");
        DownLoadViewHolder holder= (DownLoadViewHolder) task.getTag();
        switch (status) {
            case FileDownloadStatus.pending:
                holder.tv_state.setText(R.string.wait_download);
                holder.tv_completed.setText("");
                holder.btn_operation.setText(Utils.getStringFromRes(mContext,R.string.pause));
                break;

            case FileDownloadStatus.progress:
//                notifyItemChanged(holder.getPosition());
                int soFarBytes=task.getSmallFileSoFarBytes();
                int totalBytes=task.getSmallFileTotalBytes();
                int progress=soFarBytes*100/totalBytes;
                holder.tv_completed.setText(progress+"%");
                holder.tv_state.setText(R.string.downloading);
                holder.tv_speed.setText(Utils.getSpeedStr(task.getSpeed()));
                holder.tv_size.setText(Utils.getDataSize(totalBytes));
                holder.btn_operation.setText(Utils.getStringFromRes(mContext,R.string.pause));
                Log.d(TAG, "update: progress"+progress);

                if (holder.once)
                {
                    updateTaskTotalSize(totalBytes,holder);
                }

                break;
            case FileDownloadStatus.paused:
                holder.tv_state.setText(R.string.pause);
                holder.tv_completed.setText("");
                holder.tv_speed.setText("");
                holder.btn_operation.setText(Utils.getStringFromRes(mContext,R.string.continue_download));

                updateTaskState(FileDownloadStatus.paused,holder.getPosition());

                break;
            case FileDownloadStatus.completed:
                holder.tv_state.setText(R.string.completed);
                holder.tv_completed.setText("");
                holder.tv_speed.setText("");

                updateTaskState(FileDownloadStatus.completed,holder.getPosition());

                break;
            case FileDownloadStatus.error:
                holder.tv_state.setText(R.string.failed);
                holder.tv_completed.setText("");
                holder.tv_speed.setText("");
                holder.tv_size.setText(Utils.getStringFromRes(mContext,R.string.unkown_size));
                holder.btn_operation.setText(Utils.getStringFromRes(mContext,R.string.download_again));

                updateTaskState(FileDownloadStatus.error,holder.getPosition());
                break;


        }
    }

};

    public DownLoadAdpater(Context context)
    {
        mContext=context;
        DownLoaderManager.getImpl().addUpdater(mDownloadUpdater);
        Log.d(TAG, "onResume: addUpdater"+mDownloadUpdater);
//        setHasStableIds(true);
    }

    public void setOnKeyEventListener(OnKeyEventListener onKeyEventListener) {
        this.onKeyEventListener = onKeyEventListener;
    }

    public void setOnItemFocusListener(OnItemFocusListener onItemFocusListener) {
        this.onItemFocusListener = onItemFocusListener;
    }
    //创建View，被layoutmannager所调用
    @Override
    public DownLoadViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.downitem,parent,false);
        final DownLoadViewHolder viewHolder=new DownLoadViewHolder(itemView);
        itemView.setTag(viewHolder);
        return viewHolder;
    }
    //将数据与界面进行绑定
    @Override
    public void onBindViewHolder(DownLoadViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position--"+position);
        TaskManagerModel model=TaskManager.getImpl().get(position);

//        BaseDownloadTask baseDownloadTask= FileDownloader.getImpl().create(model.getUrl())
//                                                                    .setPath(model.getPath())
//                                                                    .setCallbackProgressTimes(20)
//                                                                    .setListener(fileDownloadListener);

//            baseDownloadTask.setTag(holder);

//            baseDownloadTask.start();
            holder.tv_name.setText(model.getName());
            holder.tv_size.setText(getTaskTotalSize(model.getTotalbytes()));

             Log.d(TAG, "onBindViewHolder: totalsize"+getTaskTotalSize(model.getTotalbytes()));

//        if (TaskManager.getImpl().isReady()) {
//            final int status = TaskManager.getImpl().getStatus(model.getId());
        final int status = model.getState();
        Log.d(TAG, "onBindViewHolder: status--"+status);
        switch (status) {
                case FileDownloadStatus.pending:
                case FileDownloadStatus.started:
                case FileDownloadStatus.connected:
                    holder.tv_state.setText(R.string.wait_download);
                    Log.d(TAG, "onBindViewHolder: switch "+"等待下载");
                    holder.btn_operation.setText(R.string.pause);
//                    baseDownloadTask.start();
                    DownLoaderManager.getImpl().getDownloadTask(model.getUrl(),model.getPath(),20,holder);
                    break;
                case FileDownloadStatus.progress:
                    holder.tv_state.setText(R.string.downloading);
                    Log.d(TAG, "onBindViewHolder: switch "+"下载中");
                    holder.btn_operation.setText(R.string.pause);
//                    if (!baseDownloadTask.isUsing())
//                        baseDownloadTask.start();
                    DownLoaderManager.getImpl().getDownloadTask(model.getUrl(),model.getPath(),20,holder);
                    break;
                case FileDownloadStatus.paused:
                    holder.tv_state.setText(R.string.pause);
                    Log.d(TAG, "onBindViewHolder: switch "+"暂停");
                    holder.btn_operation.setText(R.string.continue_download);
                    break;
                case FileDownloadStatus.completed:
                    holder.tv_state.setText(R.string.completed);
                    Log.d(TAG, "onBindViewHolder: switch "+"已完成");
                    holder.btn_operation.setText(R.string.open_file);
                    break;
                case FileDownloadStatus.error:
                    holder.tv_state.setText(R.string.failed);
                    Log.d(TAG, "onBindViewHolder: switch "+"下载失败");
                    holder.btn_operation.setText(R.string.download_again);
//                    baseDownloadTask.start();
                    DownLoaderManager.getImpl().getDownloadTask(model.getUrl(),model.getPath(),20,holder);
                    break;
                case Integer.MIN_VALUE:
//                    Log.d(TAG, "onBindViewHolder: MIN_VALUE"+holder.getPosition()+"---"+baseDownloadTask+"----"+baseDownloadTask.getTag()+"----now:"+holder);
//                            holder.tv_speed.setText("default");
//                    if (!baseDownloadTask.isUsing())
//                            baseDownloadTask.start();

                    DownLoaderManager.getImpl().getDownloadTask(model.getUrl(),model.getPath(),20,holder);

            }
//        }


    }

    @Override
    public int getItemCount() {
        return TaskManager.getImpl().getTaskCount();
    }

    private String getTaskTotalSize(long totalbytes) {
        if (totalbytes==Integer.MIN_VALUE)
        {
            return Utils.getStringFromRes(mContext,R.string.unkown_size);
        }else{
            return Utils.getDataSize(totalbytes);
        }
    }

    public void updateTaskState(int state,int position)
    {
        TaskManagerModel model=TaskManager.getImpl().get(position);
        if (model!=null) {
            model.setState(state);
            TaskManager.getImpl().updateTask(model);
        }
    }

    public void updateTaskTotalSize(int totalBytes,DownLoadViewHolder holder)
    {
        TaskManagerModel model=TaskManager.getImpl().get(holder.getPosition());
//        Log.d(TAG, "updateTaskTotalSize: holder.getposition"+holder.getPosition());
        if (model!=null) {
            model.setTotalbytes(totalBytes);
            TaskManager.getImpl().updateTask(model);
            holder.once = false;
        }
    }

    class DownLoadViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_name;
        public TextView tv_size;
        public TextView tv_state;
        public TextView tv_completed;
        public TextView tv_speed;
        public LinearLayout ll_menu;
        public ImageView iv_more;
        public KKButton btn_operation;
        public KKButton btn_del;
        public boolean once=true;//只调用一次 将文件大小写入数据库

        public DownLoadViewHolder(final View itemView) {
            super(itemView);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_size= (TextView) itemView.findViewById(R.id.tv_size);
            tv_state= (TextView) itemView.findViewById(R.id.tv_state);
            tv_completed= (TextView) itemView.findViewById(R.id.tv_completed);
            tv_speed= (TextView) itemView.findViewById(R.id.tv_speed);
            iv_more= (ImageView) itemView.findViewById(R.id.iv_more);
            ll_menu= (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.menu,null).findViewById(R.id.menu);
            btn_operation= (KKButton) ll_menu.findViewById(R.id.btn_operation);
            btn_del= (KKButton) ll_menu.findViewById(R.id.btn_del);

//            btn_del.setKkButtonBg(R.drawable.menu_btn_back);
//            btn_operation.setKkButtonBg(new ColorDrawable(Color.argb(0,0,0,0)));

            itemView.setFocusable(true);
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
//                    if (onItemFocusListener!=null)
//                        onItemFocusListener.OnItemFocus(view,b);

                    if (b)
                    {
                        iv_more.setVisibility(View.VISIBLE);
                    }else{
                        iv_more.setVisibility(View.GONE);
                    }

                }
            });
            itemView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keycode, KeyEvent keyEvent) {

                    if (onKeyEventListener != null&&keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                        return  onKeyEventListener.OnKeyEvent(view,keycode,keyEvent,getPosition());
                    }
                    return false;
                }
            });
        }
    }

    interface OnItemFocusListener{
        void OnItemFocus(View view, boolean hasFocus);
    }

    interface OnKeyEventListener{
        boolean OnKeyEvent(View view, int keycode, KeyEvent keyEvent,int position);
    }

}
