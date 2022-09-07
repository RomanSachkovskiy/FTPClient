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
            client.authorization();
            client.help();
            while (!quit) {
                System.out.print(">> ");
                String message = in.nextLine();
                String[] splitMessage = message.split(" ");
                switch (splitMessage[0]) {
                    case "help":
                        client.help();
                        break;
                    case "quit":
                        client.disconnect();
                        quit = true;
                        break;
                    case "getSt":
                        if (splitMessage.length == 1) {
                            System.out.println("Enter an argument");
                            break;
                        }
                        client.studentInfo(splitMessage[1]);
                        break;
                    case "lstSt":
                        client.studentList();
                        break;
                    case "addSt":
                        if (splitMessage.length == 1) {
                            System.out.println("Enter an argument");
                            break;
                        }
                        client.addStudent(splitMessage[1]);
                        break;
                    case "delSt":
                        if (splitMessage.length == 1) {
                            System.out.println("Enter an argument");
                            break;
                        }
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
