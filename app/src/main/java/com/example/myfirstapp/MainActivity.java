package com.example.myfirstapp;

//import java.util.Iterator;
//import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
//import android.location.GpsSatellite;
//import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;

import android.view.View;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = "MainActivity";

    private LocationManager lm;
    private LocationListener locationListener;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        lm.removeUpdates(locationListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //判断GPS是否正常启动
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
            return;
        }
        //位置监听 private
         LocationListener locationListener = new LocationListener() {

             /**
              * 位置信息变化时触发
              */
             public void onLocationChanged(Location location) {
                 //updateView(location);
                 Log.i(TAG, "时间：" + location.getTime());
                 Log.i(TAG, "经度：" + location.getLongitude());
                 Log.i(TAG, "纬度：" + location.getLatitude());
                 Log.i(TAG, "海拔：" + location.getAltitude());
             }

             /**
              * GPS状态变化时触发
              */
             public void onStatusChanged(String provider, int status, Bundle extras) {
                 switch (status) {
                     //GPS状态为可见时
                     case LocationProvider.AVAILABLE:
                         Log.i(TAG, "当前GPS状态为可见状态");
                         break;
                     //GPS状态为服务区外时
                     case LocationProvider.OUT_OF_SERVICE:
                         Log.i(TAG, "当前GPS状态为服务区外状态");
                         break;
                     //GPS状态为暂停服务时
                     case LocationProvider.TEMPORARILY_UNAVAILABLE:
                         Log.i(TAG, "当前GPS状态为暂停服务状态");
                         break;
                 }
             }

             /**
              * GPS开启时触发
              */
             public void onProviderEnabled(String provider) {
                 Location location = lm.getLastKnownLocation(provider);
                 //updateView(location);
             }

             /**
              * GPS禁用时触发
              */
             public void onProviderDisabled(String provider) {
                 //updateView(null);
             }
         };

        //为获取地理位置信息时设置查询条件
        Criteria criteria = new Criteria();
        String bestProvider = lm.getBestProvider(criteria, true);

        //获取位置信息
        //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        Location location = lm.getLastKnownLocation(bestProvider);
        //updateView(location);
        //监听状态
        //lm.addGpsStatusListener(listener);
        //lm.addGpsStatusListener(Listener listener);
        //绑定监听，有4个参数
        //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        //参数2，位置信息更新周期，单位毫秒
        //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        //参数4，监听
        //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        lm.requestLocationUpdates(lm.GPS_PROVIDER, 1000, 1, locationListener);
    }


    public void sendMessage(View view) {
                //Do something in response to button
                Intent intent = new Intent(this, DisplayMessageActivity.class);
                EditText editText = (EditText) findViewById(R.id.editText);
                String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
        }

}

