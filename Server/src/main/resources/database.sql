CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       accepted BOOLEAN DEFAULT FALSE
);

CREATE TABLE sessions (
                          id VARCHAR(64) PRIMARY KEY,
                          user_id INT NOT NULL,
                          ip VARCHAR(45),
                          connection_time DATETIME,
                          status VARCHAR(20),
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE messages (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          sender_id INT NOT NULL,
                          receiver_id INT NOT NULL,
                          content_type ENUM('TEXT', 'FILE') NOT NULL,
                          date DATETIME NOT NULL,
                          FOREIGN KEY (sender_id) REFERENCES users(id),
                          FOREIGN KEY (receiver_id) REFERENCES users(id)
);

CREATE TABLE text_contents (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               message_id INT UNIQUE,
                               text TEXT,
                               FOREIGN KEY (message_id) REFERENCES messages(id)
);

CREATE TABLE file_contents (
                               id VARCHAR(64) PRIMARY KEY,
                               message_id INT UNIQUE,
                               name VARCHAR(255),
                               size BIGINT,
                               data LONGBLOB,
                               FOREIGN KEY (message_id) REFERENCES messages(id)
);
