package com.example.aircontroller.Client;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.example.aircontroller.ControlSelectionActivity;
import com.example.aircontroller.Model.PC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class ServerConnector {

    public ServerConnector(Context con) {
        this.context = con;
    }

    private final Context context;
    private DatagramSocket waitingSocket;
    private boolean isWaiting = false;

    public void Connect(PC pc) throws IOException {
        if (!isWaiting) startWaitingConnectionMessage();
        sendConnectionMessage(pc);
    }

    private void startWaitingConnectionMessage() throws SocketException {
        isWaiting = true;
        waitingSocket = new DatagramSocket(Settings.CONNECTION_PORT);
        waitingThread.start();
    }

    public void stopWaitingConnectionMessage() {
        isWaiting = false;
        waitingSocket.close();
        waitingSocket = null;
    }

    private final Thread waitingThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (isWaiting) {
                    waitingConnectionMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    private void waitingConnectionMessage() throws IOException {
        DatagramPacket inputPacket = new DatagramPacket(new byte[200], 200);
        waitingSocket.receive(inputPacket);
        String message = new String(getNewBuffer(inputPacket.getData(), inputPacket.getLength()), StandardCharsets.UTF_8);
        if (message.split("_")[0].equals(Settings.CONNECTION_KEY)) {
            Settings.ConnectedPC = new PC(inputPacket.getAddress().toString().substring(1), message.split("_")[1]);
            stopWaitingConnectionMessage();
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    context.startActivity(new Intent(context, ControlSelectionActivity.class));
                }
            });
        }
    }

    private void sendConnectionMessage(PC pc) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] data = Settings.CONNECTION_KEY.getBytes();
        socket.send(
            new DatagramPacket(
            data,
            data.length,
            InetAddress.getByName(pc.Ip),
            Settings.CONNECTION_PORT)
        );
        socket.close();
    }

    private byte[] getNewBuffer(byte[] buffer, int len)
    {
        byte[] result = new byte[len];
        System.arraycopy(buffer, 0, result, 0, len);
        return result;
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
