CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    accepted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE sessions (
    id VARCHAR(50) PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    ip VARCHAR(45) NOT NULL,
    connection_time TIMESTAMP NOT NULL,
    disconnection_time TIMESTAMP NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    sender_id INT NOT NULL REFERENCES users(id),
    receiver_id INT NOT NULL REFERENCES users(id),
    content_type VARCHAR(10) NOT NULL,
    date TIMESTAMP NOT NULL
);

CREATE TABLE message_receivers (
    id SERIAL PRIMARY KEY,
    message_id INT NOT NULL REFERENCES messages(id),
    session_id VARCHAR(50) NOT NULL REFERENCES sessions(id)
);

CREATE TABLE text_contents (
    message_id INT PRIMARY KEY REFERENCES messages(id),
    text TEXT NOT NULL
);

CREATE TABLE file_contents (
    message_id INT PRIMARY KEY REFERENCES messages(id),
    name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    data BYTEA NOT NULL
);
