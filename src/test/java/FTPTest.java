//package src.test.java.ftpclientTest;

import src.main.java.ftpclient.FTPClient;
import src.main.java.Main;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;

public class FTPTest {

    private String testJson;

    FTPClient client;

    public FTPTest() throws IOException {
        client = new FTPClient(Inet4Address.getLocalHost().getHostAddress(), "qwerty123", "ftpnetwork", "jsTest.txt");
        testJson = """
                {
                \t"students": [
                \t {
                \t\t"id": 1,
                \t\t"name": "Dmitry"
                \t },
                \t {
                \t\t"id": 2,
                \t\t"name": "Vasya"
                \t },
                \t {
                \t\t"id": 3,
                \t\t"name": "Shahomirov"
                \t }
                \t]
                }""";
    }

    @BeforeTest
    public void StoreFile() throws IOException {
        client.authorization(true);
        FileWriter fw = new FileWriter("jsTest.txt");
        fw.write(testJson);
        fw.close();
        client.downloadToServer("jsTest.txt");
    }

    @Test(priority = 1)
    public void testGet() throws IOException {
        String testStr = client.studentInfo("getSt 1");
        Assert.assertEquals(testStr, "Dmitry", "Names dont't match!");
    }

    @Test(priority = 2)
    public void testLst() throws IOException {
        ArrayList<String> testList = client.studentList("lstSt");
        Assert.assertEquals(testList, Arrays.asList("Dmitry (id: 1)", "Shahomirov (id: 3)", "Vasya (id: 2)"), "The lists of students dont't match!");
    }

    @Test(priority = 3)
    public void testAdd() throws IOException {
        client.addStudent("addSt Anna");
        ArrayList<String> testList = client.studentList("lstSt");
        Assert.assertTrue(testList.contains("Anna (id: 4)"), "Add a student doesn't works!");
    }

    @Test(priority = 4)
    public void testDelete() throws IOException {
        client.deleteStudent("delSt 4");
        ArrayList<String> testList = client.studentList("lstSt");
        Assert.assertFalse(testList.contains("Anna (id: 4)"), "Delete a student doesn't works!");
    }

    @Test(priority = 5)
    public void testQuit() throws IOException {
        client.disconnect();
        Assert.assertTrue(client.getClientSocket().isClosed(), "Couldn't quit a server!");
    }
}
