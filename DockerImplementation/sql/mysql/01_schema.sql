CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    accepted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE sessions (
    id VARCHAR(50) PRIMARY KEY,
    user_id INT NOT NULL,
    ip VARCHAR(45) NOT NULL,
    connection_time DATETIME NOT NULL,
    disconnection_time DATETIME NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE messages (
    id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    content_type VARCHAR(10) NOT NULL,  -- TEXT / FILE
    date DATETIME NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);

CREATE TABLE message_receivers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    message_id INT NOT NULL,
    session_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id),
    FOREIGN KEY (session_id) REFERENCES sessions(id)
);

CREATE TABLE text_contents (
    message_id INT PRIMARY KEY,
    text TEXT NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id)
);

CREATE TABLE file_contents (
    message_id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    data LONGBLOB NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id)
);
