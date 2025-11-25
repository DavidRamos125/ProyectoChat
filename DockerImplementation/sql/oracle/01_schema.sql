CREATE USER myapp IDENTIFIED BY mypassword;

GRANT CONNECT, RESOURCE TO myapp;

ALTER SESSION SET CURRENT_SCHEMA = myapp;

CREATE TABLE users (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(100) NOT NULL UNIQUE,
    password VARCHAR2(200) NOT NULL,
    accepted NUMBER(1) DEFAULT 0 NOT NULL CHECK (accepted IN (0,1))
);

CREATE TABLE sessions (
    id VARCHAR2(50) PRIMARY KEY,
    user_id NUMBER NOT NULL,
    ip VARCHAR2(45) NOT NULL,
    connection_time DATE NOT NULL,
    disconnection_time DATE NULL,
    status VARCHAR2(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE messages (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sender_id NUMBER NOT NULL,
    receiver_id NUMBER NOT NULL,
    session_sender_id VARCHAR2(50) NOT NULL,
    content_type VARCHAR2(10) NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id),
    FOREIGN KEY (session_sender_id) REFERENCES sessions(id)
);

CREATE TABLE message_receivers (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    message_id NUMBER NOT NULL,
    session_id VARCHAR2(50) NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id),
    FOREIGN KEY (session_id) REFERENCES sessions(id)
);

CREATE TABLE text_contents (
    message_id NUMBER PRIMARY KEY,
    text CLOB NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id)
);

CREATE TABLE file_contents (
    message_id NUMBER PRIMARY KEY,
    name VARCHAR2(255) NOT NULL,
    size NUMBER NOT NULL,
    data BLOB NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id)
);
