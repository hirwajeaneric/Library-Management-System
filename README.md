# Library Management System

Welcome to the Library Management System, a Spring Boot application designed to manage library operations such as user registration, book management, borrowing, and returning books. This project is built with Java 17, Spring Boot 3+, and PostgreSQL, following best practices like validation, logging, security, exception handling, pagination, and testing.

## Project Overview

## Features

* **User Roles:**
    * **ADMIN:** Manages user access.
    * **LIBRARIAN:** Adds books, manages borrowing/returning.
    * **USER:** Borrows and returns books, views borrowing history.
* **Borrowing Rules:**
    * Maximum 3 books per user at a time.
    * 2-week (14-day) return deadline per book.
* **Activity Tracking:** Records borrowing and returning activities.
* **Database Views:** Provides summaries (e.g., borrowed books per user, overdue books).
* **Best Practices:**
    * Validation on entities.
    * Logging with SLF4J.
    * Role-based authentication with Spring Security.
    * Exception handling.
    * Pagination and sorting for history.
    * Unit and integration tests.

## Tech Stack

* **Java:** 17+
* **Spring Boot:** 3.2.3 (or latest stable)
* **Database:** PostgreSQL
* **IDE:** IntelliJ IDEA
* **Build Tool:** Maven
* **Dependencies:** Spring Web, Spring Data JPA, PostgreSQL Driver, Spring Security, Lombok, JUnit, Mockito

## Current Date

March 22, 2025 (assumed for documentation purposes).

## Prerequisites

Before setting up the project, ensure you have the following installed on your PC:

* **Java 17+:** [Download and install from AdoptOpenJDK](https://adoptopenjdk.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/).
    * **Verify:** `java -version`
* **Maven:** [Download from Apache Maven](https://maven.apache.org/download.cgi) or use IntelliJ's built-in Maven.
    * **Verify:** `mvn -version`
* **PostgreSQL:** [Install from PostgreSQL Downloads](https://www.postgresql.org/download/).
    * **Default port:** 5432.
    * **Verify:** `psql --version`
* **IntelliJ IDEA:** [Download from JetBrains](https://www.jetbrains.com/idea/download/).
    * Community or Ultimate edition (Community is sufficient).
* **Git (optional):** For cloning the repository: [Git Downloads](https://git-scm.com/downloads).
    * **Verify:** `git --version`

## Getting Started

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
    * Connect to `library_db` and execute:
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

## Step 3: Configure the Project

* **Open in IntelliJ IDEA:**
    * Launch IntelliJ IDEA.
    * Select `File > Open`, then navigate to the `library-management-system` directory and click `OK`.
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
    * Go to `File > Settings > Plugins`, search for "Lombok", install it, and restart IntelliJ.
    * Enable annotation processing: `File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors > Enable annotation processing`.

## Step 4: Build and Run

* **Build the Project:**
    * In IntelliJ, open the Maven tab (right sidebar).
    * Expand `library` > `Lifecycle`, double-click `clean` then `install`.
* **Run the Application:**
    * Open `src/main/java/com/example/library/LibraryApplication.java`.
    * Right-click and select `Run 'LibraryApplication.main()'`.
    * The app starts on `http://localhost:8080`.

## Step 5: Test the Application

Use a tool like Postman or curl to test the endpoints:

* **Register a User:**
    * `POST http://localhost:8080/api/auth/register`
    * **Body (raw JSON):**
        ```json
        {
            "username": "admin",
            "password": "password123",
            "email": "admin@example.com",
            "role": "ADMIN"
        }
        ```
      Repeat for a librarian (role: `"LIBRARIAN"`) and user (role: `"USER"`).
* **Add a Book (Librarian role required):**
    * `POST http://localhost:8080/api/librarian/books`
    * **Headers:** `Authorization: Basic <base64(username:password)>` (e.g., `admin:password123` becomes `YWRtaW46cGFzc3dvcmQxMjM=`).
    * **Body:**
        ```json
        {
            "title": "Spring in Action",
            "author": "Craig Walls",
            "isbn": "9781617294945",
            "totalCopies": 10
        }
        ```
* **Borrow a Book (Librarian role):**
    * `POST http://localhost:8080/api/librarian/borrow?userId=1&bookId=1`
* **Return a Book (Librarian role):**
    * `POST http://localhost:8080/api/librarian/return/1`
* **View User History (User role):**
    * `GET http://localhost:8080/api/user/history?userId=1&page=0&size=10&sort=borrowDate,desc`
* **Borrow Summary (Librarian role):**
    * `GET http://localhost:8080/api/librarian/borrow-summary`
* **Overdue Books (Librarian role):**
    * `GET http://localhost:8080/api/librarian/overdue-books`

## Project Structure

```text
library-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/library/
│   │   │       ├── LibraryApplication.java          # Main app entry point
│   │   │       ├── config/
│   │   │       │   └── SecurityConfig.java         # Security config
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java         # Authentication endpoints
│   │   │       │   ├── LibrarianController.java    # Librarian endpoints
│   │   │       │   └── UserController.java         # User endpoints
│   │   │       ├── exception/
│   │   │       │   └── GlobalExceptionHandler.java # Exception handling
│   │   │       ├── model/
│   │   │       │   ├── User.java                   # User entity
│   │   │       │   ├── Book.java                   # Book entity
│   │   │       │   ├── BorrowRecord.java           # Borrow record entity
│   │   │       │   ├── UserBorrowSummary.java      # Borrow summary view
│   │   │       │   └── OverdueBook.java            # Overdue books view
│   │   │       ├── repository/
│   │   │       │   ├── UserRepository.java         # User repo
│   │   │       │   ├── BookRepository.java         # Book repo
│   │   │       │   ├── BorrowRecordRepository.java # Borrow record repo
│   │   │       │   ├── UserBorrowSummaryRepository.java # Borrow summary repo
│   │   │       │   └── OverdueBookRepository.java  # Overdue books repo
│   │   │       └── service/
│   │   │           ├── UserService.java            # User logic
│   │   │           ├── BookService.java            # Book logic
│   │   │           └── BorrowService.java          # Borrow/return logic
│   │   └── resources/
│   │       └── application.properties              # App config
│   └── test/
│       ├── java/
│       │   └── com/example/library/
│   │       │       ├── LibraryApplicationTests.java    # Integration tests
│   │       │       └── service/
│   │       │           └── BorrowServiceTest.java      # Unit tests
└── pom.xml                                         # Maven config
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

* Open `src/test/java/com/example/library/service/BorrowServiceTest.java`.
* Right-click and select `Run 'BorrowServiceTest'`.

### Integration Tests

* Open `src/test/java/com/example/library/LibraryApplicationTests.java`.
* Right-click and select `Run 'LibraryApplicationTests'`.

## Troubleshooting

* **Database Connection Error:** Ensure PostgreSQL is running and credentials in `application.properties` match your setup. Check port 5432 is not blocked.
* **Lombok Not Working:** Verify the Lombok plugin is installed and annotation processing is enabled in IntelliJ.
* **Authentication Issues:** Use Basic Auth with `username:password` encoded in Base64 (e.g., `admin:password123` → `YWRtaW46cGFzc3dvcmQxMjM=`).
* **Build Fails:** Run `mvn clean install` in the terminal to diagnose dependency issues.

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