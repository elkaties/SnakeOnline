package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    Scanner scanner;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    Socket socket;
    ArrayList<String> list;

    Client() {
        list = new ArrayList<>();
        try {
            socket = new Socket("localhost", 5050);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            //objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
        }
        scanner = new Scanner(System.in);
        read();
    }

    void read() {
        while (true) {
            String string = scanner.nextLine();
            if (string.equals("")) {
                try {
                    objectOutputStream.writeObject(list);
                    objectOutputStream.flush();
                    objectOutputStream.reset();
                    list.clear();
                } catch (IOException e) {
                }
            }
            list.add(string);
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
