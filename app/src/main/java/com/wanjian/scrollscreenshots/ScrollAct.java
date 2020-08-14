package com.wanjian.scrollscreenshots;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanjian on 2018/1/3.
 */

public class ScrollAct extends Activity {

    private ListView listView;

    private int height;
    private int width;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_scroll);
        listView = findViewById(R.id.listview);

        setAdapter();

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoScroll();
            }
        });
        Button btn = (Button)findViewById(R.id.test);

        Log.d("ccgTest", "width=" + getResources().getDisplayMetrics().widthPixels);
        findViewById(R.id.test).setOnClickListener(v -> {
            View view = this.getWindow().getDecorView();
            int height = view.getHeight();
            TestUtil testUtil = new TestUtil();
            boolean supportLongScreenshot = testUtil.isSupportLongScreenshot(this);
            Log.d("ccgTest", "is =" + supportLongScreenshot);
            if(supportLongScreenshot) {
                testScroll(testUtil.getScrollableView());
            }
            if (view instanceof ViewGroup) {
//                ViewGroup vp = (ViewGroup) view;
//                testScroll(vp);
//                getListView(vp, vp.getHeight(), vp.getWidth());
//                getAllChildViews(vp);
            }

        });

    }

    private void testScroll(ViewGroup viewGroup) {
        final int delay = 16;
        final MotionEvent motionEvent = MotionEvent.obtain(SystemClock.uptimeMillis()
                , SystemClock.uptimeMillis()
                , MotionEvent.ACTION_DOWN
                , viewGroup.getWidth() / 2
                , viewGroup.getHeight() / 2
                , 0);
        viewGroup.dispatchTouchEvent(motionEvent);//先分发 MotionEvent.ACTION_DOWN 事件
        viewGroup.getHeight();
        Log.d("ccgTe", "viewGroup child count=" + viewGroup.getChildCount());
        View yC = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        viewGroup.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("ccgTe", "v y=" + viewGroup.getY() + "v c y="
                        + yC.getY());
                motionEvent.setAction(MotionEvent.ACTION_MOVE); //延时分发 MotionEvent.ACTION_MOVE 事件
                //改变y坐标，越大滚动越快，但太大可能会导致掉帧
                motionEvent.setLocation((int) motionEvent.getX(), (int) motionEvent.getY() - 10);
                viewGroup.dispatchTouchEvent(motionEvent);
                viewGroup.postDelayed(this, delay);
            }
        }, delay);
    }

    private void getListView(View view, int totalHeight, int totalWidth) {
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
//                Log.d("ccgTest", "class = " + viewchild.getClass().getName());
                if (viewchild.getHeight() > (totalHeight /2) && viewchild.getWidth() == totalWidth) {
                    if (viewchild instanceof ListView || viewchild instanceof ScrollView) {
                        Log.d("ccgTest", "getListView 111");
                        testScroll((ListView) viewchild);
                        return;
                    } else {
                        getListView(viewchild, totalHeight, totalWidth);
                    }
                }
            }
        }
    }

    private List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            Log.d("ccgTest", "height =" + height
                    + ", vp height = " + vp.getHeight()
                        + ", child count = " + vp.getChildCount()
                    + ", name = " + vp.getClass().getName());
            if(vp instanceof ListView) {
                Log.d("ccgTest", "come in");
                testScroll((ListView) vp);
            }
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                //再次调用本身（递归）
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }

    private void autoScroll() {
        final int delay = 16;
        final MotionEvent motionEvent = MotionEvent.obtain(SystemClock.uptimeMillis()
                , SystemClock.uptimeMillis()
                , MotionEvent.ACTION_DOWN
                , listView.getWidth() / 2
                , listView.getHeight() / 2
                , 0);
        listView.dispatchTouchEvent(motionEvent);//先分发 MotionEvent.ACTION_DOWN 事件
        listView.getHeight();
        Log.d("ccgTest", "listView.getHeight() = " + listView.getHeight()
                + ", child 0 = " + listView.getChildAt(0).getHeight()
                + ", count = " + listView.getChildCount());
        listView.computeScroll();

        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                motionEvent.setAction(MotionEvent.ACTION_MOVE); //延时分发 MotionEvent.ACTION_MOVE 事件
                //改变y坐标，越大滚动越快，但太大可能会导致掉帧
                motionEvent.setLocation((int) motionEvent.getX(), (int) motionEvent.getY() - 10);
                listView.dispatchTouchEvent(motionEvent);
                listView.postDelayed(this, delay);
                Log.d("ccgTest", "listView.getHeight() = " + listView.getHeight()
                        + ", foot = " + listView.getFooterViewsCount()
                        + ", child = " + listView.getChildCount()
                        + ", count = " + listView.getCount());
            }
        }, delay);
    }

    private void setAdapter() {

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
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
                if (convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, listView, false);
                    convertView.setTag(R.id.index, convertView.findViewById(R.id.index));
                }
                ((TextView) convertView.getTag(R.id.index)).setText(position + "");
                return convertView;
            }
        });

    }
}
