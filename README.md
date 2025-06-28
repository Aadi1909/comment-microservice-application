# comment-microservice-application

A scalable, lightweight microservice for managing user comments, built with Spring Boot and MongoDB.

## Features

- **Threaded Comments:** Supports replies and discussion threads.
- **Upvote/Downvote:** Allows users to upvote or downvote comments.
- **Cursor-Based Pagination:** Efficient pagination for large comment datasets.
- **Microservices Architecture:** Modular design with separate services for comments and users.
- **User Integration:** Fetches user information from a dedicated user service.

## Technologies

- Java, Spring Boot
- MongoDB
- Lombok

## Getting Started

### Prerequisites

- Java 17+
- MongoDB

### Running the Services

1. Clone the repository.
2. Make sure MongoDB is running locally or update the configuration in `application.properties`.
3. Build the project:
   ```sh
   ./mvnw clean install
   ```
4. Start the user and comment services:
   ```sh
   cd user-service
   ./mvnw spring-boot:run
   # In a new terminal:
   cd ../comment-service
   ./mvnw spring-boot:run
   ```

### REST API Overview

- `POST /comment/{topicId}` — Add a new comment.
- `GET /{topicId}/comments` — Get comments with pagination.
- `POST /comment/{comment-id}/reply` — Reply to a comment.
- `PUT /comment/{comment-id}/upvote` — Upvote a comment.
- `PUT /comment/{comment-id}/downvote` — Downvote a comment.
- `DELETE /comment/{comment-id}` — Delete a comment and its replies.

## Contributing

Contributions are welcome! Please open issues or pull requests.

## License

MIT License
