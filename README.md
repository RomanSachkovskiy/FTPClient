# FTPClient

Клиент для работы с FTP сервером. В качестве сервера было использовано готовое решение, которое находится по ссылке: https://github.com/pReya/ftpServer.git. Данное приложение работает как на ОС Windows, так и на ОС Linux.

#Инструкция по сборке проекта



javac -cp bin/lib/jcommander-1.72.jar;bin/lib/testng-7.0.0.jar -sourcepath src/main -d bin src/main/java/ftpclient/*.java src/main/java/ftpserver/*.java src/main/java/Main.java src/test/java/*.java

jar -cmf manifest/test-manifest.mf FTPTest.jar -C bin .






                    "{\n" +
                   "\t\"students\": [\n" +
                   "\t {\n" +
                   "\t\t\"id\": 1,\n" +
                   "\t\t\"name\": \"Dmitry\"\n" +
                   "\t },\n" +
                   "\t {\n" +
                   "\t\t\"id\": 2,\n" +
                   "\t\t\"name\": \"Vasya\"\n" +
                   "\t },\n" +
                   "\t {\n" +
                   "\t\t\"id\": 3,\n" +
                   "\t\t\"name\": \"Shahomirov\"\n" +
                   "\t }\n" +
                   "\t]\n" +
                   "}";
![image](https://user-images.githubusercontent.com/84938597/188894380-ebe6379e-45e6-4de6-bf4e-ff7e353c6842.png)
