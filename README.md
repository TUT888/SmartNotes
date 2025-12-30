# SmartNotes Backend
> **Active development** happens on [develop](https://github.com/TUT888/SmartNotes/tree/develop) branch. 
> The [main](https://github.com/TUT888/SmartNotes/tree/main) branch contains **stable release code**.

Backend service for **SmartNotes**, a personal project that enables smart note-taking with AI integration to 
help individuals have a better experience in note-taking, organizing, and revising knowledge.

**SmartNotes Frontend Repository:** [https://github.com/pvdev1805/SmartNotes](https://github.com/pvdev1805/SmartNotes)

## Table of Contents

- [Project Info](#project-info)
    - [Tech Stack](#tech-stack)
    - [Branching Strategy with CI/CD](#branching-strategy-with-cicd)
- [Supported Features](#supported-features)
    - [User Authentication](#1-user-authentication)
    - [AI-Powered Quiz Generation](#2-ai-powered-quiz-generation)
    - [CRUD Management Features](#3-crud-management-features)
- [Project Setup](#project-setup)
    - [Prerequisites](#prerequisites)
    - [HuggingFace API](#huggingface-api)
    - [Environment Variables](#environment-variables)
- [How to Test](#how-to-test)
    - [Test Commands](#test-commands-windows)
    - [Test Structure](#test-structure)
    - [Test Notes](#test-notes)
- [Contributors](#contributors)

## Project Info
### Tech Stack
- **Java 17** + **Spring Boot 3.5.5** for backend services
- **Maven** for build and dependency management
- **HuggingFace API** for AI quiz generation
- **MySQL** for database and persistence
- **Redis** for caching and session management
- **Docker** for containerization
- **Cloud and Deployment**: AWS (planned)
- **CI/CD**: GitHub Actions

### Branching Strategy with CI/CD
> For detail documentation about CI/CD process, please refer to another [dedicated README](./.github/workflows/README.md)

- **main**: stable, production‑ready code
- **develop**: integration branch with the latest completed features
- **feature/***: branches for individual features or fixes, merged into `develop` via pull requests

[Back to top](#smartnotes-backend)

## Supported Features
### 1. User Authentication
- Register, login, logout, and refresh tokens
- JWT-based secure authentication
- Secure access to user profile information

### 2. AI-Powered Quiz Generation
- Generate quizzes from notes using HuggingFace models
- Sample endpoint for testing without authentication

### 3. CRUD Management Features
**Document Management (CRUD)**: All types of documents including notes, flashcards, and quizzes:
- List all documents by user, or delete specific document

**Note Management**:
- Create, update, retrieve, and delete notes

**Flashcard & Flashcard Sets Management**:
- Create, update, retrieve, and delete flashcards
- Organize flashcards into sets

**Quiz & Quiz Sets Management**:
- Create, update, retrieve, and delete quizzes
- Organize quizzes into sets
- Track quiz attempts and scores

[Back to top](#smartnotes-backend)

## Project Setup
### Prerequisites
- **Java 17** or higher
- **MySQL**
- **Docker** 
  - For integration tests with Testcontainers
  - For Redis (optional for local development on Windows)
- **HuggingFace Account** (for AI features)

### HuggingFace API
- Create a HuggingFace account
- Create Access Token:
  - Click on your profile icon and select Access Token
  - Click Create New Token
  - Enable the Make calls to inference provider permission
- Accept Model Usage Conditions:
  - Navigate to the model's page, select a model you wish to use
  - Review and accept the conditions to use the model

### Environment variables
In project root directory, create `.env` file and paste your **ACCESS TOKEN** here
```
# AI INFERENCE
API_TOKEN=<YOUR-TOKEN-HERE>
API_URL=<YOUR-INFERENCE-PROVIDER>
MODEL=<YOUR-SELECTED-MODEL>

AI_API_USER_ROLE=user
AI_API_SYSTEM_ROLE=system
AI_API_TEMPERATURE=<YOUR-CUSTOM-VALUE>
AI_API_TOP_P=<YOUR-CUSTOM-VALUE>

# Database
DB_PORT=<YOUR-DATABASE-PORT>
DB_NAME=<YOUR-DATABASE-NAME>
DB_USERNAME=<YOUR-DATABASE-DB_USERNAME>
DB_PASSWORD=<YOUR-DATABASE-DB_PASSWORD>

# JWT Config
JWT_KEYSTORE_PASSWORD=<YOUR-KEYSTORE>
JWT_KEY_PASSWORD=<YOUR-PASSWORD>

# Redis Config
REDIS_HOST=<YOUR-HOST>
REDIS_PORT=<YOUR-PORT>
```

[Back to top](#smartnotes-backend)

## How to test
### Test commands (Windows)
Run all tests
```bash
# Run all tests (unit + integration)
./mvnw.cmd verify
```

Run specific type of test
```bash
# Run specific test file
./mvnw.cmd test -Dtest=NoteServiceTest

# Run all unit tests (service tests with mocks)
./mvnw.cmd test -Punit-test

# Run all integration tests (repository + controller with containers)
./mvnw.cmd verify -Pintegration-test
```

Run specific test group with tag, use **boolean expression** for tags combination
```bash
# Run test with "note" tag
./mvnw.cmd test -Dgroups="note"

# Run tests with "note" or "document" tags
./mvnw.cmd test -Dgroups="note | document"

# Combine with test type: run all unit tests with "note" tag
./mvnw.cmd test -Punit-test -Dgroups="note | document"
```

### Test Structure
```
src/test/java/
├── unit/                       # Fast tests (Surefire)
│   └── service/                # Service unit tests with mocked dependencies
│
└── integration/                # Slower tests (Failsafe)
    ├── repository/             # Repository tests with H2
    └── controller/             # Full-stack tests with MySQL + Redis containers
```

### Test Notes
- **Unit tests** run with Surefire and use mocked dependencies
- **Integration tests** run with Failsafe and use Testcontainers
- Docker must be running for integration tests (uses MySQL and Redis containers)
- First integration test run may take longer (downloads container images)

[Back to top](#smartnotes-backend)

## Contributors
**Project Maintainers:** This project (both frontend and backend) is developed and maintained by:

- **Alice Tat** ([@TUT888](https://github.com/TUT888))
- **Phu Vo** ([@pvdev1805](https://github.com/pvdev1805))

**Project Repositories:**
- Backend: [SmartNotes Backend](https://github.com/TUT888/SmartNotes)
- Frontend: [SmartNotes Frontend](https://github.com/pvdev1805/SmartNotes)

[Back to top](#smartnotes-backend)