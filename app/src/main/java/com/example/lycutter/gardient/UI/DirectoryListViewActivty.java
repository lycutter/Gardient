package com.example.lycutter.gardient.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lycutter.gardient.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private FileLongClickListAdapter fileOperationListAdapter;
    private LinearLayout mFileOperationList; //文件操作菜单
    private LinearLayout copyFile;
    private LinearLayout deleteFile;
    private LinearLayout pasteFile;

    private String mOldFilePath = "";
    private String mNewFilePath = "";
    private String mCurrentFile;
    private static FileInputStream mFileInputStream;
    private static FileOutputStream mFileOutputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_list_view_activty);
        initView();
    }



    private void initView() {
        canusememory = findViewById(R.id.canusemenmery);
        canuseSDmemory = findViewById(R.id.canuseSDmenmery);
        back = findViewById(R.id.back);
        directoryReturn = findViewById(R.id.dialog_save_turnback);
        currentDirectory = findViewById(R.id.dialog_save_path);
        directoryListView = findViewById(R.id.dir_listview);
        mFileOperationList = findViewById(R.id.file_operation_list);
        copyFile = mFileOperationList.findViewById(R.id.ll_copy_file);
        deleteFile = mFileOperationList.findViewById(R.id.ll_delete_file);
        pasteFile = mFileOperationList.findViewById(R.id.ll_paste_file);

        back.setOnClickListener(this);
        directoryReturn.setOnClickListener(this);
        copyFile.setOnClickListener(this);
        deleteFile.setOnClickListener(this);
        pasteFile.setOnClickListener(this);

        fileScan = new FileScan();
        final DirectoryInfo mDirectoryInfo = fileScan.getFileDirectory(defaultPath);
        createFileListView(mDirectoryInfo);
    }

    private void createFileListView(final DirectoryInfo mDirectoryInfo) {
        directoryListAdapter = new DirectoryListAdapter(this, mDirectoryInfo, mDirectoryInfo.currentDirectory + "/");
        directoryListView.setAdapter(directoryListAdapter);
        directoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mFileOperationList.getLayoutParams(); //记得要强转类型
                params.height = 200;
                mFileOperationList.setLayoutParams(params);
                mCurrentFile = mDirectoryInfo.currentDirectory + "/" + mDirectoryInfo.directoryName.get(position);
                mOldFilePath = mDirectoryInfo.currentDirectory;
                System.out.println("currentFileName = " + mDirectoryInfo.directoryName.get(position));
                System.out.println("mCurrentFile = " + mCurrentFile);
                System.out.println("mOldFilePath = " + mOldFilePath);
                return true;
            }
        });

        mNewFilePath = mDirectoryInfo.currentDirectory;
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
    public void onClick(View v){
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
            case R.id.ll_copy_file: {
                CopyOperation();
                break;
            }
            case R.id.ll_delete_file: {
                DeleteOperation();
                break;
            }
            case R.id.ll_paste_file: {
                try{
                    PasteOperation();
                } catch (Exception e) {
                    e.printStackTrace();
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



    private void CopyOperation() {
        File file = new File(mCurrentFile);
        if (file.isFile()) {
            Toast.makeText(DirectoryListViewActivty.this, "复制了文件", Toast.LENGTH_SHORT).show();
            CopyFile(mCurrentFile);
        } else {
            Toast.makeText(DirectoryListViewActivty.this, "失败", Toast.LENGTH_SHORT).show();
            CopyDirectory(mCurrentFile);
        }
    }

    private void PasteOperation() throws Exception{
        File file = new File(mCurrentFile);
        if (file.isFile()) {
            PasteFile(mCurrentFile);
        } else {
            PasteDirectory(mCurrentFile);
        }
    }

    private void CopyFile(String mCurrentFile) {
        mFileInputStream = null;
        try {
            mFileInputStream = new FileInputStream(mCurrentFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("复制文件失败");
        }

        if (mFileInputStream == null) {
            Toast.makeText(DirectoryListViewActivty.this, "复制文件失败", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void CopyDirectory(String mCurrentFile) {

    }

    private void PasteFile(String mCurrentFile) throws Exception{

        String fileName = mCurrentFile.substring(mCurrentFile.lastIndexOf("/") + 1);


        String newFileAbsolutePath = mNewFilePath + "/" + fileName;
        File file = new File(newFileAbsolutePath);

        System.out.println("当前绝对路径是" + file.getAbsolutePath());
        if (file.exists()) {
            Toast.makeText(DirectoryListViewActivty.this, "文件已存在", Toast.LENGTH_SHORT).show();
            return;
        } else {
            try {
                mFileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            } catch (FileNotFoundException e) {
                System.out.println(e);
                System.out.println("FileOutputStream抛出异常");
            }
            int byteRead;
            int byteSum = 0;
            byte[] buffer = new byte[1024];

            System.out.println("正在写入新文件");
            while ((byteRead = mFileInputStream.read(buffer)) != -1) {
                byteSum += byteRead; //字节数 文件大小
                System.out.println(byteSum);
                mFileOutputStream.write(buffer, 0, byteRead);
                System.out.println("正在写入");
            }
            mFileInputStream.close();
            mFileOutputStream.flush();
            mFileOutputStream.close();

            createFileListView(fileScan.getFileDirectory(mNewFilePath));
            directoryListAdapter.notifyDataSetChanged();
            directoryListView.invalidateViews();

            Toast.makeText(DirectoryListViewActivty.this, "粘贴完成", Toast.LENGTH_SHORT).show();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mFileOperationList.getLayoutParams(); //记得要强转类型
            params.height = 0;
            mFileOperationList.setLayoutParams(params);
        }

    }

    private void PasteDirectory(String mCurrentFile) {

    }

    private void DeleteOperation() {
        File file = new File(mCurrentFile);
        if (file.isFile()) {
            DeleteFile(mCurrentFile);
        } else {
            DeleteDirectory(mCurrentFile);
        }
    }

    private void DeleteFile(String mCurrentFile) {
        File currentFile = new File(mCurrentFile);
        createFileListView(fileScan.getFileDirectory(mCurrentFile.substring(mCurrentFile.lastIndexOf("/") + 1)));
        if (currentFile.delete()) {
            Toast.makeText(DirectoryListViewActivty.this, "删除文件成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DirectoryListViewActivty.this, "删除文件失败", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(DirectoryListViewActivty.this, "粘贴完成", Toast.LENGTH_SHORT).show();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mFileOperationList.getLayoutParams(); //记得要强转类型
        params.height = 0;
        mFileOperationList.setLayoutParams(params);
    }

    private void DeleteDirectory(String mCurrentFile) {

    }
}


