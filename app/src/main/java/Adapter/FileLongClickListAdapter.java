package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.lycutter.gardient.R;

import java.util.ArrayList;

public class FileLongClickListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mFileOperationList;

    public FileLongClickListAdapter(Context context, ArrayList fileOperationList) {
        mContext = context;
        mFileOperationList = fileOperationList;
    }

    @Override
    public int getCount() {
        return mFileOperationList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.from(mContext).inflate(R.layout.layout_fileoperationitem, null);
        TextView tv = convertView.findViewById(R.id.tv_fileoperationitem);
        tv.setText(mFileOperationList.get(position));
        return convertView;
    }
}
