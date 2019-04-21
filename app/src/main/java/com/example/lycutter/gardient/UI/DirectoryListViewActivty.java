package com.example.lycutter.gardient.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lycutter.gardient.R;

import java.io.File;
import java.util.ArrayList;

import Adapter.FileLongClickListAdapter;
import entity.DirectoryInfo;
import utils.FileScan;

public class DirectoryListViewActivty extends Activity implements View.OnClickListener {

    private TextView canusememory;
    private TextView canuseSDmemory;
    private ImageButton directoryReturn;
    private TextView currentDirectory;
    private ListView directoryListView;  //文件的listView
    private ImageButton back;
    private FileScan fileScan;
    public static final String defaultPath = "/sdcard";
    private DirectoryInfo directoryInfo;
    private DirectoryListAdapter directoryListAdapter;
    private File mFile;
    private String completedFileName;
    private ArrayList fileOperationList;  // 长按文件有什么操作
    private ListView mFileOperationListView; //文件操作的listview
    private View fileOperationListView; // 获取文件操作listView的布局
    private FileLongClickListAdapter fileOperationListAdapter;

    private int screenHeight;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_list_view_activty);
        initView();

        //设置监听
        FileListener();
    }

    private void FileListener() {


        fileOperationList = new ArrayList();
        fileOperationList.add("复制文件");
        fileOperationList.add("删除文件");
        fileOperationList.add("剪切文件");

    }

    private void initView() {
        canusememory = findViewById(R.id.canusemenmery);
        canuseSDmemory = findViewById(R.id.canuseSDmenmery);
        back = findViewById(R.id.back);
        directoryReturn = findViewById(R.id.dialog_save_turnback);
        currentDirectory = findViewById(R.id.dialog_save_path);
        directoryListView = findViewById(R.id.dir_listview);

        back.setOnClickListener(this);
        directoryReturn.setOnClickListener(this);

        fileScan = new FileScan();
        final DirectoryInfo mDirectoryInfo = fileScan.getFileDirectory(defaultPath);
        createFileListView(mDirectoryInfo);
    }

    private void createFileListView(final DirectoryInfo mDirectoryInfo) {
        directoryListAdapter = new DirectoryListAdapter(this, mDirectoryInfo, mDirectoryInfo.currentDirectory + "/");
        directoryListView.setAdapter(directoryListAdapter);

//        directoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                fileOperationListView = getLayoutInflater().inflate(R.layout.layout_fileoperation, null, true);
//                final PopupWindow fileOperationWindow = new PopupWindow(fileOperationListView, 300, 300);
//                fileOperationWindow.setFocusable(true);
//                fileOperationWindow.setOutsideTouchable(true);
//                fileOperationWindow.setBackgroundDrawable(new BitmapDrawable());
//                int[] windowPos = CalPopWindowLocation(view, parent);
//                fileOperationWindow.showAtLocation(fileOperationListView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
//                mFileOperationListView = fileOperationListView.findViewById(R.id.lv_fileoperation);
//                fileOperationListAdapter = new FileLongClickListAdapter(DirectoryListViewActivty.this, fileOperationList);
//                mFileOperationListView.setAdapter(fileOperationListAdapter);
//                mFileOperationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        if (fileOperationWindow != null) {
//                            fileOperationWindow.dismiss();
//                        }
//                        switch (position) {
//                            case 0: {
//                                //复制文件
//                                break;
//                            }
//                            case 1: {
//                                //删除文件
//                            }
//                            case 2: {
//                                //剪切文件
//                            }
//                        }
//                    }
//                });
//                return true; // 返回true代表消费完了该事件，false代表没消费完
//            }
//        });

        directoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileItem = mDirectoryInfo.currentDirectory + "/" + mDirectoryInfo.directoryName.get(position);
                File file = new File(fileItem);

                if (file.isFile()) { // 点击的是文件
                    Toast.makeText(DirectoryListViewActivty.this, "点击了文件", Toast.LENGTH_SHORT).show();
                } else { //点击的是文件夹
                    directoryInfo = fileScan.getFileDirectory(fileItem);
                    createFileListView(directoryInfo);
                }
            }
        });

        currentDirectory.setText(mDirectoryInfo.currentDirectory);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                finish();
                break;
            }
            case R.id.dialog_save_turnback: {
                if (!directoryInfo.currentDirectory.equals("/sdcard")) {
                    directoryInfo = fileScan.getFileDirectory(directoryInfo.fatherDirectory);
                    createFileListView(directoryInfo);
                }

                break;
            }
        }
    }

    private class DirectoryListAdapter extends BaseAdapter {

        private ArrayList<String> directoryName, childAmount;
        String completedFilePath;
        private LayoutInflater mInflater;

        public DirectoryListAdapter(Context context, DirectoryInfo directoryInfo, String completedPath) {
            mInflater = LayoutInflater.from(context);
            directoryName = directoryInfo.directoryName;
            childAmount = directoryInfo.childDirectoryContain;
            completedFilePath = completedPath;
        }

        @Override
        public int getCount() {
            return directoryName == null ? 0 : directoryName.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
//            return directoryName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
//            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.directorty_list_items, null);
            viewHolder.directoryImage = convertView.findViewById(R.id.directory_item_image);
            viewHolder.directoryName = convertView.findViewById(R.id.directory_item_name);
            viewHolder.directoryChildAmount = convertView.findViewById(R.id.directory_item_childamount);

            viewHolder.directoryName.setText(directoryName.get(position));
            completedFileName = completedFilePath + directoryName.get(position);
            mFile = new File(completedFileName);

            String fileEnds = completedFileName.substring(completedFileName.lastIndexOf(".") + 1, completedFileName.length()).toLowerCase();//取出文件后缀名并转成小写

            if (directoryName.contains("com.google.android.apps.nexuslaun")) {
                System.out.println("long name = " + completedFilePath);
                System.out.println("short name = " + directoryName.get(position));
            }

            viewHolder.directoryChildAmount.setText(childAmount.get(position));
            if (!mFile.isDirectory()) {
//                viewHolder.directoryImage.setImageResource(R.drawable.file_image);
                if (fileEnds.equals("m4a") || fileEnds.equals("mp3") || fileEnds.equals("mid") || fileEnds.equals("xmf") || fileEnds.equals("ogg") || fileEnds.equals("wav")) {
                    viewHolder.directoryImage.setImageResource(R.drawable.audio);
                } else if (fileEnds.equals("3gp") || fileEnds.equals("mp4")) {
                    viewHolder.directoryImage.setImageResource(R.drawable.video);
                } else if (fileEnds.equals("jpg") || fileEnds.equals("gif") || fileEnds.equals("png") || fileEnds.equals("jpeg") || fileEnds.equals("bmp")) {
                    viewHolder.directoryImage.setImageResource(R.drawable.image);
                } else if (fileEnds.equals("apk")) {
                    viewHolder.directoryImage.setImageResource(R.drawable.apk);
                } else if (fileEnds.equals("txt")) {
                    viewHolder.directoryImage.setImageResource(R.drawable.txt);
                } else if (fileEnds.equals("zip") || fileEnds.equals("rar")) {
                    viewHolder.directoryImage.setImageResource(R.drawable.zip_icon);
                } else if (fileEnds.equals("html") || fileEnds.equals("htm") || fileEnds.equals("mht")) {
                    viewHolder.directoryImage.setImageResource(R.drawable.web_browser);
                } else {
                    viewHolder.directoryImage.setImageResource(R.drawable.others);
                }
            } else {
                viewHolder.directoryImage.setImageResource(R.drawable.folder);

            }
            return convertView;
        }

        private class ViewHolder {
            public ImageView directoryImage;
            public TextView directoryName;
            public TextView directoryChildAmount;
        }
    }

    /**
     * 计算popwindow的弹出位置
     * @param anchorView
     * @param contentView
     * @return
     */
    private int[] CalPopWindowLocation(View anchorView, View contentView) {

        screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽

        screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;

    }
}


