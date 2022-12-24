package com.example.aircontroller.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircontroller.Client.ServerConnector;
import com.example.aircontroller.Client.Settings;
import com.example.aircontroller.MainActivity;
import com.example.aircontroller.Model.PC;
import com.example.aircontroller.R;

import java.io.IOException;

public class MainActivityUtils {

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public static void createPCLayout(PC pc, int windowWidth, LinearLayout layout, Context context, ServerConnector serverConnector) {
        LinearLayout pcLayout = new LinearLayout(context);

        pcLayout.setTag(pc);
        pcLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        pcLayout.setBackground(context.getResources().getDrawable(R.drawable.layout_border));
        pcLayout.setLayoutParams(layoutParams);

        TextView textViewPcName = new TextView(context);
        textViewPcName.setText(pc.Name + " " + pc.Ip);
        textViewPcName.setTypeface(Typeface.MONOSPACE);
        textViewPcName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)(windowWidth * 0.037));

        ImageView img = new ImageView(context);
        RelativeLayout.LayoutParams imgLP = new RelativeLayout.LayoutParams(50, 50);
        imgLP.setMargins(0, 0, 20, 0);
        img.setLayoutParams(imgLP);
        img.setImageDrawable(context.getResources().getDrawable(R.drawable.windows_52px));

        pcLayout.addView(img);
        pcLayout.addView(textViewPcName);
        layout.addView(pcLayout);

        pcLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PC conPc = (PC)v.getTag();
                try {
                    serverConnector.Connect(conPc);
                } catch (IOException e) {
                    Toast.makeText(context, "Unable to connect to device " + pc.Name, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
