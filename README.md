# Filo Cloud Storage

![Overview](https://github.com/user-attachments/assets/b4180cb7-a5d7-4b37-8e05-208878ccb47b)

## Overview
Filo is a web-based cloud file storage application. Registered users can:
- **Upload** files and entire folders  
- **Browse** directory tree, create, rename and delete folders  
- **Download** individual files or folders in zip format
- **Move** files & folders around  
- **Search** by filename  
- **Track storage quota** with live progress bar  

All your data is stored in MinIO (S3-compatible), metadata in PostgreSQL, and sessions in Redis.<br>
Frontend of this application is - https://github.com/ssss1131/Filo-frontend

---

## ⚙️ Technologies & Tools

### Backend  
<p float="left">
  <img src="https://img.shields.io/badge/Java-17-black?logo=java" alt="Java 17" height="28px"/>
  <img src="https://img.shields.io/badge/Spring Boot-3.x-black?logo=springboot" alt="Spring Boot" height="28px"/>
  <img src="https://img.shields.io/badge/Spring%20Security-black?logo=springsecurity" alt="Spring Security" height="28px"/>
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-black?logo=springdata" alt="Spring Data JPA" height="28px"/>
  <img src="https://img.shields.io/badge/Spring%20Data%20Redis-black?logo=redis" alt="Spring Data Redis" height="28px"/>
  <img src="https://img.shields.io/badge/MinIO-black?logo=minio" alt="MinIO" height="28px"/>
  <img src="https://img.shields.io/badge/PostgreSQL-black?logo=postgresql" alt="PostgreSQL" height="28px"/>
  <img src="https://img.shields.io/badge/Flyway-DB-black?logo=flyway" alt="Flyway" height="28px"/>
</p>

### Deployment  
<p float="left">
  <img src="https://img.shields.io/badge/Docker-black?logo=docker" alt="Docker" height="28px"/>
  <img src="https://img.shields.io/badge/Docker–Compose-black?logo=docker" alt="Docker Compose" height="28px"/>
  <img src="https://img.shields.io/badge/Nginx-black?logo=nginx" alt="Nginx" height="28px"/>
</p>

## Requirements
- Java 17+
- Docker, Docker-compose
- Git

## Local setup

1. Clone the repository:
```bash
git clone https://github.com/ssss1131/Filo-backend
cd Filo-backend
```

2. Start the required services using Docker Compose:
```bash
docker-compose up -d
```
This will start:
- PostgreSQL database (port 5433)
- Redis server (port 6379)
- MinIO object storage (ports 9000 and 9090)

3. Build and run the Spring Boot application:
```bash
./gradlew bootRun
```

The backend will start on the default port 8080.

Frontend setup you can read in https://github.com/ssss1131/Filo-frontend


