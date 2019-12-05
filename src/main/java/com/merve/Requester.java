package com.merve;

import com.google.gson.Gson;

import java.io.DataInputStream;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.net.Socket;

public class Requester {
    private static DataInputStream input = null;
    private static DataOutputStream out = null;
    public static void main(String[] args) throws IOException {
        Request request = new Request();
        request.place = "Istanbul";
        request.type = RequestType.REPORT;
        Socket socket = new Socket("127.0.0.1", 2909);
        out = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
        out.writeBytes(new Gson().toJson(request));
        out.writeBytes("\n");
        String responseLine;
        System.out.println("here");
        while (!(responseLine = input.readLine()).equals("&&")) {
            System.out.println("com.merve.Server: " + responseLine);

        }
    }
}
