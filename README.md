🚀 Customer Relationship Management (CRM) System
A comprehensive, full-stack CRM web application built with Java and Spring Boot. This system is designed to help businesses manage customer interactions, track leads, assign tasks, and streamline communication, all from a centralized dashboard.

✨ Key Features
👤 Admin & User Roles: Secure login and distinct dashboards for administrators and standard users.

📇 Contact Management: Create, view, update, and delete customer contacts and leads.

📋 Task Management: Admins can create and assign tasks to users, who can then view and report on them.

📊 Reporting System: View task completion reports and user activity metrics.

🤖 AI-Powered Insights: An integrated module for generating AI-based reports.

📢 Broadcast Messaging: Send notifications and messages to all users or specific groups.

👥 User Management: Administrators can manage all user accounts within the system.

🛠️ Tech Stack
Backend: Java, Spring Boot

Data: Spring Data JPA (Hibernate)

Security: Spring Security

Frontend: HTML, JavaScript

Build Tool: Apache Maven

CI/CD: GitHub Actions

📋 Prerequisites
Before you begin, ensure you have the following installed on your local machine:

Java Development Kit (JDK) 17 or later

Apache Maven (or you can use the included Maven Wrapper)

🚀 Getting Started
Follow these instructions to get the project up and running on your local machine.

1. Clone the Repository
git clone [https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git](https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git)
cd YOUR_REPOSITORY

2. Configure the Application
Open the src/main/resources/application.properties file to configure your database connection. For example:

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_crm_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update

# Server Port
server.port=8080

3. Build the Project
This project includes the Maven Wrapper, so you don't need a system-wide Maven installation.

On macOS/Linux:

./mvnw clean package

On Windows:

./mvnw.cmd clean package

4. Run the Application
Once the build is complete, run the application with:

java -jar target/CustomerRelationshipManagement-0.0.1-SNAPSHOT.jar

(Note: The version number in the .jar file might differ based on your pom.xml.)

The application should now be running at http://localhost:8080.

📂 Project Structure
Here is a detailed look at the project's file structure:

├── .github/workflows/       # GitHub Actions CI/CD pipeline
│   └── static.yml
├── .mvn/wrapper/            # Maven Wrapper configuration
├── src/
│   ├── main/
│   │   ├── java/com/CustomerRelationshipManagement/
│   │   │   ├── CustomerRelationshipManagementApplication.java  # Main application entry point
│   │   │   ├── config/          # Spring Security configurations
│   │   │   ├── controller/      # Handles API endpoints and web requests
│   │   │   ├── entity/          # JPA entities (database models)
│   │   │   ├── repository/      # Data access layer (JPA repositories)
│   │   │   └── service/         # Business logic layer
│   │   └── resources/
│   │       ├── application.properties  # Main application configuration
│   │       └── static/                 # All frontend assets (HTML, JS, CSS)
│   └── test/                    # Unit and integration tests
├── .gitignore               # Specifies files for Git to ignore
├── mvnw & mvnw.cmd          # Maven Wrapper executables
└── pom.xml                  # Maven project configuration (dependencies, build)

🤖 Automation (CI/CD)
This project uses GitHub Actions for continuous integration. The workflow, defined in .github/workflows/static.yml, automatically triggers on every push to the main branch to:

Set up the Java environment.

Build the application using Maven.

Run all tests to ensure code integrity.

🤝 Contributing
Contributions are welcome! If you'd like to contribute, please fork the repository and create a pull request.

Fork the project.

Create your Feature Branch (git checkout -b feature/AmazingFeature).

Commit your changes (git commit -m 'Add some AmazingFeature').

Push to the branch (git push origin feature/AmazingFeature).

Open a Pull Request.

📄 License
This project is licensed under the MIT License. See the LICENSE file for details.
