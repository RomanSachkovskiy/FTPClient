# FTPClient

javac -cp bin/lib/jcommander-1.72.jar;bin/lib/testng-7.0.0.jar -sourcepath src/main -d bin src/main/java/ftpclient/*.java src/main/java/ftpserver/*.java src/main/java/Main.java src/test/java/*.java

jar -cmf manifest/test-manifest.mf FTPTest.jar -C bin .
