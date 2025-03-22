# Library Management System

Welcome to the **Library Management System**, a Spring Boot application designed to manage library operations such as user registration, book management, borrowing, and returning books. This project is built with Java 17, Spring Boot 3+, and PostgreSQL, following best practices like validation, logging, security, exception handling, pagination, and testing.

## Project Overview

### Features
- **User Roles**:
  - **ADMIN**: Manages user access.
  - **LIBRARIAN**: Adds books, manages borrowing/returning.
  - **USER**: Borrows and returns books, views borrowing history.
- **Borrowing Rules**:
  - Maximum 3 books per user at a time.
  - 2-week (14-day) return deadline per book.
- **Activity Tracking**: Records borrowing and returning activities.
- **Database Views**: Provides summaries (e.g., borrowed books per user, overdue books).
- **Best Practices**:
  - Validation on entities.
  - Logging with SLF4J.
  - Role-based authentication with Spring Security.
  - Exception handling.
  - Pagination and sorting for history.
  - Unit and integration tests.

### Tech Stack
- **Java**: 17+
- **Spring Boot**: 3.2.3 (or latest stable)
- **Database**: PostgreSQL
- **IDE**: IntelliJ IDEA
- **Build Tool**: Maven
- **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver, Spring Security, Lombok, JUnit, Mockito

### Current Date
- March 22, 2025 (assumed for documentation purposes).

---

## Prerequisites

Before setting up the project, ensure you have the following installed on your PC:

1. **Java 17+**:
  - Download and install from [AdoptOpenJDK](https://adoptopenjdk.net/) or [Oracle](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
  - Verify: `java -version`

2. **Maven**:
  - Download from [Apache Maven](https://maven.apache.org/download.cgi) or use IntelliJ's built-in Maven.
  - Verify: `mvn -version`

3. **PostgreSQL** (for local setup without Docker):
  - Install from [PostgreSQL Downloads](https://www.postgresql.org/download/).
  - Default port: `5432`.
  - Verify: `psql --version`

4. **IntelliJ IDEA**:
  - Download from [JetBrains](https://www.jetbrains.com/idea/download/).
  - Community or Ultimate edition (Community is sufficient).

5. **Docker** (for containerized setup):
  - Install Docker Desktop from [Docker Hub](https://www.docker.com/products/docker-desktop/).
  - Verify: `docker --version` and `docker-compose --version`

6. **Git** (optional):
  - For cloning the repository: [Git Downloads](https://git-scm.com/downloads).
  - Verify: `git --version`

---

## Getting Started (Local Setup)

### Step 1: Clone the Repository
If the project is hosted on a Git repository (e.g., GitHub), clone it:

```bash
git clone <repository-url>
cd library-management-system
```

Alternatively, if you’re starting from scratch, copy the project files into a new directory named `library-management-system`.

## Step 2: Set Up PostgreSQL

* **Start PostgreSQL:**
  * **On Windows:** Use pgAdmin or start via services.
  * **On macOS/Linux:** `sudo service postgresql start`.
* **Create the Database:**
  * Open a PostgreSQL client (e.g., psql or pgAdmin).
  * Run:
      ```sql
      CREATE DATABASE library_db;
      ```
* **Create Views:**
  * Connect to `library_db` and execute the SQL from `init.sql`.

## Step 3: Configure the Project

* **Open in IntelliJ IDEA:**
  * Launch IntelliJ IDEA.
  * Select `File > Open`, navigate to `library-management-system`, and click `OK`.
* **Update `application.properties`:**
  * **Location:** `src/main/resources/application.properties`.
  * **Edit with your PostgreSQL credentials:**
      ```properties
      spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
      spring.datasource.username=postgres
      spring.datasource.password=yourpassword
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true
      spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
      logging.level.org.springframework=INFO
      logging.level.com.example.library=DEBUG
      ```
    Replace `yourpassword` with your PostgreSQL password.
* **Enable Lombok:**
  * Go to `File > Settings > Plugins`, install "Lombok", and restart IntelliJ.
  * Enable annotation processing: `File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors`.

## Step 4: Build and Run

* **Build the Project:**
  * Open the Maven tab, expand `library` > `Lifecycle`, double-click `clean` then `install`.
* **Run the Application:**
  * Open `LibraryApplication.java`, right-click, and select `Run 'LibraryApplication.main()'`.
  * App runs on `http://localhost:8080`.

## Getting Started (Docker Setup)

### Step 1: Prepare the Project

* Ensure you have the `Dockerfile` and `init.sql` files in the project root (see below for content).
* **Build the Spring Boot JAR:**
  * In IntelliJ, open the Maven tab, run `clean` and `install`.
  * Or, in terminal: `mvn clean install`.

### Step 2: Create Docker Files

* **Dockerfile** (in project root):
    ```dockerfile
    FROM openjdk:17-jdk-slim
    WORKDIR /app
    COPY target/library-0.0.1-SNAPSHOT.jar app.jar
    EXPOSE 8080
    ENTRYPOINT ["java", "-jar", "app.jar"]
    ```
* **init.sql** (in project root):
    ```sql
    CREATE VIEW user_borrow_summary AS
    SELECT
        u.id AS user_id,
        u.username,
        COUNT(br.id) AS borrowed_count
    FROM users u
    LEFT JOIN borrow_records br
        ON u.id = br.user_id
        AND br.status = 'BORROWED'
    GROUP BY u.id, u.username;

    CREATE VIEW overdue_books AS
    SELECT
        br.id AS record_id,
        u.id AS user_id,
        u.username,
        b.id AS book_id,
        b.title,
        br.due_date
    FROM borrow_records br
    JOIN users u ON br.user_id = u.id
    JOIN books b ON br.book_id = b.id
    WHERE br.status = 'BORROWED'
      AND br.due_date < CURRENT_TIMESTAMP;
    ```

### Step 3: Run with Docker Compose

* **Start Services:**
  * In the project root, run:
      ```bash
      docker-compose up --build
      ```
  * This builds the app image and starts both services.
* **Access the App:**
  * App: `http://localhost:8080`
  * PostgreSQL: `localhost:5432` (if needed externally).
* **Stop Services:**
  * Press `Ctrl+C` or run:
      ```bash
      docker-compose down
      ```
  * To remove volumes: `docker-compose down -v`.

## Testing the Application

Use Postman or curl to test endpoints (same for local or Docker setup):

* **Register a User:**
  * `POST http://localhost:8080/api/auth/register`
  * **Body:**
      ```json
      {
          "username": "admin",
          "password": "password123",
          "email": "admin@example.com",
          "role": "ADMIN"
      }
      ```
* **Add a Book (Librarian role):**
  * `POST http://localhost:8080/api/librarian/books`
  * **Headers:** `Authorization: Basic YWRtaW46cGFzc3dvcmQxMjM=`
  * **Body:**
      ```json
      {
          "title": "Spring in Action",
          "author": "Craig Walls",
          "isbn": "9781617294945",
          "totalCopies": 10
      }
      ```
* **Borrow a Book:** `POST http://localhost:8080/api/librarian/borrow?userId=1&bookId=1`
* **Return a Book:** `POST http://localhost:8080/api/librarian/return/1`
* **User History:** `GET http://localhost:8080/api/user/history?userId=1&page=0&size=10&sort=borrowDate,desc`
* **Borrow Summary:** `GET http://localhost:8080/api/librarian/borrow-summary`
* **Overdue Books:** `GET http://localhost:8080/api/librarian/overdue-books`

## Project Structure

```text
library-management-system/
├── Dockerfile                  # Docker image for the app
├── docker-compose.yml          # Docker Compose configuration
├── init.sql                    # SQL script for database views
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/library/
│   │   │       ├── LibraryApplication.java
│   │   │       ├── config/
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── LibrarianController.java
│   │   │       │   └── UserController.java
│   │   │       ├── exception/
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── model/
│   │   │       │   ├── User.java
│   │   │       │   ├── Book.java
│   │   │       │   ├── BorrowRecord.java
│   │   │       │   ├── UserBorrowSummary.java
│   │   │       │   └── OverdueBook.java
│   │   │       ├── repository/
│   │   │       │   ├── UserRepository.java
│   │   │       │   ├── BookRepository.java
│   │   │       │   ├── BorrowRecordRepository.java
│   │   │       │   ├── UserBorrowSummaryRepository.java
│   │   │       │   └── OverdueBookRepository.java
│   │   │       └── service/
│   │   │           ├── UserService.java
│   │   │           ├── BookService.java
│   │   │           └── BorrowService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/
│       │   └── com/example/library/
│   │       │       ├── LibraryApplicationTests.java
│   │       │       └── service/
│   │       │           └── BorrowServiceTest.java
└── pom.xml
```
## Database Schema

### Tables

* **users:**
  * `id` (PK, bigint)
  * `username` (varchar, unique)
  * `password` (varchar)
  * `email` (varchar, unique)
  * `role` (enum: 'ADMIN', 'LIBRARIAN', 'USER')
  * `created_at` (timestamp)
  * `updated_at` (timestamp)
* **books:**
  * `id` (PK, bigint)
  * `title` (varchar)
  * `author` (varchar)
  * `isbn` (varchar, unique)
  * `total_copies` (int)
  * `available_copies` (int)
  * `created_at` (timestamp)
  * `updated_at` (timestamp)
* **borrow\_records:**
  * `id` (PK, bigint)
  * `user_id` (FK → users)
  * `book_id` (FK → books)
  * `borrow_date` (timestamp)
  * `due_date` (timestamp)
  * `return_date` (timestamp, nullable)
  * `status` (enum: 'BORROWED', 'RETURNED', 'OVERDUE')
  * `created_at` (timestamp)
  * `updated_at` (timestamp)

### Views

* **user\_borrow\_summary:** Summarizes borrowed books per user.
* **overdue\_books:** Lists overdue borrow records.

## Running Tests

### Unit Tests

Open `BorrowServiceTest.java`, right-click, and select `Run 'BorrowServiceTest'`.

### Integration Tests

Open `LibraryApplicationTests.java`, right-click, and select `Run 'LibraryApplicationTests'`.

## Troubleshooting

* **Docker Not Starting:** Ensure Docker Desktop is running. Check for port conflicts (5432, 8080).
* **Database Connection Fails:** Verify `POSTGRES_PASSWORD` in `docker-compose.yml` matches `SPRING_DATASOURCE_PASSWORD`.
* **App Not Starting:** Ensure the JAR file is built (`mvn clean install`). Check logs: `docker logs library-app`.

## Contributing

1.  Fork the repository.
2.  Create a feature branch: `git checkout -b feature/your-feature`.
3.  Commit changes: `git commit -m "Add your feature"`.
4.  Push to branch: `git push origin feature/your-feature`.
5.  Open a pull request.

## Contact

For questions, reach out to the project maintainer:

* **Name:** Jean Eric Hirwa
* **Email:** hirwajeric@gmail.com

Happy coding! Let’s build an awesome library system together!