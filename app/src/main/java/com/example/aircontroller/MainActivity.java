package com.example.aircontroller;

import com.example.aircontroller.Client.*;
import com.example.aircontroller.Model.PC;
import com.example.aircontroller.Utils.MainActivityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView textViewInfo;
    LinearLayout devicesLayout;
    ServerFinder serverFinder;
    int windowWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        textViewInfo = findViewById(R.id.textViewInfo);
        devicesLayout = findViewById(R.id.devicesLayout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        windowWidth = displayMetrics.widthPixels;

        serverFinder = new ServerFinder(getApplicationContext(), devicesLayout, windowWidth);

        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Settings.SubnetIpAddress = serverFinder.getSubnetAddress(wifiManager.getDhcpInfo().ipAddress);
    }

    @SuppressLint("SetTextI18n")
    public void startButton_Click(View view) throws SocketException {
        if (!serverFinder.searchIsAlive()) {
            serverFinder.startSearch();
            textViewInfo.setText("Search has started, please wait.");
        }
    }

    @SuppressLint({"SetTextI18n", "RtlHardcoded", "UseCompatLoadingForDrawables"})
    public void stopButton_Click(View view) {
        if (serverFinder.searchIsAlive()) {
            serverFinder.stopSearch();
            textViewInfo.setText("Press the start button to search for devices.");
        }
    }
}