package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;


public class ClientHandler implements Runnable {
    private final Socket socket;
    private final List<ClientHandler> allClients;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.allClients = clients;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                if (message.startsWith("ADD_PARTICIPANT")) {
                    broadcast("REFRESH_LIST");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        out.println(msg);
    }

    private void broadcast(String msg) {
        for (ClientHandler client : allClients) {
            client.send(msg);
        }
    }
}

