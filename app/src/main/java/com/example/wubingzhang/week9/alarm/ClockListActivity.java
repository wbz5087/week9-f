package com.example.wubingzhang.week9.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wubingzhang.week9.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by LWQ on 2016/4/8.
 */
public class ClockListActivity extends Activity {
    private ArrayList<Clock> mClocks;
    public static final String PLAY_ALARM = "com.liwenquan.sl.playalarm";
    public static final String EXTRA_CRIME_ID = "com.example.wubingzhang.sleep.clock_id";
    private ListView mListView;
    ClockAdapter adapter;
    static boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        firstTime = prefs.getBoolean("first_time_to_enter", true);
        if (firstTime) {
            /*
            暂时没有添加这一部分的样式，添加之后实现引导
             */
            //startActivity(new Intent(ClockListActivity.this, HelloActivity.class));
            SharedPreferences.Editor pEdit = prefs.edit();
            pEdit.putBoolean("first_time_to_enter", false);
            pEdit.commit();
        }
        setContentView(R.layout.activity_clock_list);
        mClocks = ClockLab.get(this).getClocks();
        adapter = new ClockAdapter(mClocks);

        mListView = (ListView) findViewById(R.id.list_view_main);
        if (adapter != null)
            mListView.setAdapter(adapter);


        findViewById(R.id.clock_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), AddAlarmActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //刷新列表信息；
        if (mClocks.size() != 0) {
            findViewById(R.id.list_view_main).setVisibility(View.VISIBLE);
            findViewById(R.id.noclock).setVisibility(View.INVISIBLE);
            //findViewById(R.id.downarrow).setVisibility(View.INVISIBLE);
            //findViewById(R.id.addtext).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.list_view_main).setVisibility(View.INVISIBLE);
            findViewById(R.id.noclock).setVisibility(View.VISIBLE);
            //findViewById(R.id.downarrow).setVisibility(View.VISIBLE);
            //findViewById(R.id.addtext).setVisibility(View.VISIBLE);

        }
        adapter.notifyDataSetChanged();
    }

    //定制列表项，创建ArrayAdapter作为CrimeListFragment的内部类
    private class ClockAdapter extends ArrayAdapter<Clock> {


        SimpleDateFormat dateFormater;

        public ClockAdapter(ArrayList<Clock> crimes) {
            super(ClockListActivity.this, 0, crimes);
        }

        //覆盖getView方法
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_cell, null);
                holder = new ViewHolder();
                holder.mswitchOn = (Switch) convertView.findViewById(R.id.switchOn);
                holder.mtvClockClock = (TextView) convertView.findViewById(R.id.tvTime);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.tvlable);
                holder.mtime_left = (TextView) convertView.findViewById(R.id.time_left);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            //已解决
            final Clock c = getItem(position);
            holder.titleTextView.setText(c.getLable());

            holder.mswitchOn.setChecked(c.isOn());
            final ViewHolder finalHolder = holder;
            holder.mswitchOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    finalHolder.mswitchOn.setChecked(isChecked);
                    c.setOn(isChecked);
                    ClockLab.get(getApplicationContext()).saveClocks();
                    if (isChecked == false) {
                        //Toast.makeText(getContext(), "闹钟ID" + c.getId().hashCode(), Toast.LENGTH_SHORT).show();
                        //此处有问题，关闭闹钟功能不能用待解决
                        // 20160411版本√测试通过
                        Intent i = new Intent(getContext(), AlarmReceiver.class);
                        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                        PendingIntent pi = PendingIntent.getBroadcast(getContext(), Integer.valueOf(c.getId().hashCode()), i, 0);
                        am.cancel(pi);
                    }
                }
            });

            //格式化时间
            dateFormater = new SimpleDateFormat("HH:mm");
            TextView dateTextView = (TextView) convertView.findViewById(R.id.tvTime);
            dateTextView.setText(dateFormater.format(c.getDate()));


            holder.mtvClockClock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ClockListActivity.this, SetAlarmActivity.class);
                    i.putExtra(EXTRA_CRIME_ID, c.getId());
                    startActivity(i);
                }
            });

            String stime = getTimeDiff(c);

            holder.mtime_left.setText(getString(R.string.string_time_left, stime));

            return convertView;
        }

        public final class ViewHolder {
            public Switch mswitchOn;
            public TextView mtvClockClock;
            public TextView titleTextView;
            public TextView mtime_left;
        }

        public String getTimeDiff(Clock c) {
            Calendar clock = Calendar.getInstance();
            long time1 = clock.getTimeInMillis();
            long time2 = c.getDate().getTime();
            long diff = time2 - time1;
            int hourdiff = 0;
            int minutediff = 0;
            int minutediffInminute = (int) (diff / 1000 / 60);
            String stime = null;
            if (minutediffInminute < 0) {
                minutediffInminute += 24 * 60;
                hourdiff = (minutediffInminute / 60);
                minutediff = (minutediffInminute % 60);
                if (hourdiff == 0)
                    stime = minutediff + "minutes later";
                else if (minutediff == 0)
                    stime = hourdiff + "hours later";
                else stime = hourdiff + "hours" + minutediff + "minutes later";
            } else if (minutediffInminute == 0) {
                stime = "in one minute";
            } else if (minutediffInminute > 0) {
                hourdiff = (minutediffInminute / 60);
                minutediff = (minutediffInminute % 60);
                if (hourdiff == 0)
                    stime = minutediff + "minutes later";
                else if (minutediff == 0)
                    stime = hourdiff + "hours later";
                else stime = hourdiff + "hours" + minutediff + "minutes later";
            }
            return stime;
        }
    }

}
