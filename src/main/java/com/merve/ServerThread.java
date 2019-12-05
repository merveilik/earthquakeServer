package com.merve;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private BufferedReader in;
    private BufferedWriter out;
    PrintStream out2;
    public String city;
    RequestType type;
    City[] cities;
    ServerThread(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out2 = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            String data;
            while ((data = in.readLine()) != null) {
                String type = data.substring(data.indexOf("type")+5);
                System.out.print(type);

                if(type.equals("getList")) {
                    System.out.println();
                    City[] c = Server.listCities();
                    String s = arrayToStringHelper(c);
                    out2.print(s);
                    break;

                } else {
                    //String cityAndMessage = type.substring(type.indexOf("report")+7);
                    //String city = cityAndMessage.substring(0,cityAndMessage.indexOf(":"));
                    //String message = cityAndMessage.substring(cityAndMessage.indexOf(":"));
                        String city = type.substring(type.indexOf("report")+7);
                        Server.incrementCounterForCity(city);
                    //Server.updateMessagesOfACity(message,city);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(String json) throws IOException {
        out.write(json);
        out.newLine();
        out.flush();
    }

    public String arrayToStringHelper(City[] cities){
        String s="";
        for(int i = 0; i<cities.length; i++){
            s += " "+cities[i].name;
            s += " "+cities[i].counter;
            if(cities[i].messages.length() == 0){
                s += " *";
            } else {
                s += " "+cities[i].messages;
            }
        }
        System.out.println(s);
        return s+" &&";
    }
}
