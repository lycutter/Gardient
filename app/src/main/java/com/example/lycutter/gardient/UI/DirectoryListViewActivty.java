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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lycutter.gardient.R;

import java.io.File;
import java.util.ArrayList;

import entity.DirectoryInfo;
import utils.FileScan;

public class DirectoryListViewActivty extends Activity implements View.OnClickListener {

    private TextView canusememory;
    private TextView canuseSDmemory;
    private ImageButton directoryReturn;
    private TextView currentDirectory;
    private ListView directoryListView;
    private ImageButton back;
    private FileScan fileScan;
    public static final String defaultPath = "/sdcard";
    private DirectoryInfo directoryInfo;
    private DirectoryListAdapter directoryListAdapter;

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

        back.setOnClickListener(this);
        directoryReturn.setOnClickListener(this);

        fileScan = new FileScan();
        final DirectoryInfo mDirectoryInfo = fileScan.getFileDirectory(defaultPath);
        createFileListView(mDirectoryInfo);
    }

    private void createFileListView(final DirectoryInfo mDirectoryInfo) {
        directoryListAdapter = new DirectoryListAdapter(this, mDirectoryInfo);
        directoryListView.setAdapter(directoryListAdapter);
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
        switch (v.getId()){
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
        private LayoutInflater mInflater;

        public DirectoryListAdapter(Context context, DirectoryInfo directoryInfo) {
            mInflater = LayoutInflater.from(context);
            directoryName = directoryInfo.directoryName;
            childAmount = directoryInfo.childDirectoryContain;
        }

        @Override
        public int getCount() {
            return directoryName == null ? 0 : directoryName.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.directorty_list_items, null);
            viewHolder.directoryImage = convertView.findViewById(R.id.directory_item_image);
            viewHolder.directoryName = convertView.findViewById(R.id.directory_item_name);
            viewHolder.directoryChildAmount = convertView.findViewById(R.id.directory_item_childamount);

            viewHolder.directoryName.setText(directoryName.get(position));
            viewHolder.directoryChildAmount.setText(childAmount.get(position));
            viewHolder.directoryImage.setImageResource(R.drawable.ic_dxhome_file_manager);


            return convertView;
        }

        private class ViewHolder {
            public ImageView directoryImage;
            public TextView directoryName;
            public TextView directoryChildAmount;
        }
    }
}
