package ftpclient;

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
    private String jsonFile;
    private ServerSocket serverSocket;
    private InputStream is;
    private int dataPort;
    Socket clientSocket;
    private Logger log;
    private String mod;
    private BufferedReader readSocket;
    private BufferedWriter writeSocket;

    public FTPClient(String login, String password, String filename) throws IOException {
        this.login = login;
        this.password = password;
        jsonFile = filename;
        clientSocket = new Socket("localhost", 1025);
        readSocket = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writeSocket = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        log = Logger.getLogger(FTPClient.class.getName());
        LogManager.getLogManager().reset();
        log.addHandler(new FileHandler("log.txt"));
        log.info(readSocket.readLine());
    }

    public void authorization(boolean test) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("username: ");
        writeSocket.write("USER " + login + "\n");
        writeSocket.flush();
        log.info(readSocket.readLine());
        System.out.print("password: ");
        writeSocket.write("PASS " + password + "\n");
        writeSocket.flush();
        log.info(readSocket.readLine());
        log.info(readSocket.readLine());
        System.out.println("Please choose mode: passive or active (enter p or a)");
        if (!test) {
            mod = in.nextLine();
            if (mod != "p" || mod != "a") mod = "p";
            if (mod == "a") dataPort = in.nextInt();
        }
        else mod = "p";
    }

    public void downloadFromServer(String message) throws IOException {
        mode();
        String[] splitMessage;
        splitMessage = message.split(" ", 2);
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
        String str = null;
        int id = 0;
        int i = 0;
        while ((str = fr.readLine()) != null) {
            sb.append(str);
            fr.mark(i);
            if (str.contains("\"id\"")) id = Integer.parseInt(str.split(": ")[1].replace(",", ""));
            if ((str.endsWith("}") || str.endsWith("[") && fr.readLine().endsWith("]")) && !(sb.toString().endsWith("]\n}")) && json && !del) {
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
        if (del) {
            int ind = str.indexOf(splitMessage[1]);
            while (str.charAt(ind) != ':')
                ind += 1;
            ind += 3;
            String s = "";
            char c = str.charAt(ind);
            while (str.charAt(ind) != '"') {
                s += str.charAt(ind);
                ind += 1;
            }
            c = str.charAt(ind + 5);
            if (str.charAt(ind + 5) == ',')
                str = str.replace("\n\t {\n\t\t\"id\": " + splitMessage[1] + ",\n\t\t\"name\": \"" + s + "\"\n\t },", "");
            else if (!str.contains("},"))
                str = str.replace("\n\t {\n\t\t\"id\": " + splitMessage[1] + ",\n\t\t\"name\": \"" + s + "\"\n\t }", "");
            else
                str = str.replace(",\n\t {\n\t\t\"id\": " + splitMessage[1] + ",\n\t\t\"name\": \"" + s + "\"\n\t }", "");
            s = "\t { \n\t\t\"id\": " + splitMessage[1] + ",\n\t\t\"name\": \"" + s + "\"\n\t }";
        }
//        if (del) {
//            if (sb.indexOf("\t ,") != -1)
//                str = sb.toString().substring(0, sb.indexOf("[") * Integer.parseInt(splitMessage[2]));
//            else
//                str = sb.toString().substring(sb.indexOf("}") * Integer.parseInt(splitMessage[2]) - 1, sb.indexOf("\t }") * (Integer.parseInt(splitMessage[2]) - 1));
//        }
//        sb.replace(sb.indexOf("}"), sb.indexOf(str), "");
        socket.getOutputStream().write(str.getBytes());
        socket.close();
        log.info(readSocket.readLine());
    }

    public void downloadToServer(String message) throws IOException {
        downloadToServer(message, false, false);
    }

    public void mode() throws IOException {
        Scanner in = new Scanner(System.in);
        if (Objects.equals(mod, "p")) {
            writeSocket.write("PASV" + "\n");
            writeSocket.flush();
            String ServerMessage = readSocket.readLine();
            log.info(ServerMessage);
            System.out.println(ServerMessage);
            String[] ServerSplit = ServerMessage.split(",", 6);
            System.out.println(ServerSplit);
            socket = new Socket("localhost", 256 * Integer.parseInt(ServerSplit[4]) + Integer.parseInt(ServerSplit[5].replace(")", "")));
        } else if (Objects.equals(mod, "a")) {
            if(serverSocket == null)
                serverSocket = new ServerSocket(dataPort);
            int p1 = dataPort / 256;
            int p2 = dataPort % 256;
            String ip = Inet4Address.getLocalHost().getHostAddress().replace(".", ",") + "," + p1 + "," + p2;
//            socket = new Socket(Inet4Address.getLocalHost().getHostAddress(), dataPort);
            writeSocket.write("PORT " + ip + "\n");
            writeSocket.flush();
            socket = serverSocket.accept();
            String ServerMessage = readSocket.readLine();
            log.info(ServerMessage);
        }
    }

    public ArrayList<String> studentList(String message) throws IOException {
        String[] splitMessage;
        splitMessage = message.split(" ", 2);
        downloadFromServer(jsonFile);
        BufferedReader fr = new BufferedReader(new FileReader(jsonFile));
        String str = null;
        ArrayList<String> lst = new ArrayList<String>();
        int id = 0;
        while ((str = fr.readLine()) != null) {
            if (str.contains("\"id\""))
                id = Integer.parseInt(str.split(": ")[1].replace(",", ""));
            else if (str.contains("\"name\""))
                lst.add(str.split(": ")[1].replace("\"", "") + " (id: " + id + ")");
        }
        Collections.sort(lst);
        for (String i: lst) {
            System.out.println(i);
        }
        lst.contains("Anna");
        return lst;
    }

    public String studentInfo(String message) throws IOException {
        String[] splitMessage;
        splitMessage = message.split(" ", 2);
        downloadFromServer(jsonFile);
        BufferedReader fr = new BufferedReader(new FileReader(jsonFile));
        String str = null;
        while ((str = fr.readLine()) != null && !str.contains("\"id\": " + splitMessage[1]));
        str = fr.readLine().split(": ")[1];
        System.out.println(str);
        return str;
    }

    public void addStudent(String message) throws IOException {
        String[] splitMessage;
        splitMessage = message.split(" ", 2);
        downloadFromServer(jsonFile);
        downloadToServer(jsonFile + " " + splitMessage[1], true, false);
    }

    public void deleteStudent(String message) throws IOException {
        String[] splitMessage;
        splitMessage = message.split(" ", 2);
        downloadFromServer(jsonFile);
        downloadToServer(jsonFile + " " + splitMessage[1], true, true);
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

    public static void main(String[] args) throws IOException {
        FTPClient client = new FTPClient("qwerty123", "ftpnetwork", "json.txt");

       // client.readSocket = new BufferedReader(new InputStreamReader(client.clientSocket.getInputStream()));
//        client.writeSocket = new PrintWriter(socket.getOutputStream(), true);
       // client.writeSocket = new BufferedWriter(new OutputStreamWriter(client.clientSocket.getOutputStream()));
        BufferedReader readSocket1 = null;
        BufferedWriter writeSocket1 = null;
        Scanner in = new Scanner(System.in);
        //client.log.info(client.readSocket.readLine());
        try {
            client.authorization(false);
            String[] splitMessage;
            while (true) {
                String message = in.nextLine();
                if (message.equals("quit")) {
                    client.disconnect();
                    break;
                }

                else if (message.startsWith("getFile")) {
                    client.downloadFromServer(message);
                } else if (message.startsWith("putFile")) {
                    client.downloadToServer(message);
                } else if (message.startsWith("getSt")) {
                    client.studentInfo(message);
                } else if (message.startsWith("lstSt")) {
                    client.studentList(message);
                } else if (message.startsWith("addSt")) {
                    client.addStudent(message);
                } else if (message.startsWith("delSt")) {
                    client.deleteStudent(message);
                }
            }
//            client.writeSocket.close();
//            client.readSocket.close();
//            client.socket.close();
//            client.clientSocket.close();
        } catch (Exception e) {
           System.out.println(e);
        }
    }
}