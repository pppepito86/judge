
# Development installation (using eclipse)

1. Open Eclipse. In Servers tab -> Add -> New. Search for WildFly. Next. Download the WildFly environment or point the home dir.
2. In Servers tab -> Add -> New. Choose WildFly 10. 
3. Start the server. Open the server installation dir -> /bin. Start add-user.sh (on Mac and Linux) or add-user.bat (on Win). Choose Management User. Proceed with the instructions.
4. Download the MySQL driver .jar from here https://dev.mysql.com/downloads/connector/j/5.0.html . Add the .jar in SERVER_INSTALL_DIR/standalone/deployments
5. Download MySQL. Start it and create database 'judge'.
6. Open localhost:8080 -> Administration Console -> Configuration -> Subsystems -> Datasource -> Non-XA -> Add. Choose MySQL Datasource.
  * In Step 1/3
    * Name = MySqlDS
    * JNDIName = 	java:jboss/datasources/MySqlDS
  * In Step 2/3  (JDBC Driver) choose DetectedDriver and "mysql-connector-java-5.1.40-bin.jar_com.mysql.jdbc.Driver_5_1" (or similar). Next.
  * In Step 3/3  set up the database connection. e.g. ConnectionURL = jdbc:mysql://localhost:3306/judge (if mysql is running on port 3306 and the database is named 'judge')

# API
The common prefix is ```rest/VERSION_ID/```, where ```VERSION_ID``` specifies the version, e.g. ```rest/1.0/```.
All resources have the typical endpoints. For example the resource ```XXX``` has endpoints
  * ```GET /XXX``` - get all XXX
  * ```GET /XXX/{id}``` - get item by id
  * ```POST /XXX``` - create new item
  * ```PUT /XXX/{id}``` - change item by id
  
Available resources are:
  * ```assignments```
  * ```groups```
  * ```problems```
  * ```submissions```
  * ```tags```
  * ```users```
  
Some other methods are:
  * ```GET /assignments/{id}/problems``` - all problems in an assignment
  * ```GET /assignments/{id}/submissions``` - all submissions in an assignment 
  * ```GET /assignments/{id}/users/{userId}/submissions``` - all submissions in an assignment for a user 
  * ```GET /assignments/{id}/users/{userId}/points``` - number of points gained by a user in an assignment
  * ```GET /groups/{id}/users``` - list of users in a group
  * ```GET /problems/{id}/tags``` - list of tags for a problem
  
  
