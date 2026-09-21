
README.md


🐞 Bug Tracking System
A full-stack Bug Tracking System designed to simulate a real-world
software development workflow.

The project focuses on JWT authentication, role-based authorization,
project membership, bug assignment, comments, attachments, and
ownership-based access control.

🎯 Project Objective
The system supports four main roles:

ADMIN --- manages users, projects, and system-level operations

MANAGER --- manages projects and project members

DEVELOPER --- works on assigned bugs and projects they belong to

TESTER --- reports bugs and works with project-related bug
information

✨ Key Features
🔐 Authentication & Authorization
User registration and JWT login

Spring Security integration

Role-based authorization

Protected REST APIs

@PreAuthorize method security

401 Unauthorized and 403 Forbidden handling

Logged-in user profile through /users/me

👥 User Management
Create, read, update and delete users

Email uniqueness validation

BCrypt password encryption

Ownership validation for profile updates

📁 Project Management
Create, read, update and delete projects

Project status tracking

Project creator tracking

Role-based project access

👨‍👩‍👧 Project Member Management
Add users to projects

Get all project members

Get a specific project member

Remove project members

Prevent duplicate membership

Developers and testers can access only projects in which they are
members.

🐞 Bug Management
Create, read, update and delete bugs

Role-based bug visibility

Assign bugs to developers

Validate that an assigned developer belongs to the project

Bug visibility: - ADMIN / MANAGER → all bugs - DEVELOPER →
assigned bugs - TESTER → bugs reported by themselves

💬 Comments
Add, read, update and delete comments

Ownership-based modification/deletion

📎 Attachments
Upload attachments to bugs

Store file information

Track uploader

Allow ADMIN or uploader to delete an attachment

🏗️ Architecture
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
DTOs and MapStruct are used to keep API models separate from JPA
entities.

Controller → DTO → Service → Mapper → Entity → Repository → MySQL
🛠️ Technology Stack
Backend
Java 17

Spring Boot

Spring Security

JWT

Spring Data JPA

Hibernate

Maven

Lombok

Bean Validation

MapStruct

Database
MySQL

API Testing
Postman

Frontend
React.js

Vite

Bootstrap

Frontend development is currently in progress.

🔑 Role & Permission Overview
Feature ADMIN MANAGER DEVELOPER TESTER

Create User ✅ ❌ ❌ ❌
View All Users ✅ ❌ ❌ ❌
View User by ID ✅ ✅ ❌ ❌
View Own Profile ✅ ✅ ✅ ✅
Create Project ✅ ✅ ❌ ❌
View All Projects ✅ ✅ Member Projects Member Projects
Update Project ✅ ✅ ❌ ❌
Delete Project ✅ ❌ ❌ ❌
Add Project Member ✅ ✅ ❌ ❌
Remove Project Member ✅ ✅ ❌ ❌
Create Bug ✅ ❌ ❌ ✅
View All Bugs ✅ ✅ Assigned Bugs Reported Bugs
Assign Bug ✅ ✅ ❌ ❌
Update Bug ❌ ❌ Assigned Bug Own Bug
Delete Bug ✅ ✅ ❌ ❌

🔄 Bug Assignment Workflow
ADMIN / MANAGER
       ↓
Select Bug
       ↓
Select Developer
       ↓
Check Developer Role
       ↓
Check Project Membership
       ↓
Assign Bug
A bug cannot be assigned to a developer who is not a member of that
project.

🔒 Security Flow
User Login
    ↓
Username + Password
    ↓
AuthenticationManager
    ↓
JWT Generated
    ↓
Frontend Sends JWT
    ↓
JwtAuthenticationFilter
    ↓
Validate JWT
    ↓
Load UserDetails
    ↓
Set SecurityContext
    ↓
Controller Authorization
    ↓
Service Business Rules
Example:

Authorization: Bearer <JWT_TOKEN>
🌐 Main API Groups
Authentication
POST /auth/login
Users
POST   /users
GET    /users
GET    /users/{id}
GET    /users/me
PUT    /users/{id}
DELETE /users/{id}
Projects
POST   /projects
GET    /projects
GET    /projects/{id}
PUT    /projects/{id}
DELETE /projects/{id}
Project Members
POST   /projects/{projectId}/members/{userId}
GET    /projects/{projectId}/members
GET    /projects/{projectId}/members/{userId}
DELETE /projects/{projectId}/members/{userId}
Bugs
POST   /bugs
GET    /bugs
GET    /bugs/{id}
PUT    /bugs/{id}
DELETE /bugs/{id}
PUT    /bugs/{bugId}/assign/{developerId}
📂 Project Structure
src/main/java
└── com
    ├── configuration
    ├── controller
    ├── dto
    ├── entity
    ├── exception
    ├── filters
    ├── mapper
    ├── repository
    ├── service
    │   └── impl
    └── BugTrackerApplication.java
🚀 Running the Backend
1. Clone the repository
git clone <repository-url>
cd BugTracker
2. Configure MySQL
Create a MySQL database and configure the application properties.

spring.datasource.url=jdbc:mysql://localhost:3306/bug_tracker
spring.datasource.username=root
spring.datasource.password=your_password
3. Run the application
mvn spring-boot:run
Or run BugTrackerApplication.java from your IDE.

🧪 Testing
Recommended Postman flow:

Register Users
      ↓
Login Users
      ↓
Save JWT Tokens
      ↓
Create Project
      ↓
Add Project Members
      ↓
Create Bug
      ↓
Assign Bug
      ↓
Update Bug
      ↓
Test Role-Based Access
🎨 Frontend Status
The React frontend is currently being developed.

Planned dashboards:

ADMIN
 ├── Dashboard
 ├── Users
 ├── Projects
 ├── Bugs
 └── Project Members

MANAGER
 ├── Dashboard
 ├── Projects
 ├── Bugs
 └── Project Members

DEVELOPER
 ├── Dashboard
 ├── My Projects
 └── Assigned Bugs

TESTER
 ├── Dashboard
 ├── My Projects
 └── Reported Bugs
📌 Current Development Status
Backend Development       ✅ Completed
Authentication & JWT      ✅ Completed
Role Authorization        ✅ Completed
Project Management        ✅ Completed
Project Members           ✅ Completed
Bug Management            ✅ Completed
Comments                  ✅ Completed
Attachments               ✅ Completed
React Frontend            🚧 In Progress
Testing & Refinement      🚧 Upcoming
Deployment                ⏳ Upcoming
👨‍💻 Learning Focus
This project is helping me gain practical experience with:

Spring Boot architecture

REST API design

Spring Security and JWT

Role-based authorization

JPA relationships

DTO and Mapper patterns

Business-level authorization

Exception handling

React frontend integration

Full-stack application development

🙏 Acknowledgement
Special thanks to Prasoon Sir for teaching and guiding me throughout
this learning journey. The concepts and practical knowledge learned
through his guidance helped me turn them into a real-world project.

📬 Author
Mohammad Ramzan

Java Developer | Java Full Stack Developer

Currently looking for opportunities to learn, contribute, and grow as a
software developer.
