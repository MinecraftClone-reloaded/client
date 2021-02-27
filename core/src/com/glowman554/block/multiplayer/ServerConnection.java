package com.glowman554.block.multiplayer;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection {
    public final String host;
    public final int port;

    public ServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendServer(String what) {

        String answer = null;

        try (Socket socket = new Socket(this.host, this.port)) {

            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);

            printWriter.println(what);

            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            answer = bufferedReader.readLine();
            socket.close();

        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }
        return answer;
    }

    public void SetBlock(String block, int x, int y, int z) {
        String msg = String.format("sb %d %d %d", x, y, z);
        this.sendServer(msg);
    }

    public String getChunk(int x, int y) {
        String msg = String.format("gc %d %d", x, y);
        String chunk = this.sendServer(msg);
        return chunk;
    }

    public String getEvent() {
        String event = this.sendServer("ge");
        return event;
    }

    public void login(String player) {
        String msg = String.format("lp %s", player);
        this.sendServer(msg);
    }
}
