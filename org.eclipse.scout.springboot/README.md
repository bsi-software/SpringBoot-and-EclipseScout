# The Application

## Organization of the Code

```
+-- org.eclipse.scout.tasks (Maven project, this folder)
    +-- src/main/java
        +-- org.eclipse.scout.tasks (main Java package)
            +-- model
            |   +-- service
            |   |   + TaskService.java (task model in plain Java)
            |   |   + ... other service interfaces
            |   + Task.java (plain java with javax.validation where appropriate, no other dependencies like Scout/Spring etc.)
            |   + ... other model classes
            |
            +-- scout
            |   +-- auth (contains servlet filter, credential verifier, access control service)
            |   +-- platform (contains components relevant for the integration of Scout with Spring Boot)
            |   +-- ui (UI components and UI business logic)
            |       +-- admin (UI components for admin outline)
            |       +-- task (UI components for task management outline)
            |       |   + TaskForm.java (the dialog to create/edit tasks)
            |       |   + ... other task related UI components
            |       + Desktop.java (desktop overall layout, desktop menus etc.)
            |       + ... other general UI components
            |
            +-- spring
            |   +-- controller (REST API)
            |   |   + TaskController.java (REST controller making tasks available on /api/tasks using Spring Web)
            |   +-- repository (JPA persistence)
            |   |   +-- entity
            |   |   |   +-- converter
            |   |   |   |   + UuidConverter.java (JPA conversions for special attibutes like UUIDs)
            |   |   |   |    + ... other converter classes
            |   |   |   + TaskEntity.java (JPA entity for tasks, depending on task model and javax.persistence only)
            |   |   |   + ... other JPA entity classes
            |   |   + TaskRepository.java (task repository interface using Spring Data)
            |   |   + ... other repository interfaces
            |   +-- service (Spring services)
            |       + DefaultTaskService.java (provides a plain Java API and links to JPA repository)
            |       + ... other service implementations
            + Application.java (applications main class, makes it a Spring Boot application)
            + ScoutServlet.java (installs Scout UI servlet filter and credential verification)
            + WebMvcConfig.java (redirection of UI related requests to Scout servlet and managing requests to /api with Spring)
```

# Running the Application

## In the Eclipse IDE:

Run 'org.eclipse.scout.tasks.Application' as normal Java program

## From the Console

`mvn spring-boot:run`

## In the Browser

Once the application is running (see above) open it in the browser at [localhost:8080](http://localhost:8080/). By default, the application requires user authentication. The credentials are 'root/eclipse', 'alice/test', 'bob/test'.
