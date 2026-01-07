# JGame REST API Guide

**Version**: 1.0  
**Last Updated**: January 2026

---

## Overview

JGame provides a RESTful API for game management, user authentication, scores, and ratings. The server is built with Javalin and uses JWT for authentication.

## Base URL

```
http://localhost:8080/api
```

---

## Authentication

### Register User

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "player1",
  "password": "securepass123",
  "email": "player1@example.com"
}
```

**Response** (201 Created):

```json
{
  "success": true,
  "message": "Registration successful"
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "player1",
  "password": "securepass123"
}
```

**Response** (200 OK):

```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "player1"
  }
}
```

### Using JWT Token

Include the token in subsequent requests:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Games

### List All Games

```http
GET /api/games
```

**Response**:

```json
{
  "games": [
    {
      "id": "chess",
      "name": "Chess",
      "description": "Classic 2-player strategy",
      "minPlayers": 2,
      "maxPlayers": 2
    },
    {
      "id": "checkers",
      "name": "Checkers",
      "description": "Jump and capture",
      "minPlayers": 2,
      "maxPlayers": 2
    }
  ]
}
```

### Get Game Details

```http
GET /api/games/{gameId}
```

**Response**:

```json
{
  "id": "chess",
  "name": "Chess",
  "description": "Classic 2-player strategy game",
  "minPlayers": 2,
  "maxPlayers": 2,
  "averageRating": 4.5,
  "totalRatings": 127
}
```

---

## Ratings

### Get Game Ratings

```http
GET /api/games/{gameId}/ratings
```

### Submit Rating (Authenticated)

```http
POST /api/ratings/{gameId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "stars": 5,
  "comment": "Great game!"
}
```

---

## User Profile (Authenticated)

### Get Profile

```http
GET /api/user/profile
Authorization: Bearer <token>
```

### Get User Scores

```http
GET /api/user/scores
Authorization: Bearer <token>
```

**Response**:

```json
{
  "scores": [
    {
      "gameId": "chess",
      "wins": 15,
      "losses": 8,
      "points": 1250
    }
  ]
}
```

---

## Leaderboard

### Get Game Leaderboard

```http
GET /api/scores/{gameId}/leaderboard
Authorization: Bearer <token>
```

**Response**:

```json
{
  "leaderboard": [
    {"rank": 1, "username": "grandmaster", "points": 2500},
    {"rank": 2, "username": "player1", "points": 1250}
  ]
}
```

---

## Error Responses

| Status Code | Description |
|-------------|-------------|
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Missing/invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource doesn't exist |
| 500 | Internal Server Error |

**Error Format**:

```json
{
  "success": false,
  "error": "Error message here"
}
```

---

## WebSocket Events

Connect to `ws://localhost:8080/ws/game/{gameId}` for real-time game updates.

### Events

| Event | Direction | Description |
|-------|-----------|-------------|
| `join` | Client → Server | Join game session |
| `move` | Client → Server | Make a move |
| `state` | Server → Client | Game state update |
| `turn` | Server → Client | Turn notification |
| `end` | Server → Client | Game over |

---

## Code Examples

### Java Client

```java
GameApiClient client = new GameApiClient("http://localhost:8080");
client.login("player1", "password");
List<Game> games = client.getGames();
```

### JavaScript

```javascript
const response = await fetch('/api/auth/login', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  body: JSON.stringify({username: 'player1', password: 'pass'})
});
const {token} = await response.json();
```
