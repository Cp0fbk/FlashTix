# FlashTix

FlashTix is an event ticketing web application with a React-based frontend and a Java Spring Boot backend.

## Introduction

FlashTix lets users browse upcoming events, view event details, and book tickets through a simple, modern web interface. The backend exposes REST APIs for managing events and bookings, while the frontend provides a responsive, single-page experience.

The project is split into two main parts:
- `backend`: Java Spring Boot REST API
- `frontend`: React + TypeScript SPA built with Vite and Tailwind CSS

## How to Run

### Prerequisites
- Java 21+ installed and on your `PATH`
- Maven installed (or use the included `mvnw.cmd` on Windows)
- Node.js (LTS) and npm installed

Project root: `FlashTix`

### 1. Run the Backend

From the project root:

```powershell
cd backend

# On Windows using the Maven wrapper
mvnw.cmd spring-boot:run

# Or, if Maven is installed globally
mvn spring-boot:run
```

The backend will start on `http://localhost:8081` (unless overridden in `application.yaml`).

### 2. Run the Frontend

Open a new terminal window and, from the project root:

```powershell
cd frontend

# Install dependencies
npm install

# Start the dev server
npm run dev
```

By default, Vite serves the app at `http://localhost:5173` (check the terminal output for the exact URL).

### 3. Access the Application

- Frontend UI: `http://localhost:5173`
- Backend API base URL (example): `http://localhost:8080/api/...` (adjust to match your controller mappings)

## Project Structure

- `backend/`: Spring Boot application (`src/main/java/com/flashtix`, `application.yaml`)
- `frontend/`: React application (`src/App.tsx`, components, data, types)
- `README.md`: Project documentation

