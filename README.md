# Open Source Business Application
Demo application with **[Spring Boot](https://projects.spring.io/spring-boot/)** and **[Eclipse Scout](http://www.eclipse.org/scout/) UI**

**Please Note**: This is work in progress and has not (yet) been tested for productive usage.

## Application Description
Minimal but fully functioning business application
* Business use case: Task management
* Administration use case: User, role and permission management
* Interfaces: Both UI and REST API

### Application Login
When running from within Eclipse, open your browser at [localhost](http://localhost:8080). The login dialog then appears as shown below. You may use root/eclipse as login/password for the development environment. The other default users are alice/test and bob/test. 

![Login Screen](/screenshots/console_login_form.png)

### Task Management

In the **Tasks** outline you may add new tasks and/or edit existing tasks. Mandatory fields for tasks are title, responsible and due date. When tasks 

![Add a new Task](/screenshots/ui_new_task.png)

### User and Role Management

Users that have the **root** role assigned may manage roles and users in the **Administration** outline. Editing a user is shown in the screenshot below. 

![Edit a User](/screenshots/ui_edit_user.png)

In the dev setup only user root has admin priviledges, users alice and bob do not have root access and therefore don't see the admin outline.

Users with the root role may also add/change roles and defining its associted permissions.

![Edit Roles](/screenshots/ui_edit_role.png)

## Technologies per Component
* Main frameworks: Spring Boot and Eclipse Scout
* Authentication and authorization: Servlet filters, java.security, Eclipse Scout
* Internationalization: Eclipse Scout
* Persistence: javax.persistence, Spring Data JPA, Hibernate, H2
* Validation: javax.validation
* Business logic: Plain Java: Independent of Spring and Scout
* User interface: Eclipse Scout
* REST API: Spring

## Implemented Features / Status
* Spring and Scout integration
* Business logic
* Persistences for tasks, users and roles
* Authorization and authentication (for UI)
* User interface for tasks and admin
* REST API (readonly, no authentication so far)

## Roadmap
* Add authentication for REST services
* Add tests and Travis CI
* Dockerize application with multi-container setup: Data, DB, Application
* Use PostgreSQL for realistic setup
* Create Maven artefact based on this application
* Add user signup functionaliy (+ password reset by email)

View [README](org.eclipse.scout.springboot/README.md) for more information
