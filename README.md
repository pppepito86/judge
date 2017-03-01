
# Development installation (using eclipse)

1. Open Eclipse. In Servers tab -> Add -> New. Search for WildFly. Next. Download the WildFly environment or point the home dir.
2. In Servers tab -> Add -> New. Choose WildFly 10. 
3. Start the server. Open the server installation dir -> /bin. Start add-user.sh (on Mac and Linux) or add-user.bat (on Win). Choose Management User. Proceed with the instructions.
4. Download the MySQL driver .jar from here https://dev.mysql.com/downloads/connector/j/5.0.html . Add the .jar in SERVER_INSTALL_DIR/standalone/deployments
5. Download MySQL. Start it and create database 'judge'.
6. Open localhost:8080 -> Administration Console -> Configuration -> Subsystems -> Datasource -> Non-XA -> Add. Choose MySQL Datasource.
In Step 1/3
Name = MySqlDS
JNDIName = 	java:jboss/datasources/MySqlDS
In Step 2/3  (JDBC Driver) choose DetectedDriver and "mysql-connector-java-5.1.40-bin.jar_com.mysql.jdbc.Driver_5_1" (or similar). Next.
In Step 3/3  set up the database connection. e.g. ConnectionURL = jdbc:mysql://localhost:3306/judge (if mysql is running on port 3306 and the database is named 'judge')

