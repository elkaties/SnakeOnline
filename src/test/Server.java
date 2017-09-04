package test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    ServerSocket serverSocket;
    Socket socket;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    BufferedReader bufferedReader;
    InputStream inputStream;

    private Server() {
        try {
            serverSocket = new ServerSocket(5050);
            socket = serverSocket.accept();
            System.out.println("Connected");
            inputStream = socket.getInputStream();
            //bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf8"));
            //objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Thread thread = new Thread(new Reader());
            thread.start();
//        ExecutorService tp = Executors.newFixedThreadPool(2);
//        tp.execute(() -> {
//            try (Socket socket = serverSocket.accept()) {
//
//            } catch (IOException e ) {}
//        });
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        new Server();
    }


    class Reader implements Runnable {
        @Override
        public void run() {
            ArrayList<String> list;
            while (!socket.isClosed()) {
                try {
                    list = (ArrayList<String>) objectInputStream.readObject();
                    for (String str : list) {
                        System.out.println("--------------------------");
                        System.out.println(str);
                        System.out.println("--------------------------");
                    }
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

