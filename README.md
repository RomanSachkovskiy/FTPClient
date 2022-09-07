# FTPClient

Клиент для работы с FTP сервером. В качестве сервера было использовано готовое решение, которое находится по ссылке: https://github.com/pReya/ftpServer.git. Данное приложение работает как на ОС Windows, так и на ОС Linux.

# Инструкция по сборке проекта

С нуля проект можно собрать с помощью выполнения следующих действий:
1. Прописать команду для создания class файлов (они будут создаться в директории bin):
+ для Windows: **javac -cp bin/lib/jcommander-1.72.jar;bin/lib/testng-7.0.0.jar -sourcepath src/main -d bin src/main/java/ftpclient/FTPClient.java src/main/java/ftpserver/*.java src/main/java/Main.java src/test/java/FTPTest.java**
+ для Linux: **javac -cp bin/lib/jcommander-1.72.jar:bin/lib/testng-7.0.0.jar -sourcepath src/main -d bin src/main/java/ftpclient/FTPClient.java src/main/java/ftpserver/*.java src/main/java/Main.java src/test/java/FTPTest.java**
2. Прописать команду для создания JAR файлов: 
+ для сервера: jar -cmf manifest/server-manifest.mf FTPServer.jar -C bin .
+ для клиента: jar -cmf manifest/client-manifest.mf FTPClient.jar -C bin .
+ для тестов: jar -cmf manifest/test-manifest.mf FTPTest.jar -C bin .

Необходимые JAR файлы для работы проекта уже есть в главном каталоге, поэтому сборку производить необязательно.

# Инструкция по запуску приложения

Для запуска приложения необходимо в главном каталоге прописать команду:
+ для сервера: **java -jar FTPServer.jar**
+ для клиента: **java -jar FTPClient.jar [ip] [name] [password]**
Приложение поддерживает одного пользователя с параметрами: name - qwerty123, password - ftpnetwork, ip - ip-адрес сервера.

# Инструкция по работе с приложением

После успешной авторизации, на стороне клиента выводится следующее сообщение:

![image](https://user-images.githubusercontent.com/84938597/188916768-b30569bd-12f0-4dad-a534-d004d263a0bb.png)

Он должен выбрать, в каком режиме будет происходить обмен данными с сервером: в пассивном (p) или в активном (a). По умолчанию стоит пассивный режим.
Если пользователь выберет активный режим, то ему нужно будет ввести порт, на который будет подключаться сервер для обмена данными:

![image](https://user-images.githubusercontent.com/84938597/188921058-cf461a5c-dc6c-461a-ab35-da64a283ac3e.png)

После того, как пользователь выбрал режим, на экран выводится справка по всем командам:

![image](https://user-images.githubusercontent.com/84938597/188921475-f5e083af-fe04-4b07-80d6-d3233b6bca4d.png)

В справке приведены команды, которые можно использовать:
+ lstSt - получить отсортированный по алфавиту список студентов по имени
+ getSt <student's id> - получить информацию (имя) студента по id
+ addSt <student's name> - добавить студента с именем student's name, при этом id генерируется автоматически
+ delSt <student's id> - удалить студента по id
+ quit - завершение работы программы
+ help - вывод справки по комндам

## Примеры работы команд

+ lstSt

![image](https://user-images.githubusercontent.com/84938597/188923390-7950ffa2-08fa-4fe1-8a8d-7f705956e2e4.png)

+ getSt

![image](https://user-images.githubusercontent.com/84938597/188923550-5dfc1a27-f2c5-442e-9c6b-e9409b6843c9.png)

+ addSt

![image](https://user-images.githubusercontent.com/84938597/188924748-51ab504c-60da-4865-9226-93c0cfe47a09.png)

Список студентов (команда lstSt) после команды addSt Vasya

![image](https://user-images.githubusercontent.com/84938597/188925089-14ae9e0e-c2e0-428e-afd5-81109cd0efb6.png)

+ delSt

![image](https://user-images.githubusercontent.com/84938597/188925242-e31f6ce4-278c-41d6-ab72-995a08b0e312.png)

Список студентов (команда lstSt) после команды delSt 4

![image](https://user-images.githubusercontent.com/84938597/188925357-5b2138b9-f101-4203-950b-783a1ce8e53a.png)

+ help

![image](https://user-images.githubusercontent.com/84938597/188925688-4cf97d99-7ac8-44e6-b92a-d078c8734aac.png)

+ quit

![image](https://user-images.githubusercontent.com/84938597/188925776-7608633c-3535-4a7e-b51e-a7394011fe65.png)

# Инструкция по запуску тестов

Для запуска тестов необходимо в главном каталоге прописать команду: **java -jar FTPTest.jar**
Тесты проверяют работу всех основных команд: lstSt, getSt [id], addSt [name], delSt [id], quit.
Для того, чтобы осуществить данную проверку на сервер отправляется файл jsTest.txt, с заданным списком студентов.
Далее проверяются все команды:
+ getList - проверяется, эквивалентен ли список, возвращаемый с сервера, заданному списку
+ getSt <id> - проверяется, верное ли имя студента вернулось по id
+ addSt <name> - происходит добавление на сервер студента с именем name, а далее проверяется, есть ли данный студент в файле на сервере
+ delSt <id> - происходит удаление студента по id на сервере, после этого проверяется, нет ли данного студента в файле на сервере
+ exit - проверяется, закрыт ли сокет после выхода из программы
  
  
