# 🚀 Customer Relationship Management (CRM) System

Welcome to the **CRM System** – a modern, robust platform to streamline every aspect of customer engagement, communication, and team productivity!

---

## Features

- **🔒 Secure Authentication:** Advanced Spring Security for protected access  
- **📊 Role-based Dashboards:** Distinct admin/user interfaces with engaging animations  
- **📣 Broadcast Messaging:** Organization-wide notifications and updates  
- **🗂️ Task & Report Management:** Assign, track, and analyze tasks effortlessly  
- **📇 Contact Management:** Full CRUD for efficient contact handling  
- **🤖 AI Reporting:** Data-driven insights with built-in reporting  

---

## Workflow & Project Structure
```
├── .github/
│ └── workflows/
│ └── static.yml # CI/CD pipeline config
├── .mvn/
│ └── wrapper/
│ └── maven-wrapper.properties # Maven Wrapper config
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── CustomerRelationshipManagement/
│ │ │ ├── CustomerRelationshipManagementApplication.java # Entry point
│ │ │ ├── config/ # Security configs
│ │ │ ├── controller/ # API endpoints
│ │ │ ├── entity/ # Domain models (entities)
│ │ │ ├── repository/ # Data access layer
│ │ │ └── service/ # Business logic
│ │ └── resources/
│ │ ├── application.properties # App configs
│ │ └── static/
│ │ ├── admin/ # Admin HTML & assets
│ │ ├── user/ # User HTML & assets
│ │ ├── js/ # JavaScript files
│ │ └── animations/ # Lottie animations
│ └── test/
│ └── java/
│ └── com/
│ └── CustomerRelationshipManagement/
│ └── CustomerRelationshipManagementApplicationTests.java
├── .gitignore # Files to ignore by Git
├── mvnw & mvnw.cmd # Maven Wrapper executables
text
```
---

## Quickstart

Clone the repository:
git clone https://github.com/your-username/crm-system.git

Set up prerequisites:
- Java 17+
- Maven 3.8+
- (Optional) Docker for containers
Run locally:
./mvnw spring-boot:run

Access UI at:
http://localhost:8080

text

---

## Core Components

| Component         | Description                                |
|-------------------|--------------------------------------------|
| AdminController    | Admin-specific APIs and broadcast messages |
| TaskController     | Task assignment/reporting functionality    |
| BroadcastEntity    | Models organization-wide notifications     |
| UserService       | Handles registration and management         |
| SecurityConfig    | Manages authentication and authorization    |

---

## Frontend Highlights

- **🎥 Lottie Animations:** Engaging dashboards for both admin and user roles  
- **⚙️ Custom JS:** Interactive experiences (`lottie-player.js`, `particles.min.js`)  

---

## Contributing

1. Fork this repository  
2. Create a new branch (`git checkout -b feature/amazing-feature`)  
3. Commit and push your changes  
4. Create a pull request to share your improvements  

---

## License

Project licensed under the MIT License.

---

**⭐ Star the repo and supercharge your customer relationships today!**
