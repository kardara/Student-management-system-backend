# Student Management System

## AUCA Best Programming Practice - Mid-term Exam (INSY8322)

A comprehensive web-based student management system built with Spring Boot, designed to handle university administration tasks including student enrollment, course management, grading, attendance tracking, and financial operations.

## Features

- **Student Management**: Complete CRUD operations for student records
- **Academic Structure**: Management of faculties, departments, and programs
- **Course Management**: Course offerings and student registrations
- **Grade Management**: Academic performance tracking and GPA calculation
- **Attendance Tracking**: Class attendance monitoring
- **Fee & Payment System**: Financial management and payment processing
- **Authentication**: JWT-based authentication with OAuth2 integration (Google, GitHub)
- **Email Services**: Automated notifications and OTP verification
- **Global Search**: Cross-entity search functionality

## Technology Stack

- **Backend**: Spring Boot 3.4.5
- **Database**: PostgreSQL
- **Security**: JWT Authentication + OAuth2
- **Build Tool**: Maven
- **Containerization**: Docker
- **Version Control**: Git
- **Language**: Java 21

## Architecture

The application follows a layered architecture:
- **Controller Layer**: REST API endpoints
- **Service Layer**: Business logic implementation
- **Repository Layer**: Data access with JPA
- **Model Layer**: Entity classes and DTOs

## Design Patterns

- **Repository Pattern**: Data access abstraction
- **MVC Pattern**: Separation of concerns
- **Service Layer Pattern**: Business logic encapsulation
- **Factory Pattern**: UserDetails creation

## Getting Started

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL
- Docker (optional)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd studentManagement
   ```

2. **Configure Environment Variables**
   Create a `.env` file with the following variables:
   ```env
   DB_URL=jdbc:postgresql://localhost:5432/studentdb
   DB_USER=your_db_user
   DB_PASS=your_db_password
   FRONT_END=http://localhost:3000
   EMAIL_USERNAME=your_email@gmail.com
   EMAIL_PASSWORD=your_app_password
   OAUTH_GOOGLE_ID=your_google_client_id
   OAUTH_GOOGLE_SECRET=your_google_client_secret
   OAUTH_GITHUB_ID=your_github_client_id
   OAUTH_GITHUB_SECRET=your_github_client_secret
   ```

3. **Build and Run**
   ```bash
   # Using Maven
   mvn clean install
   mvn spring-boot:run

   # Using Docker Compose (recommended for full setup with database)
   docker-compose up --build

   # Or using Docker only (you need PostgreSQL running separately)
   docker build -t student-management .
   docker run -p 8080:8080 --env-file .env student-management
   ```

## API Documentation

### Authentication
- `POST /auth/login` - User login
- `POST /auth/register` - User registration
- `GET /auth/oauth2/google` - Google OAuth login
- `GET /auth/oauth2/github` - GitHub OAuth login

### Student Management
- `GET /student/get` - Get all students
- `GET /student/get?id={id}` - Get student by ID
- `POST /student/add` - Add new student
- `PUT /student/update` - Update student
- `DELETE /student/delete` - Delete student

### Other Endpoints
- Course management, grade management, attendance, payments, academic units, etc.

## Testing

Run tests with:
```bash
mvn test
```

## Docker Support

The application includes a multi-stage Dockerfile for optimized containerization.

## Project Structure

```
src/
├── main/
│   ├── java/com/kardara/studentManagement/
│   │   ├── controller/          # REST controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access
│   │   ├── model/               # Entities and enums
│   │   ├── DTO/                 # Data transfer objects
│   │   └── config/              # Configuration classes
│   └── resources/
│       ├── application.properties
│       └── META-INF/
└── test/                        # Test classes
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is developed as part of AUCA Best Programming Practice course requirements.

## Contact

For questions or support, please contact the development team.
