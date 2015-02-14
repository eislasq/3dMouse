package com.example.eislas.touchpad;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by eislas on 10/02/15.
 */
public class SocketClient {
    Runnable runable;
    public static String message = "Wait";
    Context applicationContext;


    public SocketClient(Context appContext) {
        applicationContext = appContext;

        Toast.makeText(applicationContext, "Creando el runable ", Toast.LENGTH_SHORT).show();
        System.err.println("Creando el runable");

        runable = new Runnable() {

            @Override
            public void run() {
                String serverName = "192.168.15.4";
                int port = 8000;
                Socket echoSocket = null;
                PrintWriter out = null;
                BufferedReader in = null;
                //Toast.makeText(applicationContext, "Toast desde Thread :P " , Toast.LENGTH_SHORT).show();
                System.err.println("Thread :P");

                try {
                    echoSocket = new Socket(serverName, port);
                    out = new PrintWriter(echoSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                } catch (UnknownHostException e) {
                    System.err.println("Don't know about host: " + serverName + ":" + port);
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    return;
                } catch (IOException e) {
                    System.err.println("Couldn't get I/O for " + "the connection to: " + serverName + ":" + port);
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    return;
                }

                String serveResponse;

                try {
                    serveResponse = in.readLine();
                    System.out.println("Server dice: " + serveResponse);


                    while (!message.equals("Bye")) {
                        //while ((serveResponse = in.readLine()) != null) {
                        if (message.equals("Wait")) {
                            continue;
                        }
                        out.println(message);
                        message = "Wait";

                        serveResponse = in.readLine();
                        System.out.println("Server dice: " + serveResponse);
                    }

                    out.close();
                    in.close();
                    echoSocket.close();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        };
    }
}
