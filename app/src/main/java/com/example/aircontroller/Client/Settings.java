package com.example.aircontroller.Client;

import com.example.aircontroller.Model.PC;

import java.util.ArrayList;

public class Settings {
    // Константы
    public static final int CONNECTION_PORT = 17103;
    public static final int DETECTION_SERVER_PORT = 17104;
    public static final int DETECTION_CLIENT_PORT = 17105;
    public static final int KEYBOARD_PORT = 17106;
    public static final String DETECTION_KEY = "hzaXh4yc";
    public static final String CONNECTION_KEY = "6Kd3iGbf";

    // Иные значения
    public static PC ConnectedPC = null;
    public static String SubnetIpAddress = "";
    public static ArrayList<PC> DetectedPC = new ArrayList<>();
}
