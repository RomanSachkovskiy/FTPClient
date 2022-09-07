package src.main.java.ftpclient;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class FTPClient {
    private String login;
    private String password;
    private Socket socket;
    private final String jsonFile;
    private ServerSocket serverSocket;
    private int dataPort;
    private final Socket clientSocket;
    private final Logger log;
    private String mod;
    private final BufferedReader readSocket;
    private final BufferedWriter writeSocket;
    private boolean test;

    public FTPClient(String ip, String login, String password, String filename) throws IOException {
        this(ip, login, password, filename, false);
    }

    public FTPClient(String ip, String login, String password, String filename, boolean test) throws IOException {
        this.login = login;
        this.password = password;
        jsonFile = filename;
        clientSocket = new Socket(ip, 1025);
        readSocket = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //для чтения данных с сервера
        writeSocket = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); //для передачи данных на сервер
        log = Logger.getLogger(FTPClient.class.getName()); //для записи данных сервера в лог
        LogManager.getLogManager().reset();
        log.addHandler(new FileHandler("log.LOG"));
        log.info(readSocket.readLine());
        if (test) this.test = true;
    }

    public void help() {
        System.out.println();
        System.out.println("Select a command:");
        System.out.println("lstSt\tGet sorted list of students by name");
        System.out.println("getSt <student's id>\tGet information (name) of student by id");
        System.out.println("addSt <student's name>\tAdd a new student");
        System.out.println("delSt <student's id>\tDelete a student by id");
        System.out.println("quit\tShutdown");
        System.out.println("help\tShows this message");
        System.out.println();
    }

    public void authorization() throws IOException {
        Scanner in = new Scanner(System.in);
        while (!Objects.equals(login, "qwerty123") || !Objects.equals(password, "ftpnetwork")) {
            System.out.println("Not logged in!\nPlease, enter a valid username and password");
            System.out.print("username: ");
            login = in.nextLine();
            System.out.print("password: ");
            password = in.nextLine();
        }
        writeSocket.write("USER " + login + "\n");
        writeSocket.flush();
        log.info(readSocket.readLine());
        writeSocket.write("PASS " + password + "\n");
        writeSocket.flush();
        log.info(readSocket.readLine());
        log.info(readSocket.readLine());
        if (!test) {
            System.out.println("Authorization is successfull!");
            System.out.println("Please choose mode: passive or active (enter p or a)");
            mod = in.nextLine();
            if (!Objects.equals(mod, "p") && !mod.equals("a")) mod = "p";
            if (mod.equals("a")) {
                String str;
                System.out.print("Port: ");
                while (!(str = in.nextLine()).matches("[+]?\\d+")) {
                    System.out.println("Port must only contains numbers");
                    System.out.print("Port: ");
                }
                dataPort = Integer.parseInt(str);
                System.out.println("You're in Active mode");
            }
            if (Objects.equals(mod, "p")) System.out.println("You're in Passive mode");
        } else mod = "p";
    }

    public void downloadFromServer(String message) throws IOException {
        mode();
        writeSocket.write("RETR " + message + "\n");
        writeSocket.flush();
        log.info(readSocket.readLine());
        log.info(readSocket.readLine());
        byte[] buffer = new byte[1000];
        int length = socket.getInputStream().read(buffer);
        String str = new String(buffer, 0, length);
        FileWriter fw = new FileWriter(message);
        fw.write(str);
        fw.close();
        socket.close();
    }

    public void downloadToServer(String message, boolean json, boolean del) throws IOException {
        mode();
        String[] splitMessage;
        splitMessage = message.split(" ");
        writeSocket.write("STOR " + splitMessage[0] + "\n");
        writeSocket.flush();
        log.info(readSocket.readLine());
        BufferedReader fr = new BufferedReader(new FileReader(splitMessage[0]));
        StringBuilder sb = new StringBuilder();
        String str;
        int id = 0;
        int i = 0;
        while ((str = fr.readLine()) != null) {
            sb.append(str);
            fr.mark(i);
            if (str.contains("\"id\"")) {
                id = Integer.parseInt(str.split(": ")[1].replace(",", ""));
            }
            if ((str.endsWith("}") || (str.endsWith("[") && fr.readLine().endsWith("]"))) && !(sb.toString().endsWith("]\n}")) && json && !del) {
                fr.reset();
                if (str.endsWith("[") && fr.readLine().endsWith("]")) sb.append("\n\t {\n");
                else sb.append(",\n\t {\n");
                sb.append("\t\t\"id\": " + (id + 1) + ",\n\t\t\"name\": \"" + splitMessage[1] + "\"\n");
                sb.append("\t }");
            }
            fr.reset();
            sb.append("\n");
            i += 1;
        }
        str = sb.toString();
        if (del) str = delete(str, splitMessage);
        socket.getOutputStream().write(str.getBytes());
        socket.close();
        log.info(readSocket.readLine());
    }

    public void downloadToServer(String message) throws IOException {
        downloadToServer(message, false, false);
    }

    public String delete(String str, String[] splitMessage) {
        int ind = str.indexOf(splitMessage[1]);
        while (str.charAt(ind) != ':')
            ind += 1;
        ind += 3;
        StringBuilder s = new StringBuilder();
        while (str.charAt(ind) != '"') {
            s.append(str.charAt(ind));
            ind += 1;
        }
        if (str.charAt(ind + 5) == ',')
            str = str.replace("\n\t {\n\t\t\"id\": " + splitMessage[1] + ",\n\t\t\"name\": \"" + s + "\"\n\t },", "");
        else if (!str.contains("},"))
            str = str.replace("\n\t {\n\t\t\"id\": " + splitMessage[1] + ",\n\t\t\"name\": \"" + s + "\"\n\t }", "");
        else
            str = str.replace(",\n\t {\n\t\t\"id\": " + splitMessage[1] + ",\n\t\t\"name\": \"" + s + "\"\n\t }", "");
        return str;
    }

    public void mode() throws IOException {
        if (Objects.equals(mod, "p")) {
            writeSocket.write("PASV" + "\n");
            writeSocket.flush();
            String ServerMessage = readSocket.readLine();
            log.info(ServerMessage);
            String[] ServerSplit = ServerMessage.split(",", 6);
            socket = new Socket("localhost", 256 * Integer.parseInt(ServerSplit[4]) + Integer.parseInt(ServerSplit[5].replace(")", "")));
        } else if (Objects.equals(mod, "a")) {
            if(serverSocket == null)
                serverSocket = new ServerSocket(dataPort);
            int p1 = dataPort / 256;
            int p2 = dataPort % 256;
            String ip = Inet4Address.getLocalHost().getHostAddress().replace(".", ",") + "," + p1 + "," + p2;
            writeSocket.write("PORT " + ip + "\n");
            writeSocket.flush();
            socket = serverSocket.accept();
            String ServerMessage = readSocket.readLine();
            log.info(ServerMessage);
        }
    }

    public ArrayList<String> studentList() throws IOException {
        downloadFromServer(jsonFile);
        BufferedReader fr = new BufferedReader(new FileReader(jsonFile));
        String str;
        ArrayList<String> lst = new ArrayList<String>();
        int id = 0;
        while ((str = fr.readLine()) != null) {
            if (str.contains("\"id\""))
                id = Integer.parseInt(str.split(": ")[1].replace(",", ""));
            else if (str.contains("\"name\""))
                lst.add(str.split(": ")[1].replace("\"", "") + " (id: " + id + ")");
        }
        Collections.sort(lst);
        if (!test) {
            for (String i : lst) {
                System.out.println(i);
            }
        }
        return lst;
    }

    public String studentInfo(String message) throws IOException {
        return studentInfo(message, true);
    }

    public String studentInfo(String message, boolean out) throws IOException {
        String str;
        if (!message.matches("[+]?\\d+")) {
            str = "Error in argument: id must contains only numbers!";
            if (!test && out) System.out.println(str);
            return str;
        }
        downloadFromServer(jsonFile);
        BufferedReader fr = new BufferedReader(new FileReader(jsonFile));
        while ((str = fr.readLine()) != null && !str.contains("\"id\": " + message));
        if (str != null ) str = fr.readLine().split(": ")[1].replace("\"", "");
        else str = "The student was not found";
        if (!test && out) System.out.println(str);
        return str;
    }

    public void addStudent(String message) throws IOException {
        downloadFromServer(jsonFile);
        downloadToServer(jsonFile + " " + message, true, false);
    }

    public void deleteStudent(String message) throws IOException {
        String str = studentInfo(message, false);
        if (Objects.equals(str, "The student was not found")) {
            System.out.println("The student was not found, so you can't delete it");
            return;
        } else if (Objects.equals(str, "Error in argument: id must contains only numbers!")) {
            System.out.println(str);
            return;
        }
        downloadFromServer(jsonFile);
        downloadToServer(jsonFile + " " + message, true, true);
    }

    public void disconnect() throws IOException {
        if (!clientSocket.isClosed()) {
            writeSocket.write("QUIT\n");
            writeSocket.flush();
            log.info(readSocket.readLine());
            writeSocket.close();
            readSocket.close();
            clientSocket.close();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

}