# Car Book Application Documentation

## API Documentation

For detailed API documentation and to test the endpoints interactively, visit the Swagger UI:

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

This interface allows you to explore the available REST API endpoints, view request/response details, and execute API calls directly from the browser.

## REST API Endpoints

### **Car Controller**

#### 1. Get All Cars
- **Endpoint:** `GET /api/v1/car`
- **Description:** Fetches all cars associated with the authenticated user.
- **Response:**
    - `200 OK`: Returns a list of cars.

#### 2. Get Car by ID
- **Endpoint:** `GET /api/v1/car/{id}`
- **Description:** Fetches a specific car by ID for the authenticated user.
- **Path Variables:**
    - `id` (Long): The ID of the car.
- **Response:**
    - `200 OK`: Returns the car details.
    - `404 Not Found`: If the car is not found or does not belong to the user.

#### 3. Create Car
- **Endpoint:** `POST /api/v1/car`
- **Description:** Creates a new car for the authenticated user.
- **Request Body:**
    - `CarRequest`: The car details.
- **Response:**
    - `201 Created`: Returns the created car details.

#### 4. Update Car
- **Endpoint:** `PUT /api/v1/car/{id}`
- **Description:** Updates an existing car for the authenticated user.
- **Path Variables:**
    - `id` (Long): The ID of the car.
- **Request Body:**
    - `CarRequest`: The updated car details.
- **Response:**
    - `200 OK`: Returns the updated car details.
    - `404 Not Found`: If the car is not found or does not belong to the user.

#### 5. Delete Car
- **Endpoint:** `DELETE /api/v1/car/{id}`
- **Description:** Deletes a car by ID for the authenticated user.
- **Path Variables:**
    - `id` (Long): The ID of the car.
- **Response:**
    - `204 No Content`: If the deletion is successful.
    - `404 Not Found`: If the car is not found or does not belong to the user.

### **Maintenance Record Controller**

#### 1. Get Records by Car ID
- **Endpoint:** `GET /api/v1/record/car/{carId}`
- **Description:** Fetches all maintenance records for a specific car.
- **Path Variables:**
    - `carId` (Long): The ID of the car.
- **Response:**
    - `200 OK`: Returns a paginated list of maintenance records.

#### 2. Get All Records Grouped by Car
- **Endpoint:** `GET /api/v1/record`
- **Description:** Fetches all maintenance records grouped by car for the authenticated user.
- **Response:**
    - `200 OK`: Returns a map where each car is associated with its maintenance records.

#### 3. Get Record by ID
- **Endpoint:** `GET /api/v1/record/{id}`
- **Description:** Fetches a specific maintenance record by ID for the authenticated user.
- **Path Variables:**
    - `id` (Long): The ID of the maintenance record.
- **Response:**
    - `200 OK`: Returns the maintenance record details.
    - `404 Not Found`: If the record is not found or does not belong to the user.

#### 4. Create Maintenance Record
- **Endpoint:** `POST /api/v1/record`
- **Description:** Creates a new maintenance record for a car.
- **Request Body:**
    - `MaintenanceRecordRequest`: The maintenance record details.
- **Response:**
    - `201 Created`: Returns the created maintenance record details.

#### 5. Search Maintenance Records
- **Endpoint:** `GET /api/v1/record/search`
- **Description:** Searches for maintenance records by description.
- **Request Parameters:**
    - `description` (String): The description to search for.
- **Response:**
    - `200 OK`: Returns a paginated list of matching maintenance records.

#### 6. Update Maintenance Record
- **Endpoint:** `PUT /api/v1/record/{recordId}`
- **Description:** Updates an existing maintenance record for a car.
- **Path Variables:**
    - `recordId` (Long): The ID of the maintenance record.
- **Request Body:**
    - `MaintenanceRecordRequest`: The updated maintenance record details.
- **Response:**
    - `200 OK`: Returns the updated maintenance record details.
    - `404 Not Found`: If the record is not found or does not belong to the user.

#### 7. Delete Maintenance Record
- **Endpoint:** `DELETE /api/v1/record/{id}`
- **Description:** Deletes a maintenance record by ID.
- **Path Variables:**
    - `id` (Long): The ID of the maintenance record.
- **Response:**
    - `204 No Content`: If the deletion is successful.
    - `404 Not Found`: If the record is not found or does not belong to the user.

### **User Controller**

#### 1. Register User
- **Endpoint:** `POST /api/v1/users/register`
- **Description:** Registers a new user.
- **Request Body:**
    - `UserRequest`: The user details.
- **Response:**
    - `201 Created`: Returns the created user details.

#### 2. Update User
- **Endpoint:** `PUT /api/v1/users/{id}`
- **Description:** Updates an existing user.
- **Path Variables:**
    - `id` (Long): The ID of the user.
- **Request Body:**
    - `UserRequest`: The updated user details.
- **Response:**
    - `200 OK`: Returns the updated user details.
    - `404 Not Found`: If the user is not found.

#### 3. Delete User
- **Endpoint:** `DELETE /api/v1/users/{id}`
- **Description:** Deletes a user by ID.
- **Path Variables:**
    - `id` (Long): The ID of the user.
- **Response:**
    - `204 No Content`: If the deletion is successful.
    - `404 Not Found`: If the user is not found.

Key Points:
- ${DB_HOST}, ${DB_PORT}, ${DB_NAME}, ${DB_USERNAME}, and ${DB_PASSWORD} are placeholders in the application.yml that 
  Spring Boot will replace with the actual values from the environment variables.

## Docker Setup

### **1. Docker Compose Configuration**

```yaml
version: '3.1'

services:
  db:
    image: postgres:latest
    restart: always
    environment:
      POSTGRES_DB: mydatabase
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypassword
    ports:
      - "5432:5432"

  app:
    image: your-spring-boot-app-image
    restart: always
    environment:
      DB_HOST: db
      DB_PORT: 5432
      DB_NAME: mydatabase
      DB_USERNAME: myuser
      DB_PASSWORD: mypassword
      SPRING_PROFILES_ACTIVE: prod
    ports:
      - "8080:8080"
    depends_on:
      - db
