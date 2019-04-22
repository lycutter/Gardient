package com.example.lycutter.gardient.UI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lycutter.gardient.R;

import java.util.ArrayList;

import Adapter.MyListViewAdapter;
import Adapter.MyPagerAdapter;
import entity.PopItem;

public class MainActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TextView tools;
    private TextView opitmization;
    private TextView appmanager;
    private ViewPager mViewpager;
    private ImageView trans_bar;
    private View viewTools;
    private View viewOptimization;
    private View viewAppmanager;
    private ArrayList<View> pageList;
    private TextView curTv;
    private TextView[] mViews;
    private ImageView more;
    private PopItem item;
    private ArrayList<PopItem> list = new ArrayList<>();
    private int width;
    private int height;
    private View contentview;
    private ListView listview;
    private LinearLayout ly_filemanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //获取读取SD卡的权限
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100); // 权限分配，不能这样写啊

        initView();
        setImageParam();
        initContentView();
        initPage();
    }

    public void initView() {
        tools = findViewById(R.id.tv_tools);
        opitmization = findViewById(R.id.tv_optimization);
        appmanager = findViewById(R.id.tv_appmanager);
        trans_bar = findViewById(R.id.trans_bar);
        mViewpager = findViewById(R.id.viewpager);
        more = findViewById(R.id.more);

        tools.setOnClickListener(this);
        opitmization.setOnClickListener(this);
        appmanager.setOnClickListener(this);

        mViewpager.setOnPageChangeListener(this);
        pageList = new ArrayList<>();

        mViews = new TextView[3];
        mViews[0] = tools;
        mViews[1] = opitmization;
        mViews[2] = appmanager;

        curTv = tools;
        setTextColor(tools);
        imgTrans(tools);

        more.setOnClickListener(this);
        initMoreList();


    }

    /**
     * 初始化page
     */
    public void initPage() {
        ly_filemanager = viewTools.findViewById(R.id.ly_filemanager);
        ly_filemanager.setOnClickListener(new toolsClickListener());
    }

    private void  showPopWindow() {
        contentview = getLayoutInflater().inflate(R.layout.pop_main, null, true);
        listview = contentview.findViewById(R.id.pop_list);
        listview.setAdapter(new MyListViewAdapter(this, list));

        final PopupWindow mPopupWindow = new PopupWindow(contentview, 300, 660);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        mPopupWindow.showAsDropDown(contentview, width, height); // 不知道为什么点击了会变来变去
        mPopupWindow.showAtLocation(contentview, Gravity.LEFT | Gravity.TOP, width, height);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                switch (position) {
                    case 0:{
                        startActivity(new Intent(MainActivity.this, UserCenterActivity.class));
                        overridePendingTransition(R.anim.slide_from_left, android.R.anim.fade_out);
                        break;
                    }
                    case 1:{
                        Toast.makeText(MainActivity.this, "111", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 2:{
                        break;
                    }
                    case 3:{
                        break;
                    }
                    case 4:{
                        break;
                    }
                    case 5:{
                        break;
                    }
                }
            }
        });
    }

    /**
     * 初始化弹出菜单
     */
    private void initMoreList() {
        item = new PopItem("用户中心");
        list.add(item);
        item = new PopItem("系统设置");
        list.add(item);
        item = new PopItem("设置项");
        list.add(item);
        item = new PopItem("待设置");
        list.add(item);
        item = new PopItem("待设置");
        list.add(item);
        item = new PopItem("待设置");
        list.add(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tools:{
                curTv = tools;
                imgTrans(tools);
                setTextColor(tools);
                mViewpager.setCurrentItem(0);
                break;
            }
            case R.id.tv_optimization: {
                curTv = opitmization;
                imgTrans(opitmization);
                setTextColor(opitmization);
                mViewpager.setCurrentItem(1);
                break;
            }
            case R.id.tv_appmanager: {
                curTv = appmanager;
                imgTrans(appmanager);
                setTextColor(appmanager);
                mViewpager.setCurrentItem(2);
                break;
            }
            case R.id.more: {
                int[] location = new int[2];
                more.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                width = x;
                height = y;
                showPopWindow();
            }
            default:
                break;
        }
    }

    /**
     * 改变下标图片的位置
     * @param endText
     */
    public void imgTrans(TextView endText) {
        int startMid = curTv.getLeft() + curTv.getWidth() / 2;
        int startLeft = startMid - trans_bar.getWidth() / 2;
        int endMid = endText.getLeft() + endText.getWidth() / 2;
        int endLeft = endMid - trans_bar.getWidth() / 2;


        TranslateAnimation move = new TranslateAnimation(startLeft, endLeft, 0, 0);
        move.setDuration(500);
        move.setFillAfter(true);
        trans_bar.startAnimation(move);
        curTv = endText;
    }

    /**
     * 切换文本颜色
     * @param endText
     */
    public void setTextColor(TextView endText) {
        if (tools != endText) {
            tools.setTextColor(Color.WHITE);
        }
        if (opitmization != endText) {
            opitmization.setTextColor(Color.WHITE);
        }
        if (appmanager != endText) {
            appmanager.setTextColor(Color.WHITE);
        }
        endText.setTextColor(Color.GREEN);
        curTv = endText;
    }

    /**
     * 动态设置图片的大小
     */
    private void setImageParam() {
        int disWidth = getWindowManager().getDefaultDisplay().getWidth();
        int imgWidth = (int) (disWidth / 4 * 0.75);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) trans_bar.getLayoutParams();
        params.width = imgWidth;
        trans_bar.setLayoutParams(params);

        TranslateAnimation move = new TranslateAnimation(0, (disWidth / 4 - imgWidth) / 2, 0, 0);
        move.setDuration(100);
        move.setFillAfter(true);
        trans_bar.startAnimation(move);
    }

    private void initContentView() {
//        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutInflater inflater = getLayoutInflater();
        viewTools = inflater.inflate(R.layout.activity_main_tools, null); // inflate方法返回是View对象
        viewOptimization = inflater.inflate(R.layout.activity_main_optimization, null);
        viewAppmanager = inflater.inflate(R.layout.activity_main_appmanager, null);

        pageList.add(viewTools);
        pageList.add(viewOptimization);
        pageList.add(viewAppmanager);
        mViewpager.setAdapter(new MyPagerAdapter(pageList));


    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int pageid) {
//        System.out.println("select ===== " + pageid);
        switch (pageid) {
            case 0:
                imgTrans(tools);
                setTextColor(tools);

                break;
            case 1:

                imgTrans(opitmization);
                setTextColor(opitmization);
                break;
            case 2:

                imgTrans(appmanager);
                setTextColor(appmanager);
                break;

            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private class toolsClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ly_filemanager : {
                    startActivity(new Intent(MainActivity.this, DirectoryListViewActivty.class));
                    break;
                }

                default:
                    break;
            }
        }
    }
}
