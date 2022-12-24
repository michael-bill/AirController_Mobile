package com.example.aircontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircontroller.Client.Settings;

public class ControlSelectionActivity extends AppCompatActivity {

    TextView textViewConnectedPC;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_selection);
        textViewConnectedPC = findViewById(R.id.textViewConnectedPC);
        textViewConnectedPC.setText("Device " + Settings.ConnectedPC.Name + " connected, choose what you want to control.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Settings.ConnectedPC = null;
    }

    public void buttonControlMouseAndKeyboard_Click(View view) {
        getApplicationContext().startActivity(new Intent(getApplicationContext(), ControlMouseAndKeyboardActivity.class));
    }
}