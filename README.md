# Parser Tool - Spring Batch Demo 

* The goal is to write a parser in Java that parses web server access log file, loads the log to MySQL and checks if a given IP makes more than a certain number of requests for the given duration. 

* This tool can parse and load the given log file(access.log) to MySQL. The delimiter of the log file is pipe (|). 
The titles of the fields are "Date", "IP", "Request", "Status", "User Agent". The date format is "yyyy-MM-dd HH:mm:ss.SSS".

* The tool takes "accesslog", "startDate", "duration" and "threshold" as command line arguments. 
The format of "startDate" is of "yyyy-MM-dd.HH:mm:ss".
"duration" can take only "hourly", "daily" as inputs and "threshold" can be an integer.
* This tool uses:
  * Java 1.8
  * Maven
  * Spring Boot
  * Spring Batch
  * Spring JPA
  * Flyway
  * Lombok
  * MySQL
  * H2
# Setting Up The Environment
* MySQL
  * You can run a MySQL docker container as below:
    
    ```
    docker run --name mysql_parser_db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=parser_db -e MYSQL_USER=wh_parser -e MYSQL_PASSWORD=wh_parser -d -p 3310:3306 mysql:8.0
    ```
  * Or you can use your own MySQL. But in this case, you have to change below parameters in `application.properties` file accordingly:
    
    ```
    spring.datasource.url=jdbc:mysql://localhost:3310/parser_db
    spring.datasource.username=wh_parser
    spring.datasource.password=wh_parser
    ```
* Prepare The Jar
  * Run the below command in the root directory of the project to prepare the jar file: `target/parser-0.0.1-SNAPSHOT.jar`
    
    ```
    mvn clean package
    ```
# Usage Of The Tool
* In the first run `--accesslog` argument must be provided. 
* After loading the data with the first run, the following runs will not load the same file even it is provided in the argument.
* Hence, first run will take some time but following runs will be faster.

* Below usage will load the file and find IPs that made more than 200 requests starting from 2017-01-01.15:00:00 to 2017-01-01.16:00:00 (one hour) and print them to console. 
 It also load them to another MySQL table with comments on why it's blocked. 
```
java -jar target/parser-0.0.1-SNAPSHOT.jar --accesslog=access.log --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200
```

Output:
```
***> Blocked Clients are as below(startDate: 2017-01-01T15:00, duration: hourly, threshold: 200)
***> Ip : Request Count
***> 192.168.106.134 : 232
***> 192.168.11.231 : 211
```

Related DB Queries:
```
docker container exec -it mysql_parser_db mysql -u root -proot -e "select count(*),ip from parser_db.access_log t where t.access_date between  '2017-01-01 15:00:00' and '2017-01-01 15:59:59' group by t.ip having count(*) >= 200;"

+----------+-----------------+
| count(*) | ip              |
+----------+-----------------+
|      232 | 192.168.106.134 |
|      211 | 192.168.11.231  |
+----------+-----------------+

```

```
docker container exec -it mysql_parser_db mysql -u root -proot -e "select t.* from parser_db.blocked_client t where t.description like '%200%' order by t.request_count desc;"

+----+---------------------------------------------------------------------------------------------------------------------------------+-----------------+---------------+
| id | description                                                                                                                     | ip              | request_count |
+----+---------------------------------------------------------------------------------------------------------------------------------+-----------------+---------------+
|  1 | This ip has been blocked. Because it made more than 200 requests starting from 2017-01-01T15:00 to 2017-01-01T16:00 (one hour)  | 192.168.106.134 |           232 |
|  2 | This ip has been blocked. Because it made more than 200 requests starting from 2017-01-01T15:00 to 2017-01-01T16:00 (one hour)  | 192.168.11.231  |           211 |
+----+---------------------------------------------------------------------------------------------------------------------------------+-----------------+---------------+

```
* Below usage will load the file and find IPs that made more than 500 requests starting from 2017-01-01.00:00:00 to 2017-01-02.00:00:00 (24 hour) and print them to console. 
 It also load them to another MySQL table with comments on why it's blocked. 

```
java -jar target/parser-0.0.1-SNAPSHOT.jar --accesslog=access.log --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500
```

Output:
```
**> Blocked Clients are as below(startDate: 2017-01-01T00:00, duration: daily, threshold: 500)
**> Ip : Request Count
**> 192.168.129.191 : 747
**> 192.168.38.77 : 743
**> 192.168.143.177 : 729
**> 192.168.199.209 : 640
**> 192.168.162.248 : 623
**> 192.168.51.205 : 610
**> 192.168.203.111 : 601
**> 192.168.31.26 : 591
**> 192.168.33.16 : 584
**> 192.168.62.176 : 582
**> 192.168.52.153 : 541
**> 192.168.206.141 : 536
**> 192.168.219.10 : 533
**> 192.168.185.164 : 528
**> 192.168.102.136 : 513
```

Related DB Queries:

```
docker container exec -it mysql_parser_db mysql -u root -proot -e "select count(*),ip from parser_db.access_log t where t.access_date between  '2017-01-01 00:00:00' and '2017-01-01 23:59:59' group by t.ip having count(*) >= 500;"

+----------+-----------------+
| count(*) | ip              |
+----------+-----------------+
|      623 | 192.168.162.248 |
|      640 | 192.168.199.209 |
|      513 | 192.168.102.136 |
|      743 | 192.168.38.77   |
|      582 | 192.168.62.176  |
|      601 | 192.168.203.111 |
|      528 | 192.168.185.164 |
|      541 | 192.168.52.153  |
|      747 | 192.168.129.191 |
|      536 | 192.168.206.141 |
|      610 | 192.168.51.205  |
|      729 | 192.168.143.177 |
|      591 | 192.168.31.26   |
|      533 | 192.168.219.10  |
|      584 | 192.168.33.16   |
+----------+-----------------+
```

```
docker container exec -it mysql_parser_db mysql -u root -proot -e "select t.* from parser_db.blocked_client t where t.description like '%500%' order by t.request_count desc;"

+----+---------------------------------------------------------------------------------------------------------------------------------+-----------------+---------------+
| id | description                                                                                                                     | ip              | request_count |
+----+---------------------------------------------------------------------------------------------------------------------------------+-----------------+---------------+
| 11 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.129.191 |           747 |
|  6 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.38.77   |           743 |
| 14 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.143.177 |           729 |
|  4 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.199.209 |           640 |
|  3 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.162.248 |           623 |
| 13 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.51.205  |           610 |
|  8 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.203.111 |           601 |
| 15 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.31.26   |           591 |
| 17 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.33.16   |           584 |
|  7 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.62.176  |           582 |
| 10 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.52.153  |           541 |
| 12 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.206.141 |           536 |
| 16 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.219.10  |           533 |
|  9 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.185.164 |           528 |
|  5 | This ip has been blocked. Because it made more than 500 requests starting from 2017-01-01T00:00 to 2017-01-02T00:00 (24 hours)  | 192.168.102.136 |           513 |
+----+---------------------------------------------------------------------------------------------------------------------------------+-----------------+---------------+
```

# MySQL Schema
* Tables are migrated with `FlyWay`. You can file sql files under `resources/db/migration` directory:
  * `V1__Create_AccessLog_Table.sql`:
  ```
  CREATE TABLE `access_log_pk` (
    `next_val` bigint(20) DEFAULT NULL
  );
  
  INSERT INTO access_log_pk(next_val) VALUES (1);
  
  CREATE TABLE `access_log` (
    `id` bigint(20) NOT NULL,
    `access_date` datetime DEFAULT NULL,
    `ip` varchar(15) DEFAULT NULL,
    `request` varchar(20) DEFAULT NULL,
    `status` int(11) DEFAULT NULL,
    `user_agent` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
  );
  ```
  * `V2__Create_BlockedClient_Table.sql`:
  ```
  CREATE TABLE `blocked_client_pk` (
    `next_val` bigint(20) DEFAULT NULL
  );
  
  INSERT INTO blocked_client_pk(next_val) VALUES (1);
  
  CREATE TABLE `blocked_client` (
    `id` bigint(20) NOT NULL,
    `description` varchar(150) DEFAULT NULL,
    `ip` varchar(15) DEFAULT NULL,
    `request_count` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
  );
  ```
# Some Sql Queries:
* MySQL query to find IPs that mode more than a certain number of requests for a given time period.  
```
select count(*),ip from parser_db.access_log t where t.access_date between  '2017-01-01 00:00:00' and '2017-01-01 23:59:59' group by t.ip having count(*) >= 500;
```
* MySQL query to find requests made by a given IP
```
select * from parser_db.access_log where ip = '192.168.162.248'
```