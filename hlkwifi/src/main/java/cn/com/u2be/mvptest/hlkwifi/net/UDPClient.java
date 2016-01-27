package cn.com.u2be.mvptest.hlkwifi.net;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPClient implements Runnable {

    public static final int BUFFER_SIZE = 4096;
    private final String HOST = "224.0.0.1";
    private final int RECIVE_PORT = 18665;
    private final int PORT = 988;

    private MulticastSocket socket;
    private InetAddress inetAddress;

    private byte[] buffer = new byte[BUFFER_SIZE];
    private DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
    private DatagramPacket sendPacket = null;

    private Handler handler;


    public UDPClient(Handler handler) {
        this.handler = handler;


        try {
            socket = new MulticastSocket(RECIVE_PORT);
            inetAddress = InetAddress.getByName(HOST);
            socket.joinGroup(inetAddress);
            sendPacket = new DatagramPacket(new byte[0], 0, inetAddress, PORT);

            new Thread(this).start();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendPacket.setData(content.getBytes());
                    socket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    @Override
    public void run() {

        try {

            while (true) {
                socket.receive(receivePacket);
                receive(receivePacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void receive(DatagramPacket receivePacket) {
        Message msg = new Message();
        msg.obj = receivePacket.getAddress().getHostAddress();
        handler.sendMessage(msg);
    }
}
