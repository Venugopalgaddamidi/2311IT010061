# Notification System Design

# Stage 1

## Introduction

The Campus Notification System is designed to deliver important announcements to authenticated users in a secure and reliable manner. Notifications may include placement drives, examination schedules, event announcements, academic updates, and other campus-related information. The service follows RESTful API principles and exchanges data in JSON format to ensure seamless integration with frontend applications.

---

# Core Actions

The notification platform should support the following operations:

- Create a notification
- Retrieve all notifications for a user
- Retrieve a specific notification
- Update an existing notification
- Delete a notification
- Mark a notification as read
- Mark all notifications as read
- Retrieve unread notifications

---

# REST API Endpoints

## 1. Create Notification

### Endpoint

```http
POST /api/v1/notifications
```

### Description

Creates a new notification.

### Request Headers

```http
Authorization: Bearer <JWT>
Content-Type: application/json
Accept: application/json
```

### Request Body

```json
{
  "title": "Placement Drive",
  "message": "TCS recruitment drive on Friday at 10:00 AM.",
  "type": "PLACEMENT",
  "priority": "HIGH"
}
```

### Success Response

```json
{
  "id": 101,
  "message": "Notification created successfully."
}
```

### Status Code

```
201 Created
```

---

## 2. Get All Notifications

### Endpoint

```http
GET /api/v1/notifications
```

### Description

Returns all notifications available for the logged-in user.

### Request Headers

```http
Authorization: Bearer <JWT>
Accept: application/json
```

### Success Response

```json
{
  "notifications": [
    {
      "id": 101,
      "title": "Placement Drive",
      "message": "TCS recruitment drive on Friday.",
      "type": "PLACEMENT",
      "priority": "HIGH",
      "isRead": false,
      "createdAt": "2026-07-01T09:30:00Z"
    },
    {
      "id": 102,
      "title": "Exam Schedule",
      "message": "Mid-semester timetable has been released.",
      "type": "ACADEMIC",
      "priority": "MEDIUM",
      "isRead": true,
      "createdAt": "2026-06-29T08:00:00Z"
    }
  ]
}
```

### Status Code

```
200 OK
```

---

## 3. Get Notification By ID

### Endpoint

```http
GET /api/v1/notifications/{id}
```

### Description

Returns complete information for a single notification.

### Request Headers

```http
Authorization: Bearer <JWT>
Accept: application/json
```

### Success Response

```json
{
  "id": 101,
  "title": "Placement Drive",
  "message": "TCS recruitment drive on Friday at 10:00 AM.",
  "type": "PLACEMENT",
  "priority": "HIGH",
  "isRead": false,
  "createdAt": "2026-07-01T09:30:00Z"
}
```

### Status Code

```
200 OK
```

---

## 4. Update Notification

### Endpoint

```http
PUT /api/v1/notifications/{id}
```

### Description

Updates an existing notification.

### Request Body

```json
{
  "title": "Placement Drive Updated",
  "message": "The interview venue has changed to Seminar Hall 2.",
  "priority": "HIGH"
}
```

### Success Response

```json
{
  "message": "Notification updated successfully."
}
```

### Status Code

```
200 OK
```

---

## 5. Delete Notification

### Endpoint

```http
DELETE /api/v1/notifications/{id}
```

### Description

Deletes the specified notification.

### Request Headers

```http
Authorization: Bearer <JWT>
```

### Success Response

```json
{
  "message": "Notification deleted successfully."
}
```

### Status Code

```
204 No Content
```

---

## 6. Mark Notification as Read

### Endpoint

```http
PATCH /api/v1/notifications/{id}/read
```

### Description

Marks a notification as read.

### Success Response

```json
{
  "message": "Notification marked as read."
}
```

### Status Code

```
200 OK
```

---

## 7. Mark All Notifications as Read

### Endpoint

```http
PATCH /api/v1/notifications/read-all
```

### Description

Marks every unread notification of the logged-in user as read.

### Success Response

```json
{
  "message": "All notifications marked as read."
}
```

### Status Code

```
200 OK
```

---

## 8. Get Unread Notifications

### Endpoint

```http
GET /api/v1/notifications/unread
```

### Description

Returns only unread notifications.

### Success Response

```json
{
  "notifications": [
    {
      "id": 105,
      "title": "Hackathon Registration",
      "message": "Registration closes tomorrow.",
      "type": "EVENT",
      "priority": "MEDIUM",
      "isRead": false
    }
  ]
}
```

### Status Code

```
200 OK
```

---

# Notification JSON Schema

```json
{
  "id": 101,
  "title": "Placement Drive",
  "message": "TCS recruitment drive on Friday.",
  "type": "PLACEMENT",
  "priority": "HIGH",
  "recipientId": 55,
  "isRead": false,
  "createdAt": "2026-07-01T09:30:00Z",
  "updatedAt": "2026-07-01T09:45:00Z"
}
```

# Real-Time Notification Mechanism

The application uses **WebSocket** to deliver notifications instantly without requiring the user to refresh the page.

### Workflow

1. User logs into the application.
2. Authentication is completed using JWT.
3. The client establishes a WebSocket connection.
4. An administrator creates a notification.
5. The notification is stored in the database.
6. The notification service publishes the event.
7. The WebSocket server pushes the notification to all eligible connected users.
8. The frontend immediately displays the new notification.


# Conclusion

The proposed REST API provides a consistent interface for managing notifications in the Campus Notification System. The design follows REST principles, uses meaningful endpoint naming, standard HTTP methods, JWT-based authentication, JSON payloads, and WebSocket-based real-time delivery. This design offers a solid foundation for future implementation in Spring Boot while providing frontend developers with a clear API contract.


------------------------------------------------------------------------------
---

# Stage 2

# Persistent Storage Selection

For the notification platform, I would choose **PostgreSQL** as the primary database.

The notification system stores structured information such as users, notifications, and read/unread status. These entities have well-defined relationships, making a relational database the most suitable option.



# Database Design

The notification platform requires three main tables.

## Users Table

| Column | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL | Primary Key |
| name | VARCHAR(100) | User Name |
| email | VARCHAR(150) | Unique Email Address |
| created_at | TIMESTAMP | Account Creation Time |

---

## Notifications Table

| Column | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL | Primary Key |
| title | VARCHAR(255) | Notification Title |
| message | TEXT | Notification Content |
| category | VARCHAR(50) | Placement, Event, Result, etc. |
| priority | VARCHAR(20) | LOW, MEDIUM, HIGH |
| created_at | TIMESTAMP | Notification Creation Time |

---

## User_Notifications Table

This table maps notifications to individual users.

| Column | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL | Primary Key |
| user_id | BIGINT | References Users Table |
| notification_id | BIGINT | References Notifications Table |
| is_read | BOOLEAN | Read Status |
| read_at | TIMESTAMP | Time Notification Was Read |

---

## 1. Slow Search Performance

Searching through millions of records may increase response time.

### Solution

Create indexes on frequently searched columns.

Example:

- user_id
- notification_id
- created_at
- is_read

---

## 2. Increasing Storage Requirements

Older notifications continue occupying database space.

### Solution

Move old notifications to archive tables while keeping recent notifications in the primary database.

---

## 3. Heavy Concurrent Traffic

Thousands of users may request notifications simultaneously.

### Solution

Introduce Redis caching to reduce repeated database access.

---

## 4. Delayed Notification Delivery

Sending notifications one user at a time becomes inefficient.

### Solution

Use a message broker such as RabbitMQ or Apache Kafka so notification delivery happens asynchronously.

---

## 5. Large Table Sizes

Very large tables slow down sorting and filtering operations.

### Solution

Partition notification tables based on creation date (monthly or yearly partitions).

---

# SQL Queries

## Create Notification

```sql
INSERT INTO notifications
(title, message, category, priority)
VALUES
(
'Placement Drive',
'Infosys recruitment drive on Friday',
'PLACEMENT',
'HIGH'
);
```

---

## Retrieve All Notifications

```sql
SELECT *
FROM notifications
ORDER BY created_at DESC;
```

---

## Retrieve Notification by ID

```sql
SELECT *
FROM notifications
WHERE id = 101;
```

---

## Update Notification

```sql
UPDATE notifications
SET
title = 'Placement Drive Updated',
message = 'Interview venue changed',
priority = 'HIGH'
WHERE id = 101;
```

---

## Delete Notification

```sql
DELETE FROM notifications
WHERE id = 101;
```

---

## Mark Notification as Read

```sql
UPDATE user_notifications
SET
is_read = TRUE,
read_at = CURRENT_TIMESTAMP
WHERE
user_id = 15
AND notification_id = 101;
```

---

## Retrieve Unread Notifications

```sql
SELECT n.id,
       n.title,
       n.message,
       n.priority,
       n.created_at
FROM notifications n
INNER JOIN user_notifications un
ON n.id = un.notification_id
WHERE
un.user_id = 15
AND un.is_read = FALSE
ORDER BY n.created_at DESC;
```

---

# Performance Improvements

To ensure the application performs well even as the number of users grows, the following optimizations can be adopted:

- Create indexes on frequently searched columns.
- Use pagination to limit records returned in a single request.
- Cache frequently accessed notifications using Redis.
- Archive historical notifications.
- Partition large tables.
- Process notification delivery asynchronously using RabbitMQ or Kafka.

---

# Conclusion

PostgreSQL is an appropriate database for the notification platform because it provides reliable transaction handling, efficient relationship management, and excellent scalability. With indexing, caching, table partitioning, and asynchronous processing, the notification service can continue to deliver fast and reliable performance even as the number of users and notifications grows substantially.




----------------------------------------------------------
---

# Stage 3

# Query Analysis and Optimization

The current SQL query provided by the previous developer is:

```sql
SELECT FROM notifications *
WHERE studentID = 1042
AND isRead = false
ORDER BY createdAt DESC;
```

---

# Is the Query Correct?

No. The query contains a syntax error.

The `*` should come immediately after the `SELECT` keyword.

### Correct Query

```sql
SELECT *
FROM notifications
WHERE studentID = 1042
AND isRead = FALSE
ORDER BY createdAt DESC;
```

---

# Why is the Query Slow?

When the notification table contains nearly **5 million records**, retrieving data becomes more expensive if the database cannot efficiently locate the required rows.

The main reasons for slower execution are:

### 1. Full Table Scan

Without suitable indexes, the database scans every notification record before applying the filter.

### 2. Sorting Overhead

The query sorts notifications by `createdAt`. Sorting a large result set consumes additional processing time.

### 3. Large Number of Notifications

A single student may have accumulated thousands of notifications over time, increasing the amount of data that must be processed.

### 4. Growing Database Size

As more notifications are stored, disk I/O increases and query execution naturally becomes slower.

---

# Recommended Improvements

Instead of indexing every column, create a composite index that matches the filtering and sorting conditions used by the query.

### Composite Index

```sql
CREATE INDEX idx_student_read_created
ON notifications
(studentID, isRead, createdAt DESC);
```

This index helps the database:

- Locate a student's notifications quickly.
- Filter unread notifications efficiently.
- Return results already ordered by `createdAt`.
- Reduce unnecessary sorting operations.

---

# Expected Computational Cost

### Without Index

```
O(N)
```

The database may examine every notification record.

For approximately **5,000,000** notifications, this results in a full table scan.

---

### With Composite Index

```
O(log N + K)
```

Where:

- **N** = Total number of notifications
- **K** = Number of unread notifications belonging to the requested student

The database searches the index using logarithmic time and retrieves only the matching rows.

---

# Should Every Column be Indexed?

No.

Adding indexes to every column is generally not a good database design strategy.

### Reasons

- Every INSERT operation must also update every index.
- UPDATE statements become slower.
- DELETE operations require additional index maintenance.
- More disk space is consumed.
- Many indexes remain unused and provide no benefit.

Indexes should only be created on columns that are frequently used in:

- WHERE clauses
- JOIN conditions
- ORDER BY clauses
- GROUP BY clauses

Choosing indexes based on query patterns provides better performance than indexing every column.

---

# SQL Query

## Students Who Received Placement Notifications During the Last 7 Days

```sql
SELECT DISTINCT studentID
FROM notifications
WHERE notificationType = 'Placement'
AND createdAt >= CURRENT_DATE - INTERVAL '7 days';
```

---

# Additional Optimization Suggestions

To keep query performance consistent as the database grows, the following improvements can also be adopted:

- Partition the notifications table using the creation date.
- Archive old notifications that are no longer accessed frequently.
- Use pagination when returning notification lists.
- Cache frequently requested notification data using Redis.
- Analyze slow queries periodically using PostgreSQL's `EXPLAIN ANALYZE` command.

---

# Conclusion

The original query contains a syntax error and is inefficient for a table containing millions of records. A carefully designed composite index significantly improves performance by reducing full table scans and unnecessary sorting. Rather than indexing every column, indexes should be created based on actual query patterns to balance read performance with write efficiency.