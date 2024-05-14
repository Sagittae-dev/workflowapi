# Workflow API

## Introduction
The Workflow API is a RESTful web service designed to manage tasks and comments within a workflow system. It provides endpoints for creating, updating, and retrieving tasks, as well as adding, searching, and retrieving comments associated with tasks.

## Features
 - ### Task management
   Create, update, and retrieve tasks with various attributes such as name, priority, status, type, description, due date, assignment, and attachments.
 - ### Comment management
   Add comments to tasks, search comments by content, retrieve and remove comments associated with tasks.
 - ### User management
   CRUD operations on users, getting tasks and comments assigned and created by specific user.
   
## Technologies used
  - Spring Boot version: 3.2.4
  - Spring Data JPA
  - Spring security
  - PostgreSQL database version: 42.7.3
  - Java 22
  - Mockito 5.7.0
  - Junit 5

## Getting started
### Prerequisites
  - JDK 22
  - Apache maven
  - Git
### Installation and running
  1) Fork repository
  2) Clone the repository
     `git clone https://github.com/your-repo/workflow-api.git`
  3) Navigate to the project root
  4) Build the application with command: `mvn clean install`
  5) mvn spring-boot:run
     
     You can also run via IDE for example: IntelliJ Idea by Run WorkflowapiApplication
     
### Usage
  This is application using API references, You can use Postman to make specific web REST calls
### Api Referrences
 - GET http://localhost:8080/api/v1/users/{userId} - getting user by id
 - POST http://localhost:8080/api/v1/users - create new user, example body: `{"username": "Mateusz", "email": "mateuszb@mail.com", "isActive": true}`
 - POST http://localhost:8080/api/v1/tasks - create new Task, example body: `{
    "name": "sample task2",
    "priority": "MEDIUM",
    "taskType": "BUG",
    "description": "This is a sample task description",
    "taskStatus": "IN_PROGRESS",
    "comments": [{ "content": "example comment 2", "likes": 3, "unlikes": 2, "creationDate": "2024-05-14"}],"attachment": null}`
- POST http://localhost:8080/api/v1/comments/{taskId}?username={username} - add comment to specific task
## Disclaimer
This is a work-in-progress application, and functionalities might be under development.
## Contact
mateusz.baranski8@gmail.com
 
