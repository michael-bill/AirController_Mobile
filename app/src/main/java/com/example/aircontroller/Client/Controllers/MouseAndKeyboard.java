package com.example.aircontroller.Client.Controllers;

import android.util.Log;
import android.util.Xml;

import com.example.aircontroller.Client.Settings;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Base64;

public class MouseAndKeyboard {

    private final DatagramSocket udpSocket;

    public MouseAndKeyboard() throws SocketException {
        udpSocket = new DatagramSocket(Settings.KEYBOARD_PORT);
    }

    public void SendKey(char letter) throws IOException {
        byte[] data = (letter + "").getBytes();
        udpSocket.send(
            new DatagramPacket(
                data,
                data.length,
                InetAddress.getByName(Settings.ConnectedPC.Ip),
                Settings.KEYBOARD_PORT)
        );
    }
}
