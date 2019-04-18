package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lycutter.gardient.R;

import java.util.ArrayList;
import java.util.List;

import entity.PopItem;

public class MyListViewAdapter extends BaseAdapter {

    private ArrayList<PopItem> list = new ArrayList<>();
    private Context context;

    public MyListViewAdapter(Context context, ArrayList list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public PopItem getItem(int position) {
//        return list.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
//        return position;
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        PopItem item = getItem(position);
//        convertView = LayoutInflater.from(context).inflate(R.layout.pop_item, null);
//        TextView tv = convertView.findViewById(R.id.text);
//        tv.setText(item.name);
//        return convertView;

        convertView = LayoutInflater.from(context).inflate(R.layout.pop_item, null);
        TextView tv = convertView.findViewById(R.id.text);
        tv.setText(list.get(position).name);
        return convertView;
    }
}
