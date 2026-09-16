# Launchpad Job Portal

A runnable full-stack job portal starter using React, Spring Boot, REST APIs, JPA, and MySQL.

## Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 18+
- Docker Desktop (recommended for MySQL)

## Run locally

1. Start the database from the repository root. This project publishes MySQL on host port `3307` to avoid conflicts with an existing local MySQL installation:

```bash
docker compose up -d
```

2. Start the API in a second terminal:

```bash
cd backend
mvn spring-boot:run
```

The API runs at `http://localhost:8080`.

3. Start React in a third terminal:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

## Features

- Candidate and recruiter registration/login with JWT
- Recruiter-only job publishing
- Candidate applications with optional PDF/DOC/DOCX resume upload
- Candidate application history and status tracking
- Recruiter application status updates

## API

- `GET /api/jobs?query=java` searches jobs
- `GET /api/jobs/{id}` gets one job
- `POST /api/auth/register` creates a candidate or recruiter account
- `POST /api/auth/login` returns a JWT
- `POST /api/jobs` creates a recruiter job
- `POST /api/applications/jobs/{jobId}` applies as a candidate
- `GET /api/applications/mine` lists the candidate's applications
- `GET /api/applications/job/{jobId}` lists applications for a recruiter job
- `PATCH /api/applications/{id}/status?value=REVIEWING` updates an application
- `PUT /api/jobs/{id}` and `DELETE /api/jobs/{id}` manage jobs as a recruiter

The next natural additions are JWT authentication, candidate applications, recruiter accounts, resume uploads, and an admin dashboard.
