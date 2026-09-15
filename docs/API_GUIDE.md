# JGame REST API Guide

**Version**: 2.1  
**Last Updated**: September 2026

---

## Overview

JGame provides a high-performance RESTful API for user authentication, game catalog discovery, real-time lobbies, ratings, user profiles, and persistent leaderboards. The server is built with Javalin 6 and uses JWT (HMAC-SHA256) for stateful token validation and revocation.

## Base URL

```
http://localhost:8080/api
```

---

## Authentication & Session Management

### Register User

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "player1",
  "password": "Password123!",
  "email": "player1@example.com"
}
```

**Response** (`201 Created`):

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "player1"
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "player1",
  "password": "Password123!"
}
```

**Response** (`200 OK`):

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "player1"
}
```

### Logout (Authenticated)

Revokes the active JWT token and registers it in the server-side `TokenBlacklist`.

```http
POST /api/auth/logout
Authorization: Bearer <token>
```

**Response** (`200 OK`):

```json
{
  "message": "Logged out successfully"
}
```

### Using JWT Token

Include the token in the `Authorization` header for protected endpoints:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## User Profile & Stats (Authenticated)

### Get Profile

```http
GET /api/user/profile
Authorization: Bearer <token>
```

**Response** (`200 OK`):

```json
{
  "userId": "1",
  "username": "player1",
  "role": "user"
}
```

### Update Profile

```http
PUT /api/user/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "email": "newemail@example.com",
  "password": "NewPassword123!"
}
```

**Response** (`200 OK`):

```json
{
  "message": "Profile updated successfully"
}
```

### Get User Scores

```http
GET /api/user/scores
Authorization: Bearer <token>
```

**Response** (`200 OK`):

```json
[
  {
    "userId": "player1",
    "gameId": "chess",
    "points": 1250,
    "gamesPlayed": 23,
    "wins": 15,
    "losses": 8,
    "totalTime": "PT1H30M",
    "lastPlayed": "2026-09-15T10:00:00Z"
  }
]
```

---

## Game Discovery

### List All Games

```http
GET /api/games?sort=name&q=chess
```

**Response** (`200 OK`):

```json
[
  {
    "id": "chess",
    "name": "Chess",
    "version": "1.0",
    "author": "JGame",
    "description": "Classic chess game",
    "rules": "Standard chess rules",
    "minPlayers": 2,
    "maxPlayers": 2,
    "metadata": {}
  }
]
```

### Get Game Details

```http
GET /api/games/{gameId}
```

---

## Ratings & Reviews

### Get Game Ratings

```http
GET /api/games/{gameId}/ratings
```

**Response** (`200 OK`):

```json
{
  "ratings": [
    {
      "userId": "player1",
      "gameId": "chess",
      "stars": 5,
      "comment": "Superb implementation!",
      "createdAt": "2026-09-15T09:30:00Z",
      "updatedAt": "2026-09-15T09:30:00Z"
    }
  ],
  "average": 4.8,
  "count": 12
}
```

### Submit Rating (Authenticated)

Comments are sanitized automatically against HTML/script injection.

```http
POST /api/ratings/{gameId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "stars": 5,
  "comment": "Great strategic depth!"
}
```

### Delete Rating (Authenticated)

```http
DELETE /api/ratings/{gameId}
Authorization: Bearer <token>
```

---

## Leaderboards

### Get Game Leaderboard

```http
GET /api/scores/{gameId}/leaderboard?limit=10
```

**Response** (`200 OK`):

```json
[
  {
    "userId": "grandmaster",
    "gameId": "chess",
    "points": 2500,
    "gamesPlayed": 45,
    "wins": 40,
    "losses": 5
  }
]
```

---

## Error Handling & Status Codes

| Status Code | Description |
|---|---|
| `200 OK` | Request succeeded |
| `201 Created` | Resource created |
| `204 No Content` | Action succeeded with no body |
| `400 Bad Request` | Validation failure or malformed payload |
| `401 Unauthorized` | Missing, invalid, or revoked JWT token |
| `404 Not Found` | Requested game, rating or resource not found |
| `409 Conflict` | Username already registered |
| `429 Too Many Requests` | Rate limit exceeded (brute-force defense) |
| `500 Internal Server Error` | Unhandled server error |

**Standard Error Format**:

```json
{
  "error": "Error description message"
}
```

---

## Client Code Integration

### Java Client Example

```java
GameApiClient client = new GameApiClient("http://localhost:8080");
client.login("player1", "Password123!").thenAccept(res -> {
    System.out.println("Logged in with token: " + res.token());
});
```

### JavaScript Web Client Example

```javascript
const api = new JGameAPI("http://localhost:8080");
await api.login("player1", "Password123!");
const games = await api.getGames();
```

