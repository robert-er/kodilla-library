# kodilla-library
Kodilla Library Project

### 1. Project description 
The project consist of backend library service, based on REST architecture. 
### 2. Demo
Project is not uploaded to remote server yet. Application tested on localhost

### 3. Requirements
Please make sure You have following software:
1) Java 8
2) Gradle 6.6.1
3) MySQL 8

### 4. Project 
In order to launch project You have to start LibraryApplication class.
You can check endpoints operation on [http://localhost:8080](http://localhost:8080) address.

### 5. Jwt authentication
Jwt authentication is applied in this project. Token type is Bearer. 
First of all you need to register your user using endpoint `./user/auth/signup`. 
Please use `./user/auth/signin` endpoint to get token.

### 6. Endpoint description
All information regarding endpoints are covered in Swagger documentation.
Please visit [http://localhost:8080/swagger-ui.html#/](http://localhost:8080/swagger-ui.html#/) after project is started and click on desired function.

![](src/main/resources/swagger.png)

You can test all endpoint via swagger.

### 7. Future plans
Plans to improve project with:
1) Spring authentication (done: Jwt authentication, endpoints are secured to use with user, admin or moderator roles)
2) Transactional features
3) Automatic mailing features

### 8. Troubleshooting 
If You encounter any problems regarding operation, please let us know. 