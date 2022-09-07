# FTPClient

Клиент для работы с FTP сервером. В качестве сервера было использовано готовое решение, которое находится по ссылке: https://github.com/pReya/ftpServer.git. Данное приложение работает как на ОС Windows, так и на ОС Linux.

# Инструкция по сборке проекта

С нуля проект можно собрать с помощью выполнения следующих действий:
1. Прописать команду для создания class файлов (они будут создаться в директории bin):
+ для Windows: **javac -cp bin/lib/jcommander-1.72.jar;bin/lib/testng-7.0.0.jar -sourcepath src/main -d bin src/main/java/ftpclient/*.java src/main/java/ftpserver/*.java src/main/java/Main.java src/test/java/*.java**
+ для Linux: **javac -cp bin/lib/jcommander-1.72.jar:bin/lib/testng-7.0.0.jar -sourcepath src/main -d bin src/main/java/ftpclient/*.java src/main/java/ftpserver/*.java src/main/java/Main.java src/test/java/*.java**
2. Прописать команду для создания JAR файлов: 
+ для сервера: jar -cmf manifest/server-manifest.mf FTPServer.jar -C bin .
+ для клиента: jar -cmf manifest/client-manifest.mf FTPClient.jar -C bin .
+ для тестов: jar -cmf manifest/test-manifest.mf FTPTest.jar -C bin .

Необходимые JAR файлы для работы проекта уже есть в главном каталоге, поэтому сборку производить необязательно.

# Инструкция по запуску приложения

Для запуска приложения необходимо в главном каталоге прописать команду:
+ для сервера: **java -jar FTPServer.jar**
+ для клиента: **java -jar FTPClient.jar** [ip] [name] [password]
Приложение поддерживает одного пользователя с параметрами: name - qwerty123, password - ftpnetwork, ip - ip-адрес сервера.

# Инструкция по работе с приложением

После успешной авторизации, клиенту выводится следующее сообщение:

->![image](https://user-images.githubusercontent.com/84938597/188916768-b30569bd-12f0-4dad-a534-d004d263a0bb.png)<-


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
