package com.example.aircontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aircontroller.Client.Controllers.MouseAndKeyboard;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.IOException;
import java.net.SocketException;
import java.sql.Struct;

public class ControlMouseAndKeyboardActivity extends AppCompatActivity {

    EditText editTextInputKeyboard;
    MouseAndKeyboard mouseAndKeyboard;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_mouse_and_keyboard);
        try {
            mouseAndKeyboard = new MouseAndKeyboard();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        editTextInputKeyboard = findViewById(R.id.editTextInputKeyboard);
        editTextInputKeyboard.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextInputKeyboard.getText().length() != 0) {
                    char charForSend = editTextInputKeyboard.getText().charAt(0);
                    editTextInputKeyboard.setText("");
                    try {
                        mouseAndKeyboard.SendKey(charForSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void imageViewKeyboard_Click(View view) {
        UIUtil.showKeyboard(this, editTextInputKeyboard);
    }
}