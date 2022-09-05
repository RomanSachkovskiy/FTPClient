package src.main.java;

import src.main.java.ftpclient.FTPClient;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        FTPClient client = new FTPClient(args[0], args[1], args[2], "json.txt");
        Scanner in = new Scanner(System.in);
        boolean quit = false;
        try {
            client.authorization(false);
            String[] splitMessage;
            while (!quit) {
                System.out.print(">> ");
                String message = in.nextLine();
                splitMessage = message.split(" ");
                switch (splitMessage[0]) {
                    case "quit":
                        client.disconnect();
                        quit = true;
                        break;
                    case "getSt":
                        client.studentInfo(message);
                        break;
                    case "lstSt":
                        client.studentList(message);
                        break;
                    case "addSt":
                        client.addStudent(message);
                        break;
                    case "delSt":
                        client.deleteStudent(message);
                        break;
                    default:
                        System.out.println("Command not found");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
