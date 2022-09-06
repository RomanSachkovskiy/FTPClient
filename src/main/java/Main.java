package src.main.java;

import src.main.java.ftpclient.FTPClient;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        FTPClient client = new FTPClient("localhost", "qwerty123", "ftpnetwork", "json.txt");
        Scanner in = new Scanner(System.in);
        boolean quit = false;
        try {
            client.authorization();
            client.help();
            String[] splitMessage;
            while (!quit) {
                System.out.print(">> ");
                String message = in.nextLine();
                splitMessage = message.split(" ");
                switch (splitMessage[0]) {
                    case "help":
                        client.help();
                        break;
                    case "quit":
                        client.disconnect();
                        quit = true;
                        break;
                    case "getSt":
                        client.studentInfo(splitMessage[1]);
                        break;
                    case "lstSt":
                        client.studentList();
                        break;
                    case "addSt":
                        client.addStudent(splitMessage[1]);
                        break;
                    case "delSt":
                        client.deleteStudent(splitMessage[1]);
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
