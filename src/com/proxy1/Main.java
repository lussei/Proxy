package com.proxy1;

import java.net.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;


        int port = 8888;	//default

        if(args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception c) {
                System.out.println("Parametro invalido");
            }
        }


        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Started on: " + port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(-1);
        }

        try {
            //Crear un thread por cada incoming request en el puerto de entrada
            while (true) {
                new ProxyThread(serverSocket.accept()).start();
            }
        }catch (Exception e){
            serverSocket.close();

        }

    }
}






















































































