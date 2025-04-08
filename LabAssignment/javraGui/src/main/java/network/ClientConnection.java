package network;

import javafx.application.Platform;
import org.example.javragui.HelloController;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientConnection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private HelloController controller;
    public ClientConnection(String host, int port, HelloController controller) {
        this.controller = controller;
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread listener = new Thread(this::listen);
            listener.setDaemon(true);
            listener.start();
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            out = new PrintWriter(System.out);
            in = new BufferedReader(new InputStreamReader(System.in));
        }
    }


    public void sendMessage(String msg) {
        out.println(msg);
    }

    private void listen() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equals("REFRESH_LIST")) {
                    Platform.runLater(() -> controller.refreshTableFromServer());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

