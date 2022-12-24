package com.example.aircontroller.Client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.MainThread;

import com.example.aircontroller.MainActivity;
import com.example.aircontroller.Model.PC;
import com.example.aircontroller.Utils.MainActivityUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class ServerFinder {

    public ServerFinder(Context con, LinearLayout devLay, int winWidth) {
        this.context = con;
        this.devicesLayout = devLay;
        this.windowWidth = winWidth;
        this.serverConnector = new ServerConnector(con);
    }

    public void startSearch() throws SocketException {
        startWaitingDetectionMessage();
        startSendingDetectionMessage();
    }

    public void stopSearch() {
        stopSendingDetectionMessage();
        stopWaitingDetectionMessage();
    }

    public boolean searchIsAlive() {
        return sendThread.isAlive() && waitingThread.isAlive();
    }

    private final Context context;
    private final int windowWidth;
    private final ServerConnector serverConnector;
    private final LinearLayout devicesLayout;
    private boolean isWaiting = false;
    private boolean isSending = false;
    private DatagramSocket waitingSocket;

    // Начинает постоянную отправку на 256 локальных ip адресов ключ обнаружения
    private void startSendingDetectionMessage() {
        isSending = true;
        sendThread.start();
    }

    // Начинает постоянное ожидание ответов с устройств
    private void startWaitingDetectionMessage() throws SocketException {
        isWaiting = true;
        waitingSocket = new DatagramSocket(Settings.DETECTION_CLIENT_PORT);
        waitingThread.start();
    }

    // Останавливает получение ответных сообщений и закрывает сокет
    private void stopWaitingDetectionMessage() {
        isWaiting = false;
        waitingSocket.close();
        waitingSocket = null;
    }

    // Останавливает отправку сообщений
    private void stopSendingDetectionMessage() {
        isSending = false;
    }

    private final Thread sendThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (isSending) {
                    sendDetectionMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    private final Thread waitingThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (isWaiting) {
                    waitingDetectionMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    private void sendDetectionMessage() throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] data = Settings.DETECTION_KEY.getBytes();
        for (int i = 0; i < 256; i++) {
            socket.send(
                    new DatagramPacket(
                    data,
                    data.length,
                    InetAddress.getByName(Settings.SubnetIpAddress + "." + i),
                    Settings.DETECTION_SERVER_PORT)
            );
        }
        socket.close();
    }

    private void waitingDetectionMessage() throws IOException {
        DatagramPacket inputPacket = new DatagramPacket(new byte[200], 200);
        waitingSocket.receive(inputPacket);
        String message = new String(getNewBuffer(inputPacket.getData(), inputPacket.getLength()), StandardCharsets.UTF_8);
        if (message.split("_")[0].equals(Settings.DETECTION_KEY)) {
            PC pc = new PC(inputPacket.getAddress().toString().substring(1), message.split("_")[1]);
            if (!ContainsPC(pc)) {
                Settings.DetectedPC.add(pc);
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivityUtils.createPCLayout(
                            pc,
                            windowWidth,
                            devicesLayout,
                            context,
                            serverConnector
                        );
                    }
                });
            }
        }
    }

    private boolean ContainsPC(PC pc) {
        for (PC i : Settings.DetectedPC) {
            if (i.Name.equals(pc.Name) && i.Ip.equals(pc.Ip))
                return true;
        }
        return false;
    }

    private byte[] getNewBuffer(byte[] buffer, int len)
    {
        byte[] result = new byte[len];
        System.arraycopy(buffer, 0, result, 0, len);
        return result;
    }

    public String getSubnetAddress(int address)
    {
        @SuppressLint("DefaultLocale")
        String ipString = String.format("%d.%d.%d", address & 0xff, address >> 8 & 0xff, address >> 16 & 0xff);
        return ipString;
    }
}
