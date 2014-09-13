# spring-boot-example #

This is an example of project using spring boot with spring security to register and login in users. 
It can be used as a template for a new webapp or simply as a working example on how a spring boot webapp works. 
At the bottom of this readme I'll explain how some of it works.

## Dependencies ##

- Maven
- MySQL (or run the MySQL vm using vagrant and ansible)

## Usage ##

1. Create MySQL user credentials for user `spring_boot_user` with password `spring_boot_pass`
1. Apply the database schema mysql-vm/provisioning/roles/database/files/spring_boot_schema.sql
1. To start the app you can:
  1. Run the com.codeng.springboot.Application in your IDE
  1. Build and run the jar from commandline
    1. `mvn clean package`
    1. `java -jar target/spring-boot-example.jar`

## Project Explanation ##

### Implementing Security ###

The reason for this project is to show how I could use Spring security to secure my app without having to overly
alter my current application. In the `com.codeng.springboot.domain` package there is only one domain object to 
represent a users account in the system. If we didn't need login authentication we'd only need one table in the 
database for the `account` and that would be that. 

However if you look at the database schema you will see 2 other tables: 
`users` and `authorities`. This are tables used by Spring Security. However, I've made a slight modification to the
original table design so there is a foreign key: `users.username` -> `account.email`. This is to link the security
 credentials to the project's domain. Now I can use Spring security to log into my website but still have my own
 domain model that isn't polluted by spring security dependencies. The caveat is that the RegistrationController
 must create the Account first before it can create the login credentials, but this is a reasonable expectation.
