package com.konka.downloadcenter.manager;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.konka.android.kkui.lib.KKActivity;
import com.konka.android.kkui.lib.KKButton;
import com.konka.android.kkui.lib.KKDialog;
import com.konka.android.kkui.lib.KKToast;
import com.konka.downloadcenter.R;
import com.konka.downloadcenter.domain.TaskManagerModel;
import com.liulishuo.filedownloader.FileDownloadMonitor;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

public class MainActivity extends KKActivity {
    private static final String TAG = "MainActivity";
    private View mShadowBg;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTitle;
    private TextView mNoTask;
    private DownLoadAdpater mAdapter;
    private FrameLayout mRlMain;//MainActivity的布局
    private View mCurrentItemView;//当前选中item


    //menu
    private LinearLayout ll_menu;
    private KKButton btn_operation;
    private KKButton btn_del;
    private boolean once = true;
    private int mCurrentItemPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setmDefaultBlockDrawble(getResources().getDrawable(R.drawable.block));
        initView();
        FileDownloadMonitor.setGlobalMonitor(FileDownloadMonitor.getMonitor());
//        DownLoaderManager.getImpl().setmDownloadUpdater(mAdapter.mDownloadUpdater);
//        DownLoaderManager.getImpl().addUpdater(mAdapter.mDownloadUpdater);
//        TaskManager.getImpl().onCreate(new WeakReference<>(this));
    }

    private void initView() {
        mShadowBg=findViewById(R.id.shadow_bg);

        mRlMain = (FrameLayout) findViewById(R.id.rl_main);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mNoTask = (TextView) findViewById(R.id.tv_notask);


        mRecyclerView = (RecyclerView) findViewById(R.id.rc_downloadlist);
        if (TaskManager.getImpl().getTaskCount()>0)
       {
           initRecyclerView();
       }else{
           mRecyclerView.setVisibility(View.GONE);
           mNoTask.setVisibility(View.VISIBLE);
       }

    }

    private void initBtnListener() {
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRlMain.removeView(ll_menu);
                mShadowBg.setVisibility(View.GONE);
                btnFocus_HandleMenu(mCurrentItemView);
                showConfirmDialog();
            }
        });
        View.OnKeyListener btnOnKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                switch (keycode) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        btnFocus_HandleMenu(btn_del);
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        btnFocus_HandleMenu(btn_operation);
                        break;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        btnFocus_HandleMenu(view);
                        break;

                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        btnFocus_HandleMenu(view);
                        break;
                    case KeyEvent.KEYCODE_BACK:
                        if (ll_menu.getVisibility() == View.VISIBLE) {
//                            setMenuVisibility(View.GONE);
                            mRlMain.removeView(ll_menu);
                            mShadowBg.setVisibility(View.GONE);
                            btnFocus_HandleMenu(mCurrentItemView);
                            return true;
                        }
                        break;
                }
                return false;
            }
        };
        btn_del.setOnKeyListener(btnOnKeyListener);
        btn_operation.setOnKeyListener(btnOnKeyListener);

        btn_operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence action =btn_operation.getText();
                Log.d(TAG, "onClick: mCurrentItemPosition  " + mCurrentItemPosition);
                if (action.equals(getResources().getString(R.string.open_file)))
                {
                   File file=new File(TaskManager.getImpl().get(mCurrentItemPosition).getPath());
                    String fileName=file.getName();
                    Log.d(TAG, "onClick: fileName"+fileName);
                    String prefix=fileName.substring(fileName.lastIndexOf(".")+1);

                    if (file.exists() && ("apk".equals(prefix))){
                        openInstallPage(file);
                    } else{
                        KKToast.makeText(MainActivity.this,"不支持此文件类型").show();
                    }
                }else if (action.equals(getResources().getString(R.string.pause))) {
                    FileDownloader.getImpl().pause(TaskManager.getImpl().get(mCurrentItemPosition).getId());

                }else if (action.equals(getResources().getString(R.string.download_again))||
                            action.equals(getResources().getString(R.string.continue_download)))
                {
                    final TaskManagerModel model = TaskManager.getImpl().get(mCurrentItemPosition);
//                    final BaseDownloadTask task = FileDownloader.getImpl().create(model.getUrl())
//                            .setPath(model.getPath())
//                            .setCallbackProgressTimes(20);
//                    task.setListener(mAdapter.fileDownloadListener);
//                    BaseDownloadTask task=DownLoaderManager.getImpl().getDownloadTask(model.getUrl(),model.getPath(),20, holder);
                    DownLoaderManager.getImpl().getDownloadTask(model.getUrl(),model.getPath(),20, (DownLoadAdpater.DownLoadViewHolder) mCurrentItemView.getTag());
//                    task.setTag(mCurrentItemView.getTag());
//                    task.start();
                }
            }
        });
    }
    /**
     * 打开系统安装页面
     * */
    private void openInstallPage(File file)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }


    public void btnFocus_HandleMenu(View view) {
        reviseFocusBlockPositionAndSize(view, new MyFocusAnimatorListener() {
            @Override
            public void onAnimationEnd(View view, Animator animator) {
                view.setFocusable(true);
                view.requestFocus();
//                Log.d(TAG, "onAnimationEnd: CurFocus  "+mCurrentItemView.findFocus());
            }
        });
    }

    private void showConfirmDialog() {
        final KKDialog.Builder builder = new KKDialog.Builder(this);
        builder.setMessage("确定要中止下载并删除该任务吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Log.d(TAG, "onClick: position--"+mCurrentItemPosition);
                TaskManagerModel model = TaskManager.getImpl().get(mCurrentItemPosition);
                if (TaskManager.getImpl().deleteTask(model.getId(), mCurrentItemPosition)) {
                    File file = new File(model.getPath());
                    if (file.exists()) {
                        file.delete();
                    }
                    mAdapter.notifyItemRemoved(mCurrentItemPosition);
                    setFocusAfterRemove();

                } else {
                    KKToast.makeText(MainActivity.this, R.string.delete_failed).show();
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

/**
 * RecyclerView删除item后，设置焦点位置
 * */
    private void setFocusAfterRemove(){
        if ((mCurrentItemPosition!=mRecyclerView.getChildCount()-1)&&mRecyclerView.getChildCount()>1)
        {
            Log.d(TAG, "onClick: focusposition--"+mCurrentItemPosition);
            mRecyclerView.getChildAt(mCurrentItemPosition+1).requestFocus();
            reviseFocusBlockPositionAndSize(mCurrentItemView, new MyFocusAnimatorListener() {
                @Override
                public void onAnimationEnd(View view, Animator animator) {
                    view.setFocusable(true);
                }
            });
        }else if (mRecyclerView.getChildCount()==1){
            Log.d(TAG, "onClick: position--"+mCurrentItemPosition);
            mRecyclerView.setVisibility(View.GONE);
            mNoTask.setVisibility(View.VISIBLE);
            setFocusBlockViewVisiblable(View.GONE);

        }else if (mCurrentItemPosition==mRecyclerView.getChildCount()-1){
            btnFocus_HandleMenu(mRecyclerView.getChildAt(mCurrentItemPosition-1));
        }
    }


    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DownLoadAdpater(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL_LIST));

        Log.d(TAG, "initRecyclerView: childcount:" + mRecyclerView.getChildCount());


        mRecyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b&&once) {
                    if (mRecyclerView.getChildCount() > 0) {
                        Log.d(TAG, "initRecyclerView: childcount:" + mRecyclerView.getChildCount());

                        mRecyclerView.getChildAt(0).requestFocus();
                        once = false;
                    }
                }
            }
        });
        mAdapter.setOnKeyEventListener(new DownLoadAdpater.OnKeyEventListener() {

            @Override
            public boolean OnKeyEvent(View view, int keycode, KeyEvent keyEvent, int position) {
                mCurrentItemView = view;
                mCurrentItemPosition = position;
//                Log.d(TAG, "OnKeyEvent: position  "+position);
                switch (keycode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:

                        showMenu(view, keyEvent);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        return true;
                }
                return false;
            }
        });


    }

    /*
    * 设置Menu的位置，并让暂停/删除按钮获取焦点
    * */
    public void showMenu(View view, KeyEvent keyEvent) {
            setMenuPosition(view);
              reviseFocusBlockPositionAndSize(btn_operation,true,true,5, new MyFocusAnimatorListener() {

            @Override
            public void onAnimationEnd(View view, Animator animator) {
                btn_del.setFocusable(true);
                btn_operation.setFocusable(true);
                btn_operation.requestFocus();
            }
        });

    }

    //设置Menu位置
    public void setMenuPosition(View v) {
        ll_menu = ((DownLoadAdpater.DownLoadViewHolder) mCurrentItemView.getTag()).ll_menu;
        if (ll_menu.getParent() != null) {
            ViewGroup parent = (ViewGroup) ll_menu.getParent();
            parent.removeView(ll_menu);
        }
        mRlMain.addView(ll_menu);
        mShadowBg.setVisibility(View.VISIBLE);
        btn_operation = (KKButton) ll_menu.findViewById(R.id.btn_operation);
        btn_del = (KKButton) ll_menu.findViewById(R.id.btn_del);
        initBtnListener();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ll_menu.getLayoutParams();
        params.width = 500;
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.RIGHT;
        ll_menu.setLayoutParams(params);
        ll_menu.setVisibility(View.VISIBLE);
    }


    public void postNotifyDataChanged() {
        if (mAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        DownLoaderManager.getImpl().addUpdater(mAdapter.mDownloadUpdater);
//        Log.d(TAG, "onResume: addUpdater"+mAdapter.mDownloadUpdater);
//    }

    @Override
    protected void onDestroy() {

//            TaskManager.getImpl().onDestroy();
     DownLoaderManager.getImpl().removeUpdater(mAdapter.mDownloadUpdater);
        mAdapter=null;
        finish();
        super.onDestroy();


    }



}
