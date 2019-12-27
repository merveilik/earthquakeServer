package com.merve;

import com.google.gson.Gson;
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private static String citiesFile = "cities.json";

    private static BinarySemaphore citiesFileMutex = new BinarySemaphore(true);
    private static BinarySemaphore threadsMutex = new BinarySemaphore(true);

    private static List<ServerThread> threads = new LinkedList<ServerThread>();
    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(80);
            System.out.println("LISTENING");
            while (true) {
                Socket socket = server.accept();
                ServerThread thread = new ServerThread(socket);
                thread.start();

                threadsMutex.P();
                threads.add(thread);
                threadsMutex.V();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void incrementCounterForCity(String city) throws IOException {
        citiesFileMutex.P();
        BufferedReader reader = new BufferedReader(new FileReader(citiesFile));
        City[] cities = new Gson().fromJson(reader, City[].class);
        reader.close();
        for (int i = 0; i < cities.length; i++) {
            System.out.println(cities[i].name);
            if (cities[i].name.equals(city)) {
                System.out.println(cities[i].counter);
                cities[i].counter++;
                break;
            }
        }
        Writer writer = new FileWriter(citiesFile);
        new Gson().toJson(cities, writer);
        writer.flush();
        writer.close();
        citiesFileMutex.V();
    }

    public static void updateMessagesOfACity(String message, String city) throws IOException {
        citiesFileMutex.P();
        BufferedReader reader = new BufferedReader(new FileReader(citiesFile));
        City[] cities = new Gson().fromJson(reader, City[].class);
        reader.close();
        for (int i = 0; i < cities.length; i++) {
            System.out.println(cities[i].name);
            if (cities[i].name.equals(city)) {
                if(!cities[i].messages.contains(":")){
                    cities[i].messages += ":"+message;
                    break;
                } else {
                    cities[i].messages += message+":";
                    break;
                }
            }
        }
        Writer writer = new FileWriter(citiesFile);
        new Gson().toJson(cities, writer);
        writer.flush();
        writer.close();
        citiesFileMutex.V();
    }

    public static City[] listCities() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(citiesFile));
        City[] cities = new Gson().fromJson(reader, City[].class);
        reader.close();

        Writer writer = new FileWriter("out.txt");
        new Gson().toJson(cities, writer);
        writer.flush();
        writer.close();
        return cities;
    }
}
class BinarySemaphore {
    private boolean value;

    BinarySemaphore(boolean initValue) {
        value = initValue;
    }

    public synchronized void P() {
        while (!value) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        value = false;
    }

    public synchronized void V() {
        value = true;
        notify();
    }
}
