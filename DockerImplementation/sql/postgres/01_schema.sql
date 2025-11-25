CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    accepted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE sessions (
    id VARCHAR(50) PRIMARY KEY,
    user_id INTEGER NOT NULL,
    ip VARCHAR(45) NOT NULL,
    connection_time TIMESTAMP NOT NULL,
    disconnection_time TIMESTAMP NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    session_sender_id VARCHAR(50) NOT NULL,
    content_type VARCHAR(10) NOT NULL,
    date TIMESTAMP NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id),
    FOREIGN KEY (session_sender_id) REFERENCES sessions(id)
);

CREATE TABLE message_receivers (
    id SERIAL PRIMARY KEY,
    message_id INTEGER NOT NULL,
    session_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id),
    FOREIGN KEY (session_id) REFERENCES sessions(id)
);

CREATE TABLE text_contents (
    message_id INTEGER PRIMARY KEY,
    text TEXT NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id)
);

CREATE TABLE file_contents (
    message_id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    data BYTEA NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id)
);
